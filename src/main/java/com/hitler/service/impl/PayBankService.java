package com.hitler.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hitler.core.repository.GenericRepository;
import com.hitler.core.utils.CollectionHelper;
import com.hitler.entity.PayBank;
import com.hitler.entity.PayBank.BankTransferStatus;
import com.hitler.entity.PayPlatformBank;
import com.hitler.repository.IPayBankRepository;
import com.hitler.service.IPayBankService;
import com.hitler.service.IPayPlatformBankService;
import com.hitler.service.IPayTenantMerchantBankService;
import com.hitler.service.support.GenericService;

/**
 * 支付平台可支付方式(银行卡)
 * 
 * @author klp
 *
 */
@Service
public class PayBankService extends GenericService<PayBank, Integer> implements IPayBankService {

	@Resource
	private IPayBankRepository repository;
	@Resource
	private IPayTenantMerchantBankService payTenantMerchantBankService;
	@Resource
	private IPayPlatformBankService payPlatformBankService;
	
	@Resource
	private IPayBankService payBankService;

	public PayBankService() {
		super(PayBank.class);
	}

	@Override
	protected GenericRepository<PayBank, Integer> getRepository() {
		return repository;
	}

	@Override
	public List<PayBank> queryPayBank(Integer merchantId, Integer connId) {
		List<Integer> pcmList = payTenantMerchantBankService.queryPayBankIdList(connId,merchantId);
		List<PayBank> pbList = new ArrayList<PayBank>();
		for (Integer id : pcmList) {
			PayPlatformBank pbf=payPlatformBankService.find(id);
			PayBank pb=payBankService.find(pbf.getBankId().getId());
			if(pb.getBankTransferStatus().equals(BankTransferStatus.开放)){
				pbList.add(pb);
			}
		}
		return pbList;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deleteByIdList(List<Integer> idList) {
		// TODO Auto-generated method stub
		repository.deleteByIdList(idList);
	}

	@Override
	public List<PayBank> queryPlatformBank(Integer payPlatformId) {
		List<PayPlatformBank> list=payPlatformBankService.queryByPlatformId(payPlatformId);
		List<PayBank> bankList=new ArrayList<PayBank>();
		if(CollectionHelper.isNotEmpty(list)){
			for(PayPlatformBank ppf:list){
				PayBank pb=repository.findOne(ppf.getBankId().getId());
				bankList.add(pb);
			}
			return bankList;
		}
		return null; 
	}

	@Override
	public List<PayBank> queryPayBankByIdList(List<Integer> ids) {
		return repository.queryPayBankByIdList(ids);
	}

	 

}
