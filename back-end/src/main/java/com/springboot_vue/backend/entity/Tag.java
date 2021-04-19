package com.springboot_vue.backend.entity;

import com.springboot_vue.backend.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "blog_tag")
public class Tag extends BaseEntity<Integer> {

	private static final long serialVersionUID = 5025313969040054182L;

	@NotBlank
	@Column(name = "avatar")
	private String avatar;

	@NotBlank
	@Column(name = "tag_name")
	private String tag_name;

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getTagName() {
		return tag_name;
	}

	public void setTagName(String tag_name) {
		this.tag_name = tag_name;
	}

}
