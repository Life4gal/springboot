package com.springboot_vue.backend.repository.impl;

import com.springboot_vue.backend.entity.Article;
import com.springboot_vue.backend.repository.wrapper.ArticleWrapper;
import com.springboot_vue.backend.vo.ArticleVo;
import com.springboot_vue.backend.vo.PageVo;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.IntegerType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class ArticleRepositoryImpl implements ArticleWrapper {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Article> listArticles(PageVo page) {

		StringBuilder hql = new StringBuilder("from Article");

		if (null != page.getName() && !"".equals(page.getName())) {
			hql.append(" order by ");
			hql.append(page.getName());
		}

		if (null != page.getSort() && !"".equals(page.getSort())) {
			hql.append(" ");
			hql.append(page.getSort());
		}

		NativeQuery query = getSession().createNativeQuery(hql.toString());

		if (null != page.getPageNumber() && null != page.getPageSize()) {
			query.setFirstResult(page.getPageSize() * (page.getPageNumber() - 1));
			query.setMaxResults(page.getPageSize());
		}

		return query.list();

	}

	@Override
	public List<Article> listArticles(ArticleVo article, PageVo page) {

		StringBuilder hql = new StringBuilder("from Article a ");

		if (null != article.getTagId()) {

			hql.append(" inner join fetch a.tags t");
		}

		hql.append(" where 1=1 ");

		if (null != article.getCategoryId()) {

			hql.append(" and a.category.id = :categoryId");
		}

		if (null != article.getTagId()) {

			hql.append(" and t.id = :tagId");
		}


		if (null != article.getYear()) {
			hql.append(" and YEAR(a.createDate) = :year");
		}

		if (null != article.getMonth()) {
			hql.append(" and MONTH(a.createDate) = :month");
		}

		if (null != page.getName() && !"".equals(page.getName())) {
			hql.append(" order by ");
			hql.append(page.getName());
		}

		if (null != page.getSort() && !"".equals(page.getSort())) {
			hql.append(" ");
			hql.append(page.getSort());
		}


		NativeQuery query = getSession().createNativeQuery(hql.toString());

		if (null != article.getYear()) {
			query.setParameter("year", article.getYear());
		}

		if (null != article.getMonth()) {
			query.setParameter("month", article.getMonth());
		}

		if (null != article.getTagId()) {
			query.setParameter("tagId", article.getTagId());
		}

		if (null != article.getCategoryId()) {
			query.setParameter("categoryId", article.getCategoryId());
		}

		if (null != page.getPageNumber() && null != page.getPageSize()) {
			query.setFirstResult(page.getPageSize() * (page.getPageNumber() - 1));
			query.setMaxResults(page.getPageSize());
		}

		return query.list();
	}

	@Override
	public List<ArticleVo> listArchives() {

		String sql = "select year(create_date) as year,month(create_date) as month,count(*) as count from blog_article group by year(create_date),month(create_date)";

		NativeQuery query = getSession().createNativeQuery(sql);

		query.addScalar("year");
		query.addScalar("month");
		query.addScalar("count", IntegerType.INSTANCE);

		return query.list();
	}

	private Session getSession() {
		return em.unwrap(Session.class);
	}

}
