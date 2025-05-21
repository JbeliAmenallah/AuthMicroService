package com.example.sfmproject.Controllers.OAuthGitHub;

import com.example.sfmproject.DTO.GitHubRepoRequest;
import com.example.sfmproject.JWT.JwtProvider;
import com.example.sfmproject.ServiceImpl.GitHubService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@RestController
@RequestMapping("/api/github")
public class GitHubController {

    @Autowired
    private GitHubService gitHubService;

    private final JwtProvider jwtProvider;
    private final RestTemplate restTemplate;

    @Autowired
    public GitHubController(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
        this.restTemplate = new RestTemplate();
    }

    private static final String GITHUB_API_URL = "https://api.github.com/user/repos";


    @GetMapping("/user")
    public ResponseEntity<?> getGitHubUserInfo(@RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        String githubToken = jwtProvider.extractGithubAccessToken(jwt); // extrait du JWT

        ResponseEntity<String> response = gitHubService.getGitHubUserInfo(githubToken);
        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/repos")
    public ResponseEntity<?> getGitHubRepos(@RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        String githubToken = jwtProvider.extractGithubAccessToken(jwt);

        ResponseEntity<String> response = gitHubService.getUserRepos(githubToken);
        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/create-repo")
    public ResponseEntity<?> createRepo(@RequestBody GitHubRepoRequest repoRequest,
                                        @RequestHeader("Authorization") String authHeader) {
        try {
            // Step 1: Extract JWT from header
            String jwt = authHeader.replace("Bearer ", "");

            // Step 2: Extract GitHub token from your JWT
            String githubToken = jwtProvider.extractGithubAccessToken(jwt);

            // Step 3: Prepare headers for GitHub API
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(githubToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<GitHubRepoRequest> entity = new HttpEntity<>(repoRequest, headers);

            // Step 4: Call GitHub API
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://api.github.com/user/repos",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body("GitHub API Error: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}