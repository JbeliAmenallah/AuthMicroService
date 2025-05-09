package com.example.sfmproject.ServiceImpl;

import com.example.sfmproject.Entities.Enum.RoleUser;
import com.example.sfmproject.Entities.Role;
import com.example.sfmproject.Entities.User;
import com.example.sfmproject.Repositories.RoleRepository;
import com.example.sfmproject.Repositories.UserRepository;
import com.example.sfmproject.Services.RoleServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class RoleServiceIMP implements RoleServiceInterface {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public void addRole(RoleUser roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        roleRepository.save(role);
    }

    @Override
    public void deleteRole(RoleUser roleName) {
        Role roleToDelete = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        roleRepository.delete(roleToDelete);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void AddALLRoles() {
        for (RoleUser roleName : RoleUser.values()) {
            addRole(roleName);
        }
    }

    public ResponseEntity<String> updateuserrole(String roleName, Long idUser) {
        Role userRole = roleRepository.findByRoleName(RoleUser.valueOf(roleName.trim()))
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
        User userToUpdate = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User not find."));
        //userToUpdate.getRoles().clear(); //Might change depending on number of roles of the user
        userToUpdate.getRoles().add(userRole);
        userRepository.save(userToUpdate);
        return ResponseEntity.ok().body("User Role updated successfully!");
    }

}