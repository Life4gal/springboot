package com.springboot_vue.backend.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.springboot_vue.backend.common.entity.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "blog_article")
public class Article extends BaseEntity<Integer> {

	public static final int Article_TOP = 1;

	public static final int Article_Common = 0;

	private static final long serialVersionUID = -4470366380115322213L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id")
	private User author;

	@OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "body_id")
	private ArticleBody body;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy.MM.dd HH:mm")
	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	/**
	 * 上一次修改时间
	 */
	@JSONField(format = "yyyy.MM.dd HH:mm")
	@Column(name = "update_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	@NotBlank
	@Column(name = "title", length = 40)
	private String title;

	@NotBlank
	@Column(name = "summary", length = 100)
	private String summary;

	/**
	 * 权重
	 */
	private int weight = Article_Common;

	@Column(name = "view_counts")
	private int viewCounts;

	@Column(name = "comment_counts")
	private int commentCounts;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "blog_article_tag",
			joinColumns = {@JoinColumn(name = "article_id")},
			inverseJoinColumns = {@JoinColumn(name = "tag_id")})
	private List<Tag> tags;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "article", orphanRemoval = true)
	private List<Comment> comments;

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}


	public ArticleBody getBody() {
		return body;
	}


	public void setBody(ArticleBody body) {
		this.body = body;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getCommentCounts() {
		return commentCounts;
	}

	public void setCommentCounts(int commentCounts) {
		this.commentCounts = commentCounts;
	}

	public int getViewCounts() {
		return viewCounts;
	}

	public void setViewCounts(int viewCounts) {
		this.viewCounts = viewCounts;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

}
