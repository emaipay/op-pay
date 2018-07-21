package com.hitler.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayOrder;
import com.hitler.repository.IPayMerchantRepository;
import com.hitler.repository.IPayOrderRepository;
import com.hitler.service.IPayOrderService;
import com.hitler.service.support.GenericService;

/**
 * 在线充值记录 实现
 * @author klp
 *
 */
@Service
public class PayOrderService extends GenericService<PayOrder, Integer> implements IPayOrderService {
	
	@Resource
	private IPayOrderRepository repository;
	
	@Resource
	private IPayMerchantRepository merchantRepository;

	public PayOrderService() {
		super(PayOrder.class);
	}

	@Override
	protected GenericRepository<PayOrder, Integer> getRepository() {
		return repository;
	}

	@Override
	public PayOrder queryOrderByTransBillno(String billno) {
		return repository.queryOrderByTransBillno(billno);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean updateOrderStatus(PayOrder order) {
		int i=repository.updateOrderStatus(order.getId(), order.getOrderStatus().getValue(), order.getOrderStatusDesc());
		PayMerchant pm=order.getMerchantId();
		Double fee=order.getFactAmount()*(pm.getFeePercent()/100);
		int j=merchantRepository.updatecurrentBalance(order.getFactAmount()-fee, pm.getId());
		return i>0&&j>0;
	}

	@Override
	public PayOrder queryOrderByConnBillno(String billno) {
		return repository.queryOrderByConnBillno(billno);
	}
}
