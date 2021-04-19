package com.springboot_vue.backend.repository;

import com.springboot_vue.backend.entity.Category;
import com.springboot_vue.backend.repository.wrapper.CategoryWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer>, CategoryWrapper {

	/*
	@Query(value="select c.*, count(a.category_id) as articles from blog_category c "
			+ "left join blog_article a on a.category_id = c.id group by a.category_id",nativeQuery=true)
	List<Category> findAllDetail();
	*/
}
