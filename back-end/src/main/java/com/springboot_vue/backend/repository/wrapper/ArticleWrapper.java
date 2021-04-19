package com.springboot_vue.backend.repository.wrapper;

import com.springboot_vue.backend.entity.Article;
import com.springboot_vue.backend.vo.ArticleVo;
import com.springboot_vue.backend.vo.PageVo;

import java.util.List;

public interface ArticleWrapper {

	List<Article> listArticles(PageVo page);

	List<Article> listArticles(ArticleVo article, PageVo page);

	List<ArticleVo> listArchives();

}
