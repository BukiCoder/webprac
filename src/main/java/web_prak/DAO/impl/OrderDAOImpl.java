package web_prak.DAO.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import web_prak.DAO.OrderDAO;
import web_prak.models.Client;
import web_prak.models.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Repository
public class OrderDAOImpl extends CommonDAOImpl<Order, Long> implements OrderDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public OrderDAOImpl() {
        super(Order.class);
    }

    @Override
    public List<Order> getOrdersListByClient(Client client) {
        List<Order> res = new ArrayList<>();
        for(Order ord : getAll()) {
            if (Objects.equals(ord.getClient().getId(), client.getId())) {
                res.add(ord);
            }
        }
        return res;
    }
}