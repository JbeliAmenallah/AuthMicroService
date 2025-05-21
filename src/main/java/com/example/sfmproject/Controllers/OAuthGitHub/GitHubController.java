package com.example.sfmproject.Controllers.OAuthGitHub;

import com.example.sfmproject.DTO.CollaboratorRequest;
import com.example.sfmproject.JWT.JwtProvider;
import com.example.sfmproject.ServiceImpl.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}

