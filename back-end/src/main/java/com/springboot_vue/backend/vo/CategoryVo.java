package com.springboot_vue.backend.vo;

import com.springboot_vue.backend.entity.Category;

public class CategoryVo extends Category {

	private static final long serialVersionUID = -2975739216517528014L;

	private int articles;

	public int getArticles() {
		return articles;
	}

	public void setArticles(int articles) {
		this.articles = articles;
	}

}
