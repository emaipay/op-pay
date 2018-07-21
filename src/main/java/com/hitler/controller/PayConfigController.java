package com.hitler.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hitler.controller.support.CRUDController;
import com.hitler.dto.PayConfigCreateDTO;
import com.hitler.dto.PayConfigDTO;
import com.hitler.dto.PayConfigUpdateDTO;
import com.hitler.entity.PayConfig;
import com.hitler.service.IPayConfigService;
import com.hitler.service.support.IGenericService;
import com.hitler.table.PayConfigTable;

/**
 * 各支付平台配置控制层
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 上午11:46:42
 */
@Controller
@RequestMapping("back/" + PayConfigController.PATH)
public class PayConfigController extends
		CRUDController<PayConfig, Integer, PayConfigDTO, PayConfigCreateDTO, PayConfigUpdateDTO, PayConfigTable<PayConfigDTO>> {
	private Logger log=LoggerFactory.getLogger(PayConfigController.class);
	public static final String PATH = "payConfig";

	@Resource
	private IPayConfigService payConfigService;

	public PayConfigController() {
		super(PATH);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IGenericService<PayConfig, Integer> getService() {
		// TODO Auto-generated method stub
		return payConfigService;
	}
	
	@Override
	@RequiresPermissions(value="back/payConfig")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, ServletRequest request) throws Exception {
		return super.list(model, request);
	}
	
	@Override
	@RequiresPermissions(value="payConfig/create")
	protected void preCreate(Model model, PayConfigCreateDTO createDTO, ServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		super.preCreate(model, createDTO, request);
	}

	@Override
	@RequiresPermissions(value="payConfig/update")
	protected void preUpdate(Model model, PayConfigUpdateDTO updateDTO) throws Exception {
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
		
			payConfigService.deleteByIdList(idList);
			for (Integer id : idList) {
				log.info("支付类型--管理员批量删除后台账户操作", "记录id：" + id);
			}
		} catch (Exception e) {
			log.info("支付类型--管理员批量删除后台账户异常：" + e.getClass() + "&" + e.getMessage());
		}
		return PATH + "/list";
	}

}
