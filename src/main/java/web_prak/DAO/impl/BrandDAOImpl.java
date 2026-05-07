package web_prak.DAO.impl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import web_prak.DAO.BrandDAO;
import web_prak.models.*;

import java.util.List;

@Repository
public class BrandDAOImpl extends CommonDAOImpl<Brand, Long> implements BrandDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public BrandDAOImpl() {
        super(Brand.class);
    }

    @Transactional
    public List<Brand> findBrandsByName(String name) {
            Query<Brand> query = (Query<Brand>) entityManager.createQuery("FROM Brand WHERE name LIKE :name", Brand.class).setParameter("name", "%" + name + "%");
            List<Brand> results = query.getResultList();
            return results.isEmpty() ? null : results;

    }
}