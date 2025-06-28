package com.example.sfmproject.DTO;

import lombok.Data;

@Data
public class GitHubRepoResponse {
    private String html_url; // This field maps to the JSON response
    private String name;
    private String description;
    // Add other fields as needed
}