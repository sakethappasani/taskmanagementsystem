package com.stms.dev.controllers;

import com.stms.dev.dto.ProjectDisplayDTO;
import com.stms.dev.services.DeveloperService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("api/dev")
public class DeveloperController {
    private final DeveloperService developerService;

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

    @GetMapping("/projects")
    public ResponseEntity<?> getMyProjects() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            List<ProjectDisplayDTO> myProjects = developerService.getMyProjects(username);
            if (myProjects == null || myProjects.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(myProjects);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
