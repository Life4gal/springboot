package com.springboot_vue.backend.repository.impl;

import com.springboot_vue.backend.repository.wrapper.TagWrapper;
import com.springboot_vue.backend.vo.TagVo;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.IntegerType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class TagRepositoryImpl implements TagWrapper {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<TagVo> findAllDetail() {

		String sql = "select t.*,count(at.tag_id ) as articles from blog_article_tag at "
				+ "right join blog_tag t on t.id = at.tag_id group by t.id ";

		NativeQuery query = getSession().createNativeQuery(sql);
		query.addScalar("id");
		query.addScalar("tag_name");
		query.addScalar("avatar");
		query.addScalar("articles", IntegerType.INSTANCE);

		return query.list();

	}

	@Override
	public TagVo getTagDetail(Integer tagId) {

		String sql = "select t.*,count(at.tag_id ) as articles from blog_article_tag at "
				+ "right join blog_tag t on t.id = at.tag_id where t.id = :tagId ";

		NativeQuery query = getSession().createNativeQuery(sql);
		query.setParameter("tagId", tagId);

		query.addScalar("id");
		query.addScalar("tag_name");
		query.addScalar("avatar");
		query.addScalar("articles", IntegerType.INSTANCE);

		return (TagVo) query.uniqueResult();

	}


	private Session getSession() {
		return em.unwrap(Session.class);
	}

}
