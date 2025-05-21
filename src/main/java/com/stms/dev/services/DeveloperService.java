package com.stms.dev.services;

import com.stms.dev.dto.ProjectDisplayDTO;
import com.stms.dev.models.Project;
import com.stms.dev.models.User;
import com.stms.dev.repository.ProjectRepository;
import com.stms.dev.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class DeveloperService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public DeveloperService(UserRepository userRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public List<ProjectDisplayDTO> getMyProjects(String username) {
        Optional<User> opUser = userRepository.findByUsername(username);
        if (opUser.isEmpty()) return List.of();
        User user = opUser.get();
        List<Project> projectList = projectRepository.findProjectsByDeveloperId(user.getId());
        if (projectList.isEmpty()) return List.of();
        return projectList.stream()
                .map(project -> new ProjectDisplayDTO(
                        project.getId(),
                        project.getTitle(),
                        project.getDescription(),
                        project.getStartDate(),
                        project.getEndDate(),
                        project.getCreateDate(),
                        project.getManager().getName()))
                .toList();
    }
}
