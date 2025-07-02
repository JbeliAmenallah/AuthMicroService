package com.example.sfmproject.Controllers;

import com.example.sfmproject.DTO.GitHubRepoRequest;
import com.example.sfmproject.DTO.GitHubRepoResponse;
import com.example.sfmproject.Entities.Repository;
import com.example.sfmproject.Entities.User;
import com.example.sfmproject.JWT.JwtProvider;
import com.example.sfmproject.Repositories.RepositoryRepository;
import com.example.sfmproject.Repositories.UserRepository;
import com.example.sfmproject.ServiceImpl.GitHubService;
import com.example.sfmproject.ServiceImpl.RepositoryServiceIMP;
import com.example.sfmproject.ServiceImpl.UserServiceIMP;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/repository")
public class RepositoryController {

    @Autowired
    private RepositoryServiceIMP repositoryService;

    @Autowired
    private UserServiceIMP userService;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private RepositoryRepository repositoryRepository;

    private final JwtProvider jwtProvider;
    private final RestTemplate restTemplate;

    public RepositoryController(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
        this.restTemplate = new RestTemplate();
    }
    @PostMapping("/create-repository")
    public ResponseEntity<?> createRepository(@RequestBody GitHubRepoRequest repoRequest,
                                              @RequestHeader("Authorization") String authHeader) {
        try {
            // Step 1: Extract JWT from header
            String jwt = authHeader.replace("Bearer ", "");

            // Step 2: Extract user subject (username or email) from your JWT
            String userSub = extractUserSubFromJwt(jwt);
            if (userSub == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            // Step 3: Fetch the user based on the extracted subject
            User creator = userService.findByUsername(userSub); // Implement this method to find user by subject
            if (creator == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Step 4: Extract GitHub token from your JWT
            String githubToken = jwtProvider.extractGithubAccessToken(jwt);
            System.out.println("githubtoken: " + githubToken);

            // Step 5: Prepare headers for GitHub API
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(githubToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<GitHubRepoRequest> entity = new HttpEntity<>(repoRequest, headers);

            // Step 6: Call GitHub API
            ResponseEntity<GitHubRepoResponse> response = restTemplate.exchange(
                    "https://api.github.com/user/repos",
                    HttpMethod.POST,
                    entity,
                    GitHubRepoResponse.class // Use the new response class
            );

            // Step 7: If successful, save the repository to the database
            if (response.getStatusCode().is2xxSuccessful()) {
                GitHubRepoResponse repoResponse = response.getBody(); // Get the parsed response
                Repository repository = new Repository();
                repository.setGithubName(repoRequest.getName());
                repository.setGithubUrl(repoResponse.getHtml_url()); // Extract URL
                repository.setDescription(repoRequest.getDescription());
                repository.setCreatedAt(new Date()); // Set current date
                repository.setCreator(creator);
                repository.setClasse(creator.getClassEntity());// Set the creator as the fetched user

                // Save the repository to the database
                repositoryRepository.save(repository);

                return ResponseEntity.status(HttpStatus.CREATED).body("Repository created and saved to database.");
            }

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body("GitHub API Error: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
    private String jwtSecret = "azerty"; // Injecting the secret from application properties

    public String extractUserSubFromJwt(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject(); // Return the subject directly
        } catch (Exception e) {
            // Handle exceptions (e.g., token expired, invalid token)
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/repos")
    public ResponseEntity<?> getGitHubRepos(@RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        String githubToken = jwtProvider.extractGithubAccessToken(jwt);

        // Fetching user details using the token if needed
        User currentUser = userService.getCurrentUserFromToken(jwt).get();

        // Fetching repositories from the database
        List<Repository> userRepos = repositoryService.getRepositoriesByUser(currentUser);

        return ResponseEntity.ok(userRepos);
    }

    @PutMapping("/{repositoryId}/assign-average-grade")
    public ResponseEntity<Void> assignAverageGrade(@PathVariable Long repositoryId) {
        repositoryService.assignAverageGradeToRepository(repositoryId);
        return ResponseEntity.ok().build(); // Return 200 OK if successful
    }
}
