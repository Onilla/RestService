package com.homework.repository;

import com.homework.entity.User;

import java.util.List;

public interface UserRepository extends Repository <User,Long>{
    List<User> findByCompanyId(Long id);
}
