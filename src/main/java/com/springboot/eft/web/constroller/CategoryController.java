package com.springboot.eft.web.constroller;

import com.springboot.eft.additional_utils.Checker;
import com.springboot.eft.additional_utils.Formatter;
import com.springboot.eft.additional_utils.constant.ValueConstants;
import com.springboot.eft.annotation.AuthInterceptor;
import com.springboot.eft.entity.Category;
import com.springboot.eft.enums.InterceptorLevel;
import com.springboot.eft.service.ICategoryService;
import com.springboot.eft.utils.ControllerUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@Api(value = "/category", description = "文件分类相关操作")
public class CategoryController {

	private final ICategoryService categoryService;

	@Autowired
	public CategoryController(ICategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@ApiOperation(value = "新增一个分类")
	@AuthInterceptor(InterceptorLevel.ADMIN)
	@RequestMapping(value = "/{name}", method = RequestMethod.POST)
	public String add(@PathVariable("name") String name) {
		return ControllerUtils.getResponse(categoryService.insert(name));
	}

	@ApiOperation(value = "更新分类名称")
	@ApiImplicitParam(name = "name", value = "新的名称", required = true)
	@AuthInterceptor(InterceptorLevel.ADMIN)
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(@PathVariable("id") int id, String name) {
		boolean isSuccess = Checker.isNotEmpty(name) && categoryService.update(id, name);
		return ControllerUtils.getResponse(isSuccess);
	}

	@ApiOperation(value = "删除一个分类")
	@AuthInterceptor(InterceptorLevel.ADMIN)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String remove(@PathVariable("id") int id) {
		return ControllerUtils.getResponse(categoryService.remove(id));
	}

	@ApiOperation(value = "获取一个分类")
	@AuthInterceptor(InterceptorLevel.NONE)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String getById(@PathVariable("id") int id) {
		Category category = categoryService.getById(id);
		if (Checker.isNull(category)) {
			return ControllerUtils.getResponse(ValueConstants.FALSE);
		} else {
			return category.toString();
		}
	}

	@ApiOperation(value = "获取所有分类")
	@AuthInterceptor(InterceptorLevel.NONE)
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public String getAll() {
		return Formatter.listToJson(categoryService.list());
	}
}
