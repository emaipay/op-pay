package com.hitler.controller;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hitler.controller.support.CRUDController;
import com.hitler.core.dto.ResultDTO;
import com.hitler.core.realm.ShiroCacheUtil;
import com.hitler.core.repository.OP;
import com.hitler.core.repository.SearchFilter;
import com.hitler.core.utils.DateUtil;
import com.hitler.dto.PayOrderDto;
import com.hitler.entity.PayConfig;
import com.hitler.entity.PayConfig.MethodType;
import com.hitler.entity.PayOrder;
import com.hitler.entity.PayPlatform;
import com.hitler.entity.PayTenant;
import com.hitler.entity.PayUser;
import com.hitler.entity.PayUser.AccountType;
import com.hitler.gen.PayOrder_;
import com.hitler.payservice.support.PayOrderQueryData;
import com.hitler.service.IPayConfigService;
import com.hitler.service.IPayOrderService;
import com.hitler.service.IPayPlatformService;
import com.hitler.service.IPayTenantService;
import com.hitler.service.IPayUserService;
import com.hitler.service.support.IGenericService;
import com.hitler.table.PayOrderTable;

@Controller
@RequestMapping("back/" + PayOrderListController.PATH)
public class PayOrderListController
		extends CRUDController<PayOrder, Integer, PayOrderDto, PayOrderDto, PayOrderDto, PayOrderTable<PayOrderDto>> {
	private Logger log = LoggerFactory.getLogger(PayOrderListController.class);
	public static final String PATH = "payOrder";
	@Resource
	private IPayOrderService payOrderService;

	@Resource
	private IPayPlatformService payPlatformService;
	@Resource
	private IPayConfigService payConfigService;

	@Resource
	private IPayTenantService payTenantService;
	@Resource
	private IPayUserService payUserService;

	public PayOrderListController() {
		super(PATH);
	}

	@Override
	protected IGenericService<PayOrder, Integer> getService() {
		return payOrderService;
	}

	@Override
	@RequiresPermissions(value = "back/payOrder")
	protected void preList(Model model, ServletRequest request) throws Exception {
		List<PayPlatform> list = payPlatformService.findAll();
		model.addAttribute("ppList", list);
		super.preList(model, request);
	}

	@Override
	protected void postList(Model model, Pageable pageable, ServletRequest request, List<SearchFilter> filters)
			throws Exception {
		PayUser pu = payUserService.find(ShiroCacheUtil.getUserId());
		if (!pu.getAccountType().name().equals(AccountType.管理员)) {
			System.out.println(ShiroCacheUtil.getUserId());
			List<PayTenant> tenantList = payTenantService.queryTenantByUserId(ShiroCacheUtil.getUserId());
			Integer[]	ids=new Integer[tenantList.size()];
			for (int i=0;i<tenantList.size();i++) {
				ids[i]=tenantList.get(i).getId();
			}
			filters.add(new SearchFilter(PayOrder_.tenantId.getName(), OP.IN, Arrays.asList(ids)));
		}
		super.postList(model, pageable, request, filters);
	}

	@ResponseBody
	@RequestMapping(value = { "/reorder/{orderId}" }, method = RequestMethod.GET)
	public ResultDTO<Void> reorder(@PathVariable Integer orderId, HttpServletRequest request) {
		PayOrder order = payOrderService.find(orderId);
		if (null == order) {
			return ResultDTO.error("-1", "订单查询为空");
		}

		if (order.getOrderStatus().equals(PayOrder.OrderStatus.支付成功)) {

			return ResultDTO.error("-1", "订单已支付成功,不需补单");
		}
		try {
			PayPlatform payPlatform = payPlatformService.find(order.getPlatformId().getId());
			PayConfig pc = payConfigService.queryConfigByType(payPlatform.getPlatformTypeId().getPlatformType(),
					MethodType.订单查询);
			Class<?> cls = Class.forName(pc.getClassPath());
			Method method = ReflectionUtils.findMethod(cls, pc.getClassMethod(),
					new Class<?>[] { HttpServletRequest.class, order.getClass() });
			PayOrderQueryData data = (PayOrderQueryData) ReflectionUtils.invokeMethod(method, cls.newInstance(),
					new Object[] { request, order });
			if (data != null && !"0".equals(data.getCode())) {
				return ResultDTO.error(data.getCode(), data.getRespMsg());
			}
			if (!(order.getOrderAmount().doubleValue() == data.getAmount())) {
				return ResultDTO.error("-1", "返回的金额和实际金额不一致");
			}
			order.setFactAmount(data.getAmount());
			order.setOrderStatus(PayOrder.OrderStatus.支付成功);
			order.setOrderStatusDesc("补单成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询订单失败!", e);
			return ResultDTO.error("-1", "订单失败" + e);
		}
		if (order.getOrderStatus().equals(PayOrder.OrderStatus.支付成功)) {
			order.setOrderStatusDesc(order.getOrderStatusDesc() + "，补单操作者：" + ShiroCacheUtil.getUserName() + ",补单时间:"
					+ DateUtil.getNow());
			boolean result = payOrderService.updateOrderStatus(order);
			if (result == false) {
				log.error("补单{}修改订单状态失败", order.getBillNo());
				return ResultDTO.error("-1", "补单失败,修改订单状态失败 ");
			}
		}
		return ResultDTO.success();
	}

}
