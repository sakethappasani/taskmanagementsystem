package com.stms.dev.services;

import com.stms.dev.dto.AssignDevelopersDTO;
import com.stms.dev.dto.DevelopersListDTO;
import com.stms.dev.dto.ProjectDisplayDTO;
import com.stms.dev.models.Project;
import com.stms.dev.models.Role;
import com.stms.dev.models.User;
import com.stms.dev.repository.ProjectRepository;
import com.stms.dev.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public ManagerService(UserRepository userRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public List<ProjectDisplayDTO> getProjects(String username) {
        Optional<User> opUser = userRepository.findByUsername(username);
        if (opUser.isEmpty()) return new ArrayList<>();
        User user = opUser.get();
        List<Project> projectList = projectRepository.findManagerProjectsById(user.getId());
        if (projectList.isEmpty()) return new ArrayList<>();
        return projectList.stream()
                .map(project -> new ProjectDisplayDTO(project.getId(), project.getTitle(), project.getDescription(), project.getStartDate(), project.getEndDate(), project.getCreateDate(), project.getManager().getName()))
                .toList();
    }

    public List<DevelopersListDTO> getDevelopers() {
        List<User> developersList = userRepository.findUserByRole(Role.DEVELOPER);
        if (developersList.isEmpty()) return new ArrayList<>();
        else return developersList.stream()
                .map(dev -> new DevelopersListDTO(dev.getId(), dev.getName(), dev.getUsername()))
                .toList();
    }

    public boolean assignDeveloper(String username, AssignDevelopersDTO assignDevelopersDTO) {
        Optional<User> opManager = userRepository.findByUsername(username);
        Optional<Project> opProject = projectRepository.findById(assignDevelopersDTO.getProjectId());
        if(opManager.isEmpty() || opProject.isEmpty()) return false;
        User manager = opManager.get();
        Project project = opProject.get();
        if (project.getManager() == null || !project.getManager().getId().equals(manager.getId())) return false;
        List<User> developersToAssign = new ArrayList<>();
        for (Long devId : assignDevelopersDTO.getDeveloperIds()) {
            Optional<User> opDeveloper = userRepository.findById(devId);
            if (opDeveloper.isPresent()) {
                User developer = opDeveloper.get();
                developersToAssign.add(developer);
            }
        }
        if (developersToAssign.isEmpty()) return false;
        List<User> currentTeam = project.getTeamMembers();
        if (currentTeam == null) currentTeam = new ArrayList<>();
        currentTeam.addAll(developersToAssign);
        project.setTeamMembers(currentTeam);
        projectRepository.save(project);
        return true;
    }


    public List<DevelopersListDTO> getDevelopersNotInTeam(String username, long projectId) {
        Optional<User> opManager = userRepository.findByUsername(username);
        Optional<Project> opProject = projectRepository.findById(projectId);
        if(opManager.isEmpty() || opProject.isEmpty()) return new ArrayList<>();
        User manager = opManager.get();
        Project project = opProject.get();
        if(!project.getManager().getId().equals(manager.getId())) return new ArrayList<>();
        List<User> teamMembers = project.getTeamMembers();
        if(teamMembers == null || teamMembers.isEmpty()) return getDevelopers();
        List<Long> developerIds = new ArrayList<>();
        for(User id : teamMembers){
            developerIds.add(id.getId());
        }
        return getDevelopers().stream().filter(dev -> !developerIds.contains(dev.getDeveloperId()))
                .toList();
    }



}
