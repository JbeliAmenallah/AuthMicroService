package com.example.sfmproject.Services;

import com.example.sfmproject.Entities.Enum.RoleUser;
import com.example.sfmproject.Entities.Role;

import java.util.List;

public interface RoleServiceInterface {
    void addRole(RoleUser roleUser);
    void deleteRole(RoleUser roleUser);
    List<Role> getAllRoles();
    void AddALLRoles();
}
