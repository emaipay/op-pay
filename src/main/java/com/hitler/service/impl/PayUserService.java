package com.hitler.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.hitler.core.exception.BusinessException;
import com.hitler.core.repository.GenericRepository;
import com.hitler.core.repository.SearchFilter;
import com.hitler.core.utils.BeanMapper;
import com.hitler.core.utils.StringUtils;
import com.hitler.dto.user.PayUserCreateDTO;
import com.hitler.dto.user.PayUserDTO;
import com.hitler.entity.PayRole;
import com.hitler.entity.PayRoleUser;
import com.hitler.entity.PayUser;
import com.hitler.entity.PayUser.AccountStatus;
import com.hitler.gen.PayUser_;
import com.hitler.repository.IPayUserRepository;
import com.hitler.service.IPayRoleUserService;
import com.hitler.service.IPayUserService;
import com.hitler.service.support.GenericService;

/**
 * @since 2017-03-15
 */
@Service
public class PayUserService extends GenericService<PayUser, Integer> implements IPayUserService {
	@Resource
	private IPayUserRepository repository;
	@Resource
	private IPayRoleUserService roleUserService;

	public PayUserService() {
		super(PayUser.class);
	}

	@Override
	protected GenericRepository<PayUser, Integer> getRepository() {
		return repository;
	}

	@Override
	public List<PayUserDTO> find(List<SearchFilter> list, Pageable pageable) {
		String sql = " SELECT u.ID as id,u.USER_NAME as userName ,u.NICK_NAME as nickName,"
				+ " u.ACCOUNT_TYPE as accountType,u.ONLINE_STATUS as onlineStatus,u.ACCOUNT_LOCKED as accountLocked,"
				+ " u.LOGIN_LOCKED as loginLocked," + " u.CREATED_DATE as createdDate,u.LOGIN_TIME as loginTime,"
				+ " u.LOGIN_ADDR as loginAddr " + " FROM T_PAY_USER u " + " WHERE  u.ACCOUNT_TYPE !=:accountType";
		Map<String, Object> params = Maps.newHashMap();
		if (list != null && list.size() > 0) {
			for (SearchFilter sf : list) {
				if (StringUtils.isNotBlank(sf.getValue().toString())) {
					params.put(sf.getFieldName(), sf.getValue());
				}
			}
		}
		return repository.nativeSqlResultDTO(PayUserDTO.class, sql, params, pageable);
	}

	@Override
	public PayUser findByUserName(String userName) {
		return repository.findByUserName(userName);
	}

	@Override
	public Boolean isExistUser(String tenantCode, String userName) {
		return findByUserName(userName) == null;
	}

	/**
	 * 获取给定用户ID的用户的角色集合
	 */
	@Override
	public Collection<PayRole> getRolesByUserId(Integer userId) {
		List<PayRoleUser> roleUsers = roleUserService.findByUserId(userId);
		Collection<PayRole> roles = new HashSet<PayRole>();
		for (PayRoleUser roleUser : roleUsers) {
			roles.add(roleUser.getRoleId());
		}
		return roles;
	}

	@Override
	public void accountLocked(Integer id, PayUser.AccountStatus accountStatus) throws Exception {
		PayUser user = this.find(id);
		user.setAccountLocked(accountStatus);
		// this.updateColumn(user, "accountLocked");
	}

	@Override
	public void batchAccountLocked(List<Integer> ids, PayUser.AccountStatus accountStatus) {
		repository.batchAccount(ids, accountStatus);
	}

	/**
	 * 创建管理员账户
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void creatAdmin(PayUserCreateDTO createDTO) {
		PayUser user = BeanMapper.map(createDTO, PayUser.class);
		user.setPwdSalt(createDTO.getSalt());
		repository.save(user);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void onLoginSuccess(PayUser user, String loginIp) throws Exception {
		// 更改账户状态
		user.setOnlineStatus(PayUser.OnlineStatus.在线);
		// 上一次的登录时间、地址 为 本次登录的 最近登录时间，地址
		user.setLastLoginAddr(user.getLoginAddr());
		user.setLastLoginTime(user.getLoginTime());
		user.setLoginTime(new Date());
		user.setLoginAddr(loginIp);

		this.updateColumn(user, PayUser_.onlineStatus.getName(), PayUser_.lastLoginAddr.getName(),
				PayUser_.lastLoginTime.getName(), PayUser_.loginTime.getName(), PayUser_.loginAddr.getName());
	}

	@Override
	public void batchAccountDelete(List<Integer> ids, AccountStatus accountStatus) throws Exception {
		repository.batchAccount(ids, accountStatus);
	}

	@Override

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void updateUser(Integer ids, Object value, String cloumName) throws Exception {
		// 这个cloumName在controller传入，不在页面传
		String hql = "update PayUser set " + cloumName + "= :value where id=:ids ";
		Map<String, Object> parms = Maps.newHashMap();
		parms.put("value", value);
		parms.put("ids", ids);
		repository.updateByHQL(hql, parms);
	}

	@Override
	public List<PayUser> findByIdIn(Collection<Integer> ids) {
		// TODO Auto-generated method stub
		return repository.findByIdIn(ids);
	}

	@Override
	public List<PayUser> queryNormalUser() {
		// TODO Auto-generated method stub
		return repository.queryNormalUser();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updatePwLogin(Integer usId, String oldPwdLogin, String pwdLogin) throws Exception {
		PayUser user = repository.getOne(usId);
		if (user.getPwdLogin().equals(oldPwdLogin)) {
			user.setPwdLogin(pwdLogin);
		} else {
			throw new BusinessException("旧密码错误!");
		}
		return repository.updatePwdLogin(usId, pwdLogin);
	}
}
