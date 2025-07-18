package com.example.sfmproject.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GitHubRepoRequest {
    private String name;
    private String description;
    @JsonProperty("private")
    private boolean isPrivate;
    private String html_url; // This field maps to the JSON response

}

