package com.stms.dev.repository;

import com.stms.dev.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("select p from Project p where p.createdBy.id = ?1")
    List<Project> findAdminProjectsById(Long id);

    @Query("select p from Project p where p.manager.id = ?1")
    List<Project> findManagerProjectsById(Long id);

    @Query("select p from Project p JOIN p.teamMembers tm where tm.id = ?1")
    List<Project> findProjectsByDeveloperId(Long developerId);
}
