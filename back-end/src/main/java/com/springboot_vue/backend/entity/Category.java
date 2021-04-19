package com.springboot_vue.backend.entity;

import com.springboot_vue.backend.common.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "blog_category")
public class Category extends BaseEntity<Long> {

	private static final long serialVersionUID = 5025313969040054182L;

	@NotBlank
	private String avatar;

	@NotBlank
	private String category_name;

	@NotBlank
	private String description;

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getCategoryName() {
		return category_name;
	}

	public void setCategoryName(String category_name) {
		this.category_name = category_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
