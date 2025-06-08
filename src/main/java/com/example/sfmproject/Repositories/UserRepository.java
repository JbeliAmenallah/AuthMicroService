package com.example.sfmproject.Repositories;

import com.example.sfmproject.Entities.Classe;
import com.example.sfmproject.Entities.Role;
import com.example.sfmproject.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String username);

    //User findByUsername(String username);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByRolesContains(Role role);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.roleName = 'enseignant'")
    List<User> findAllTeachers();


    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.roleName = 'etudiant'")
    List<User> findAllStudents();

    @Query("SELECT u FROM User u WHERE u.classEntity.idClasse = :classId AND :role MEMBER OF u.roles")
    List<User> findStudentsByClassId(@Param("classId") Long classId, @Param("role") Role role);

    @Query("SELECT u.classEntity FROM User u WHERE u.id = :studentId AND :role MEMBER OF u.roles")
    Optional<Classe> findClassByStudentIdAndRole(@Param("studentId") Long studentId, @Param("role") Role role);
}
