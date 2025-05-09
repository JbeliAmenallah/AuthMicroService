package com.example.sfmproject.JWT;

import com.example.sfmproject.ServiceImpl.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Component
public class JwtAuthTokenFilter implements Filter {

    @Value("${cryptoserver.app.jwtSecret}")
    private String jwtSecret;

    @Value("${cryptoserver.app.jwtExpiration}")
    private int jwtExpiration;

    @Autowired
    private JwtProvider tokenProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthTokenFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();

        // Exclude specific URLs from JWT validation
        Set<String> excludedUrls = new HashSet<>();
        excludedUrls.add("/api/user/forgetpass");
        excludedUrls.add("/api/auth/");
        excludedUrls.add("/OTP/");
        excludedUrls.add("/Msg");
        excludedUrls.add("/api/auth/refreshToken");

        String requestUri = httpRequest.getRequestURI();
        boolean isExcluded = false;
        for (String excludedUrl : excludedUrls) {
            if (requestUri.startsWith(excludedUrl)) {
                isExcluded = true;
                break;
            }
        }

        if (isExcluded) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String jwt = getJwt(httpRequest, session);
            if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
                setAuthentication(jwt, httpRequest);
            } else {
                // If token is invalid or expired, handle authentication error
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return;
            }
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token: {}", e.getMessage());
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
            return;
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        } catch (Exception e) {
            logger.error("Error processing JWT token: {}", e.getMessage());
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
            return;
        }

        chain.doFilter(request, response);
    }

    public void setAuthentication(String jwt, HttpServletRequest request) {
        String username = tokenProvider.getUserNameFromJwtToken(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public String getJwt(HttpServletRequest request, HttpSession session) {
        // Check for access token
        String accessToken = extractAccessToken(request);
        if (accessToken != null) {
            return accessToken;
        }

        // Check for refresh token from session
        String refreshToken = (String) session.getAttribute("refreshToken");
        if (refreshToken != null && isValidRefreshToken(refreshToken)) {
            // Issue new access token
            String newAccessToken = issueNewAccessToken(refreshToken);
            return newAccessToken;
        }

        return null;
    }

    public String issueNewAccessToken(String refreshToken) {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(refreshToken).getBody();
            String username = claims.getSubject();
            long ACCESS_TOKEN_VALIDITY_MS = 60 * 1000; // 1 minute
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + ACCESS_TOKEN_VALIDITY_MS))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();
        } catch (Exception e) {
            logger.error("Error issuing new access token: {}", e.getMessage());
            return null;
        }
    }

    public boolean isValidRefreshToken(String refreshToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(refreshToken);
            return true;
        } catch (Exception e) {
            logger.error("Invalid refresh token: {}", e.getMessage());
            return false;
        }
    }

    public String extractAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public String extractRefreshToken(HttpServletRequest request) {
        // Retrieve the refresh token from the request attributes or parameters
        String refreshToken = request.getParameter("RefreshToken");
        if (refreshToken != null && !refreshToken.isEmpty()) {
            return refreshToken;
        } else {
            // If the refresh token is not found in parameters, try retrieving it from headers
            refreshToken = request.getHeader("RefreshToken");
            if (refreshToken != null ) {
                // Extract the token from the Authorization header
                //  refreshToken = refreshToken.substring(7);
                return refreshToken;
            }
        }
        return null; // Return null if refresh token is not found in both parameters and headers
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic, if needed
    }

    @Override
    public void destroy() {
        // Cleanup logic, if needed
    }
}