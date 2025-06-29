package com.example.sfmproject.Repositories;

import com.example.sfmproject.Entities.Repository;
import com.example.sfmproject.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositoryRepository  extends JpaRepository<Repository, Long> {
    List<Repository> findByCreator(User creator); // Find repositories created by the user
    List<Repository> findByCollaboratorsContaining(User collaborator);
    Repository findByGithubName(String githubName); // Find repository by its GitHub name
}
