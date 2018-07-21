package com.hitler.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hitler.controller.support.CRUDController;
import com.hitler.core.Constants;
import com.hitler.core.dto.ResultDTO;
import com.hitler.core.repository.OP;
import com.hitler.core.repository.SearchFilter;
import com.hitler.core.utils.CollectionHelper;
import com.hitler.core.utils.StringUtils;
import com.hitler.dto.PayTenantMerchantCreateDTO;
import com.hitler.dto.PayTenantMerchantDTO;
import com.hitler.dto.PayTenantMerchantUpdateDTO;
import com.hitler.entity.PayBank;
import com.hitler.entity.PayPlatform;
import com.hitler.entity.PayTenantLimit;
import com.hitler.entity.PayTenantMerchant;
import com.hitler.gen.PayTenant_;
import com.hitler.service.IPayBankService;
import com.hitler.service.IPayTenantLimitService;
import com.hitler.service.IPayTenantMerchantService;
import com.hitler.service.support.IGenericService;
import com.hitler.table.PayTenantMerchantTable;
import com.hitler.vo.ConnMerchantVo;
import com.hitler.vo.MerchantBankVo;
import com.hitler.vo.PayConnLimitVo;

/**
 * 接入商户对应第三方支付商户 控制层
 * @author klp
 *
 */
@Controller
@RequestMapping("pay/" + PayTenantMerchantController.PATH)
public class PayTenantMerchantController extends
		CRUDController<PayTenantMerchant, Integer, PayTenantMerchantDTO, PayTenantMerchantCreateDTO, PayTenantMerchantUpdateDTO, PayTenantMerchantTable<PayTenantMerchantDTO>> {
	public static final String PATH = "payTenantMerchant";
	@Resource
	private IPayTenantMerchantService  payTenantMerchantService;
	@Resource
	private IPayBankService payBankService;
	@Resource
	private IPayTenantLimitService payTenantLimitService;

	public PayTenantMerchantController() {
		super(PATH);
	}

	
	
	
	
	@Override
	protected IGenericService<PayTenantMerchant, Integer> getService() {
		return payTenantMerchantService;
	}

	@Override
	protected void postList(Model model, Pageable pageable, ServletRequest request, List<SearchFilter> filters)
			throws Exception {
		String username = request.getParameter("platformName");
		if (StringUtils.isNotEmpty(username)) {
			filters.add(new SearchFilter(PayTenant_.platformName.getName(), OP.NE, username));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/merchant", method = RequestMethod.GET)
	public ResultDTO<?> getConnMerchantList(HttpServletRequest request) {
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort() + "/";
		String memberId = request.getParameter("memberId");
		if (StringUtils.isEmpty(memberId)) {
			return ResultDTO.error(Constants.MERCHANT_NO_EMPTY, "商户号不能为空!");
		}
		String terminalId = request.getParameter("terminalId");
		if (StringUtils.isEmpty(terminalId)) {
			return ResultDTO.error(Constants.TERMINAL_NO_EMPTY, "终端号不能为空!");
		}
		List<PayTenantMerchant> pmList = payTenantMerchantService.queryPayMerchant(memberId, terminalId);
		if (CollectionHelper.isEmpty(pmList)) {
			return ResultDTO.error(Constants.MERCHANT_NOT_EXISTS, "商户不存在!");
		}
		String merKey = request.getParameter("merKey");
		if (!pmList.get(0).getTenantId().getMerKey().equals(merKey)) {
			return ResultDTO.error(Constants.MER_KEY_ERROR, "秘钥不正确!");
		}
		List<ConnMerchantVo> list = new ArrayList<ConnMerchantVo>();
		for (PayTenantMerchant pm : pmList) {
			PayPlatform pp = pm.getPayMerchantId().getPlatformId();
			ConnMerchantVo vo = new ConnMerchantVo();
			vo.setPlatformId(pp.getId());
			vo.setMerchantId(pm.getPayMerchantId().getId());
			vo.setPlatformName(pp.getName());
			vo.setPlatformLogo(basePath + pp.getPlatformLogo());
			List<PayBank> pbList = payBankService.queryPayBank(pm.getPayMerchantId().getId(), pm.getTenantId().getId());
			List<MerchantBankVo> mbvList = new ArrayList<MerchantBankVo>();
			for (PayBank pb : pbList) {
				MerchantBankVo mbv = new MerchantBankVo();
				mbv.setBankId(pb.getId());
				mbv.setBankLogo(basePath + pb.getLogoFilePath());
				mbv.setBankName(pb.getName());
				mbvList.add(mbv);
			}
			PayTenantLimit pcl = payTenantLimitService.queryPayConnLimt(pm.getTenantId().getId(), pp.getId());
			if (pcl != null) {
				PayConnLimitVo pclv = new PayConnLimitVo();
				pclv.setDailyRechargeAmountMax(pcl.getDailyRechargeAmountMax());
				pclv.setDailyRechargeTimesMax(pcl.getDailyRechargeTimesMax());
				pclv.setOnetimeRechargeAmountMax(pcl.getOnetimeRechargeAmountMax());
				pclv.setOnetimeRechargeAmountMin(pcl.getOnetimeRechargeAmountMin());
				vo.setPayConnLimitVo(pclv);
			}else{
				PayConnLimitVo pclv = new PayConnLimitVo();
				pclv.setDailyRechargeAmountMax(0D);
				pclv.setDailyRechargeTimesMax(0);
				pclv.setOnetimeRechargeAmountMax(0D);
				pclv.setOnetimeRechargeAmountMin(0D);
				vo.setPayConnLimitVo(pclv);
			}
			vo.setBankList(mbvList);
			list.add(vo);
		}

		return ResultDTO.success(list, Constants.SUCCESS, "成功");
	}

}
