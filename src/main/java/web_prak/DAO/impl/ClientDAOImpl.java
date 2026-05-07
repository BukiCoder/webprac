package web_prak.DAO.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import web_prak.DAO.ClientDAO;
import web_prak.filters.ClientFilter;
import web_prak.models.*;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientDAOImpl extends CommonDAOImpl<Client, Long> implements ClientDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public ClientDAOImpl() {
        super(Client.class);
    }

    @Override
    @Transactional
    public Client getClientByEmail(String email) {

            Query<Client> query = (Query<Client>) entityManager.createQuery("FROM Client WHERE email = :email", Client.class)
                    .setParameter("email", email);
            return query.getResultList().isEmpty() ? null : query.getSingleResult();

    }

    @Override
    @Transactional
    public Client getClientByPhone(String phone) {

            Query<Client> query = (Query<Client>) entityManager.createQuery("FROM Client WHERE phone = :phone", Client.class)
                    .setParameter("phone", phone);
            return query.getResultList().isEmpty() ? null : query.getSingleResult();

    }
    @Override
    @Transactional
    public List<Client> serchClients(ClientFilter client) {

            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Client> criteriaQuery = builder.createQuery(Client.class);
            Root<Client> root = criteriaQuery.from(Client.class);

            List<Predicate> predicates = new ArrayList<>();
            var clientMap = client.getClassMap();
            for (String key : clientMap.keySet()) {
                Object val1 = clientMap.get(key);
                if (val1 != null && val1 != "")
                    if(key.equals("email") || key.equals("phone") || key.equals("name"))
                      predicates.add(builder.like(root.get(key), "%"+val1.toString()+"%"));
                    else
                        predicates.add(builder.equal(root.get(key), val1));
            }
            if (!predicates.isEmpty())
            {
                criteriaQuery.where(predicates.toArray(new Predicate[0]));
            }
            return entityManager.createQuery(criteriaQuery).getResultList();
        }

    }
