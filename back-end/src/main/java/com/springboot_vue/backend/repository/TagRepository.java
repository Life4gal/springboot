package com.springboot_vue.backend.repository;

import com.springboot_vue.backend.entity.Tag;
import com.springboot_vue.backend.repository.wrapper.TagWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer>, TagWrapper {

	@Query(value = "select t.*,count(at.tag_id) as count from blog_article_tag at "
			+ "right join blog_tag t on t.id = at.tag_id "
			+ "group by t.id order by count(at.tag_id) desc limit :limit", nativeQuery = true)
	List<Tag> listHotTagsByArticleUse(@Param("limit") int limit);

}
