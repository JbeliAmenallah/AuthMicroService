package com.example.sfmproject.Services;


import com.example.sfmproject.Entities.Enum.RoleUser;
import com.example.sfmproject.Entities.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {

    public List<User> getAllUser() ;

    public User getUserById(Long idUser);

    public List<User> getUserByRoles(RoleUser roleName);

    public User deleteUser(Long id);


    public void bloqueUser(Long id);

    public void updateUser(Long id);

    public void validInscription(Long id) ;

    public ResponseEntity<User> registerUser(User user1, String roleName);

    public ResponseEntity<User> registerEntreprise(User user1);



    public ResponseEntity<User> registerAdmin(@Valid @RequestBody User user);




    public Optional<User> getCurrentUser() ;


    public User updateUser(User user ,  Long idUser) ;
}