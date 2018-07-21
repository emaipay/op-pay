package com.hitler.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hitler.controller.support.CRUDController;
import com.hitler.core.exception.EntityNotExistsException;
import com.hitler.core.utils.BeanMapper;
import com.hitler.dto.PayMerchantCreateDTO;
import com.hitler.dto.PayMerchantDTO;
import com.hitler.dto.PayMerchantUpdateDTO;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayPlatform;
import com.hitler.service.IPayMerchantService;
import com.hitler.service.IPayPlatformService;
import com.hitler.service.support.IGenericService;
import com.hitler.table.PayMerchantTable;

/**
 * 第三方商户控制层
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 上午11:46:42
 */
@Controller
@RequestMapping("back/" + PayMerchantController.PATH)
public class PayMerchantController extends
		CRUDController<PayMerchant, Integer, PayMerchantDTO, PayMerchantCreateDTO, PayMerchantUpdateDTO, PayMerchantTable<PayMerchantDTO>> {
	public static final String PATH = "payMerchant";

	@Resource
	private IPayMerchantService payMerchantService;

	@Resource
	private IPayPlatformService payPlatformService;

	public PayMerchantController() {
		super(PATH);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IGenericService<PayMerchant, Integer> getService() {
		// TODO Auto-generated method stub
		return payMerchantService;
	}

	@Override
	@RequiresPermissions(value="back/payMerchant")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, ServletRequest request) throws Exception {
		List<PayPlatform> pt = payPlatformService.findAll();
		model.addAttribute("payPlatformList", pt);
		return super.list(model, request);
	}

	@Override
	@RequiresPermissions(value="payMerchant/create")
	protected void preCreate(Model model, PayMerchantCreateDTO createDTO, ServletRequest request) throws Exception {
		List<PayPlatform> pt = payPlatformService.findAll();
		model.addAttribute("payPlatformList", pt);
		super.preCreate(model, createDTO, request);
	}

	@Override
	@RequiresPermissions(value="payMerchant/update")
	protected void preUpdate(Model model, PayMerchantUpdateDTO updateDTO) throws Exception {
		List<PayPlatform> pt = payPlatformService.findAll();
		model.addAttribute("payPlatformList", pt);
		//PayMerchant payMerchant = payMerchantService.find(updateDTO.getId());
		super.preUpdate(model, updateDTO);
	}

	@RequestMapping(value = { "/update/{id}" }, method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("preloadEntity") PayMerchantUpdateDTO updateDTO, BindingResult br,
			Model model) throws Exception {
		if (!br.hasErrors()) {
			payMerchantService.updateByRelevance(updateDTO);
		}
		return PATH + "/payMerchant";
	}

	/**
	 * 由于更新时调用的是jpa封装好的save方法，所以如果表单页面中没有提到的参数，这时保存的时候，没有提到的参数就会被置为null，
	 * 使用的是spring mvc 的@ModelAttribute的特性来处理
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute("preloadEntity")
	public PayMerchantUpdateDTO preload(@RequestParam(value = "id", required = false) Integer id) throws Exception {
		if (id != null) {
			PayMerchant payMerchant = payMerchantService.find(id);
			payMerchant.setPlatformId(null);
			return BeanMapper.map(payMerchant, PayMerchantUpdateDTO.class);
		}
		return null;
	}
	
	
	/**
	 * 启用和停用管理员账户
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	@RequiresPermissions(value="payMerchant/updateStatus")
	@RequestMapping(value = { "/updateStatus:{status}/{id}" }, method = RequestMethod.GET)
	public String updateByStatus(@PathVariable Integer id, @PathVariable Boolean status) throws Exception {
		try {
			PayMerchant user = getEntityByID(id);
			if (user == null) {
				throw EntityNotExistsException.ERROR;
			}
			Boolean available=status?true:false;
			payMerchantService.updateStatus(id, available, "available");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return PATH + "/list";
	}

}
