package com.springboot_vue.backend.repository.wrapper;

import com.springboot_vue.backend.vo.TagVo;

import java.util.List;

public interface TagWrapper {

	List<TagVo> findAllDetail();

	TagVo getTagDetail(Integer tagId);

}
