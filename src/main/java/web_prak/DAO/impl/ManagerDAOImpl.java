package web_prak.DAO.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import web_prak.DAO.ManagerDAO;
import web_prak.models.*;

@Repository
public class ManagerDAOImpl extends CommonDAOImpl<Manager, Long> implements ManagerDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Manager getManagerByEmail(String email) {

        Query<Manager> query = (Query<Manager>) entityManager.createQuery("FROM Manager WHERE email = :email", Manager.class)
                .setParameter("email", email);
        return query.getResultList().isEmpty() ? null : query.getSingleResult();

    }

    @Override
    @Transactional
    public Manager getManagerByPhone(String phone) {

        Query<Manager> query = (Query<Manager>) entityManager.createQuery("FROM Manager WHERE phone = :phone", Manager.class)
                .setParameter("phone", phone);
        return query.getResultList().isEmpty() ? null : query.getSingleResult();

    }

    public ManagerDAOImpl() {
        super(Manager.class);
    }
}