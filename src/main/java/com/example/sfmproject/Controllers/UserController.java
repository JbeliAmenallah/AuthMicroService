package com.example.sfmproject.Controllers;

import com.example.sfmproject.DTO.ResetPass;
import com.example.sfmproject.Entities.Classe;
import com.example.sfmproject.Entities.Enum.RoleUser;
import com.example.sfmproject.Entities.Repository;
import com.example.sfmproject.Entities.User;
import com.example.sfmproject.Repositories.UserRepository;
import com.example.sfmproject.ServiceImpl.RepositoryServiceIMP;
import com.example.sfmproject.ServiceImpl.UserServiceIMP;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserServiceIMP userServiceIMP;

    @Autowired
    UserRepository userRepository ;

    @Autowired
    RepositoryServiceIMP repositoryServiceIMP;


    @GetMapping("/list-user")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> ListUser() {
        return userServiceIMP.getAllUser();
    }

    @GetMapping("/list-repo")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Repository> ListRepo() {
        return repositoryServiceIMP.getAllRepositories();
    }

    @GetMapping("/repositories")
    public ResponseEntity<List<Repository>> getUserRepositories(@RequestHeader("Authorization") String token) {
        User loggedInUser = userServiceIMP.getCurrentUserFromToken(token) // Implement this method to extract user from token
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Repository> repositories = repositoryServiceIMP.getRepositoriesByUser(loggedInUser);
        return ResponseEntity.ok(repositories);
    }


    @PutMapping("/validate-user/{idUser}")
    @PreAuthorize("hasRole('ADMIN')")
    public void validInscription(@PathVariable("idUser") Long idUser) {
        userServiceIMP.validInscription(idUser);
    }

    @PutMapping("/debloque-user/{idUser}")
    public void debloqueUser(@PathVariable("idUser") Long idUser) {
        userServiceIMP.debloqueUser(idUser);
    }

    @PutMapping("/bloque-user/{idUser}")
    @PreAuthorize("hasRole('ADMIN')")
    public void bloqueUser(@PathVariable("idUser") Long idUser) {
        userServiceIMP.bloqueUser(idUser);
    }

    @DeleteMapping("/delete-user/{idUser}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAccount(@PathVariable("idUser") Long idUser) {
        userServiceIMP.deleteUser(idUser);
    }

    @GetMapping("/list-RolesName/{RolesName}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> ListUserByRoles(@PathVariable("RolesName") RoleUser roleName) {
        return userServiceIMP.getUserByRoles(roleName);
    }
    @PutMapping("forgetpass/{username}")
    public ResponseEntity<?> forgetPassword(@PathVariable("username") String username, @RequestBody ResetPass resetPass) {
        return userServiceIMP.updatePassword(username,resetPass);
    }
    @PostMapping("forgetpassword/{email}")
    public ResponseEntity<?> userForgetPassword(@PathVariable("email") String email) {
        return userServiceIMP.userforgetpassword(email);
    }
    @PutMapping("forgetpassbyemail/{email}")
    public ResponseEntity<?> forgetPasswordbyemail(@PathVariable("email") String email, @RequestBody ResetPass resetPass) {
        return userServiceIMP.updatePasswordBymail(email,resetPass);
    }
    /*  @GetMapping("/Alluserprofiles/")

      public List<User> getAlluserprofiles() {
          return userServiceIMP.getAlluserprofiles();
      }

     */
    @GetMapping("/getuserbyid/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long userId, @RequestHeader("Authorization") String token) {
        // Fetch the user from the database
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            // You can set the token in the user response (optional)
            user.get().setToken(token);
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /*
    @GetMapping("/list-User/ASC")
    public List<User> getUsersOrderBySum_totalAsc() {
        return userService.getUsersOrderBySum_totalAsc();
    }

    @GetMapping("/sorted-by-role/{role}")
    public List<User> getUsersSortedByRole(@PathVariable("role") String role) {
       RoleName role1= RoleName.valueOf(role);
        return userService.getAllUserByRoleOrderSum_total(role1);
    }
    */

    @PutMapping("/updateRoles/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUserRoles(@PathVariable Long userId, @RequestBody List<String> roleNames) {
        return userServiceIMP.updateUserRoles(userId, roleNames);
    }



    //imen houas
    @GetMapping("/allteachers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllTeachers() {
        return ResponseEntity.ok(userServiceIMP.getAllTeachers());
    }

    @GetMapping("/all-students")
    public ResponseEntity<List<User>> getAllStudents() {
        return ResponseEntity.ok(userServiceIMP.getAllStudents());
    }

    @GetMapping("/teacher/classes")
    @PreAuthorize("hasRole('enseignant')")
    public ResponseEntity<List<Classe>> getClassesForTeacher() {
        User loggedInUser = userServiceIMP.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Classe> classes = userServiceIMP.getClassesForTeacher(loggedInUser.getId());
        return ResponseEntity.ok(classes);
    }

    @GetMapping("/student/class")
    @PreAuthorize("hasRole('etudiant')")
    public ResponseEntity<Classe> getClassForStudent() {
        User loggedInUser = userServiceIMP.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User not found"));
        Classe studentClass = userServiceIMP.getClassForStudent(loggedInUser.getId());
        return ResponseEntity.ok(studentClass);
    }


}