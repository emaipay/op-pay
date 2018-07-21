package com.hitler.service.support;

import java.io.Serializable;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.hitler.core.dto.support.IGenericDTO;
import com.hitler.core.dto.support.IGenericTable;
import com.hitler.core.repository.AggregateExpression;


public interface IGenericService<E extends Persistable<PK>, PK extends Serializable> {
	
	<S extends E> S save(S entity) throws Exception;
	int save(Iterable<? extends E> entities) throws Exception;
	
	<S extends E> S update(S entity) throws Exception;

	void delete(PK id) throws Exception;
	void delete(E entity) throws Exception;
	void delete(Iterable<? extends E> entities) throws Exception;
	void deleteAll() throws Exception;
	boolean exists(PK id);
	long count();
	long count(Specification<E> spec);
	E find(PK id);
	E find(Specification<E> spec);
	List<E> findAll();
	List<E> findAll(Sort sort);
	List<E> findAll(Iterable<PK> ids);
	List<E> findAll(Specification<E> spec);
	Page<E> findAll(Specification<E> spec, Pageable pageable);
	Page<E> findAll(Specification<E> spec,Sort sort, Pageable pageable);
	List<E> findAll(Specification<E> spec, Sort sort);
	
	@SuppressWarnings("unchecked")
	<S> S sum(Class<S> resultClass, Specification<E> spec, SingularAttribute<E, ? extends Number>... properties);
	<S> S sum(Class<S> resultClass, Specification<E> spec, List<SingularAttribute<E, ? extends Number>> properties); 
	<S> S aggregate(Class<S> resultClass, Specification<E> spec, AggregateExpression<E> expression);
	
	<DTO extends IGenericDTO<PK>> void save(IGenericDTO<PK> dto) throws Exception;
	<DTO extends IGenericDTO<PK>> void update(IGenericDTO<PK> dto) throws Exception;
	/**
	 *  存在表关联更新方法
	 * 调用更新方法前，已取消级联关系，不用查询映射，不用经过 update(IGenericDTO<PK> dto) 方法处理
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	<DTO extends IGenericDTO<PK>> void  updateByRelevance(IGenericDTO<PK> dto) throws Exception;
 
	
	<DTO extends IGenericDTO<PK>> DTO find(Class<DTO> dtoClass, PK id) throws Exception;
	<DTO extends IGenericDTO<PK>> DTO find(Class<DTO> dtoClass, Specification<E> spec) throws Exception;
	<DTO extends IGenericDTO<PK>> List<DTO> findAll(Class<DTO> dtoClass) throws Exception;
	<DTO extends IGenericDTO<PK>> List<DTO> findAll(Class<DTO> dtoClass, Sort sort) throws Exception;
	<DTO extends IGenericDTO<PK>> List<DTO> findAll(Class<DTO> dtoClass, Iterable<PK> ids) throws Exception;
	<DTO extends IGenericDTO<PK>> List<DTO> findAll(Class<DTO> dtoClass, Specification<E> spec) throws Exception;
	<DTO extends IGenericDTO<PK>> Page<DTO> findAll(Class<DTO> dtoClass, Specification<E> spec, Pageable pageable) throws Exception;
	<DTO extends IGenericDTO<PK>> List<DTO> findAll(Class<DTO> dtoClass, Specification<E> spec, Sort sort) throws Exception;
	
	<TAB extends IGenericTable<DTO, E>, DTO extends IGenericDTO<PK>> TAB findTable(Class<TAB> tableClass, Class<DTO> dtoClass, Specification<E> spec, Pageable pageable) throws Exception;
	public boolean updateColumn(E entity, String... columns) throws Exception ;
}

