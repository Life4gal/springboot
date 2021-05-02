package com.springboot.eft.service.impl;

import com.springboot.eft.additional_utils.Checker;
import com.springboot.eft.dao.CategoryDAO;
import com.springboot.eft.entity.Category;
import com.springboot.eft.modules.constant.DefaultValues;
import com.springboot.eft.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {

	private final CategoryDAO categoryDAO;

	@Autowired
	public CategoryServiceImpl(CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}

	@Override
	public boolean insert(String name) {
		return Checker.isNotNull(name) && categoryDAO.insertCategory(name);
	}

	@Override
	public boolean remove(int id) {
		return isCategorized(id) && categoryDAO.removeCategoryById(id);
	}

	@Override
	public boolean update(int id, String name) {
		return Checker.isNotEmpty(name) && isCategorized(id) && categoryDAO.updateNameById(id, name);
	}

	private boolean isCategorized(int id) {
		return !DefaultValues.UNCATEGORIZED.equals(getById(id).getName());
	}

	@Override
	public Category getById(int id) {
		return categoryDAO.getCategoryById(id);
	}

	@Override
	public List<Category> list() {
		return categoryDAO.listCategory();
	}

	@Override
	public int getIdByName(String name) {
		try {
			return categoryDAO.getIdByName(name);
		} catch (Exception e) {
			return Integer.MAX_VALUE;
		}
	}
}
