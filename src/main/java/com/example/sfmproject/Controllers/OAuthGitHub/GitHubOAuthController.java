package com.example.sfmproject.Controllers.OAuthGitHub;

import com.example.sfmproject.Entities.Enum.RoleUser;
import com.example.sfmproject.Entities.Role;
import com.example.sfmproject.Entities.User;
import com.example.sfmproject.Entities.UserPrinciple;
import com.example.sfmproject.JWT.JwtProvider;
import com.example.sfmproject.Repositories.RoleRepository;
import com.example.sfmproject.Repositories.UserRepository;

import com.example.sfmproject.ServiceImpl.GitHubService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/oauth/github")
public class GitHubOAuthController {

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    GitHubService gitHubService;

    @GetMapping("/login")
    public ResponseEntity<Void> redirectToGitHub() {
        String githubLoginUrl = String.format(
                "https://github.com/login/oauth/authorize?client_id=%s&redirect_uri=%s&scope=%s",
                clientId,
                URLEncoder.encode(redirectUri, StandardCharsets.UTF_8),
                URLEncoder.encode("user:email read:org repo admin:repo_hook gist write:packages read:packages workflow", StandardCharsets.UTF_8)
        );

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(githubLoginUrl))
                .build();
    }

//    @GetMapping("/token")
//    public ResponseEntity<?> handleGitHubCode(@RequestParam("code") String code) {
//        try {
//            String accessToken = getAccessToken(code);
//            Map<String, Object> githubUser = getGitHubUserInfo(accessToken);
//
//            String email = (String) githubUser.get("email");
//            String name = (String) githubUser.get("name");
//            String username = (String) githubUser.get("login");
//
//            if (email == null) {
//                return ResponseEntity.badRequest().body("Could not retrieve email from GitHub");
//            }
//
//            // Same user logic as before
//            User user = userRepository.findByEmail(email).orElseGet(() -> {
//                User newUser = new User(
//                        name != null ? name : username,
//                        username,
//                        email,
//                        UUID.randomUUID().toString(),
//                        false,
//                        null,
//                        true
//                );
//                Role userRole = roleRepository.findByRoleName(RoleUser.ADMIN)
//                        .orElseThrow(() -> new RuntimeException("Role not found"));
//                newUser.setRoles(Set.of(userRole));
//                return userRepository.save(newUser);
//            });
//
//            // Auth and tokens
//            UserPrinciple userPrinciple = UserPrinciple.build(user);
//            Authentication auth = new UsernamePasswordAuthenticationToken(userPrinciple, null, userPrinciple.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(auth);
//
//            List<String> tokens = jwtProvider.generateJwtTokens(auth, accessToken);
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("accessToken", tokens.get(0));
//            response.put("refreshToken", tokens.get(1));
//            response.put("user", user);
//            response.put("githubOrgs", githubUser.get("orgs"));
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("GitHub OAuth failed: " + e.getMessage());
//        }
//    }


    @GetMapping("/token")
    public ResponseEntity<?> handleGitHubCode(@RequestParam("code") String code) {
        try {
            String accessToken = getAccessToken(code);
            Map<String, Object> githubUser = getGitHubUserInfo(accessToken);

            String email = (String) githubUser.get("email");
            String name = (String) githubUser.get("name");
            String username = (String) githubUser.get("login");

            if (email == null) {
                return ResponseEntity.badRequest().body("Could not retrieve email from GitHub");
            }

            // Check if user exists or create a new one
            User user = userRepository.findByEmail(email).orElseGet(() -> {
                User newUser = new User(
                        name != null ? name : username,
                        username,
                        email,
                        UUID.randomUUID().toString(), // Random password for OAuth users
                        false,
                        null,
                        true
                );

                Role userRole = roleRepository.findByRoleName(RoleUser.ADMIN) // Assign default role (adjust as necessary)
                        .orElseThrow(() -> new RuntimeException("Role not found"));
                newUser.setRoles(Set.of(userRole));

                return userRepository.save(newUser);
            });

            // Update the user's GitHub token
            user.setGithubToken(accessToken);
            userRepository.save(user); // Save the updated user

            // Authenticate the user
            UserPrinciple userPrinciple = UserPrinciple.build(user);
            Authentication auth = new UsernamePasswordAuthenticationToken(userPrinciple, null, userPrinciple.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

            List<String> tokens = jwtProvider.generateJwtTokens(auth, accessToken);

            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", tokens.get(0));
            response.put("refreshToken", tokens.get(1));
            response.put("user", user);
            response.put("githubOrgs", githubUser.get("orgs"));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("GitHub OAuth failed: " + e.getMessage());
        }
    }


    //    @GetMapping("/callback")
//    public ResponseEntity<?> githubCallback(@RequestParam("code") String code) {
//        try {
//            String accessToken = getAccessToken(code);
//
//            // Récupérer infos utilisateur GitHub
//            Map<String, Object> githubUser = getGitHubUserInfo(accessToken);
//
//            String email = (String) githubUser.get("email");
//            String name = (String) githubUser.get("name");
//            String username = (String) githubUser.get("login");
//
//            if (email == null) {
//                return ResponseEntity.badRequest().body("Could not retrieve email from GitHub");
//            }
//
//            // Check if user exists, create if not
//            User user = userRepository.findByEmail(email)
//                    .orElseGet(() -> {
//                        // Use the constructor from User entity
//                        User newUser = new User(
//                                name != null ? name : username, // name
//                                username,                      // username
//                                email,                         // email
//                                UUID.randomUUID().toString(),  // Random password for OAuth users
//                                false,                         // blocked
//                                null,                          // address (optional, set to null)
//                                true                           // valid
//                        );
//
//                        // Assign default role
//                        Role userRole = roleRepository.findByRoleName(RoleUser.Utilisateur)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        newUser.setRoles(new HashSet<>(Set.of(userRole)));
//
//                        return userRepository.save(newUser);
//                    });
//
//            // Create authorities from user roles
//            List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
//                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().name()))
//                    .collect(Collectors.toList());
//
//            // Create Authentication object with User as principal
//            UserPrinciple userPrinciple = UserPrinciple.build(user);
//
//            Authentication authentication = new UsernamePasswordAuthenticationToken(
//                    userPrinciple,           // Use User entity as principal
//                    null,           // No credentials for OAuth
//                    userPrinciple.getAuthorities()
//                    // Authorities based on roles
//            );
//
//            // Set authentication in SecurityContext
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            // Generate tokens
//            List<String> tokens = jwtProvider.generateJwtTokens(authentication, accessToken);
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("accessToken", tokens.get(0));
//            response.put("refreshToken", tokens.get(1));
//            response.put("user", user);
//
//            // Récupérer les organisations depuis githubUser et les mettre dans la réponse JSON
//            List<Map<String, Object>> githubOrgs = (List<Map<String, Object>>) githubUser.get("orgs");
//            response.put("githubOrgs", githubOrgs);
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("GitHub OAuth failed: " + e.getMessage());
//        }
//    }
    private String getAccessToken(String code) throws IOException {
        String url = "https://github.com/login/oauth/access_token";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        Map<String, String> body = new HashMap<>();
        body.put("client_id", clientId);
        body.put("client_secret", clientSecret);
        body.put("code", code);
        body.put("redirect_uri", redirectUri);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);


        return (String) response.getBody().get("access_token");

    }

    private Map<String, Object> getGitHubUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map<String, Object> userInfo = response.getBody();

        if (userInfo.get("email") == null) {
            ResponseEntity<List> emailsResponse = restTemplate.exchange(
                    "https://api.github.com/user/emails",
                    HttpMethod.GET,
                    entity,
                    List.class
            );
            List<Map<String, Object>> emails = emailsResponse.getBody();
            for (Map<String, Object> mailEntry : emails) {
                if (Boolean.TRUE.equals(mailEntry.get("primary"))) {
                    userInfo.put("email", mailEntry.get("email"));
                    break;
                }
            }
        }

        // ✅ 3. Ajouter la récupération des organisations ici
        ResponseEntity<List> orgsResponse = restTemplate.exchange(
                "https://api.github.com/user/orgs",
                HttpMethod.GET,
                entity,
                List.class
        );
        List<Map<String, Object>> orgs = orgsResponse.getBody();
        System.out.println("GitHub orgs response: " + orgs);
        userInfo.put("orgs", orgs);

        return userInfo;
    }



}

