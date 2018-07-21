package com.hitler.controller.support;
/*
package com.hitler.web.controller.support;

import com.hitler.core.dto.ResultDTO;
import com.hitler.core.dto.support.GenericDTO;
import com.hitler.core.dto.support.IGenericDTO;
import com.hitler.core.dto.support.IGenericTable;
import com.hitler.core.repository.DynamicSpecifications;
import com.hitler.core.repository.OP;
import com.hitler.core.repository.SearchFilter;
import com.hitler.core.service.support.IGenericService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

public abstract class JsonCRUDController<E extends Persistable<PK>, PK extends Serializable, DTO extends IGenericDTO<PK>, CDTO extends IGenericDTO<PK>, UDTO extends IGenericDTO<PK>, TAB extends IGenericTable<DTO, E>>
		extends GenericController {

	private Class<DTO> dtoClass;
	private Class<CDTO> createDtoClass;
	private Class<UDTO> updateDtoClass;
	private Class<TAB> tableClass;

	private String path;
	//后台url根路径(便于shiro过滤,请勿随意改动)
	public final static String FORE="/fore";
	public final static String MOBILE="/mobile";
	//租户代号(所有请求都必须带上租户代号)
	protected String tenantCode="1001";

	protected abstract IGenericService<E, PK> getService();

	*/
/**
	 * 列表展示页面
	 *//*

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResultDTO list(Model model, ServletRequest request) throws Exception {
		preList(model, request);
		return ResultDTO.success();
	}

	protected void preList(Model model, ServletRequest request) throws Exception {
	}

	*/
/**
	 * 列表查询
	 *//*

	@RequestMapping(method = RequestMethod.POST)
	public String list(Model model, Pageable pageable, ServletRequest request) throws Exception {
		List<SearchFilter> filters = SearchFilter.parse(request);
		filters.add(new SearchFilter("tenantCode", OP.EQ,tenantCode));
		postList(model, pageable, request, filters);
		model.addAllAttributes(getService()
				.findTable(tableClass, dtoClass, DynamicSpecifications.<E> bySearchFilter(filters), pageable).map());
		return path + "/list";
	}

	protected void postList(Model model, Pageable pageable, ServletRequest request, List<SearchFilter> filters)
			throws Exception {
	}

	*/
/**
	 * 新增 - 表单页面
	 *//*

	@RequestMapping(value = { "/create" }, method = RequestMethod.GET)
	public String create(Model model, ServletRequest request) throws Exception {
		CDTO createDTO = createDtoClass.newInstance();
		preCreate(model, createDTO, request);
		model.addAttribute("createDTO", createDTO);
		return  path + "/create";
	}

	protected void preCreate(Model model, CDTO createDTO, ServletRequest request) throws Exception {
	}

	*/
/**
	 * 新增 - 保存
	 *//*

	@RequestMapping(value = { "/create" }, method = RequestMethod.POST)
	public String create(Model model, @Valid @ModelAttribute CDTO createDTO, BindingResult br, ServletRequest request)
			throws Exception {
		if (!br.hasErrors()) {
			postCreate(model, createDTO, br);
			GenericDTO<PK> dto = (GenericDTO<PK>) createDTO;
			dto.setTenantCode(tenantCode);
			getService().save(createDTO);
		}
		return  path + "/create";
	}

	protected void postCreate(Model model, CDTO createDTO, BindingResult br) throws Exception {
	}

	*/
/**
	 * 修改 - 表单页面
	 *//*

	@RequestMapping(value = { "/update/{id}" }, method = RequestMethod.GET)
	public String update(Model model, @PathVariable PK id) throws Exception {
		UDTO updateDTO = getService().find(updateDtoClass, id);
		preUpdate(model, updateDTO);
		model.addAttribute("updateDTO", updateDTO);
		return path + "/update";
	}

	protected void preUpdate(Model model, UDTO updateDTO) throws Exception {
	}

	*/
/**
	 * 修改 - 保存
	 *//*

	@RequestMapping(value = { "/update/{id}" }, method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute UDTO updateDTO, BindingResult br) throws Exception {
		if (!br.hasErrors()) {
			GenericDTO<PK> dto = (GenericDTO<PK>) updateDTO;
			dto.setTenantCode(tenantCode);
			getService().save(updateDTO);
		}
		return path + "/update";
	}

	*/
/**
	 * 删除
	 *//*

	@RequestMapping(value = { "/delete/{id}" }, method = RequestMethod.GET)
	public String delete(@PathVariable PK id) throws Exception {
		//此处应有当前租户校验
		getService().delete(id);
		return  path + "/list";
	}
}
*/
