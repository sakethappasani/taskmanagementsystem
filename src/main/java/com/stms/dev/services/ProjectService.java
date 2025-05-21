package com.stms.dev.services;

import com.stms.dev.dto.AssignManagerDTO;
import com.stms.dev.dto.ManagerDisplayDTO;
import com.stms.dev.dto.ProjectDTO;
import com.stms.dev.dto.ProjectDisplayDTO;
import com.stms.dev.models.Project;
import com.stms.dev.models.Role;
import com.stms.dev.models.User;
import com.stms.dev.repository.ProjectRepository;
import com.stms.dev.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public boolean createProject(String username, ProjectDTO projectDTO) {
        Optional<User> userInDb = userRepository.findByUsername(username);
        if (userInDb.isEmpty()) return false;
        User user = userInDb.get();
        Project project = Project.builder().
                title(projectDTO.getTitle())
                .description(projectDTO.getDescription())
                .createDate(LocalDate.now())
                .startDate(projectDTO.getStartDate())
                .endDate(projectDTO.getEndDate())
                .createdBy(user)
                .build();
        projectRepository.save(project);
        return true;
    }

    public List<ProjectDisplayDTO> getMyProjects(String username) {
        Optional<User> userInDb = userRepository.findByUsername(username);
        if (userInDb.isEmpty()) return new ArrayList<>();
        User user = userInDb.get();
        List<Project> projectList = projectRepository.findAdminProjectsById(user.getId());
        return projectList.stream()
                .map(project -> new ProjectDisplayDTO(project.getId(), project.getTitle(), project.getDescription(), project.getStartDate(), project.getEndDate(), project.getCreateDate(), project.getManager() != null ? project.getManager().getName() : null))
                .toList();
    }

    public List<ManagerDisplayDTO> getAllManagers() {
        List<User> managersList = userRepository.findUserByRole(Role.MANAGER);
        if (managersList.isEmpty()) return new ArrayList<>();
        return managersList.stream().map(
                manager -> new ManagerDisplayDTO(manager.getId(), manager.getName())
        ).toList();
    }

    public boolean assignProjectManager(String username, AssignManagerDTO assignManagerDTO){
        Optional<User> opAdmin = userRepository.findByUsername(username);
        Optional<User> opManager = userRepository.findById(assignManagerDTO.getManagerId());
        Optional<Project> opProject = projectRepository.findById(assignManagerDTO.getProjectId());

        if(opAdmin.isEmpty() || opManager.isEmpty() || opProject.isEmpty()) return false;

        User admin = opAdmin.get();
        User manager = opManager.get();
        Project project = opProject.get();

        if(!project.getCreatedBy().equals(admin)) return false;
        project.setManager(manager);
        projectRepository.save(project);
        return true;
    }



}
