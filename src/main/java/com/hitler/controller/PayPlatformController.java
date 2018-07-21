package com.hitler.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hitler.controller.support.CRUDController;
import com.hitler.core.utils.BeanMapper;
import com.hitler.dto.PayPlatformCreateDTO;
import com.hitler.dto.PayPlatformDTO;
import com.hitler.dto.PayPlatformUpdateDTO;
import com.hitler.entity.PayPlatform;
import com.hitler.entity.PayPlatformType;
import com.hitler.service.IPayPlatformService;
import com.hitler.service.IPayPlatformTypeService;
import com.hitler.service.support.IGenericService;
import com.hitler.table.PayPlatformTable;

/**
 * 第三方支付平台管理控制层
 * 
 * @author klp
 *
 */
@Controller
@RequestMapping("back/" + PayPlatformController.PATH)
public class PayPlatformController extends
		CRUDController<PayPlatform, Integer, PayPlatformDTO, PayPlatformCreateDTO, PayPlatformUpdateDTO, PayPlatformTable<PayPlatformDTO>> {
	private Logger log = LoggerFactory.getLogger(PayPlatformController.class);
	public static final String PATH = "payPlatform";

	public PayPlatformController() {
		super(PATH);
	}

	@Resource
	private IPayPlatformService payPlatformService;

	@Resource
	private IPayPlatformTypeService payPlatformTypeService;

	@Override
	protected IGenericService<PayPlatform, Integer> getService() {
		return payPlatformService;
	}
	
	@Override
	@RequiresPermissions(value="back/payPlatform")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, ServletRequest request) throws Exception {
		return super.list(model, request);
	}

	/**
	 * 新增 - 表单页面
	 */
	@Override
	@RequiresPermissions(value="payPlatform/create")
	@RequestMapping(value = { "/create" }, method = RequestMethod.GET)
	public String create(Model model, ServletRequest request) throws Exception {
		PayPlatformCreateDTO createDTO = new PayPlatformCreateDTO();
		List<PayPlatformType> pt = payPlatformTypeService.findAll();
		preCreate(model, createDTO, request);
		model.addAttribute("payPlatformTypeList", pt);
		model.addAttribute("createDTO", createDTO);
		return PATH + "/create";
	}

	/**
	 * 修改 - 表单页面
	 */
	@Override
	@RequiresPermissions(value="payPlatform/update")
	@RequestMapping(value = { "/update/{id}" }, method = RequestMethod.GET)
	public String update(Model model, @PathVariable Integer id) throws Exception {
		PayPlatform updateDTO = payPlatformService.find(id);
		List<PayPlatformType> pt = payPlatformTypeService.findAll();
		model.addAttribute("payPlatformTypeList", pt);
		model.addAttribute("updateDTO", updateDTO);
		return PATH + "/update";
	}
	
 
	/**
	 * 修改 ，jpa没有提供update方法，此处更新（即为save）
	 * 存在表关联方法更新，执行前@ModelAttribute("preloadEntity")已取消级联关系，或重新设置表单页面中没有提到的参数，否则无法更新关联属性
	 */
	
	@Override
	@RequestMapping(value = { "/update/{id}" }, method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("preloadEntity") PayPlatformUpdateDTO updateDTO, BindingResult br,Model model) throws Exception {
		if (!br.hasErrors()) {
			payPlatformService.updateByRelevance(updateDTO);
		}
		return PATH + "/list";
	}
	
	/**
	 * 由于更新时调用的是jpa封装好的save方法，所以如果表单页面中没有提到的参数，这时保存的时候，没有提到的参数就会被置为null，
	 * 使用的是spring mvc 的@ModelAttribute的特性来处理
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ModelAttribute("preloadEntity")
	public PayPlatformUpdateDTO preload(@RequestParam(value="id",required=false)Integer id) throws Exception{
		if(id!=null){
		PayPlatform payPlatform= payPlatformService.find(id);
		payPlatform.setPlatformTypeId(null);
		 return BeanMapper.map(payPlatform, PayPlatformUpdateDTO.class);
	 
		}
		return null;
	}
	
	
	
	/**
	 * 批量账户删除
	 */
	@RequestMapping(value = { "/deletebyids/{ids}" }, method = RequestMethod.GET)
	public String deleteByIdList(@PathVariable String ids) throws Exception {
		String[] idArray = ids.split(",");
		Integer[] idIntArray = new Integer[idArray.length];
		for (int i = 0; i < idArray.length; i++) {
			idIntArray[i] = Integer.parseInt(idArray[i]);
		}
		List<Integer> idList = Arrays.asList(idIntArray);
		try {
			payPlatformService.deleteByIdList(idList);
			for (Integer id : idList) {
				log.info("支付类型--管理员批量删除后台账户操作", "记录id：" + id);
			}
		} catch (Exception e) {
			log.info("支付类型--管理员批量删除后台账户异常：" + e.getClass() + "&" + e.getMessage());
		}
		return PATH + "/list";
	}
	

}
