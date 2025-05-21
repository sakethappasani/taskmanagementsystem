package com.stms.dev.controllers;

import com.stms.dev.dto.AssignDevelopersDTO;
import com.stms.dev.dto.DevelopersListDTO;
import com.stms.dev.dto.ProjectDisplayDTO;
import com.stms.dev.services.ManagerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("my-projects")
    public ResponseEntity<List<ProjectDisplayDTO>> getMyProjects() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            List<ProjectDisplayDTO> myProjects = managerService.getProjects(username);
            if(myProjects.isEmpty()) return ResponseEntity.noContent().build();
            return ResponseEntity.ok(myProjects);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("get-developers")
    public ResponseEntity<List<DevelopersListDTO>> getAllDevelopers() {
        try{
            List<DevelopersListDTO> developers = managerService.getDevelopers();
            if(developers.isEmpty()) return ResponseEntity.noContent().build();
            return ResponseEntity.ok(developers);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("assign-developers")
    public ResponseEntity<?> assignDevelopers(@Valid @RequestBody AssignDevelopersDTO developersDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            boolean areDevelopersAssigned = managerService.assignDeveloper(username, developersDTO);
            if(areDevelopersAssigned) return ResponseEntity.noContent().build();
            else return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("project/{projectId}/available-developers")
    public ResponseEntity<?> getDevelopersForTeam(@PathVariable long projectId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            List<DevelopersListDTO> developersNotInTeam = managerService.getDevelopersNotInTeam(username, projectId);
            if(developersNotInTeam.isEmpty())
            {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(developersNotInTeam);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
