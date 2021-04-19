package com.springboot_vue.backend.service;

import com.springboot_vue.backend.entity.Category;
import com.springboot_vue.backend.vo.CategoryVo;

import java.util.List;

public interface CategoryService {

	List<Category> findAll();

	Category getCategoryById(Integer id);

	Integer saveCategory(Category category);

	Integer updateCategory(Category category);

	void deleteCategoryById(Integer id);

	List<CategoryVo> findAllDetail();

	CategoryVo getCategoryDetail(Integer categoryId);

}
