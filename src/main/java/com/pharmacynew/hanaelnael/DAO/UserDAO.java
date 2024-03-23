package com.pharmacynew.hanaelnael.DAO;

import com.pharmacynew.hanaelnael.Entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserDAO extends CrudRepository<User,Integer> {
    public User findByUsername(String userName);
    public User findByEmail(String email);
    boolean existsByEmail(String email);
}
