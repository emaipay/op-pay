package com.hitler.controller.support;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hitler.core.dto.support.IGenericDTO;
import com.hitler.core.dto.support.IGenericTable;
import com.hitler.core.exception.EntityNotExistsException;
import com.hitler.core.repository.DynamicSpecifications;
import com.hitler.core.repository.OP;
import com.hitler.core.repository.SearchFilter;
import com.hitler.service.support.IGenericService;

public abstract class CRUDController<E extends Persistable<PK>, PK extends Serializable, DTO extends IGenericDTO<PK>, CDTO extends IGenericDTO<PK>, UDTO extends IGenericDTO<PK>, TAB extends IGenericTable<DTO, E>>
		extends GenericController {

	private Class<DTO> dtoClass;
	private Class<CDTO> createDtoClass;
	private Class<UDTO> updateDtoClass;
	private Class<TAB> tableClass;

	private String path;

	protected abstract IGenericService<E, PK> getService();

	@SuppressWarnings("unchecked")
	public CRUDController(String path) {
		this.path = path;
		this.dtoClass = getClass(2);
		this.createDtoClass = getClass(3);
		this.updateDtoClass = getClass(4);
		this.tableClass = getClass(5);
	}

	/**
	 * 列表展示页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, ServletRequest request) throws Exception {
		preList(model, request);
		return path + "/list";
	}

	protected void preList(Model model, ServletRequest request) throws Exception {
	}

	/**
	 * 列表查询
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String list(Model model, Pageable pageable, ServletRequest request) throws Exception {
		List<SearchFilter> filters = SearchFilter.parse(request);
		postList(model, pageable, request, filters);
		model.addAllAttributes(getService()
				.findTable(tableClass, dtoClass, DynamicSpecifications.<E> bySearchFilter(filters), pageable).map());
		return path + "/list";
	}

	protected void postList(Model model, Pageable pageable, ServletRequest request, List<SearchFilter> filters)
			throws Exception {
	}

	/**
	 * 新增 - 表单页面
	 */
	@RequestMapping(value = { "/create" }, method = RequestMethod.GET)
	public String create(Model model, ServletRequest request) throws Exception {
		CDTO createDTO = createDtoClass.newInstance();
		preCreate(model, createDTO, request);
		model.addAttribute("createDTO", createDTO);
		return  path + "/create";
	}

	protected void preCreate(Model model, CDTO createDTO, ServletRequest request) throws Exception {
	

	}

	/**
	 * 新增 - 保存
	 */
	@RequestMapping(value = { "/create" }, method = RequestMethod.POST)
	public String create(Model model, @Valid @ModelAttribute CDTO createDTO, BindingResult br, ServletRequest request)
			throws Exception {
	
		if (!br.hasErrors()) {
			postCreate(model, createDTO, br);
			getService().save(createDTO);
		}
		CDTO dto = createDtoClass.newInstance();
		preCreate(model, createDTO, request);
		model.addAttribute("createDTO", dto);
		return  path + "/create";
	}

	protected void postCreate(Model model, CDTO createDTO, BindingResult br) throws Exception {
	}

	/**
	 * 修改 - 表单页面
	 */
	@RequestMapping(value = { "/update/{id}" }, method = RequestMethod.GET)
	public String update(Model model, @PathVariable PK id) throws Exception {
		UDTO updateDTO = getService().find(updateDtoClass, id);
		preUpdate(model, updateDTO);
		model.addAttribute("updateDTO", updateDTO);
		return path + "/update";
	}

	protected void preUpdate(Model model, UDTO updateDTO) throws Exception {
	}

	
	/**
	 * 判断被操作记录是否属于当前租户
	 * @param id
	 * @return
	 */
	protected E getEntityByID(@PathVariable PK id){
		Collection<SearchFilter> filters=new ArrayList<SearchFilter>();
		filters.add(new SearchFilter("id", OP.EQ, id));
		return getService().find(DynamicSpecifications.bySearchFilter(filters));
	}

	/**
	 * 修改 - 保存
	 */
	@RequestMapping(value = { "/updateOrSave/{id}" }, method = RequestMethod.POST)
	public String updateOrSave(@Valid @ModelAttribute UDTO updateDTO, BindingResult br) throws Exception {
		if (!br.hasErrors()) {
			getService().save(updateDTO);
		}
		return path + "/update";
	}
	
	

	/**
	 * 修改 
	 */
	@RequestMapping(value = { "/update/{id}" }, method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute UDTO updateDTO, BindingResult br,Model model) throws Exception {
		if (!br.hasErrors()) {
           getService().update(updateDTO);
		}
		UDTO DTO = getService().find(updateDtoClass, updateDTO.getId());
		preUpdate(model, DTO);
		model.addAttribute("updateDTO", updateDTO);
		return path + "/list";
	}


	/**
	 * 删除
	 */
	@RequestMapping(value = { "/delete/{id}" }, method = RequestMethod.GET)
	public String delete(@PathVariable PK id) throws Exception {
		getService().delete(id);
		return  path + "/list";
	}

	@SuppressWarnings("rawtypes")
	private Class getClass(int index) {
		Type[] params = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
		Type type = params[index];
		if (type instanceof ParameterizedType) {
			type = ((ParameterizedType) type).getRawType();
		}
		return (Class) type;
	}
	
	/**
	 * 逻辑删除(注：无租户代号必须重写此方法)
	 */
	@RequestMapping(value = { "/updateDel/{id}" }, method = RequestMethod.GET)
	public String updateDel(@PathVariable PK id) throws Exception {
		E entity =getEntityByID(id);
		if(entity==null){
			throw EntityNotExistsException.ERROR;
		}
		getService().updateColumn(entity,"isDelete");
		return  path + "/list";
	}
	
	/**
	 * 更新对象， 只更新指定的属性。根据id主键更新
	 * columns: 你要更新的属性, 名称要和entity一致。
	 */
	public String updateColumn(E entity,String... columns) throws Exception {
		getService().updateColumn(entity,columns);
		return  path + "/list";
	}
	
	
	
 

}
