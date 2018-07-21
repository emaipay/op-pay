package com.hitler.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.hitler.core.repository.SearchFilter;
import com.hitler.dto.user.PayUserCreateDTO;
import com.hitler.dto.user.PayUserDTO;
import com.hitler.entity.PayRole;
import com.hitler.entity.PayUser;
import com.hitler.service.support.IGenericService;

/**
 * 会员服务Service层
 */
public interface IPayUserService extends IGenericService<PayUser, Integer> {

    /**
     * 会员列表查询
     * @param list
     * @param pageable
     * @return
     */
    List<PayUserDTO> find(List<SearchFilter> list, Pageable pageable);
    /**
     * 获取给定用户名的用户的所有角色
     */

    public Collection<PayRole> getRolesByUserId(Integer userId);


    /**
     * @param tenantCode
     * @param userName
     * @return
     */
    public PayUser findByUserName(String userName);

    /**
     * @param tenantCode
     * @param userName
     * @return
     */

    public Boolean isExistUser(String tenantCode, String userName);


    /**
     * 账户锁定/解锁
     * @param id
     * @param accountStatus
     * @throws Exception
     */
    public void accountLocked(Integer id, PayUser.AccountStatus accountStatus) throws Exception;

    /**
     * 批量账户锁定/解锁
     * @param ids
     * @param accountStatus
     */
    public void batchAccountLocked(List<Integer> ids, PayUser.AccountStatus accountStatus);

    public void creatAdmin(PayUserCreateDTO createDTO);
    
    public void onLoginSuccess(PayUser user, String loginIp) throws Exception;
    
    /**
     * 批量删除用户
     *
     * @param id
     * @param layerLocked
     * @param userTenantCode
     * @throws Exception
     */
    void batchAccountDelete(List<Integer> ids, PayUser.AccountStatus accountStatus) throws Exception;
    
    public void updateUser(Integer ids, Object value, String cloumName)
			throws Exception ;
    
    List<PayUser> findByIdIn(Collection<Integer> ids);
    
    public List<PayUser> queryNormalUser();
    
    public int updatePwLogin(Integer usId, String oldPwdLogin, String pwdLogin) throws Exception;
}