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
import com.hitler.core.log.PayLog;
import com.hitler.core.utils.BeanMapper;
import com.hitler.dto.PayBankCreateDTO;
import com.hitler.dto.PayBankDTO;
import com.hitler.dto.PayBankUpdateDTO;
import com.hitler.entity.PayBank;
import com.hitler.service.IPayBankService;
import com.hitler.service.support.IGenericService;
import com.hitler.table.PayBankTable;

/**
 * 支付平台可支付方式(银行卡)控制层
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 上午11:46:42
 */
@Controller
@RequestMapping("back/" + PayBankController.PATH)
public class PayBankController extends
		CRUDController<PayBank, Integer, PayBankDTO, PayBankCreateDTO, PayBankUpdateDTO, PayBankTable<PayBankDTO>> {
	private Logger log=LoggerFactory.getLogger(PayBankController.class);
	public static final String PATH = "payBank";

	@Resource
	private IPayBankService bankService;

	public PayBankController() {
		super(PATH);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IGenericService<PayBank, Integer> getService() {
		// TODO Auto-generated method stub
		return bankService;
	}
	
	@Override
	@RequiresPermissions(value="back/payBank")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, ServletRequest request) throws Exception {
		return super.list(model, request);
	}
	
	
	@Override
	@RequiresPermissions(value="payBank/create")
	protected void preCreate(Model model, PayBankCreateDTO createDTO, ServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		super.preCreate(model, createDTO, request);
	}
	
	

	@Override
	@RequiresPermissions(value="payBank/update")
	protected void preUpdate(Model model, PayBankUpdateDTO updateDTO) throws Exception {
		// TODO Auto-generated method stub
		super.preUpdate(model, updateDTO);
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
			bankService.deleteByIdList(idList);
			for (Integer id : idList) {
				PayLog.getLogger().info("支付类型--管理员批量删除后台账户操作", "记录id：" + id);
			}
		} catch (Exception e) {
			log.info("支付类型--管理员批量删除后台账户异常：" + e.getClass() + "&" + e.getMessage());
		}
		return PATH + "/list";
	}
	
	
	/**
	 * 修改 ，jpa没有提供update方法，此处更新（即为save）
	 * 存在表关联方法更新，执行前@ModelAttribute("preloadEntity")已取消级联关系，或重新设置表单页面中没有提到的参数，否则无法更新关联属性
	 */
	@Override
	@RequestMapping(value = { "/update/{id}" }, method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("preloadEntity") PayBankUpdateDTO updateDTO, BindingResult br,Model model) throws Exception {
		if (!br.hasErrors()) {
			bankService.updateByRelevance(updateDTO);
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
	public PayBankUpdateDTO preload(@RequestParam(value="id",required=false)Integer id) throws Exception{
		if(id!=null){
		PayBank payBank= bankService.find(id);
    	 return BeanMapper.map(payBank, PayBankUpdateDTO.class);
	 
		}
		return null;
	}
	

}
