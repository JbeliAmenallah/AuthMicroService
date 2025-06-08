package com.example.sfmproject.Controllers.OAuthGitHub;

import com.example.sfmproject.JWT.JwtProvider;
import com.example.sfmproject.ServiceImpl.GitHubService;
import com.example.sfmproject.Entities.User; // Assuming you have a User entity
import com.example.sfmproject.Repositories.UserRepository; // Assuming you have a UserRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/github/admin")
public class GitHubAdminController {

    @Autowired
    private GitHubService gitHubService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository; // To access user data

    //githubtoken must be refreshed before using this controller
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/repos/{username}")
    public ResponseEntity<?> getUserReposAsAdmin(@PathVariable String username, @RequestHeader("Authorization") String token) {
        // Fetch the GitHub token for the specified user from the database
        String githubToken = userRepository.findByUsername(username)
                .map(User::getGithubToken)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            // Call GitHub API to get repositories using the user's token
            ResponseEntity<String> response = gitHubService.getUserRepos(githubToken);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch repositories: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/repos/{username}/{repo}/commits")
    public ResponseEntity<String> getUserRepoCommitsAsAdmin(
            @PathVariable String username,
            @PathVariable String repo,
            @RequestHeader("Authorization") String token) {

        // Fetch the GitHub token for the specified user
        String githubToken = userRepository.findByUsername(username)
                .map(User::getGithubToken)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            ResponseEntity<String> response = gitHubService.getCommits(githubToken, username, repo);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch commits: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/user/{username}")
    public ResponseEntity<String> getUserInfoAsAdmin(@PathVariable String username, @RequestHeader("Authorization") String token) {
        String githubToken = userRepository.findByUsername(username)
                .map(User::getGithubToken)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            ResponseEntity<String> response = gitHubService.getGitHubUserInfo(githubToken);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch user info: " + e.getMessage());
        }
    }


    //retrive the repo starred by username
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/starred/{username}")
    public ResponseEntity<String> getStarredReposAsAdmin(@PathVariable String username, @RequestHeader("Authorization") String token) {
        String githubToken = userRepository.findByUsername(username)
                .map(User::getGithubToken)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            ResponseEntity<String> response = gitHubService.getStarredRepos(githubToken);
            if (response.getBody() == null || response.getBody().isEmpty()) {
                return ResponseEntity.noContent().build(); // HTTP 204 No Content
            }
            return ResponseEntity.ok(response.getBody()); // HTTP 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch starred repositories: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/organizations/{username}")
    public ResponseEntity<String> getOrganizationsAsAdmin(@PathVariable String username, @RequestHeader("Authorization") String token) {
        String githubToken = userRepository.findByUsername(username)
                .map(User::getGithubToken)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            ResponseEntity<String> response = gitHubService.getOrganizations(githubToken);
            if (response.getBody() == null || response.getBody().isEmpty()) {
                return ResponseEntity.noContent().build(); // HTTP 204 No Content
            }
            return ResponseEntity.ok(response.getBody()); // HTTP 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch organizations: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/contributions/{username}")
    public ResponseEntity<String> getContributionDataAsAdmin(@PathVariable String username, @RequestHeader("Authorization") String token) {
        String githubToken = userRepository.findByUsername(username)
                .map(User::getGithubToken)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            ResponseEntity<String> response = gitHubService.getContributionData(githubToken);
            if (response.getBody() == null || response.getBody().isEmpty()) {
                return ResponseEntity.noContent().build(); // HTTP 204 No Content
            }
            return ResponseEntity.ok(response.getBody()); // HTTP 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch contribution data: " + e.getMessage());
        }
    }

}