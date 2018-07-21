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
import com.hitler.dto.PayPlatformBankCreateDTO;
import com.hitler.dto.PayPlatformBankDTO;
import com.hitler.dto.PayPlatformBankUpdateDTO;
import com.hitler.entity.PayBank;
import com.hitler.entity.PayPlatform;
import com.hitler.entity.PayPlatformBank;
import com.hitler.service.IPayBankService;
import com.hitler.service.IPayPlatformBankService;
import com.hitler.service.IPayPlatformService;
import com.hitler.service.support.IGenericService;
import com.hitler.table.PayPlatformBankTable;

/**
 * 第三方支付平台对应支付方式配置控制层
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 上午11:46:42
 */
@Controller
@RequestMapping("back/" + PayPlatformBankController.PATH)
public class PayPlatformBankController extends
		CRUDController<PayPlatformBank, Integer, PayPlatformBankDTO, PayPlatformBankCreateDTO, PayPlatformBankUpdateDTO, PayPlatformBankTable<PayPlatformBankDTO>> {
	private Logger log = LoggerFactory.getLogger(PayPlatformBankController.class);
	public static final String PATH = "payPlatformBank";

	@Resource
	private IPayPlatformBankService payPlatformBankService;

	@Resource
	private IPayBankService payBankService;

	@Resource
	private IPayPlatformService payPlatformService;

	public PayPlatformBankController() {
		super(PATH);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IGenericService<PayPlatformBank, Integer> getService() {
		// TODO Auto-generated method stub
		return payPlatformBankService;
	}

	@Override
	@RequiresPermissions(value="back/payPlatformBank")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, ServletRequest request) throws Exception {
		return super.list(model, request);
	}
	
	/**
	 * 新增 - 表单页面
	 */
	@Override
	@RequiresPermissions(value="payPlatformBank/create")
	public void preCreate(Model model,PayPlatformBankCreateDTO dto, ServletRequest request) throws Exception {
		List<PayBank> payBankList = payBankService.findAll();
		List<PayPlatform> payplatformList = payPlatformService.findAll();
		model.addAttribute("payBankList", payBankList);
		model.addAttribute("payplatformList", payplatformList);
	}
	
	

	@Override
	@RequiresPermissions(value="payPlatformBank/update")
	protected void preUpdate(Model model, PayPlatformBankUpdateDTO updateDTO) throws Exception {
		// TODO Auto-generated method stub
		super.preUpdate(model, updateDTO);
	}

	/**
	 * 修改 - 表单页面
	 */
	@Override
	@RequestMapping(value = { "/update/{id}" }, method = RequestMethod.GET)
	public String update(Model model, @PathVariable Integer id) throws Exception {
		PayPlatformBank createDTO =   payPlatformBankService.find(id);
		List<PayBank> payBankList = payBankService.findAll();
		List<PayPlatform> payplatformList = payPlatformService.findAll();
		model.addAttribute("payBankList", payBankList);
		model.addAttribute("payplatformList", payplatformList);
		model.addAttribute("updateDTO", createDTO);
		return PATH + "/update";
	}

	/**
	 * 修改 ，jpa没有提供update方法，此处更新（即为save）
	 * 存在表关联方法更新，执行前@ModelAttribute("preloadEntity")已取消级联关系，或重新设置表单页面中没有提到的参数，
	 * 否则无法更新关联属性
	 */
	@Override
	@RequestMapping(value = { "/update/{id}" }, method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("preloadEntity") PayPlatformBankUpdateDTO updateDTO, BindingResult br,Model model)
			throws Exception {
		if (!br.hasErrors()) {
			payPlatformBankService.updateByRelevance(updateDTO);
		}
		return PATH + "/list";
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
	public PayPlatformBankUpdateDTO preload(@RequestParam(value = "id", required = false) Integer id) throws Exception {
		if (id != null) {
			PayPlatformBank payPlatformBank = payPlatformBankService.find(id);
			payPlatformBank.setBankId(null);
			payPlatformBank.setPayPlatformId(null);
			return BeanMapper.map(payPlatformBank, PayPlatformBankUpdateDTO.class);

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
			payPlatformBankService.deleteByIdList(idList);
			for (Integer id : idList) {
				log.info("支付类型--管理员批量删除后台账户操作", "记录id：" + id);
			}
		} catch (Exception e) {
			log.info("支付类型--管理员批量删除后台账户异常：" + e.getClass() + "&" + e.getMessage());
		}
		return PATH + "/list";
	}

}
