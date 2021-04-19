package com.springboot_vue.backend.repository;

import com.springboot_vue.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByAccount(String account);

}
