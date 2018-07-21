package com.hitler.controller;

import java.util.Arrays;
import java.util.Date;
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

import com.hitler.controller.support.CRUDController;
import com.hitler.dto.PayPlatformTypeCreateDTO;
import com.hitler.dto.PayPlatformTypeDTO;
import com.hitler.dto.PayPlatformTypeUpdateDTO;
import com.hitler.entity.PayPlatformType;
import com.hitler.service.IPayPlatformTypeService;
import com.hitler.service.support.IGenericService;
import com.hitler.table.PayPlatformTypeTable;

/**
 * 支付类型大类控制层
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 上午11:46:42
 */
@Controller
@RequestMapping("back/" + PayPlatformTypeController.PATH)
public class PayPlatformTypeController extends
		CRUDController<PayPlatformType, Integer, PayPlatformTypeDTO, PayPlatformTypeCreateDTO, PayPlatformTypeUpdateDTO, PayPlatformTypeTable<PayPlatformTypeDTO>> {
	private Logger log = LoggerFactory.getLogger(PayPlatformTypeController.class);
	public static final String PATH = "payPlatformType";

	@Resource
	private IPayPlatformTypeService iPayPlatformTypeService;

	public PayPlatformTypeController() {
		super(PATH);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IGenericService<PayPlatformType, Integer> getService() {
		// TODO Auto-generated method stub
		return iPayPlatformTypeService;
	}

	@Override
	@RequiresPermissions(value="back/payPlatformType")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, ServletRequest request) throws Exception {
		return super.list(model, request);
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
		
	       iPayPlatformTypeService.deleteByIdList(idList);
			for (Integer id : idList) {
				log.info("支付类型--管理员批量删除后台账户操作", "记录id：" + id);
			}
		} catch (Exception e) {
			log.info("支付类型--管理员批量删除后台账户异常：" + e.getClass() + "&" + e.getMessage());
		}
		return PATH + "/list";
	}
	
	
	/**
	 * 修改 - 保存
	 * PlatformType 不允许修改
	 */
	@Override
	@RequestMapping(value = { "/update/{id}" }, method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute PayPlatformTypeUpdateDTO updateDTO, BindingResult br,Model model) throws Exception {
		if (!br.hasErrors()) {
			PayPlatformType payPlatformType = iPayPlatformTypeService.find(updateDTO.getId());
			payPlatformType.setTypeName(updateDTO.getTypeName());
			payPlatformType.setLastModifiedBy("管理员");
			payPlatformType.setLastModifiedDate(new Date());
			getService().update(payPlatformType);
		}
		return PATH + "/list";
	}

	@Override
	@RequiresPermissions(value="payPlatformType/create")
	protected void preCreate(Model model, PayPlatformTypeCreateDTO createDTO, ServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		super.preCreate(model, createDTO, request);
	}

	@Override
	@RequiresPermissions(value="payPlatformType/update")
	protected void preUpdate(Model model, PayPlatformTypeUpdateDTO updateDTO) throws Exception {
		// TODO Auto-generated method stub
		super.preUpdate(model, updateDTO);
	}


}
