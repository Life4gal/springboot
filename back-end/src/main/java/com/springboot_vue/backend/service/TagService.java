package com.springboot_vue.backend.service;

import com.springboot_vue.backend.entity.Tag;
import com.springboot_vue.backend.vo.TagVo;

import java.util.List;

public interface TagService {

	List<Tag> findAll();

	Tag getTagById(Integer id);

	Integer saveTag(Tag tag);

	Integer updateTag(Tag tag);

	void deleteTagById(Integer id);

	List<Tag> listHotTags(int limit);

	List<TagVo> findAllDetail();

	TagVo getTagDetail(Integer tagId);

}
