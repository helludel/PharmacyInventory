package com.pharmacynew.hanaelnael.DAO;

import com.pharmacynew.hanaelnael.Entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDAO extends CrudRepository<Role,Integer>{


    Role findByRoleName(String roleName);
}
