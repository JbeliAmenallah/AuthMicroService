package com.example.sfmproject.Repositories;


import com.example.sfmproject.Entities.Enum.RoleUser;
import com.example.sfmproject.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(RoleUser roleName );
    //Optional<Role> findByName(RoleUser roleName );

}
