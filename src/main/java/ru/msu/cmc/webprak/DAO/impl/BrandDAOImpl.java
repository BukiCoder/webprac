package ru.msu.cmc.webprak.DAO.impl;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprak.DAO.BrandDAO;
import ru.msu.cmc.webprak.models.*;

import java.util.List;

@Repository
public class BrandDAOImpl extends CommonDAOImpl<Brand, Long> implements BrandDAO {

    public BrandDAOImpl(LocalSessionFactoryBean sessionFactory) {
        super(Brand.class);
    }
    public List<Brand> findBrandsByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Brand> query = session.createQuery("FROM Brand WHERE name LIKE :name", Brand.class)
                    .setParameter("name", "%" + name + "%");
            List<Brand> results = query.getResultList();
            return results.isEmpty() ? null : results;
        }
    }
}