package com.hitler.controller;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hitler.controller.support.CRUDController;
import com.hitler.core.exception.EntityNotExistsException;
import com.hitler.core.realm.ShiroCacheUtil;
import com.hitler.core.repository.DynamicSpecifications;
import com.hitler.core.repository.OP;
import com.hitler.core.repository.SearchFilter;
import com.hitler.core.utils.BeanMapper;
import com.hitler.core.utils.MD5Utils;
import com.hitler.dto.PayTenantCreateDTO;
import com.hitler.dto.PayTenantDTO;
import com.hitler.dto.PayTenantLimitCreateDTO;
import com.hitler.dto.PayTenantUpdateDTO;
import com.hitler.entity.PayBank;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayPlatform;
import com.hitler.entity.PayPlatformBank;
import com.hitler.entity.PayTenant;
import com.hitler.entity.PayTenantLimit;
import com.hitler.entity.PayTenantMerchant;
import com.hitler.entity.PayTenantMerchantBank;
import com.hitler.entity.PayUser;
import com.hitler.entity.PayUser.AccountType;
import com.hitler.gen.PayMerchant_;
import com.hitler.gen.PayPlatformBank_;
import com.hitler.gen.PayTenant_;
import com.hitler.service.IPayBankService;
import com.hitler.service.IPayMerchantService;
import com.hitler.service.IPayPlatformBankService;
import com.hitler.service.IPayPlatformService;
import com.hitler.service.IPayTenantLimitService;
import com.hitler.service.IPayTenantMerchantBankService;
import com.hitler.service.IPayTenantMerchantService;
import com.hitler.service.IPayTenantService;
import com.hitler.service.IPayUserService;
import com.hitler.service.support.IGenericService;
import com.hitler.table.PayTenantTable;

import net.sf.json.JSONArray;

/**
 * 接入商户控制层
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 上午11:46:42
 */
@Controller
@RequestMapping("back/" + PayTenantController.PATH)
public class PayTenantController extends
		CRUDController<PayTenant, Integer, PayTenantDTO, PayTenantCreateDTO, PayTenantUpdateDTO, PayTenantTable<PayTenantDTO>> {
	private Logger log = LoggerFactory.getLogger(PayTenantController.class);
	public static final String PATH = "payTenant";

	@Resource
	private IPayTenantService payTenantService;

	@Resource
	private IPayTenantMerchantBankService payTenantMerchantBankService;

	@Resource
	private IPayPlatformBankService payPlatformBankService;

	@Resource
	private IPayMerchantService payMerchantService;

	@Resource
	private IPayBankService payBankService;
	@Resource
	private IPayTenantMerchantService payTenantMerchantService;
	
	@Resource
	private IPayPlatformService payPlatformService;
	
	@Resource
	private IPayTenantLimitService payTenantLimitService;
	
	@Resource
	private IPayUserService payUserService;

	public PayTenantController() {
		super(PATH);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IGenericService<PayTenant, Integer> getService() {
		// TODO Auto-generated method stub
		return payTenantService;
	}

	@Override
	@RequiresPermissions(value="back/payTenant")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, ServletRequest request) throws Exception {
		return super.list(model, request);
	}
	
	
	@Override
	protected void postList(Model model, Pageable pageable, ServletRequest request, List<SearchFilter> filters)
			throws Exception {
		PayUser pu=payUserService.find(ShiroCacheUtil.getUserId());
		if(!pu.getAccountType().equals(AccountType.管理员)){
			filters.add(new SearchFilter(PayTenant_.userId.getName(), OP.EQ, ShiroCacheUtil.getUserId()));
		}
		super.postList(model, pageable, request, filters);
	}

	/**
	 * 接入客户对应支付银行
	 * 
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions(value="payTenant/bank")
	@RequestMapping(value = { "/bank/{id}" }, method = RequestMethod.GET)
	public String bank(Model model, @PathVariable Integer id) throws Exception {
		List<PayMerchant> payMerchantList = (List<PayMerchant>) payTenantService.getPayMerchantsByTenantId(id);
		model.addAttribute("relateMerchants", payMerchantList);
		Collection<PayPlatformBank> paymentBankList = payTenantService.findByTenantId(id);
		List<PayTenantMerchantBank> ulpmbList = payTenantMerchantBankService.findByTenantId(id);
		if (payMerchantList.size() >= 1) {
			model.addAttribute("relateMerchantId", payMerchantList.get(0).getId());
		} else {
			model.addAttribute("relateMerchantId", 0);
		}

		List<Integer> bankidList = (List<Integer>) CollectionUtils.collect(paymentBankList, new Transformer() {
			@Override
			public Object transform(Object input) {
				PayPlatformBank paymentBank = (PayPlatformBank) input;
				return paymentBank.getBankId().getId();
			}
		});
		Collection<SearchFilter> filters = Arrays
				.asList(new SearchFilter(PayMerchant_.available.getName(), OP.EQ, Boolean.TRUE));
		model.addAttribute("tenantId", id);
		model.addAttribute("relateBankIds", bankidList.toString());
		payMerchantList = payMerchantService.findAll(DynamicSpecifications.bySearchFilter(filters));
		model.addAttribute("paymentMerchantList", payMerchantList);
		model.addAttribute("paymentMerchantListJson", JSONArray
				.fromObject(payMerchantList).toString());
		model.addAttribute("ulpmbList", JSONArray.fromObject(ulpmbList).toString());
		model.addAttribute("paymentBankList", JSONArray.fromObject(payPlatformBankService.findAll()).toString());
		return PATH + "/bank";
	}
	
	/**
	 * 支付平台设置处理
	 */
	@RequestMapping(value = { "/bank" }, method = RequestMethod.POST)
	public String tenantPayPlatformSave(Integer[] paymentMerchantId_cb,String pmBankids, Integer tenantId) throws Exception {
		try {
			PayTenant payTenant = payTenantService.find(tenantId);
			payTenantMerchantService.deleteByTenantId(tenantId);
			payTenantMerchantBankService.deleteByTenantIdId(tenantId);
			//paymentMerchantId_cb为null,即所有商户取消，只需执行以上删除操作
			if(paymentMerchantId_cb==null){
				return "redirect:/back/" + PATH;
			}
			for (int i = 0; i < paymentMerchantId_cb.length; i++) {
				//System.out.println("长度:"+i);
				PayMerchant merchant = payMerchantService.find(paymentMerchantId_cb[i]);
				Object bid = JSONObject.parseObject(pmBankids).get(paymentMerchantId_cb[i] + "");
				Collection<SearchFilter> filters = Arrays.asList(new SearchFilter(PayPlatformBank_.bankId.getName(), OP.IN, bid), new SearchFilter(PayPlatformBank_.payPlatformId.getName(),
						OP.EQ, payMerchantService.find(paymentMerchantId_cb[i]).getPlatformId().getId()));

				Collection<PayPlatformBank> bankList = (bid == null) ? null : payPlatformBankService.findAll(DynamicSpecifications.bySearchFilter(filters));

				Collection<PayTenantMerchantBank> ulpmbList = new HashSet<PayTenantMerchantBank>();
				PayTenantMerchant ulpm = new PayTenantMerchant();
				ulpm.setTenantId(payTenant);
				ulpm.setPayMerchantId(merchant);
				payTenantMerchantService.save(ulpm);

				for (PayPlatformBank bank : bankList) {
					PayTenantMerchantBank ulpmb = new PayTenantMerchantBank();
					ulpmb.setPayTenantId(payTenant);
					ulpmb.setPayMerchantId(merchant);
					ulpmb.setPayBankId(bank);
					ulpmbList.add(ulpmb);
				}
				payTenantMerchantBankService.save(ulpmbList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/back/" + PATH;
	}

	/**
	 * 根据显示线上支付平台，查线下银行 PayMerchant=>payplatform=>PayPlatformBank,根据payplatid查询
	 * 对应线下网银
	 * 
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = { "/findPayPlatformBank" }, method = RequestMethod.GET)
	public List<PayBank> findPayPlatformBank(Integer payPlatformId) throws Exception {
		List<PayBank> PayBankList = payBankService.queryPlatformBank(payPlatformId);
		return PayBankList;
	}

	/**
	 * payTenantMerchantBank保存选中的PayTenant和 PayMerchant 和PayPlatformBank
	 * 
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/savePayPlatformBankAndPayMerchant/{id}" }, method = RequestMethod.POST)
	public String savePayPlatformBankAndPayMerchant(Model model, @PathVariable Integer id) throws Exception {

		return PATH + "/bank";
	}

	/**
	 * 新增 - 表单页面
	 */
	@Override
	@RequiresPermissions(value="payTenant/create")
	@RequestMapping(value = { "/create" }, method = RequestMethod.GET)
	public String create(Model model, ServletRequest request) throws Exception {
		PayTenantCreateDTO createDTO = new PayTenantCreateDTO();
		model.addAttribute("createDTO", createDTO);
		return PATH + "/create";
	}
	
	

	@Override
	@RequiresPermissions(value="payTenant/update")
	protected void preUpdate(Model model, PayTenantUpdateDTO updateDTO) throws Exception {
		// TODO Auto-generated method stub
		super.preUpdate(model, updateDTO);
	}

	/**
	 * 管理员开户
	 *
	 * @param createDTO
	 * @return
	 * @throws Exception
	 */
	@Override
	@RequestMapping(value = { "/create" }, method = RequestMethod.POST)
	public String create(Model model, @Valid @ModelAttribute PayTenantCreateDTO createDTO, BindingResult br,
			ServletRequest request) throws Exception {
		if (!br.hasErrors()) {
			postCreate(model, createDTO, br);
			payTenantService.tenantSave(createDTO);
		}
		return PATH + "/list";
	}

	/**
	 * 修改 ，jpa没有提供update方法，此处更新（即为save）
	 * 存在表关联方法更新，执行前@ModelAttribute("preloadEntity")已取消级联关系，或重新设置表单页面中没有提到的参数，
	 * 否则无法更新关联属性
	 */
	@Override
	@RequestMapping(value = { "/update/{id}" }, method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute PayTenantUpdateDTO updateDTO, BindingResult br, Model model)
			throws Exception {
		if (!br.hasErrors()) {
			payTenantService.tenantUpdate(updateDTO);
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
	public PayTenantUpdateDTO preload(@RequestParam(value = "id", required = false) Integer id) throws Exception {
		if (id != null) {
			PayTenant payTenant = payTenantService.find(id);
			return BeanMapper.map(payTenant, PayTenantUpdateDTO.class);

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
			payTenantService.deleteByIdList(idList);
			for (Integer id : idList) {
				log.info("支付类型--管理员批量删除后台账户操作", "记录id：" + id);
			}
		} catch (Exception e) {
			log.info("支付类型--管理员批量删除后台账户异常：" + e.getClass() + "&" + e.getMessage());
		}
		return PATH + "/list";
	}

	/**
	 * 启用和停用管理员账户
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	@RequiresPermissions(value="payTenant/updateStatus")
	@RequestMapping(value = { "/updateStatus:{status}/{id}" }, method = RequestMethod.GET)
	public String updateByStatus(@PathVariable Integer id, @PathVariable Boolean status) throws Exception {
		try {
			PayTenant user = getEntityByID(id);
			if (user == null) {
				throw EntityNotExistsException.ERROR;
			}
			Boolean available = status ? true : false;
			payTenantService.updateStatus(id, available, "available");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return PATH + "/list";
	}

	/**
	 * 启用和停用管理员账户
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping(value = { "/genmerKey" }, method = RequestMethod.GET)
	@ResponseBody
	public String genmerKey() throws Exception {
		// 时间戳加随机三位数字
		String key = DateFormatUtils.format(Calendar.getInstance(), "yyMMddHHmmssSSS")
				+ RandomStringUtils.randomNumeric(3);
		// 生成32位md5加密字符串
		key = MD5Utils.getMD5Str(key);
		return key;
	}
	
	@RequiresPermissions(value="payTenant/limit")
	@RequestMapping(value = { "/limit/{id}" }, method = RequestMethod.GET)
	public String limit(Model model, @PathVariable Integer id) throws Exception {
		List<PayPlatform> ppList=payPlatformService.findAll();
		List<PayTenantLimit> limitList=payTenantLimitService.queryConnLimitByTenantId(id);
		PayTenant pt=payTenantService.find(id);
		model.addAttribute("ppList", ppList);
		model.addAttribute("limitList", limitList);
		model.addAttribute("tenanId", id);
		model.addAttribute("tenanName", pt.getPlatformName());
		model.addAttribute("pp", JSON.toJSONString(ppList));
		return PATH+"/limit";
	}
	@RequestMapping(value = { "/limitSave" }, method = RequestMethod.POST)
	public String limitSave(@Valid @ModelAttribute PayTenantLimitCreateDTO dto) throws Exception{
		PayTenant pt=payTenantService.find(dto.getTenantId());
		payTenantLimitService.deleteByTenantId(pt.getId());
		if(dto!=null&&dto.getPlatformId().length>0){
			for(int i=0;i<dto.getPlatformId().length;i++){
				PayTenantLimit ptl=new PayTenantLimit();
				ptl.setDailyRechargeAmountMax(dto.getDailyRechargeAmountMax()[i]);
				ptl.setDailyRechargeTimesMax(dto.getDailyRechargeTimesMax()[i]);
				ptl.setOnetimeRechargeAmountMax(dto.getOnetimeRechargeAmountMax()[i]);
				ptl.setOnetimeRechargeAmountMin(dto.getOnetimeRechargeAmountMin()[i]);
				ptl.setPlatformId(dto.getPlatformId()[i]);
				PayPlatform pp=payPlatformService.find(dto.getPlatformId()[i]);
				ptl.setPlatformCode(pp.getPlatformCode());
				ptl.setTenantId(pt.getId());
				payTenantLimitService.save(ptl);
			}
		}
		
		return PATH + "/list";
	}

}
