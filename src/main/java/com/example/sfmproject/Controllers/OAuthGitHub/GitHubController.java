package com.example.sfmproject.Controllers.OAuthGitHub;

import com.example.sfmproject.JWT.JwtProvider;
import com.example.sfmproject.ServiceImpl.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/github")
public class GitHubController {

    @Autowired
    private GitHubService gitHubService;

    @Autowired
    private JwtProvider jwtProvider;

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
}

