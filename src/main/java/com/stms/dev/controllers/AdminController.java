package com.stms.dev.controllers;

import com.stms.dev.dto.ManageRoleDTO;
import com.stms.dev.dto.UserProfileDTO;
import com.stms.dev.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("users")
    public ResponseEntity<List<UserProfileDTO>> getAllUsers() {
        try {
            List<UserProfileDTO> userProfiles = adminService.getAllUsers();
            return ResponseEntity.status(HttpStatus.OK).body(userProfiles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/manage-role")
    public ResponseEntity<?> manageRole(@Valid @RequestBody ManageRoleDTO manageRoleDTO) {
        try {
            boolean isUpdated = adminService.manageUserRole(manageRoleDTO);
            if (isUpdated) {
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
