package com.hitler.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.hitler.controller.support.GenericController;
import com.hitler.core.Constants;
import com.hitler.core.PreferenceConstants;
import com.hitler.core.dto.ResultDTO;
import com.hitler.core.enums.LockType;
import com.hitler.core.enums.PlatformTypeEnum;
import com.hitler.core.exception.BusinessException;
import com.hitler.core.exception.ThirdPayBussinessException;
import com.hitler.core.log.PayLog;
import com.hitler.core.utils.DateUtil;
import com.hitler.core.utils.DesEncryptUtils;
import com.hitler.core.utils.HttpUtils;
import com.hitler.core.utils.StringUtils;
import com.hitler.core.web.BusinessLock;
import com.hitler.entity.PayConfig;
import com.hitler.entity.PayConfig.MethodType;
import com.hitler.entity.PayOrder;
import com.hitler.entity.PayPreference;
import com.hitler.entity.PayTenant;
import com.hitler.payservice.AliPayService;
import com.hitler.payservice.JfkPayService;
import com.hitler.payservice.LfPayService;
import com.hitler.payservice.MfPayService;
import com.hitler.payservice.RhPayService;
import com.hitler.payservice.ScanCodePayService;
import com.hitler.payservice.SfPayService;
import com.hitler.payservice.WxPayService;
import com.hitler.payservice.YfbPayService;
import com.hitler.payservice.YjfPayService;
import com.hitler.service.IPayConfigService;
import com.hitler.service.IPayOrderService;
import com.hitler.service.IPayPreferenceService;

/**
 * 各支付回调控制方法
 * 
 * @author xu 2017-04-27
 */
@Controller
@RequestMapping("/pay" + PayCallBackController.PATH)
public class PayCallBackController extends GenericController {
	public static final String PATH = "/callback";

	@Resource
	private IPayConfigService payConfigService;
	@Resource
	private IPayOrderService payOrderService;
	@Resource
	private IPayPreferenceService payPreferenceService;
	
	/**
	 *   模酷付款回调页面
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = ScanCodePayService.CALLBACK_PAGE_PATH)
	public String mkPayCallbackPag(Model model, HttpServletRequest request, HttpServletResponse response) {
		PayLog.getLogger().info("[{}]页面回调了", PlatformTypeEnum.模酷支付);
		return payBackPage(PlatformTypeEnum.模酷支付, request, model);
	}
	
	/**
	 * 模酷付款回调数据接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value = ScanCodePayService.CALLBACK_DATA_PATH)
	public void kmPayCallbackData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PayLog.getLogger().info("[{}]数据回调接口调用,订单号:{}", PlatformTypeEnum.模酷支付, request.getParameter("attach"));
		ResultDTO<PayOrder> resultDto = thirdPayCallbackHandle(PlatformTypeEnum.模酷支付, request, true);
		
		PrintWriter out = response.getWriter();
		if (resultDto.getSuccess() == null || resultDto.getSuccess()==false) {
			out.println("fail"); 
			return;
		}
		out.println("success"); // 请不要修改或删除
	}
	
	/**
	 *   微信付款回调页面
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = WxPayService.CALLBACK_PAGE_PATH)
	public String wxPayCallbackPag(Model model, HttpServletRequest request, HttpServletResponse response) {
		PayLog.getLogger().info("[{}]页面回调了", PlatformTypeEnum.微信);
		return payBackPage(PlatformTypeEnum.微信, request, model);
	}
	
	/**
	 * 微信付款回调数据接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value = WxPayService.CALLBACK_DATA_PATH)
	public void wxPayCallbackData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PayLog.getLogger().info("[{}]数据回调接口调用", PlatformTypeEnum.微信);
		ResultDTO<PayOrder> resultDto = thirdPayCallbackHandle(PlatformTypeEnum.微信, request, true);
		
		PrintWriter out = response.getWriter();
		String resXml = null;
		PayOrder order = resultDto.getResult();
		if (order == null) {
			String respMsg = resultDto.getRespMsg()==null?"":resultDto.getRespMsg();
			resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[" + respMsg + "]]></return_msg>" + "</xml> ";
			out.println(resXml); 
			return;
		}
		if (!order.getOrderStatus().equals(PayOrder.OrderStatus.支付成功)) {
			resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[" + order.getOrderStatusDesc() + "]]></return_msg>" + "</xml> ";
		}else{
			 resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
		}
		out.println(resXml);  // 请不要修改或删除
	}
	
	/**
	 *   支付宝付款回调页面
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = AliPayService.CALLBACK_PAGE_PATH)
	public String aliPayCallbackPag(Model model, HttpServletRequest request, HttpServletResponse response) {
		PayLog.getLogger().info("[{}]页面回调了", PlatformTypeEnum.支付宝);
		return payBackPage(PlatformTypeEnum.支付宝, request, model);
	}
	
	/**
	 * 支付宝付款回调数据接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value = AliPayService.CALLBACK_DATA_PATH)
	public void aliPayCallbackData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PayLog.getLogger().info("[{}]数据回调接口调用,订单号:{}", PlatformTypeEnum.支付宝, request.getParameter("out_trade_no"));
		ResultDTO<PayOrder> resultDto = thirdPayCallbackHandle(PlatformTypeEnum.支付宝, request, true);
		
		PrintWriter out = response.getWriter();
		if (resultDto.getSuccess() == null || resultDto.getSuccess()==false) {
			out.println("fail"); 
			return;
		}
		out.println("success"); // 请不要修改或删除
	}
	
	/**
	 * 云聚付付款回调页面
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = YjfPayService.CALLBACK_PAGE_PATH)
	public String yjfPayCallbackPag(Model model, HttpServletRequest request, HttpServletResponse response) {
		PayLog.getLogger().info("[{}]页面回调了", PlatformTypeEnum.云聚付);
		return payBackPage(PlatformTypeEnum.云聚付, request, model);
	}
	
	/**
	 * 云聚付付款回调数据接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value = YjfPayService.CALLBACK_DATA_PATH)
	public void yjfPayCallbackData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PayLog.getLogger().info("[{}]数据回调接口调用,订单号:{}", PlatformTypeEnum.云聚付, request.getParameter("order_number"));
		ResultDTO<PayOrder> resultDto = thirdPayCallbackHandle(PlatformTypeEnum.云聚付, request, true);
		if (resultDto.getResult() == null) {
			return;
		}
		PrintWriter out = response.getWriter();
		out.println("SUCCESS"); // 请不要修改或删除
	}


	/**
	 * 乐付付款回调页面
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = LfPayService.CALLBACK_PAGE_PATH)
	public String lfPayCallbackPag(Model model, HttpServletRequest request, HttpServletResponse response) {
		PayLog.getLogger().info("[{}]页面回调了", PlatformTypeEnum.乐付);
		return payBackPage(PlatformTypeEnum.乐付, request, model);
	}

	/**
	 * 乐付付款回调数据接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value = LfPayService.CALLBACK_DATA_PATH)
	public void lfPayCallbackData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PayLog.getLogger().info("[{}]数据回调接口调用,订单号:{}", PlatformTypeEnum.乐付, request.getParameter("content"));
		ResultDTO<PayOrder> resultDto = thirdPayCallbackHandle(PlatformTypeEnum.乐付, request, true);
		if (resultDto.getResult() == null) {
			return;
		}
		PrintWriter out = response.getWriter();
		out.println("success"); // 请不要修改或删除
	}

	/**
	 * 融合付款回调页面
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = RhPayService.CALLBACK_PAGE_PATH)
	public String rhPayPage(Model model, HttpServletRequest request) {
		PayLog.getLogger().info("[{}]页面回调了", PlatformTypeEnum.融合);
		return payBackPage(PlatformTypeEnum.融合, request, model);
	}

	/**
	 * 融合付款回调数据接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value = RhPayService.CALLBACK_DATA_PATH)
	public void rhPaybackData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PayLog.getLogger().info("[{}]数据回调接口调用,订单号:{}", PlatformTypeEnum.融合, request.getParameter("outTradeNo"));
		ResultDTO<PayOrder> resultDto = thirdPayCallbackHandle(PlatformTypeEnum.融合, request, true);
		if (resultDto.getResult() == null) {
			return;
		}
		PrintWriter out = response.getWriter();
		if (resultDto.getResult().getOrderStatus().equals(PayOrder.OrderStatus.支付成功)) {
			out.println("success"); // 请不要修改或删除
		} else {
			out.println("fail"); // 请不要修改或删除
		}
	}

	/**
	 * 金付卡付款回调页面
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = JfkPayService.CALLBACK_PAGE_PATH)
	public String jfkPayCallbackPag(Model model, HttpServletRequest request, HttpServletResponse response) {
		PayLog.getLogger().info("[{}]页面回调了", PlatformTypeEnum.金付卡);
		return payBackPage(PlatformTypeEnum.金付卡, request, model);
	}

	/**
	 * 金付卡付款回调数据接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value = JfkPayService.CALLBACK_DATA_PATH)
	public void jfkPayCallbackData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultDTO<PayOrder> result = thirdPayCallbackHandle(PlatformTypeEnum.金付卡, request, true);
		if (result.getResult() == null) {
			return;
		}
		PrintWriter out = response.getWriter();
		out.print("SUCCESS");
		out.flush();
	}

	/**
	 * 秒付付款回调页面
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = MfPayService.CALLBACK_PAGE_PATH)
	public String mfPayCallbackPag(Model model, HttpServletRequest request, HttpServletResponse response) {
		PayLog.getLogger().info("[{}]页面回调了", PlatformTypeEnum.秒付);
		return payBackPage(PlatformTypeEnum.秒付, request, model);
	}

	/**
	 * 秒付付款回调数据接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value = MfPayService.CALLBACK_DATA_PATH)
	public void mfPayCallbackData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PayLog.getLogger().info("[{}]数据回调了", PlatformTypeEnum.秒付);
		ResultDTO<PayOrder> result = thirdPayCallbackHandle(PlatformTypeEnum.秒付, request, true);
		if (result.getResult() == null) {
			return;
		}
		PrintWriter out = response.getWriter();
		out.println("success");
		out.flush();
	}

	/**
	 * 闪付付款回调页面
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = SfPayService.CALLBACK_PAGE_PATH)
	public String sfPayCallbackPag(Model model, HttpServletRequest request, HttpServletResponse response) {
		PayLog.getLogger().info("[{}]页面回调了", PlatformTypeEnum.闪付);
		return payBackPage(PlatformTypeEnum.闪付, request, model);
	}

	/**
	 * 闪付付款回调数据接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value = SfPayService.CALLBACK_DATA_PATH)
	public void sfPayCallbackData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PayLog.getLogger().info("[{}]数据回调了", PlatformTypeEnum.闪付);
		ResultDTO<PayOrder> result = thirdPayCallbackHandle(PlatformTypeEnum.闪付, request, true);
		if (result.getResult() == null) {
			return;
		}
		PrintWriter out = response.getWriter();
		out.println("OK");
		out.flush();
	}
	
	/**
	 * 易付宝付款回调--无需前端调用
	 *
	 * @param request
	 *            前端不需传递,有第三方支付平台传递
	 * @return 成功展示部分付款信息,失败带有提示信息
	 */
	@RequestMapping(value = YfbPayService.CALLBACK_PAGE_PATH, method = RequestMethod.GET)
	public String yfbPayCallbackPag(HttpServletRequest request, Model model) {
		PayLog.getLogger().info("[{}]页面回调了", PlatformTypeEnum.易付宝);
		return payBackPage(PlatformTypeEnum.易付宝, request, model);
	}

	/**
	 * 易付宝付款回调数据接口--无需前端调用
	 *
	 * @param request
	 *            前端无需传递,有第三方传递
	 * @param response
	 *            响应实体
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value = YfbPayService.CALLBACK_DATA_PATH, method = RequestMethod.GET)
	public void yfbPayCallbackData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultDTO<PayOrder> result = thirdPayCallbackHandle(PlatformTypeEnum.易付宝, request, true);
		if (result.getResult() == null) {
			return;
		}
		PrintWriter out = response.getWriter();
		out.println("success");
		out.flush();
	}

	private String payBackPage(PlatformTypeEnum platformType, HttpServletRequest request, Model model) {
		ResultDTO<PayOrder> resultDto = null;
		try {
			resultDto = thirdPayCallbackHandle(platformType, request, false);
			if (resultDto.getResult() == null) {
				model.addAttribute("success", "empty");
				model.addAttribute("message", JSON.toJSONString(ResultDTO.error(Constants.ORDER_EMPTY, "订单数据查询为空")));
				return "/pay/payBackPage";
			}
			if (PayOrder.OrderStatus.支付失败.equals(resultDto.getResult().getOrderStatus())) {
				model.addAttribute("success", "true");// 虽然是支付失败,但依然可以拿到订单的returnUrl进行回调,所以设置成true
				model.addAttribute("message", JSON.toJSONString(ResultDTO.error(Constants.PAY_FAILURE, "支付失败")));
				return "/pay/payBackPage";
			} else {
				model.addAttribute("success", "true");
				model.addAttribute("message", JSON.toJSONString(ResultDTO.success("支付成功")));
			}
		} catch (Exception e) {
			model.addAttribute("success", "error");
			model.addAttribute("message",
					JSON.toJSONString(ResultDTO.error(Constants.PAY_BACK_PAGE_ERROR, "回调页面失败:") + e.getMessage()));
			return "/pay/payBackPage";
		}
		model.addAttribute("billNo", resultDto.getResult().getConnBillno());
		model.addAttribute("orderNo", resultDto.getResult().getBillNo());
		model.addAttribute("returnUrl", resultDto.getResult().getReturnUrl());
		model.addAttribute("orderDate", resultDto.getResult().getCreatedDate());
		return "/pay/payBackPage";
	}

	/**
	 * 第三方支付回调处理
	 * 
	 * @param platform
	 *            支付平台
	 * @param request
	 *            请求对象
	 * @param isSaveResult
	 *            是否保存回调结果数据(目前回调有两种回调：1、页面回调 2、数据接口回调。一般数据接口才会保存回调结果)
	 * @return
	 * @throws ThirdPayBussinessException
	 * @throws BusinessException
	 */
	private ResultDTO<PayOrder> thirdPayCallbackHandle(PlatformTypeEnum platformType, HttpServletRequest request,
			boolean isSaveResult) throws ThirdPayBussinessException, BusinessException {
		PayConfig pc = payConfigService.queryConfigByType(platformType.getName(), MethodType.单号获取);
		String orderCode = "";
		try {
			Class<?> cls = Class.forName(pc.getClassPath());
			Method method = ReflectionUtils.findMethod(cls, pc.getClassMethod(),
					new Class<?>[] { HttpServletRequest.class });
			Object obj = ReflectionUtils.invokeMethod(method, cls.newInstance(), new Object[] { request });
			orderCode = obj.toString();
		} catch (Exception e1) {
			PayLog.getLogger().error("[{}]初始化类失败,类:{}", platformType.getName(), pc.getClassPath(), e1);
			return ResultDTO.error(Constants.CLASS_REFLECT_ERROR, "初始化类失败:" + e1.getMessage());
		}
		if (StringUtils.isEmpty(orderCode)) {
			PayLog.getLogger().error("[{}]订单号为空!", platformType.getName());
			return ResultDTO.error(Constants.ORDER_NO_EMPTY, "订单号获取失败!");
		}
		final PayOrder order = payOrderService.queryOrderByTransBillno(orderCode);
		if (null == order) {
			PayLog.getLogger().error("[{}]订单不存在,订单号;{}", platformType.getName(), orderCode);
			return ResultDTO.error(Constants.ORDER_EMPTY, "订单不存在:" + orderCode);
		}
		PayLog.getLogger().info("[{}:{},{}]{}回调,标识:{}", platformType.getName(), order.getTransBillNo(),
				order.getConnBillno(), isSaveResult ? "数据" : "页面", UUID.randomUUID().toString());
		// 若已经支付成功则忽略
		if (order.getOrderStatus().equals(PayOrder.OrderStatus.支付成功) == false) {
			// 是否需要保存支付结果
			if (isSaveResult) {
				try {
					pc = payConfigService.queryConfigByType(platformType.getName(), MethodType.校验回调);
					Class<?> cls = Class.forName(pc.getClassPath());
					Method method = ReflectionUtils.findMethod(cls, pc.getClassMethod(),
							new Class<?>[] { HttpServletRequest.class, order.getClass() });
					ReflectionUtils.invokeMethod(method, cls.newInstance(), new Object[] { request, order });
					// 检查回调信息
					order.setOrderStatus(PayOrder.OrderStatus.支付成功);
					order.setOrderStatusDesc(DateUtil.getNow() + ":" + PayOrder.OrderStatus.支付成功.getName());
				} catch (Exception e) {
					order.setOrderStatus(PayOrder.OrderStatus.支付失败);
					order.setOrderStatusDesc(e.getMessage()==null?"校验失败":e.getMessage());
					PayLog.getLogger().info("[{}:{},{}]{}回调,校验失败", platformType.getName(), order.getTransBillNo(),
							order.getConnBillno(), isSaveResult ? "数据" : "页面", e);
				}
				final BusinessLock lock = new BusinessLock(LockType.充值回调, order.getBillNo());
				if (lock.checkAndLock()) {
					PayLog.getLogger().info("[{}:{},{}]{}回调,重复回调", platformType.getName(), order.getTransBillNo(),
							order.getConnBillno(), isSaveResult ? "数据" : "页面");
				} else {
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								PayOrder order2 = payOrderService.find(order.getId());
								if (!order2.getOrderStatus().equals(PayOrder.OrderStatus.支付成功)) {
									// rechargeManageService.paymentRechargeHandler(order);
									// 回调成功后的处理逻辑.
									boolean flag = payOrderService.updateOrderStatus(order);
									PayLog.getLogger().info("[{}:{},{}]{}回调,订单修改{}", platformType.getName(),
											order.getTransBillNo(), order.getConnBillno(), isSaveResult ? "数据" : "页面",
											flag ? "成功" : "失败");
									if (flag) {
										sendNotify(platformType, order);
									}
								}
								lock.unLock();
							} catch (Exception e) {
								PayLog.getLogger().info("[{}:{},{}]{}回调,处理失败", platformType.getName(),
										order.getTransBillNo(), order.getConnBillno(), isSaveResult ? "数据" : "页面", e);
							}
						}
					}).start();
				}
			}

		} else {
			PayLog.getLogger().info("[{}:{},{}]{}回调,订单支付成功,忽略处理", platformType.getName(), order.getTransBillNo(),
					order.getConnBillno(), isSaveResult ? "数据" : "页面");
		}
		return ResultDTO.success(order);
	}

	/**
	 * 异步发送通知 异步回调规则:组装回调参数后通过http发送请求 给notifyUrl接入方,接入方收到请求处理完业务
	 * 逻辑后需要out.print("SUCCESS")回来用于判断 是否回调成功,如果没有接收到,则每隔1分钟继续重复回调 回调100次后则不再回调
	 * 
	 * @param order
	 */
	private void sendNotify(PlatformTypeEnum platformType, PayOrder order) {
		PayTenant pcp = order.getTenantId();
		Map<String, String> map = new HashMap<String, String>();
		String orderDate = DateUtil.dateToStr(order.getCreatedDate());
		String param = pcp.getMemberId() + "|" + pcp.getTerminalId() + "|" + order.getConnBillno() + "|"
				+ order.getBillNo() + "|" + orderDate + "|" + order.getOrderAmount() + "|"
				+order.getOrderStatus().getValue();
		String sign = DesEncryptUtils.parseByte2HexStr(DesEncryptUtils.encrypt(param, pcp.getMerKey()));
		map.put("merNo", pcp.getMemberId());
		map.put("terminalId", pcp.getTerminalId());
		map.put("billNo", order.getBillNo());
		map.put("orderNo", order.getConnBillno());
		map.put("orderDate", orderDate);
		map.put("money", order.getOrderAmount() + "");
		map.put("status", order.getOrderStatus().getValue());
	//	map.put("reqReserved", order.getReqReserved());
		map.put("sign", sign);
		map.put("remark", order.getOrderStatusDesc());
		PayLog.getLogger().info("[{}:{},{}]异步回调,异步回调参数:{}", platformType.getName(), order.getTransBillNo(),
				order.getConnBillno(), JSON.toJSONString(map));
		PayPreference pre = payPreferenceService.queryPreference(PreferenceConstants.CALLBACK_NUM);
		int maxNum = 1;// 默认50
		if (pre != null) {
			maxNum = Integer.parseInt(pre.getValue());
		}
		int num = 0;
		while (true) {
			if (num >= maxNum) {
				break;
			}
			num++;
			try {
				String resp = HttpUtils.sendPost(order.getNotifyUrl(), map);
				if (StringUtils.isNotEmpty(resp) && ("SUCCESS".equals(resp) || "success".equals(resp))) {
					PayLog.getLogger().info("[{}:{},{}]异步回调,第{}次回调成功!", platformType.getName(), order.getTransBillNo(),
							order.getConnBillno(), num);
					break;
				}
				PayLog.getLogger().info("[{}:{},{}]异步回调,第{}次回调失败,接入端返回:{}", platformType.getName(),
						order.getTransBillNo(), order.getConnBillno(), num, resp);
				Thread.sleep(1000);
			} catch (Exception e) {
				PayLog.getLogger().info("[{}:{},{}]异步回调,异步回调失败,第{}次回调!", platformType.getName(), order.getTransBillNo(),
						order.getConnBillno(), num, e);
			}
		}

	}
	
	public static void main(String[] args) {
		 Map<String, String> map = new HashMap<>();
			map.put("billNo", "CZ20171229181133282988");
			map.put("orderNo", "456");
			map.put("orderDate", "2017-12-29 18:11:33");
			map.put("money",  "1.0");
			map.put("status","2");
		//	map.put("reqReserved", order.getReqReserved());
		map.put("sign", "A9E869AB3BF0FEDABF995ED17A88764EA29482B13844EB7542357697F7B9A34459385CC044DC6FA8F4B662438BAFE58DF8699DAC156F56D9D143AE28984FCBCE389068B9ADD810CA3B5AD512E016CCE2");
		String resp = HttpUtils.sendPost("http://zx.jjcai.net/app/callbackData", map);
		System.out.println(resp);
	}

}
