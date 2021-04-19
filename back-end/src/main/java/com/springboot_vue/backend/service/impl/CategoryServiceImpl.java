package com.springboot_vue.backend.service.impl;

import java.util.List;

import com.springboot_vue.backend.entity.Category;
import com.springboot_vue.backend.repository.CategoryRepository;
import com.springboot_vue.backend.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springboot_vue.backend.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;


	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	public Category getCategoryById(Integer id) {
		return categoryRepository.getOne(id);
	}

	@Override
	@Transactional
	public Integer saveCategory(Category category) {

		return Math.toIntExact(categoryRepository.save(category).getId());
	}

	@Override
	@Transactional
	public Integer updateCategory(Category category) {
		Category oldCategory = categoryRepository.getOne(Math.toIntExact(category.getId()));

		oldCategory.setCategoryName(category.getCategoryName());
		oldCategory.setAvatar(category.getAvatar());
		oldCategory.setDescription(category.getDescription());

		return Math.toIntExact(oldCategory.getId());
	}

	@Override
	@Transactional
	public void deleteCategoryById(Integer id) {
		categoryRepository.deleteById(id);
	}

	@Override
	public List<CategoryVo> findAllDetail() {
		return categoryRepository.findAllDetail();
	}

	@Override
	public CategoryVo getCategoryDetail(Integer categoryId) {
		return categoryRepository.getCategoryDetail(categoryId);
	}

}
