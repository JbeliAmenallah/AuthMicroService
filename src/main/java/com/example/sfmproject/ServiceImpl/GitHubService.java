package com.example.sfmproject.ServiceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GitHubService {

    private final RestTemplate restTemplate;
    private static final String GITHUB_API_URL = "https://api.github.com";

    public GitHubService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public ResponseEntity<String> getGitHubUserInfo(String githubAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubAccessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                entity,
                String.class
        );
    }

    public ResponseEntity<String> getUserRepos(String githubAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubAccessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                "https://api.github.com/user/repos",
                HttpMethod.GET,
                entity,
                String.class
        );
    }

    public Map<String, Object> createRepository(String token, String repoName, String description, boolean isPrivate) {
        String url = GITHUB_API_URL + "/user/repos";

        // Set up HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "token " + token);
        headers.set("Accept", "application/vnd.github.v3+json");

        // Create request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", repoName);
        requestBody.put("description", description);
        requestBody.put("private", isPrivate);
        requestBody.put("auto_init", true); // Initialize with README

        // Create HTTP entity with headers and body
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Make POST request to GitHub API
        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        // Return the response body
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = response.getBody();
        return responseBody;
    }

    public ResponseEntity<String> addCollaborator(String githubAccessToken, String owner, String repo, String username, String permission) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubAccessToken);
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("Content-Type", "application/json");

        String body = String.format("{\"permission\": \"%s\"}", permission);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        String url = String.format("https://api.github.com/repos/%s/%s/collaborators/%s", owner, repo, username);

        return restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                String.class
        );
    }

    // Get starred repositories
    public ResponseEntity<String> getStarredRepos(String githubAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubAccessToken);
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("Content-Type", "application/json");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = GITHUB_API_URL + "/user/starred";

        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    // Get organizations
    public ResponseEntity<String> getOrganizations(String githubAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubAccessToken);
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("Content-Type", "application/json");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = GITHUB_API_URL + "/user/orgs";

        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<String> listRepositoryContent(String githubAccessToken, String owner, String repoName) {
        // Build the API URL
        String url = GITHUB_API_URL + "/repos/" + owner + "/" + repoName + "/contents";
        System.out.println(url);
        // Set up headers with authorization
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubAccessToken);
        headers.set("Accept", "application/vnd.github.v3+json");

        // Create the request entity
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        // Make the GET request
        try {
            return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );
        } catch (Exception e) {
            // Handle exceptions (e.g., repository not found, unauthorized, etc.)
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }


    // Get contribution data (using GitHub GraphQL API or fallback)
    // Here we will fetch user public events as contribution data example
    public ResponseEntity<String> getContributionData(String githubAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubAccessToken);
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("Content-Type", "application/json");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = GITHUB_API_URL + "/users/{username}/events/public";

        // To get username, we may need to get user info first
        // Simplification: You can modify to accept username or get it internally
        // For demo, let's call /user endpoint to get username:

        ResponseEntity<Map> userResponse = restTemplate.exchange(
                GITHUB_API_URL + "/user",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class);

        if (userResponse.getStatusCode() == HttpStatus.OK && userResponse.getBody() != null) {
            String username = (String) userResponse.getBody().get("login");
            String eventsUrl = GITHUB_API_URL + "/users/" + username + "/events/public";
            return restTemplate.exchange(eventsUrl, HttpMethod.GET, entity, String.class);
        } else {
            return new ResponseEntity<>("Unable to fetch user info", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> getRepoContent(String owner, String repo, String path, String token) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.github.com/repos/{owner}/{repo}/contents/{path}")
                .buildAndExpand(owner, repo, path)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github.v3+json");

        // Optional: if the repo is private or you want to increase rate limit
        if (token != null && !token.isEmpty()) {
            headers.setBearerAuth(token);
        }

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

    }

    public String getFileContentFromApi(String owner, String repo, String path, String token) throws Exception {
        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/contents/" + path;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        if (token != null && !token.isEmpty()) {
            headers.set("Authorization", "token " + token);
        }
        headers.set("Accept", "application/vnd.github.v3+json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String body = response.getBody();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(body);

            // The content is base64 encoded
            String encodedContent = root.get("content").asText();
            // Remove newlines
            encodedContent = encodedContent.replaceAll("\\n", "");

            byte[] decodedBytes = Base64Utils.decodeFromString(encodedContent);
            return new String(decodedBytes);
        } else {
            throw new RuntimeException("Failed to fetch file content: " + response.getStatusCode());
        }
    }
}

