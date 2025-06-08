package com.example.sfmproject.Controllers.OAuthGitHub;

import com.example.sfmproject.DTO.GitHubRepoRequest;
import com.example.sfmproject.DTO.CollaboratorRequest;
import com.example.sfmproject.JWT.JwtProvider;
import com.example.sfmproject.ServiceImpl.GitHubService;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
//@CrossOrigin
@CrossOrigin(origins = "http://localhost:4200")
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

    @PostMapping("/add-collaborator")
    public ResponseEntity<?> addCollaborator(
            @RequestHeader("Authorization") String token,
            @RequestBody CollaboratorRequest request) {

        String jwt = token.replace("Bearer ", "");
        String githubToken = jwtProvider.extractGithubAccessToken(jwt);

        ResponseEntity<String> response = gitHubService.addCollaborator(
                githubToken,
                request.getOwner(),
                request.getRepo(),
                request.getUsername(),
                request.getPermission()
        );

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/repos")
    public ResponseEntity<?> getGitHubRepos(@RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        String githubToken = jwtProvider.extractGithubAccessToken(jwt);

        ResponseEntity<String> response = gitHubService.getUserRepos(githubToken);
        return ResponseEntity.ok(response.getBody());
    }

    //without service
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



    @GetMapping("/starred")
    public ResponseEntity<String> getStarredRepos(@RequestHeader("Authorization") String token) {
        try {
            // Token comes as "Bearer <token>", so strip "Bearer "
            String jwt = token.replace("Bearer ", "");
            String githubToken = jwtProvider.extractGithubAccessToken(jwt);
            String response = gitHubService.getStarredRepos(githubToken).getBody();

            // Check for null or empty response
            if (response == null || response.isEmpty()) {
                return ResponseEntity.noContent().build(); // HTTP 204 No Content
            }
            return ResponseEntity.ok(response); // HTTP 200 OK with the response body
        } catch (Exception e) {
            // Log the exception (optional) and return HTTP 500 Internal Server Error
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch starred repositories");
        }
    }

    @GetMapping("/organizations")
    public ResponseEntity<String> getOrganizations(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            String githubToken = jwtProvider.extractGithubAccessToken(jwt);
            String response = gitHubService.getOrganizations(githubToken).getBody();

            if (response == null || response.isEmpty()) {
                return ResponseEntity.noContent().build(); // HTTP 204 No Content
            }
            return ResponseEntity.ok(response); // HTTP 200 OK with the response body
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch organizations");
        }
    }

    @GetMapping("/contributions")
    public ResponseEntity<String> getContributionData(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            String githubToken = jwtProvider.extractGithubAccessToken(jwt);
            String response = gitHubService.getContributionData(githubToken).getBody();

            if (response == null || response.isEmpty()) {
                return ResponseEntity.noContent().build(); // HTTP 204 No Content
            }
            return ResponseEntity.ok(response); // HTTP 200 OK with the response body
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch contribution data");
        }
    }


    @GetMapping("/repos/{owner}/{repo}/contents/**")
    public ResponseEntity<String> getFileContent(
            @RequestHeader("Authorization") String token,
            @PathVariable String owner,
            @PathVariable String repo,
            HttpServletRequest request) {
        try {
            // Extract JWT token
            String jwt = token.replace("Bearer ", "");
            String githubToken = jwtProvider.extractGithubAccessToken(jwt);

            // Extract the dynamic path after /contents/
            String uri = request.getRequestURI();
            System.out.println("Request URI: " + uri);
            String prefix = String.format("/api/github/repos/%s/%s/contents", owner, repo);
            System.out.println("Prefix: " + prefix);
            String path = "";
            if (uri.length() > prefix.length()) {
                // The URI has something after the prefix (may or may not start with '/')
                path = uri.substring(prefix.length());
                System.out.println("Extracted path: " + path);
                // Remove leading slash if present
                if (path.startsWith("/")) {
                    path = path.substring(1);
                    System.out.println("Path after removing leading slash: " + path);
                }
            }
            // Call your service with the resolved path
            String content = gitHubService.getRepoContent(owner, repo, path, githubToken).getBody();
            return ResponseEntity.status(HttpStatus.OK).body(content);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to get file content: " + e.getMessage());
        }
    }



    @GetMapping("/fileContent/{owner}/{repo}/**")
    public ResponseEntity<String> displayFileContent(
            @PathVariable String owner,
            @PathVariable String repo,
            HttpServletRequest request) {

        try {
            // Full request URI
            String uri = request.getRequestURI();

            // Build prefix: everything before the file path
            String prefix = "/api/github/fileContent/" + owner + "/" + repo + "/";

            // Actual file path (may include ?token=... as part of it)
            String pathWithQuery = uri.substring(prefix.length());

            // Include query string if present (e.g., ?token=...)
            String query = request.getQueryString();
            if (query != null) {
                pathWithQuery += "?" + query;
            }

            // Now fetch the raw GitHub content (public access)
            String rawUrl = "https://raw.githubusercontent.com/" + owner + "/" + repo + "/" + pathWithQuery;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(rawUrl, String.class);

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching file: " + e.getMessage());
        }
    }

    @GetMapping("/repos/{owner}/{repo}/commits")
    public ResponseEntity<String> getRepositoryCommits(
            @RequestHeader("Authorization") String token,
            @PathVariable String owner,
            @PathVariable String repo) {

        String jwt = token.replace("Bearer ", "");
        String githubToken = jwtProvider.extractGithubAccessToken(jwt);

        try {
            ResponseEntity<String> response = gitHubService.getCommits(githubToken, owner, repo);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch commits: " + e.getMessage());
        }
    }
}