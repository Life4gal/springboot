package com.springboot_vue.backend.repository.impl;

import com.springboot_vue.backend.repository.wrapper.CategoryWrapper;
import com.springboot_vue.backend.vo.CategoryVo;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.IntegerType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryWrapper {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<CategoryVo> findAllDetail() {

		String sql = "select c.*, count(a.category_id) as articles from blog_category c "
				+ "left join blog_article a on a.category_id = c.id group by c.id";

		NativeQuery query = getSession().createNativeQuery(sql);
		query.addScalar("id");
		query.addScalar("category_name");
		query.addScalar("description");
		query.addScalar("avatar");
		query.addScalar("articles", IntegerType.INSTANCE);

		return query.list();
	}

	@Override
	public CategoryVo getCategoryDetail(Integer categoryId) {
		String sql = "select c.*, count(a.category_id) as articles from blog_category c "
				+ "left join blog_article a on a.category_id = c.id where c.id = :categoryId";

		NativeQuery query = getSession().createNativeQuery(sql);

		query.setParameter("categoryId", categoryId);

		query.addScalar("id");
		query.addScalar("category_name");
		query.addScalar("description");
		query.addScalar("avatar");
		query.addScalar("articles", IntegerType.INSTANCE);

		return (CategoryVo) query.uniqueResult();
	}

	private Session getSession() {
		return em.unwrap(Session.class);
	}

}
