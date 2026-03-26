package ru.msu.cmc.webprak.DAO.impl;

import org.hibernate.Session;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprak.DAO.OrderDAO;
import ru.msu.cmc.webprak.filters.OrderFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.msu.cmc.webprak.models.Client;
import ru.msu.cmc.webprak.models.Order;
@Repository
public class OrderDAOImpl extends CommonDAOImpl<Order, Long> implements OrderDAO {

    public OrderDAOImpl(LocalSessionFactoryBean sessionFactory) {
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