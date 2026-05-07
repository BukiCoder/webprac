package web_prak.DAO.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import web_prak.DAO.CommonDAO;
import web_prak.filters.CommonFilter;
import web_prak.models.CommonEntity;

import jakarta.persistence.criteria.*;
import java.io.Serializable;
import java.util.*;


@Repository
public abstract class CommonDAOImpl<T extends CommonEntity<ID>, ID extends Serializable> implements CommonDAO<T, ID> {

    @PersistenceContext
    private EntityManager entityManager;
    protected Class<T> persistentClass;

    public CommonDAOImpl(Class<T> entityClass){
        this.persistentClass = entityClass;
    }

    @Override
    @Transactional
    public List<T> searchByFilter(CommonFilter minFilter, CommonFilter maxFilter) {

            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(persistentClass);
            Root<T> root = query.from(persistentClass);

            List<Predicate> predicates = new ArrayList<>();

            Map<String, Object> minMap = minFilter != null ? minFilter.getClassMap() : Map.of();
            Map<String, Object> maxMap = maxFilter != null ? maxFilter.getClassMap() : Map.of();

            for (String field : unionKeys(minMap.keySet(), maxMap.keySet())) {
                Object minVal = minMap.get(field);
                Object maxVal = maxMap.get(field);

                if (minVal == null && maxVal == null) continue;

                if (minVal != null && maxVal != null && minVal != "" && maxVal != "" && minVal.equals(maxVal)) {
                    System.out.println(field);
                    predicates.add(builder.equal(root.get(field), maxVal));
                } else {
                    if (minVal != null && minVal != "" ) {
                        predicates.add(addRangePredicate(builder, root, field, minVal, true));
                    }
                    if (maxVal != null && maxVal != "") {
                        predicates.add(addRangePredicate(builder, root, field, maxVal, false));
                    }
                }
            }

            if (!predicates.isEmpty()) {
                query.where(predicates.toArray(new Predicate[0]));
            }

            return entityManager.createQuery(query).getResultList();

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


    @Override
    @Transactional
    public T getById(ID id) {

            return entityManager.find(persistentClass, id);

    }

    @Override
    @Transactional
    public Collection<T> getAll() {

            CriteriaQuery<T> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(persistentClass);
            criteriaQuery.from(persistentClass);
            return entityManager.createQuery(criteriaQuery).getResultList();

    }

    @Override
    @Transactional
    public void save(T entity) {

        if (entity.getId() == null) {
            entityManager.persist(entity);

        } else {
            entityManager.merge(entity);
        }

    }

    @Override
    @Transactional
    public void saveCollection(Collection<T> entities) {


            for (T entity : entities) {
                this.save(entity);
            }


    }

    @Override
    @Transactional
    public void update(T entity) {


        entityManager.merge(entity);


    }

    @Override
    @Transactional
    public void delete(T entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    @Override
    @Transactional
    public void deleteById(ID id) {


            T entity = getById(id);
        entityManager.remove(entity);


    }
}
