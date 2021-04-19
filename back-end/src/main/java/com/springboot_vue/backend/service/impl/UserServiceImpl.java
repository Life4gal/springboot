package com.springboot_vue.backend.service.impl;

import com.springboot_vue.backend.common.util.PasswordHelper;
import com.springboot_vue.backend.entity.User;
import com.springboot_vue.backend.repository.UserRepository;
import com.springboot_vue.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User getUserByAccount(String account) {
		return userRepository.findByAccount(account);
	}

	@Override
	public User getUserById(Long id) {

		Optional<User> user = userRepository.findById(id);
		return user.isEmpty() ? null : user.get();
	}

	@Override
	@Transactional
	public Long saveUser(User user) {

		PasswordHelper.encryptPassword(user);
		int index = new Random().nextInt(6) + 1;
		String avatar = "/static/user/user_" + index + ".png";

		user.setAvatar(avatar);
		return userRepository.save(user).getId();
	}


	@Override
	@Transactional
	public Long updateUser(User user) {
		Optional<User> oldUser = userRepository.findById(user.getId());

		if(oldUser.isPresent())
		{
			oldUser.get().setNickname(user.getNickname());
			return oldUser.get().getId();
		}
		return 0L;
	}

	@Override
	@Transactional
	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}

}
