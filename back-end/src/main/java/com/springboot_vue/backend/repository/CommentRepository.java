package com.springboot_vue.backend.repository;

import com.springboot_vue.backend.entity.Article;
import com.springboot_vue.backend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

	List<Comment> findByArticleAndLevelOrderByCreateDateDesc(Article a, String level);

}
