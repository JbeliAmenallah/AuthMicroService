package com.example.sfmproject.ServiceImpl;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GitHubService {

    private final RestTemplate restTemplate;

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
}

