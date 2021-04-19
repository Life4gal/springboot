package com.springboot_vue.backend.repository;

import java.util.List;

import com.springboot_vue.backend.entity.Article;
import com.springboot_vue.backend.entity.Category;
import com.springboot_vue.backend.entity.Tag;
import com.springboot_vue.backend.repository.wrapper.ArticleWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Integer>, ArticleWrapper {

	List<Article> findByTags(Tag tag);

	List<Article> findByCategory(Category category);

	@Query(value = "select * from blog_article order by view_counts desc limit :limit", nativeQuery = true)
	List<Article> findOrderByViewsAndLimit(@Param("limit") int limit);

	@Query(value = "select * from blog_article order by create_date desc limit :limit", nativeQuery = true)
	List<Article> findOrderByCreateDateAndLimit(@Param("limit") int limit);

}
