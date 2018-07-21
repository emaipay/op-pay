package com.hitler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayBank;

public interface IPayBankRepository extends GenericRepository<PayBank, Integer> {


	   @Modifying
	    @Query("DELETE FROM PayBank  WHERE id in :ids")
	    public void deleteByIdList(@Param("ids") List<Integer> ids);
	   
	   /**
		 * 查询支付配置
		 * @param type
		 * @param methodType
		 * @return
		 */
		@Query("from PayBank WHERE id in :ids")
		public List<PayBank> queryPayBankByIdList(@Param("ids") List<Integer> ids);


	   
}
