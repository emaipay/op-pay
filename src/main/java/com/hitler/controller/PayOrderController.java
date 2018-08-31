package com.hitler.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitler.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hitler.controller.support.GenericController;
import com.hitler.core.Constants;
import com.hitler.core.dto.ResultDTO;
import com.hitler.core.dto.pay.BankDto;
import com.hitler.core.dto.pay.PayMerchantDto;
import com.hitler.core.dto.pay.PayQueryOrderDto;
import com.hitler.core.log.PayLog;
import com.hitler.core.utils.BillNoUtils;
import com.hitler.core.utils.CollectionHelper;
import com.hitler.core.utils.DesEncryptUtils;
import com.hitler.core.utils.HttpUtils;
import com.hitler.core.utils.StringUtils;
import com.hitler.entity.PayBank;
import com.hitler.entity.PayConfig;
import com.hitler.entity.PayConfig.MethodType;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayOrder;
import com.hitler.entity.PayOrder.OrderStatus;
import com.hitler.entity.PayPlatform;
import com.hitler.entity.PayPlatformBank;
import com.hitler.entity.PayTenantLimit;
import com.hitler.entity.PayTenantMerchant;
import com.hitler.payservice.SmilePayService;
import com.hitler.service.IPayBankService;
import com.hitler.service.IPayConfigService;
import com.hitler.service.IPayMerchantService;
import com.hitler.service.IPayOrderService;
import com.hitler.service.IPayPlatformService;
import com.hitler.service.IPayTenantLimitService;
import com.hitler.service.IPayTenantMerchantService;
import com.hitler.service.IPayTenantService;
import com.hitler.util.HttpClientUtil;
import com.hitler.util.PayCommonUtil;

/**
 * 在线充值记录 控制层
 */
@Controller
@RequestMapping("/pay")
public class PayOrderController extends GenericController {

    @Resource
    private IPayTenantMerchantService payTenantMerchantService;

    @Resource
    private IPayBankService payBankService;

    @Resource
    private IPayMerchantService payMerchantService;

    @Resource
    private IPayPlatformService platformService;

    @Resource
    private IPayOrderService payOrderService;

    @Resource
    private IPayConfigService payConfigService;

    @Resource
    private IPayTenantLimitService payTenantLimitService;

    @Resource
    private IPayTenantService payTenantService;

    /**
     * 订单提交接口,供外部接入平台调用
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public String payOrder(HttpServletRequest request, HttpServletResponse response, Model model) {

        String merNo = request.getParameter("merNo");
        String terminalId = request.getParameter("terminalId");
        String merchantId = request.getParameter("merchantId");
        String money = request.getParameter("money");
        String bankId = request.getParameter("bankId");
        String notifyUrl = request.getParameter("notifyUrl");
        String returnUrl = request.getParameter("returnUrl");
        String reqReserved = request.getParameter("reqReserved");
        //String platformId = request.getParameter("platformId");
        String billNo = request.getParameter("billNo");
        String summary = request.getParameter("summary");
        String openid = request.getParameter("openid");
        PayMerchant payMerchant = payMerchantService.find(Integer.parseInt(merchantId));
        Integer isPage = payMerchant.getPageType();
        PayLog.getLogger().info("[支付订单提交:{}]充值订单,参数:{}", request.getParameter("billNo"),
                JSON.toJSON(request.getParameterMap()));
        List<PayTenantMerchant> pmList = payTenantMerchantService.queryPayMerchant(merNo, terminalId);
        if (isPage != null && isPage.intValue() == 0) {
            response.setContentType("application/json;charset=UTF-8");
        }

        if (!checkData(request, model, pmList)) {
            //app支付
            if (isPage != null && isPage.intValue() == 0) {
                try {
                    PrintWriter out = response.getWriter();
                    Map<String, Object> map = model.asMap();
                    out.println(map.get("error").toString());
                } catch (IOException e) {
                }
                return null;
            }
            return "/pay/paySubmit";
        }
        double amount = Double.parseDouble(money);
        PayBank payBank = payBankService.find(Integer.parseInt(bankId));

        PayPlatform payPlatform = platformService.find(payMerchant.getPlatformId().getId());
        PayTenantLimit pcl = payTenantLimitService.queryPayConnLimt(pmList.get(0).getTenantId().getId(),
                payPlatform.getId());
        if (pcl.getOnetimeRechargeAmountMax() > 0 || pcl.getOnetimeRechargeAmountMin() > 0) {
            if (amount > pcl.getOnetimeRechargeAmountMax() || amount < pcl.getOnetimeRechargeAmountMin()) {
                //app支付
                if (isPage != null && isPage.intValue() == 0) {
                    try {
                        PrintWriter out = response.getWriter();
                        out.println(JSON.toJSONString(ResultDTO.error(Constants.ORDER_SAVE_ERROR, "单笔充值金额大于最大充值金额或小于最小金额!")));
                    } catch (IOException e) {
                    }
                    return null;
                }
                model.addAttribute("error",
                        JSON.toJSONString(ResultDTO.error(Constants.ORDER_SAVE_ERROR, "单笔充值金额大于最大充值金额或小于最小金额!")));
                return "/pay/paySubmit";
            }
        }
        // 保存充值订单
        String biilno = BillNoUtils.generateBillNo(BillNoUtils.PREFIX_CZ);
        PayOrder po = new PayOrder();
        po.setBillNo(biilno);
        po.setCreatedBy(merNo);
        po.setMerchantId(payMerchant);
        Double fee = Double.parseDouble(money) * (payMerchant.getFeePercent() / 100D);
        po.setFee(fee);
        po.setPayerBankId(payBank);
        po.setOrderAmount(Double.parseDouble(money));
        po.setNotifyUrl(notifyUrl);
        po.setReturnUrl(returnUrl);
        po.setConnBillno(billNo);
        po.setOrderStatus(OrderStatus.未付款);
        po.setPlatformId(payPlatform);
        po.setTenantId(pmList.get(0).getTenantId());
        po.setTransBillNo(biilno.substring(4));
        po.setSummary(summary);
        po.setReqReserved(reqReserved);
        po.setTerminalType(payMerchant.getTerminalType());
        po.setOpenid(openid);

        try {
            payOrderService.save(po);
        } catch (Exception ignore) {
            PayOrder order = payOrderService.queryOrderByConnBillno(billNo);
            if (order != null) {
                order.setFee(po.getFee());
                order.setOrderAmount(po.getOrderAmount());
                order.setBillNo(biilno);
                order.setTransBillNo(biilno.substring(4));
                try {
                    payOrderService.update(po);
                }catch (Exception ignore1) {
                }
            }
        }

        try {
            // 获取付款银行
            PayPlatformBank ppb = platformService.queryPayPlatformBankCode(payBank.getId(), payPlatform.getId());
            PayConfig pc = payConfigService.queryConfigByType(payPlatform.getPlatformTypeId().getPlatformType(),
                    MethodType.数据组装);
            Class<?> cls = Class.forName(pc.getClassPath());
            Method method = ReflectionUtils.findMethod(cls, pc.getClassMethod(),
                    new Class<?>[]{HttpServletRequest.class, po.getClass(), String.class});
            Object obj = ReflectionUtils.invokeMethod(method, cls.newInstance(),
                    new Object[]{request, po, ppb.getPayCode()});
            Map<String, Object> map = (Map<String, Object>) obj;
            Object urlObj = map.get("postUrl");
            if (urlObj == null) {
                model.addAttribute("error",
                        JSON.toJSONString(ResultDTO.error(Constants.PARAM_EMPTY, "数据组装错误,postUrl为空!")));
                po.setOrderStatus(OrderStatus.支付失败);
                payOrderService.update(po);
                return "/pay/paySubmit";
            }


            String orderId = (String) map.get("orderId");
            if (StringUtils.isNotBlank(orderId)) {//第三方支付ID
                po.setTransactionId(orderId);
                map.remove("orderId");
            }

            Integer redirect = (Integer) map.get("redirect");
            if (redirect == null) {
                model.addAttribute("map", map);
                return "/pay/paySubmit";
            }

            switch (redirect) {
                case 0://网银或者支付宝支付//app支付
                    // payOrderService.save(po);
                    //网银或者支付宝支付，非app支付
                    if (isPage == null || isPage.intValue() != 0) {
                        //System.out.println("111111111111111111111111111");
                        response.setContentType("text/html;charset=UTF-8");
                    }

                    PrintWriter out = response.getWriter();
                    out.println(urlObj.toString());
                    return null;
                case 1://直接跳转
                    //payOrderService.save(po);
                    response.setContentType("text/html;charset=UTF-8");
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("<!DOCTYPE html>");
                    stringBuffer.append("<html lang=\"en\"><head>");
                    stringBuffer.append("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
                    stringBuffer.append("<title>支付跳转中.......</title></head>");
                    stringBuffer.append("<body>");
                    stringBuffer.append("<script type=\"text/javascript\">");
                    stringBuffer.append("window.location.href = '");
                    stringBuffer.append(urlObj.toString());
                    stringBuffer.append("';").append("</script></body></html>");
                    out = response.getWriter();
                    out.println(stringBuffer.toString());

                    return null;
                case 2://微信公众号
                    ResultDTO<SortedMap<String, String>> dto = (ResultDTO<SortedMap<String, String>>) urlObj;
                    if (dto.getSuccess()) {
                        SortedMap<String, String> parameterMap = dto.getResult();
                        for (String key : parameterMap.keySet()) {
                            model.addAttribute(key, parameterMap.get(key));
                        }
                        model.addAttribute("returnUrl", returnUrl);
                        // payOrderService.save(po);
                        return "/pay/wxPay";
                    } else {
                        model.addAttribute("error", JSON.toJSONString(dto));
                        po.setOrderStatus(OrderStatus.支付失败);
                        po.setOrderStatusDesc(dto.getRespMsg());
                         payOrderService.update(po);
                    }
                    break;
                case 3: //扫码支付
                    model.addAttribute("returnUrl", urlObj.toString());
                    // payOrderService.save(po);
                    return "/pay/scan";
            }

        } catch (Exception e) {
            PayLog.getLogger().error("[支付订单提交:{}]反射订单处理失败,参数:{}", request.getParameter("billNo"),
                    JSON.toJSON(request.getParameterMap()), e);
            model.addAttribute("error",
                    JSON.toJSONString(ResultDTO.error(Constants.REFLECT_ERROR, "充值订单保存失败,请联系相关人员!")));
            po.setOrderStatus(OrderStatus.支付失败);
            try {
                payOrderService.update(po);
            } catch (Exception e1) {
                PayLog.getLogger().error("[支付订单提交:{}]反射充值订单update失败,参数:{}", e);
            }
            //app支付
            if (isPage != null && isPage.intValue() == 0) {
                try {
                    PrintWriter out = response.getWriter();
                    out.println(JSON.toJSONString(ResultDTO.error(Constants.REFLECT_ERROR, "充值订单保存失败,请联系相关人员!")));
                } catch (IOException e1) {
                }
                return null;
            }
        }

        return "/pay/paySubmit";
    }

    private void handlePay() {

    }

    public boolean checkData(HttpServletRequest request, Model model, List<PayTenantMerchant> pmList) {
        try {
            String merNo = request.getParameter("merNo");
            String merchantId = request.getParameter("merchantId");
            String terminalId = request.getParameter("terminalId");
            //String platformId = request.getParameter("platformId");
            String bankId = request.getParameter("bankId");
            String billNo = request.getParameter("billNo");
            String money = request.getParameter("money");
            String summary = request.getParameter("summary");
            String notifyUrl = request.getParameter("notifyUrl");
            String sign = request.getParameter("sign");
            String returnUrl = request.getParameter("returnUrl");
            if (StringUtils.isEmpty(terminalId) || StringUtils.isEmpty(merNo) || StringUtils.isEmpty(merchantId)
                    /*|| StringUtils.isEmpty(platformId)*/ || StringUtils.isEmpty(bankId) || StringUtils.isEmpty(money)
                    || StringUtils.isEmpty(notifyUrl) || StringUtils.isEmpty(sign) || StringUtils.isEmpty(returnUrl)
                    || StringUtils.isEmpty(billNo) || StringUtils.isEmpty(summary)) {
                model.addAttribute("error", JSON.toJSONString(ResultDTO.error(Constants.PARAM_EMPTY, "参数缺失!")));
                PayLog.getLogger().error("[支付订单提交:{}]参数缺失:{}", billNo, JSON.toJSONString(request.getParameterMap()));

                return false;
            }
            Double orderMoney = 0D;

            orderMoney = Double.parseDouble(money);
            if (orderMoney <= 0) {
                PayLog.getLogger().error("[支付订单提交:{}]金额小于等于0:{}", billNo, orderMoney);
                model.addAttribute("error", JSON.toJSONString(ResultDTO.error(Constants.MONEY_ERROR, "金额必须大于0!")));
                return false;
            }
            // merId+"|"+terminalId+"|"+merchantId+"|"+platformId+"|"+bankId+"|"+testBillNo+"|"+date+"|"+money+"|"+notifyUrl+"|"+returnUrl
			/*String str = merNo + "|" + terminalId + "|" + merchantId + "|" + platformId + "|" + bankId + "|" + billNo
					+ "|" + money + "|" + notifyUrl + "|" + returnUrl;*/
            String str = merNo + "|" + terminalId + "|" + merchantId + "|" + bankId + "|" + billNo
                    + "|" + orderMoney + "|" + notifyUrl + "|" + returnUrl;

            if (CollectionHelper.isEmpty(pmList)) {
                model.addAttribute("error",
                        JSON.toJSONString(ResultDTO.error(Constants.MERCHANT_NOT_EXISTS, "商户不存在!")));
                PayLog.getLogger().error("[支付订单提交:{}]商户不存在!,商户:{},{}", billNo, merNo, terminalId);
                return false;
            }
            byte[] decryptFrom = DesEncryptUtils.parseHexStr2Byte(sign);
            String signEntry = new String(DesEncryptUtils.decrypt(decryptFrom, pmList.get(0).getTenantId().getMerKey()),
                    "UTF-8");
            if (!str.equals(signEntry)) {
                PayLog.getLogger().error("[支付订单提交:{}]商户不存在!,商户:{},{},密钥:{}", billNo, merNo, terminalId, str);
                model.addAttribute("error", JSON.toJSONString(ResultDTO.error(Constants.SIGN_ERROR, "秘钥验证不通过,请检查参数!")));
                return false;
            }
			/*PayOrder po = payOrderService.queryOrderByConnBillno(billNo);
			if (po != null) {
				model.addAttribute("error",
						"订单号重复,请检查您的订单号生成方式!");
				return false;
			}*/
        } catch (Exception e) {
            PayLog.getLogger().error("[支付订单提交:{}]参数:{}", request.getParameter("billNo"),
                    JSON.toJSON(request.getParameterMap()), e);
            model.addAttribute("error", JSON.toJSONString(ResultDTO.error(Constants.MONEY_ERROR, "参数错误!")));
            return false;
        }
        return true;
    }

    /**
     * 订单查询方法
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/order/query", method = RequestMethod.GET)
    public ResultDTO<PayQueryOrderDto> queryOrder(HttpServletRequest request, Model model) {
        try {
            String merNo = request.getParameter("merNo");
            String terminalId = request.getParameter("terminalId");
            String billno = request.getParameter("billNo");
            String sign = request.getParameter("sign");
            if (StringUtils.isEmpty(merNo) || StringUtils.isEmpty(sign) || StringUtils.isEmpty(terminalId)
                    || StringUtils.isEmpty(billno)) {
                return ResultDTO.error(Constants.PARAM_EMPTY, "参数缺失!");
            }
            List<PayTenantMerchant> pmList = payTenantMerchantService.queryPayMerchant(merNo, terminalId);
            if (CollectionHelper.isEmpty(pmList)) {
                PayLog.getLogger().error("[支付订单提交:{}]商户不存在!,商户:{},{}", billno, merNo, terminalId);
                return ResultDTO.error(Constants.MERCHANT_NOT_EXISTS, "商户不存在!");
            }
            String str = merNo + "|" + terminalId + "|" + billno;
            byte[] decryptFrom = DesEncryptUtils.parseHexStr2Byte(sign);
            String signEntry = new String(DesEncryptUtils.decrypt(decryptFrom, pmList.get(0).getTenantId().getMerKey()),
                    "UTF-8");
            if (!str.equals(signEntry)) {
                PayLog.getLogger().error("[支付订单提交:{}]密钥验证不通过:{}", billno, str);
                return ResultDTO.error(Constants.SIGN_ERROR, "秘钥验证不通过,请检查参数!");
            }
            PayOrder po = payOrderService.queryOrderByConnBillno(billno);
            if (po == null) {
                PayLog.getLogger().error("[支付订单提交:{}]未查询到该订单.", billno, str);
                return ResultDTO.error(Constants.NO_ORDER, "未查询到该订单!");
            }
            if (po.getOrderStatus().equals(PayOrder.OrderStatus.未付款)) {
                try {
                    // 获取付款银行
                    //PayBank payBank = po.getPayerBankId();
                    PayPlatform payPlatform = po.getPlatformId();
                    PayConfig pc = payConfigService.queryConfigByType(payPlatform.getPlatformTypeId().getPlatformType(),
                            MethodType.订单查询);
                    Class<?> cls = Class.forName(pc.getClassPath());
                    Method method = ReflectionUtils.findMethod(cls, pc.getClassMethod(),
                            new Class<?>[]{HttpServletRequest.class, po.getClass(), String.class});

                    ReflectionUtils.invokeMethod(method, cls.newInstance(), new Object[]{request, po});
					/*Object obj = ReflectionUtils.invokeMethod(method, cls.newInstance(),new Object[] { request, po});
					PayOrderQueryData data = (PayOrderQueryData) obj;*/
                    if (po.getOrderStatus().equals(PayOrder.OrderStatus.未付款) == false) {
                        payOrderService.updateOrderStatus(po);
                    }
                } catch (Exception e) {
                    PayLog.getLogger().error("支付订单查询异常:" + e.getMessage(), e);
                }
            }
            String respStr = merNo + "|" + terminalId + "|" + billno + "|" + po.getFactAmount() + "|" + po.getBillNo();
            String respSign = DesEncryptUtils
                    .parseByte2HexStr(DesEncryptUtils.encrypt(respStr, pmList.get(0).getTenantId().getMerKey()));
            PayQueryOrderDto pqod = new PayQueryOrderDto();
            pqod.setOrderStatus(po.getOrderStatus().getValue());
            pqod.setBillNo(po.getConnBillno());
            pqod.setMerNo(merNo);
            pqod.setTerminalId(terminalId);
            pqod.setMoney(po.getFactAmount());
            pqod.setOrderDate(po.getCreatedDate());
            pqod.setOrderNo(po.getBillNo());
            pqod.setSign(respSign);
            return ResultDTO.success(pqod, "成功");
        } catch (Exception e) {
            PayLog.getLogger().error("[支付订单查询:{},{},{}]参数:{}", request.getParameter("billNo"),
                    request.getParameter("merNo"), request.getParameter("terminalId"),
                    JSON.toJSON(request.getParameterMap()), e);
            return ResultDTO.error(Constants.EXCEPTION, "查询异常!");
        }
    }

    @RequestMapping(value = "/jump", method = RequestMethod.POST)
    public String jump(HttpServletRequest request, Model model) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Set<String> set = requestMap.keySet();
        Iterator<String> iter = set.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            String[] str = requestMap.get(key);
            paramMap.put(key, str[0]);
        }
        model.addAttribute("paramMap", paramMap);
        return "/pay/jump";
    }

    /**
     * 查询支付通道接口
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryPayChannels", method = RequestMethod.GET)
    public ResultDTO<List<PayMerchantDto>> queryPayChannels(HttpServletRequest request) {
        try {
            String merNo = request.getParameter("merNo");
            String terminalId = request.getParameter("terminalId");
            String sign = request.getParameter("sign");
            if (StringUtils.isEmpty(merNo) || StringUtils.isEmpty(sign) || StringUtils.isEmpty(terminalId)) {
                return ResultDTO.error(Constants.PARAM_EMPTY, "参数缺失!");
            }
            List<PayTenantMerchant> pmList = payTenantMerchantService.queryPayMerchant(merNo, terminalId);
            if (CollectionHelper.isEmpty(pmList)) {
                PayLog.getLogger().error("[查询支付通道接口]商户不存在!,商户:{},{}", merNo, terminalId);
                return ResultDTO.error(Constants.MERCHANT_NOT_EXISTS, "商户不存在!");
            }
            String str = merNo + "|" + terminalId;
            byte[] decryptFrom = DesEncryptUtils.parseHexStr2Byte(sign);
            String signEntry = new String(DesEncryptUtils.decrypt(decryptFrom, pmList.get(0).getTenantId().getMerKey()),
                    "UTF-8");
            if (!str.equals(signEntry)) {
                PayLog.getLogger().error("[查询支付通道接口]密钥验证不通过:{}，{}", str, signEntry);
                return ResultDTO.error(Constants.SIGN_ERROR, "秘钥验证不通过,请检查参数!");
            }
            Collection<PayPlatformBank> paymentBankList = payTenantService.findByTenantId(pmList.get(0).getTenantId().getId());
            //支付平台
            //Map<Integer, PayPlatform> platformMap = new HashMap<Integer, PayPlatform>();
            Map<Integer, List<PayBank>> bankMap = new HashMap<Integer, List<PayBank>>();
            List<PayBank> list = null;
            for (PayPlatformBank obj : paymentBankList) {
                //platformMap.put(obj.getPayPlatformId().getId(), obj.getPayPlatformId());
                list = bankMap.get(obj.getPayPlatformId().getId());
                if (list == null) {
                    list = new ArrayList<PayBank>();
                }
                list.add(obj.getBankId());
                bankMap.put(obj.getPayPlatformId().getId(), list);
            }
            List<PayMerchantDto> merchantList = new ArrayList<PayMerchantDto>(pmList.size());
            PayMerchantDto dto = null;
            PayPlatform plat = null;
            List<BankDto> bankList = null;
            BankDto bDto = null;
            for (PayTenantMerchant obj : pmList) {
                dto = new PayMerchantDto();
                dto.setMerchantId(obj.getPayMerchantId().getId());
                //plat = platformMap.get(obj.getPayMerchantId().getPlatformId());
                plat = obj.getPayMerchantId().getPlatformId();
                if (plat != null) {
                    dto.setPlatformName(plat.getName());
                    dto.setPlatformLogo(plat.getPlatformLogo());
                }
                list = bankMap.get(plat.getId());
                if (list != null && !list.isEmpty()) {
                    bankList = new ArrayList<BankDto>(list.size());
                    for (PayBank bank : list) {
                        bDto = new BankDto();
                        bDto.setBankId(bank.getId());
                        bDto.setBankLogo(bank.getLogoFilePath());
                        bDto.setBankName(bank.getName());
                        bDto.setMerchantId(obj.getPayMerchantId().getId());
                        bankList.add(bDto);
                    }
                    dto.setBankList(bankList);
                }
                merchantList.add(dto);
            }
            return ResultDTO.success(merchantList, "成功");
        } catch (Exception e) {
            PayLog.getLogger().error("[查询支付通道接口:{},{}]参数:{}",
                    request.getParameter("merNo"), request.getParameter("terminalId"),
                    JSON.toJSON(request.getParameterMap()), e);
            return ResultDTO.error(Constants.EXCEPTION, "查询异常!");
        }
    }

    /**
     * 查询支付通道接口
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryPayChannelList", method = RequestMethod.GET)
    public ResultDTO<List<BankDto>> queryPayChannelList(HttpServletRequest request) {
        try {
            String merNo = request.getParameter("merNo");
            String terminalId = request.getParameter("terminalId");
            String sign = request.getParameter("sign");
            if (StringUtils.isEmpty(merNo) || StringUtils.isEmpty(sign) || StringUtils.isEmpty(terminalId)) {
                return ResultDTO.error(Constants.PARAM_EMPTY, "参数缺失!");
            }
            List<PayTenantMerchant> pmList = payTenantMerchantService.queryPayMerchant(merNo, terminalId);
            if (CollectionHelper.isEmpty(pmList)) {
                PayLog.getLogger().error("[查询支付通道接口]商户不存在!,商户:{},{}", merNo, terminalId);
                return ResultDTO.error(Constants.MERCHANT_NOT_EXISTS, "商户不存在!");
            }
            String str = merNo + "|" + terminalId;
            byte[] decryptFrom = DesEncryptUtils.parseHexStr2Byte(sign);
            String signEntry = new String(DesEncryptUtils.decrypt(decryptFrom, pmList.get(0).getTenantId().getMerKey()),
                    "UTF-8");
            if (!str.equals(signEntry)) {
                PayLog.getLogger().error("[查询支付通道接口]密钥验证不通过:{}，{}", str, signEntry);
                return ResultDTO.error(Constants.SIGN_ERROR, "秘钥验证不通过,请检查参数!");
            }
            Map<Integer, PayMerchant> merchantMap = new HashMap<Integer, PayMerchant>();
            PayMerchant merchant = null;
            for (PayTenantMerchant obj : pmList) {
                merchant = obj.getPayMerchantId();
                if (merchant != null) {
                    merchantMap.put(merchant.getPlatformId().getId(), merchant);
                }
            }
            Collection<PayPlatformBank> paymentBankList = payTenantService.findByTenantId(pmList.get(0).getTenantId().getId());
            //支付平台
            PayBank bank = null;
            List<BankDto> bankList = new ArrayList<BankDto>(paymentBankList.size());
            BankDto bDto = null;
            for (PayPlatformBank obj : paymentBankList) {
                bank = obj.getBankId();
                bDto = new BankDto();
                bDto.setBankId(bank.getId());
                bDto.setBankLogo(obj.getPayPlatformId().getPlatformLogo());
                bDto.setBankName(obj.getPayPlatformId().getName());
                merchant = merchantMap.get(obj.getPayPlatformId().getId());
                if (merchant != null) {
                    bDto.setMerchantId(merchant.getId());
                }
                bankList.add(bDto);
            }
            return ResultDTO.success(bankList, "成功");
        } catch (Exception e) {
            PayLog.getLogger().error("[查询支付通道接口:{},{}]参数:{}",
                    request.getParameter("merNo"), request.getParameter("terminalId"),
                    JSON.toJSON(request.getParameterMap()), e);
            return ResultDTO.error(Constants.EXCEPTION, "查询异常!");
        }
    }

    /**
     * 提现代付接口
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/unifyWithdraw")
    public ResultDTO<?> unifyWithdraw(HttpServletRequest request) {
        String merNo = request.getParameter("merNo");//第三方平台号（魔智支付提供）
        String terminalId = request.getParameter("terminalId");//第三方终端号（魔智支付提供）
        String money = request.getParameter("money");//提现金额
        String thirdFlowNo = request.getParameter("thirdFlowNo");//提现流水号（第三方提供）
        String withdrawCustNo = request.getParameter("withdrawCustNo");//提现用户编号
        String recvBankAcctName = request.getParameter("recvBankAcctName");//收款人银行开户名(如:张三)
        String recvBankProvince = request.getParameter("recvBankProvince");//收款方银行所在省份(如:广东省)
        String recvBankCity = request.getParameter("recvBankCity");//收款方银行所在城市(如:广州市)
        String recvBankName = request.getParameter("recvBankName");//收款方银行名称（广州银行）
        String recvBankBranchName = request.getParameter("recvBankBranchName");//收款方银行网点名称(如: 广州银行三元桥分行)
        String recvBankAcctNum = request.getParameter("recvBankAcctNum");//收款方银行账户(如:621226XXXXXXXX98727)
        String cardId = request.getParameter("cardId");//收款方身份证号
        String phone = request.getParameter("phone");//收款方银行预留手机号
        String sign = request.getParameter("sign");
        if (StringUtils.isEmpty(merNo) || StringUtils.isEmpty(sign) || StringUtils.isEmpty(terminalId)
                || StringUtils.isEmpty(money) || StringUtils.isEmpty(thirdFlowNo) || StringUtils.isEmpty(recvBankProvince)
                || StringUtils.isEmpty(withdrawCustNo) || StringUtils.isEmpty(recvBankAcctName) || StringUtils.isEmpty(recvBankCity)
                || StringUtils.isEmpty(recvBankName) || StringUtils.isEmpty(recvBankBranchName) || StringUtils.isEmpty(recvBankAcctNum)
                || StringUtils.isEmpty(cardId) || StringUtils.isEmpty(phone)) {
            return ResultDTO.error(Constants.PARAM_EMPTY, "参数缺失!");
        }
        //drawType	Y	int	1	类型来源：1是酷魔提现，2是微笑提现，默认是1
        String drawType = request.getParameter("drawType");
        if (StringUtils.isEmpty(merNo)) {
            drawType = "1";
        }
        //bankCode	N	String	10	参照第5章节：银行编号，当drawType=2时必填写
        String bankCode = request.getParameter("bankCode");
        if (drawType.equals("2") && StringUtils.isEmpty(bankCode)) {
            return ResultDTO.error(Constants.PARAM_EMPTY, "银行编号缺失!");
        }
        List<PayTenantMerchant> pmList = payTenantMerchantService.queryPayMerchant(merNo, terminalId);
        if (CollectionHelper.isEmpty(pmList)) {
            PayLog.getLogger().error("[提现代付接口]商户不存在!,商户:{},{}", merNo, terminalId);
            return ResultDTO.error(Constants.MERCHANT_NOT_EXISTS, "商户不存在!");
        }
        try {
            String str = merNo + "|" + terminalId + "|" + thirdFlowNo + "|" + money + "|" + withdrawCustNo + "|" + recvBankAcctName + "|" + recvBankProvince
                    + "|" + recvBankCity + "|" + recvBankName + "|" + recvBankBranchName + "|" + recvBankAcctNum + "|" + cardId + "|" + phone;
            //sign	Y	String	64	签名：Des加密，加密字符为（merNo + "|" + terminalId + "|" + thirdFlowNo+"|" +money+ "|" +withdrawCustNo+ "|" +recvBankAcctName+ "|" +recvBankProvince+ "|" +recvBankCity+ "|" +recvBankName+ "|" +recvBankBranchName+ "|" +recvBankAcctNum+ "|" +cardId+ "|" +phone），merKey密钥（魔智支付提供）
            byte[] decryptFrom = DesEncryptUtils.parseHexStr2Byte(sign);
            String signEntry = new String(DesEncryptUtils.decrypt(decryptFrom, pmList.get(0).getTenantId().getMerKey()),
                    "UTF-8");
            if (!str.equals(signEntry)) {
                PayLog.getLogger().error("[提现代付接口]密钥验证不通过:{}，{}", str, signEntry);
                return ResultDTO.error(Constants.SIGN_ERROR, "秘钥验证不通过,请检查参数!");
            }
            if (drawType.equals("2")) {
                return wxWithdraw(pmList.get(0), money, thirdFlowNo, recvBankAcctName, recvBankProvince, recvBankCity,
                        recvBankName, recvBankBranchName, recvBankAcctNum, cardId, phone, bankCode);
            }
            return kmWithdraw(pmList.get(0), money, thirdFlowNo, recvBankAcctName, recvBankProvince,
                    recvBankCity, recvBankName, recvBankBranchName, recvBankAcctNum, cardId, phone);
        } catch (Exception e) {
            PayLog.getLogger().error("[提现代付接口:{},{}]参数:{}",
                    request.getParameter("merNo"), request.getParameter("terminalId"),
                    JSON.toJSON(request.getParameterMap()), e);
            return ResultDTO.error(Constants.EXCEPTION, e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private ResultDTO<?> wxWithdraw(PayTenantMerchant pm, String money, String thirdFlowNo, String recvBankAcctName, String recvBankProvince,
                                    String recvBankCity, String recvBankName, String recvBankBranchName, String recvBankAcctNum, String cardId, String phone
            , String bankCode) throws Exception {
        Map<String, String> paramsMap = new HashMap<String, String>();
        PayMerchant payMerchant = pm.getPayMerchantId();
        //商户号	merchantId	是	string(20)	商户识别号，由微笑支付分配
        paramsMap.put("merchantId", payMerchant.getMerchantNo());
        //交易方式	payMode	是	string(20)	固定值：withdrawal
        paramsMap.put("payMode", "withdrawal");
        //总金额	orderAmount	是	double	商户订单总金额，最小单位：分。例：12.34
        paramsMap.put("orderAmount", money);
        //户名	accountName	是	string(50)	代付账户户名
        paramsMap.put("accountName", recvBankAcctName);
        //卡号	accountNo	是	string(50)	代付账户卡号
        paramsMap.put("accountNo", recvBankAcctNum);
        //银行编号	bankCode	是	string(20)	参照第10章节：银行编号
        paramsMap.put("bankCode", bankCode);
        //开户省份	provinceName	是	string(50)	开户省份。例：广东省
        paramsMap.put("provinceName", recvBankProvince);
        //开户城市	cityName	是	string(50)	开户城市。例：广州市
        paramsMap.put("cityName", recvBankCity);
        //开户网点	branchName	是	string(256)	开户网点名称。例：黄金城道支行
        paramsMap.put("branchName", recvBankName);
        //银行预留手机号	phone	是	string(20)	开户时银行预留手机号码
        paramsMap.put("phone", phone);
        //银行开户证件号	idCard	是	string(20)	开户时证件号码
        paramsMap.put("idCard", cardId);
		/*订单备注	memo	否	string(256)	商户订单备注，原样返回
		字段原样返回，如输入非英文数字，请自行进行UrlEncode和UrlDecode，避免乱码*/
        paramsMap.put("memo", "withdraw");
        //签名方式	encodeType	是	string(10)	固定值：SHA2
        paramsMap.put("encodeType", "SHA2");
        //签名	signSHA2	否	string(128)	签名结果，详见“第4 章 签名规则”
        String signSHA2 = SmilePayService.generateSignature(paramsMap, payMerchant.getKey(), payMerchant.getPublicKey());
        paramsMap.put("signSHA2", signSHA2);
        //http://withdraw.smilepay.vip/v10/realpay/
        String resp = HttpUtils.sendPost("http://withdraw.smilepay.vip/v10/realpay/", paramsMap);
        PayLog.getLogger().info("[微笑代付返回报文：]" + resp);
        if (StringUtils.isNotBlank(resp)) {
            Map<String, Object> map = (Map<String, Object>) JSON.parse(resp);
            String code = (String) map.get("code");
            if (code.equals("00")) {//00表示成功，非00表示失败 此字段是通信标识，非交易标识，交易是否成功需要查看result来判断
                String result = (String) map.get("result");
                if (result.equals("p")) {//Y：出款完成 P: 处理中 F：出款失败
                    return ResultDTO.success("成功");
                } else if (result.equals("F")) {
                    return ResultDTO.error(Constants.DRAW_ERROR, "处理中");
                } else {
                    return ResultDTO.error(Constants.DRAW_ERROR, "出款失败");
                }
            } else {
                return ResultDTO.error(Constants.DRAW_ERROR, (String) map.get("message"));
            }
        }
        return ResultDTO.error(Constants.DRAW_ERROR, "提现失败");
    }

    @SuppressWarnings("unchecked")
    private ResultDTO<?> kmWithdraw(PayTenantMerchant pm, String money, String thirdFlowNo, String recvBankAcctName, String recvBankProvince,
                                    String recvBankCity, String recvBankName, String recvBankBranchName, String recvBankAcctNum, String cardId, String phone) throws Exception {
        Map<String, String> paramsMap = new HashMap<String, String>();
        PayMerchant payMerchant = pm.getPayMerchantId();
        paramsMap.put("appId", payMerchant.getTerminalNo());//主商户appId
        paramsMap.put("custNo", payMerchant.getMerchantNo());//主商户号	custNo
        paramsMap.put("platChannel", "04");//详见附录通道号说明 国付宝
        paramsMap.put("money", money);//提现金额
        paramsMap.put("thirdFlowNo", thirdFlowNo);//提现流水号
        paramsMap.put("withdrawCustNo", payMerchant.getMerchantNo());//提现用户编号
        paramsMap.put("recvBankAcctName", recvBankAcctName); //收款人银行开户名(如:张三)
        paramsMap.put("recvBankProvince", recvBankProvince);//收款方银行所在省份(如:广东省)
        paramsMap.put("recvBankCity", recvBankCity);//收款方银行所在城市(如:广州市)
        paramsMap.put("recvBankName", recvBankName);//收款方银行名称（广州银行）
        paramsMap.put("recvBankBranchName", recvBankBranchName);//收款方银行网点名称(如: 广州银行三元桥分行)
        paramsMap.put("recvBankAcctNum", recvBankAcctNum);//收款方银行账户(如:621226XXXXXXXX98727)
        paramsMap.put("cardId", cardId);//收款方身份证号
        paramsMap.put("phone", phone);//收款方银行预留手机号
        //公私账标识(1对公,2对私)	corpPersonFlag	C(1)	String	必输
        paramsMap.put("corpPersonFlag", "2");
        //签名	sign	C(32)	必输
        paramsMap.put("sign", generateSignature(paramsMap, payMerchant.getKey()));
        String resp = HttpClientUtil.send("https://open.goodluckchina.net/open/balance/unifyWithdraw", paramsMap, "UTF-8");
        //String resp = HttpUtil.Get(url);
        PayLog.getLogger().info("[酷模代付返回报文：]" + resp);
        if (StringUtils.isNotBlank(resp)) {
            Map<String, Object> map = (Map<String, Object>) JSON.parse(resp);
            Integer code = (Integer) map.get("code");
            if (code == 1) {//成功
                return ResultDTO.success("成功");
            } else {
                return ResultDTO.error(Constants.DRAW_ERROR, (String) map.get("msg"));
            }
        }
        return ResultDTO.error(Constants.DRAW_ERROR, "提现失败");
    }

    private String generateSignature(final Map<String, String> data, String key) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim());
        }
        sb.append(key);
        System.out.println(sb.toString());
        return PayCommonUtil.MD5(sb.toString()).toLowerCase();
    }

}
