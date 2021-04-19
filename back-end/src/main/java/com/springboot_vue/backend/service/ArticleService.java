package com.springboot_vue.backend.service;

import com.springboot_vue.backend.entity.Article;
import com.springboot_vue.backend.vo.ArticleVo;
import com.springboot_vue.backend.vo.PageVo;

import java.util.List;

public interface ArticleService {

	List<Article> listArticles(PageVo page);

	List<Article> listArticles(ArticleVo article, PageVo page);

	List<Article> findAll();

	Article getArticleById(Integer id);

	Integer publishArticle(Article article);

	Integer saveArticle(Article article);

	Integer updateArticle(Article article);

	void deleteArticleById(Integer id);

	List<Article> listArticlesByTag(Integer id);

	List<Article> listArticlesByCategory(Integer id);

	Article getArticleAndAddViews(Integer id);

	List<Article> listHotArticles(int limit);

	List<Article> listNewArticles(int limit);

	List<ArticleVo> listArchives();

}
