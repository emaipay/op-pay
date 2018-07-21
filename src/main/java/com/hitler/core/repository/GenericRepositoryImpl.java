package com.hitler.core.repository;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.google.common.collect.Lists;
@SuppressWarnings("all")
public class GenericRepositoryImpl<E extends Persistable<PK>, PK extends Serializable> extends SimpleJpaRepository<E, PK> implements GenericRepository<E, PK> {

	protected EntityManager em;

	public GenericRepositoryImpl(JpaEntityInformation<E, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.em = entityManager;
	}

	public GenericRepositoryImpl(Class<E> domainClass, EntityManager em) {
		this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
	}

	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	@Override
	public E findOne(PK id, LockModeType lockMode) {
		return em.find(getDomainClass(), id, lockMode);
	}

	@Override
	public E findOne(Specification<E> spec, LockModeType lockMode) {
		try {
			return getQuery(spec, (Sort) null).setLockMode(lockMode).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<E> findTop(int top, Specification<E> spec, Sort sort) {
		return findTop(top, spec, sort, LockModeType.NONE);
	}

	@Override
	public List<E> findTop(int top, Specification<E> spec, Sort sort, LockModeType lockMode) {
		return getQuery(spec, sort).setLockMode(lockMode).setMaxResults(top).getResultList();
	}

	@Override
	public <S extends E> S update(S entity) {
		Session session = em.unwrap(Session.class);
		session.update(entity); //使用update方法提高效率
		return entity;
	}

	private <S> S aggregate(CriteriaBuilder builder, CriteriaQuery<S> query, Root<E> root, Specification<E> spec, List<Selection<?>> selectionList, LockModeType lockMode) {
		if (selectionList != null) {
			Predicate predicate = spec.toPredicate(root, query, builder);
			if (predicate != null) {
				query.where(predicate);
			}
			query.multiselect(selectionList);
			return (S) em.createQuery(query).setLockMode(lockMode).getSingleResult();
		}
		return null;
	}

	@Override
	public <S> S aggregate(Class<S> resultClass, Specification<E> spec, AggregateExpression<E> expression, LockModeType lockMode) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<S> query = builder.createQuery(resultClass);
		Root<E> root = query.from(getDomainClass());
		List<Selection<?>> selectionList = expression.buildExpression(root, query, builder);
		return aggregate(builder, query, root, spec, selectionList, lockMode);
	}

	@Override
	public <S> S aggregate(Class<S> resultClass, Specification<E> spec, AggregateExpression<E> expression) {
		return aggregate(resultClass, spec, expression, LockModeType.NONE);
	}


	@Override
	public <S> S sum(Class<S> resultClass, Specification<E> spec, LockModeType lockMode, List<SingularAttribute<E, ? extends Number>> properties) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<S> query = builder.createQuery(resultClass);
		Root<E> root = query.from(getDomainClass());
		List<Selection<?>> selectionList = Lists.newArrayList();
		for (SingularAttribute<E, ? extends Number> property : properties) {
			selectionList.add(builder.sum(root.get(property)));
		}
		return aggregate(builder, query, root, spec, selectionList, lockMode);
	}

	@Override
	public <S> S sum(Class<S> resultClass, Specification<E> spec, List<SingularAttribute<E, ? extends Number>> properties) {
		return sum(resultClass, spec, LockModeType.NONE, properties);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> S sum(Class<S> resultClass, Specification<E> spec, SingularAttribute<E, ? extends Number>... properties) {
		return sum(resultClass, spec, Arrays.asList(properties));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> S sum(Class<S> resultClass, Specification<E> spec, LockModeType lockMode, SingularAttribute<E, ? extends Number>... properties) {
		return sum(resultClass, spec, lockMode, Arrays.asList(properties));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> nativeSqlResultDTO(Class<T> dtoClass, String sql, final Map<String, Object> params, Pageable pageable) {
		Query query = em.createNativeQuery(sql);
		if (null != params && params.size() > 0) {
			for (String key : params.keySet()) {
				query.setParameter(key, params.get(key));
			}
		}
		if(pageable != null) {
			query.setFirstResult(pageable.getOffset());
			query.setMaxResults(pageable.getPageSize());

		}
		query.unwrap(SQLQuery.class).setResultTransformer(new DTOAutoTransformer<>(dtoClass));
		return query.getResultList();
	}

    @Override
    public int updateByHQL(String hql, Map<String, Object> parameters) {
        Query query = em.createQuery(hql);
        if(parameters != null) {
            Iterator<Map.Entry<String, Object>> it = parameters.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query.executeUpdate();
    }

	@Override
	public <T> List<T> nativeSqlResultDTO(Class<T> dtoClass, String sql, final Map<String, Object> params) {
		return nativeSqlResultDTO(dtoClass, sql, params, null);
	}

	public <T> List nativeSqlResult(String sql, final Map<String, Object> params) {
		Query query = em.createNativeQuery(sql);
		if (null != params && params.size() > 0) {
			for (String key : params.keySet()) {
				query.setParameter(key, params.get(key));
			}
		}
		return query.getResultList();
	}

    @Override
    public int updateNativeSql(String sql, Map<String, Object> parameters) {
        Query query = em.createNativeQuery(sql);
        if (parameters != null) {
            Iterator<Map.Entry<String, Object>> it = parameters.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query.executeUpdate();
    }
 
 

}
