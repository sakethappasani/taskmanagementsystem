package com.stms.dev.controllers;

import com.stms.dev.dto.AssignManagerDTO;
import com.stms.dev.dto.ManagerDisplayDTO;
import com.stms.dev.dto.ProjectDTO;
import com.stms.dev.dto.ProjectDisplayDTO;
import com.stms.dev.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    private final ProjectService projectService;
    private ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-project")
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            boolean isCreated = projectService.createProject(username, projectDTO);
            if (isCreated) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Project created successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Project creation failed");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("admin/my-projects")
    public ResponseEntity<?> getMyProjects(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            List<ProjectDisplayDTO> myProjectsList = projectService.getMyProjects(username);
            if(myProjectsList.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You Have no Projects");
            return ResponseEntity.status(HttpStatus.OK).body(myProjectsList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("get-managers")
    public ResponseEntity<?> getProjectManagers(){
        try {
            List<ManagerDisplayDTO> managerList = projectService.getAllManagers();
            if(managerList.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You Have no Managers");
            else return ResponseEntity.status(HttpStatus.OK).body(managerList);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("admin/assign-manager")
    public ResponseEntity<?> assignManager(@Valid @RequestBody AssignManagerDTO assignManagerDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            boolean isAssigned = projectService.assignProjectManager(username, assignManagerDTO);
            if (isAssigned){
                return ResponseEntity.ok("Manager Assigned Successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Manager Assignment Failed");
            }
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }
}
