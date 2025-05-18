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
}

