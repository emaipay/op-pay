package com.hitler.service.support;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.metamodel.SingularAttribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hitler.core.dto.support.IGenericDTO;
import com.hitler.core.dto.support.IGenericTable;
import com.hitler.core.enums.PersistEnum;
import com.hitler.core.exception.EntityNotExistsException;
import com.hitler.core.repository.AggregateExpression;
import com.hitler.core.repository.GenericRepository;
import com.hitler.core.utils.BeanMapper;

public abstract class GenericService<E extends Persistable<PK>, PK extends Serializable>
		implements IGenericService<E, PK> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	@PersistenceContext
	protected EntityManager em;

	protected final Class<E> entityClass;

	public GenericService(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	protected abstract GenericRepository<E, PK> getRepository();

	@Override
	@Transactional(rollbackFor = Exception.class)
	public <S extends E> S save(S entity) throws Exception {
		return getRepository().save(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int save(Iterable<? extends E> entities) throws Exception {
		return getRepository().save(entities).size();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public <S extends E> S update(S entity) throws Exception {
		return getRepository().update(entity);
	}
 

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public E find(PK id) {
		return getRepository().findOne(id);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public E find(Specification<E> spec) {
		return getRepository().findOne(spec);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public boolean exists(PK id) {
		return getRepository().exists(id);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<E> findAll() {
		return getRepository().findAll();
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<E> findAll(Sort sort) {
		return getRepository().findAll(sort);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<E> findAll(Iterable<PK> ids) {
		return getRepository().findAll(ids);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<E> findAll(Specification<E> spec) {
		return getRepository().findAll(spec);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Page<E> findAll(Specification<E> spec, Pageable pageable) {
		return getRepository().findAll(spec, pageable);
	}
	
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Page<E> findAll(Specification<E> spec,Sort sort, Pageable pageable) {
		return getRepository().findAll(spec, pageable);
	}
	

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<E> findAll(Specification<E> spec, Sort sort) {
		return getRepository().findAll(spec, sort);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public long count() {
		return getRepository().count();
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public long count(Specification<E> spec) {
		return getRepository().count(spec);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(PK id) throws Exception {
		getRepository().delete(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(E entity) throws Exception {
		getRepository().delete(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Iterable<? extends E> entities) throws Exception {
		getRepository().delete(entities);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteAll() throws Exception {
		getRepository().deleteAll();
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public <S> S sum(Class<S> resultClass, Specification<E> spec,
			List<SingularAttribute<E, ? extends Number>> properties) {
		return getRepository().sum(resultClass, spec, properties);
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public <S> S sum(Class<S> resultClass, Specification<E> spec,
			SingularAttribute<E, ? extends Number>... properties) {
		return getRepository().sum(resultClass, spec, properties);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public <S> S aggregate(Class<S> resultClass, Specification<E> spec, AggregateExpression<E> expression) {
		return getRepository().aggregate(resultClass, spec, expression);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public <DTO extends IGenericDTO<PK>> void save(IGenericDTO<PK> dto) throws Exception {
		E entity = null;
		if (dto.getId() != null) { // 修改
			entity = find(dto.getId());
			if (entity == null)
				throw EntityNotExistsException.ERROR;
			BeanMapper.map(dto, entity);
		} else { // 新增
			entity = BeanMapper.map(dto, entityClass);
		}
		save(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public <DTO extends IGenericDTO<PK>> void update(IGenericDTO<PK> dto) throws Exception {
		E entity = null;
		if (dto.getId() != null) {
			entity = find(dto.getId());
			if (entity == null)
				throw EntityNotExistsException.ERROR;
			BeanMapper.map(dto, entity);
		} else {
			entity = BeanMapper.map(dto, entityClass);
		}
		update(entity);
	}

	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public <DTO extends IGenericDTO<PK>> void updateByRelevance(IGenericDTO<PK> dto) throws Exception {
          update( BeanMapper.map(dto, entityClass));
	}
	
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public <DTO extends IGenericDTO<PK>> DTO find(Class<DTO> dtoClass, PK id) throws Exception {
		return BeanMapper.map(find(id), dtoClass);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public <DTO extends IGenericDTO<PK>> DTO find(Class<DTO> dtoClass, Specification<E> spec) throws Exception {
		return BeanMapper.map(find(spec), dtoClass);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public <DTO extends IGenericDTO<PK>> List<DTO> findAll(Class<DTO> dtoClass) throws Exception {
		return BeanMapper.map(findAll(), dtoClass);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public <DTO extends IGenericDTO<PK>> List<DTO> findAll(Class<DTO> dtoClass, Sort sort) throws Exception {
		return BeanMapper.map(findAll(sort), dtoClass);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public <DTO extends IGenericDTO<PK>> List<DTO> findAll(Class<DTO> dtoClass, Iterable<PK> ids) throws Exception {
		return BeanMapper.map(findAll(ids), dtoClass);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public <DTO extends IGenericDTO<PK>> List<DTO> findAll(Class<DTO> dtoClass, Specification<E> spec)
			throws Exception {
		return BeanMapper.map(findAll(spec), dtoClass);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public <DTO extends IGenericDTO<PK>> Page<DTO> findAll(Class<DTO> dtoClass, Specification<E> spec,
			Pageable pageable) throws Exception {
		Page<E> sp = findAll(spec, pageable);
		Page<DTO> dp = new PageImpl<DTO>(BeanMapper.map(sp.getContent(), dtoClass), pageable, sp.getTotalElements());
		return dp;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public <DTO extends IGenericDTO<PK>> List<DTO> findAll(Class<DTO> dtoClass, Specification<E> spec, Sort sort)
			throws Exception {
		return BeanMapper.map(findAll(spec, sort), dtoClass);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public <TAB extends IGenericTable<DTO, E>, DTO extends IGenericDTO<PK>> TAB findTable(Class<TAB> tableClass,
			Class<DTO> dtoClass, Specification<E> spec, Pageable pageable) throws Exception {
		TAB tinst = tableClass.newInstance();
		TAB t = null;
		Page<E> sp = findAll(spec, pageable);
		if (sp.getTotalElements() > 0) {
			t = aggregate(tableClass, spec, tinst);
		}
		if (t == null) {
			t = tinst;
		}
		t.setRows(BeanMapper.map(sp.getContent(), dtoClass));
		t.setTotal(sp.getTotalElements());
		t.setRecordsFiltered(sp.getTotalElements());
		return t;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
	/**
     * 更新对象， 只更新指定的属性。根据id主键更新
     * columns: 你要更新的属性, 名称要和entity一致。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateColumn(E entity, String... columns) throws Exception {
        Class<?> clazz = entity.getClass();
        String tableName = entityClass.getAnnotation(Table.class).name();
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer("update " + tableName + " set ");
        boolean isFirse = true;

        for (String column : columns) {
            Field field = clazz.getDeclaredField(column);
            String columnName = field.getAnnotation(Column.class).name();

            if (isFirse) {
                sql.append(columnName).append(" =:").append(column);
                isFirse = false;
            } else {
                sql.append(", ").append(columnName).append(" =:").append(column);
            }

            field.setAccessible(true);
            Object value = field.get(entity);

            if (value == null) {

            } else if (value instanceof PersistEnum) {
                value = ((PersistEnum<?>) value).getValue();
            } else if (value instanceof Date) {
                Date date = (Date) value;
                value = new java.sql.Timestamp(date.getTime());
            }

            paramsMap.put(column, value);
        }

        sql.append(" where id = ").append(entity.getId());

        int i = getRepository().updateNativeSql(sql.toString(), paramsMap);

        return i > 0;
    }

}
