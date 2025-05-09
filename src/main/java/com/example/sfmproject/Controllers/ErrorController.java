package com.example.sfmproject.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        Object statusObj = request.getAttribute("jakarta.servlet.error.status_code");
        if (statusObj == null) {
            // Fallback: default to 500 if status code is not found
            response.put("status", 500);
            response.put("message", "Unknown error");
            return ResponseEntity.status(500).body(response);
        }

        int statusCode = Integer.parseInt(statusObj.toString());
        Object messageObj = request.getAttribute("jakarta.servlet.error.message");

        response.put("status", statusCode);
        response.put("message", messageObj != null ? messageObj : "No message available");

        return ResponseEntity.status(statusCode).body(response);
    }
}
