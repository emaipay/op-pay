package com.hitler.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.hitler.core.repository.GenericRepository;
import com.hitler.core.utils.BeanMapper;
import com.hitler.dto.PayTenantCreateDTO;
import com.hitler.dto.PayTenantUpdateDTO;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayPlatformBank;
import com.hitler.entity.PayTenant;
import com.hitler.entity.PayTenantMerchant;
import com.hitler.entity.PayTenantMerchantBank;
import com.hitler.entity.PayUser;
import com.hitler.repository.IPayTenantRepository;
import com.hitler.service.IPayTenantMerchantBankService;
import com.hitler.service.IPayTenantMerchantService;
import com.hitler.service.IPayTenantService;
import com.hitler.service.IPayUserService;
import com.hitler.service.support.GenericService;

/**
 * 接入商户实现
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 下午2:32:34
 */
@Service
public class PayTenantService extends GenericService<PayTenant, Integer> implements IPayTenantService {

	@Resource
	private IPayTenantRepository repository;
	
	@Resource
	private IPayUserService payUserService;
	
	@Resource
	private IPayTenantMerchantService payTenantMerchantService;
	@Resource
	private IPayTenantMerchantBankService payTenantMerchantBankService; 

	public PayTenantService() {
		super(PayTenant.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected GenericRepository<PayTenant, Integer> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deleteByIdList(List<Integer> idList) {
		// TODO Auto-generated method stub
		repository.deleteByIdList(idList);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void updateStatus(Integer ids, Object value, String cloumName)
			throws Exception {
		// 这个cloumName在controller传入，不在页面传
		String hql = "update PayTenant set " + cloumName + "= :value where id=:ids ";
		Map<String,Object> parms = Maps.newHashMap();
		parms.put("value", value);
		parms.put("ids", ids);
		repository.updateByHQL(hql, parms);
	}
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void tenantSave(PayTenantCreateDTO dto) throws Exception{
		PayTenant ten = BeanMapper.map(dto, entityClass);
		PayUser user=payUserService.findByUserName(dto.getUserName());
		ten.setUserId(user);
		ten.setUserName(user.getUserName());
		repository.save(ten);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void tenantUpdate(PayTenantUpdateDTO dto) throws Exception{
		PayTenant pt=repository.findOne(dto.getId());
		PayUser user=payUserService.findByUserName(dto.getUserName());
		pt.setMemberDomain(dto.getMemberDomain());
		pt.setMemberId(pt.getMemberId());
		pt.setPlatformName(dto.getPlatformName());
		pt.setTerminalId(dto.getTerminalId());
		pt.setUserId(user);
		pt.setUserName(user.getUserName());
		repository.update(pt);
	}
	@Override
	public Collection<PayMerchant> getPayMerchantsByTenantId(
			Integer tenantId) {
		List<PayTenantMerchant> ulpm = payTenantMerchantService.findByTenantId(tenantId);
		Collection<PayMerchant> pmList = new ArrayList<PayMerchant>();
		for (PayTenantMerchant merchant : ulpm) {
			pmList.add(merchant.getPayMerchantId());
		}
		return pmList;
	}

	@Override
	public Collection<PayPlatformBank> findByTenantId(Integer tenantId) {
		List<PayTenantMerchantBank> ulpmbList = payTenantMerchantBankService.findByTenantId(tenantId);
		Collection<PayPlatformBank> pbList = new ArrayList<PayPlatformBank>();
		for(PayTenantMerchantBank bank : ulpmbList) {
			pbList.add(bank.getPayBankId());
		}
		return pbList;
	}

	@Override
	public List<PayTenant> queryTenantByUserId(Integer userId) {
		return repository.queryTenantByUserId(userId);
	}

	@Override
	public PayTenant queryPayTenant(String memberId, String terminalId) {
		return repository.queryPayTenant(memberId, terminalId);
	}
	

}
