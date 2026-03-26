package ru.msu.cmc.webprak.DAO.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprak.DAO.CommonDAO;
import ru.msu.cmc.webprak.filters.CommonFilter;
import ru.msu.cmc.webprak.models.CommonEntity;

import java.util.*;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;


@Repository
public abstract class CommonDAOImpl<T extends CommonEntity<ID>, ID extends Serializable> implements CommonDAO<T, ID> {

    protected SessionFactory sessionFactory;

    protected Class<T> persistentClass;

    public CommonDAOImpl(Class<T> entityClass){
        this.persistentClass = entityClass;
    }

    @Override
    public List<T> searchByFilter(CommonFilter minFilter, CommonFilter maxFilter) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(persistentClass);
            Root<T> root = query.from(persistentClass);

            List<Predicate> predicates = new ArrayList<>();

            Map<String, Object> minMap = minFilter != null ? minFilter.getClassMap() : Map.of();
            Map<String, Object> maxMap = maxFilter != null ? maxFilter.getClassMap() : Map.of();

            for (String field : unionKeys(minMap.keySet(), maxMap.keySet())) {
                Object minVal = minMap.get(field);
                Object maxVal = maxMap.get(field);

                if (minVal == null && maxVal == null) continue;

                if (minVal != null && maxVal != null && minVal.equals(maxVal)) {
                    predicates.add(builder.equal(root.get(field), maxVal));
                } else {
                    if (minVal != null) {
                        predicates.add(addRangePredicate(builder, root, field, minVal, true));
                    }
                    if (maxVal != null) {
                        predicates.add(addRangePredicate(builder, root, field, maxVal, false));
                    }
                }
            }

            if (!predicates.isEmpty()) {
                query.where(predicates.toArray(new Predicate[0]));
            }

            return session.createQuery(query).getResultList();
        }
    }

    private Set<String> unionKeys(Set<String> set1, Set<String> set2) {
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        return union;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Predicate addRangePredicate(CriteriaBuilder builder, Root<T> root, String field, Object value, boolean isMin) {
        Path path = root.get(field);
        if (value instanceof Number) {
            if (isMin) {
                return builder.ge(path, (Number) value);
            } else {
                return builder.le(path, (Number) value);
            }
        } else if (value instanceof Comparable) {
            if (isMin) {
                return builder.greaterThanOrEqualTo(path, (Comparable) value);
            } else {
                return builder.lessThanOrEqualTo(path, (Comparable) value);
            }
        } else {
            throw new IllegalArgumentException("Unsupported type for range: " + value.getClass());
        }
    }


    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public T getById(ID id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(persistentClass, id);
        }
    }

    @Override
    public Collection<T> getAll() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaQuery<T> criteriaQuery = session.getCriteriaBuilder().createQuery(persistentClass);
            criteriaQuery.from(persistentClass);
            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    @Override
    public void save(T entity) {
        try (Session session = sessionFactory.openSession()) {
            if (entity.getId() != null) {
                entity.setId(null);
            }
            session.beginTransaction();
            session.saveOrUpdate(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void saveCollection(Collection<T> entities) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (T entity : entities) {
                this.save(entity);
            }
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteById(ID id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            T entity = getById(id);
            session.delete(entity);
            session.getTransaction().commit();
        }
    }
}
