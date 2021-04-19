package com.springboot_vue.backend.repository;

import com.springboot_vue.backend.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Integer> {

}
