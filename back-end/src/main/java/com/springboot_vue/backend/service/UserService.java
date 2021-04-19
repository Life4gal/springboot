package com.springboot_vue.backend.service;

import com.springboot_vue.backend.entity.User;

import java.util.List;

public interface UserService {

	List<User> findAll();

	User getUserByAccount(String account);

	User getUserById(Long id);

	Long saveUser(User user);

	Long updateUser(User user);

	void deleteUserById(Long id);

}
