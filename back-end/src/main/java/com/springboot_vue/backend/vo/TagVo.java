package com.springboot_vue.backend.vo;

import com.springboot_vue.backend.entity.Tag;

public class TagVo extends Tag {

	private static final long serialVersionUID = 5059212992497947120L;
	private int articles;

	public int getArticles() {
		return articles;
	}

	public void setArticles(int articles) {
		this.articles = articles;
	}

}
