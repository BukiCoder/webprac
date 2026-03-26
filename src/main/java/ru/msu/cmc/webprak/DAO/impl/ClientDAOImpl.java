package ru.msu.cmc.webprak.DAO.impl;

import org.hibernate.Session;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprak.DAO.ClientDAO;
import ru.msu.cmc.webprak.models.*;
import ru.msu.cmc.webprak.filters.ClientFilter;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class ClientDAOImpl extends CommonDAOImpl<Client, Long> implements ClientDAO {

    public ClientDAOImpl(LocalSessionFactoryBean sessionFactory) {
        super(Client.class);
    }

    @Override
    public Client getClientByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<Client> query = session.createQuery("FROM Client WHERE email = :email", Client.class)
                    .setParameter("email", email);
            return query.getResultList().isEmpty() ? null : query.getSingleResult();
        }
    }

    @Override
    public Client getClientByPhone(String phone) {
        try (Session session = sessionFactory.openSession()) {
            Query<Client> query = session.createQuery("FROM Client WHERE phone = :phone", Client.class)
                    .setParameter("phone", phone);
            return query.getResultList().isEmpty() ? null : query.getSingleResult();
        }
    }
    @Override
    public List<Client> serchClients(ClientFilter client) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Client> criteriaQuery = builder.createQuery(Client.class);
            Root<Client> root = criteriaQuery.from(Client.class);

            List<Predicate> predicates = new ArrayList<>();
            var clientMap = client.getClassMap();
            for (String key : clientMap.keySet()) {
                Object val1 = clientMap.get(key);
                if (val1 != null)
                    if(key.equals("email") || key.equals("phone") || key.equals("name"))
                      predicates.add(builder.like(root.get(key), "%"+val1.toString()+"%"));
                    else
                        predicates.add(builder.equal(root.get(key), val1));
            }
            if (!predicates.isEmpty())
            {
                criteriaQuery.where(predicates.toArray(new Predicate[0]));
            }
            return session.createQuery(criteriaQuery).getResultList();
        }
    }
    }
