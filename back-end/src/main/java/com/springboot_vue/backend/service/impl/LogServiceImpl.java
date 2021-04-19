package com.springboot_vue.backend.service.impl;

import com.springboot_vue.backend.entity.Log;
import com.springboot_vue.backend.repository.LogRepository;
import com.springboot_vue.backend.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {

	@Autowired
	private LogRepository logRepository;

	@Override
	public Integer saveLog(Log log) {
		return logRepository.save(log).getId();
	}

}
