package com.springboot_vue.backend.service.impl;

import com.springboot_vue.backend.entity.Tag;
import com.springboot_vue.backend.repository.TagRepository;
import com.springboot_vue.backend.service.TagService;
import com.springboot_vue.backend.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

	@Autowired
	private TagRepository tagRepository;


	@Override
	public List<Tag> findAll() {
		return tagRepository.findAll();
	}

	@Override
	public Tag getTagById(Integer id) {
		return tagRepository.getOne(id);
	}

	@Override
	@Transactional
	public Integer saveTag(Tag tag) {
		return tagRepository.save(tag).getId();
	}

	@Override
	@Transactional
	public Integer updateTag(Tag tag) {
		Tag oldTag = tagRepository.getOne(tag.getId());

		oldTag.setTagName(tag.getTagName());
		oldTag.setAvatar(tag.getAvatar());

		return oldTag.getId();
	}

	@Override
	@Transactional
	public void deleteTagById(Integer id) {
		tagRepository.deleteById(id);
	}

	@Override
	public List<Tag> listHotTags(int limit) {

		return tagRepository.listHotTagsByArticleUse(limit);
	}

	@Override
	public List<TagVo> findAllDetail() {
		return tagRepository.findAllDetail();
	}

	@Override
	public TagVo getTagDetail(Integer tagId) {
		return tagRepository.getTagDetail(tagId);
	}

}
