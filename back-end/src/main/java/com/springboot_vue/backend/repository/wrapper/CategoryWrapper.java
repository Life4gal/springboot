package com.springboot_vue.backend.repository.wrapper;

import com.springboot_vue.backend.vo.CategoryVo;

import java.util.List;

public interface CategoryWrapper {

	List<CategoryVo> findAllDetail();

	CategoryVo getCategoryDetail(Integer categoryId);

}
