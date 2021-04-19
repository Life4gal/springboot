package com.springboot_vue.backend.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import com.springboot_vue.backend.common.entity.BaseEntity;

@Entity
@Table(name = "blog_article_body")
public class ArticleBody extends BaseEntity<Long> {

	private static final long serialVersionUID = -7611409995977927628L;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String content; // 内容

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String contentHtml;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentHtml() {
		return contentHtml;
	}

	public void setContentHtml(String contentHtml) {
		this.contentHtml = contentHtml;
	}

}
