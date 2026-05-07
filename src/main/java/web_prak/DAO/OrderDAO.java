package web_prak.DAO;

import web_prak.models.Client;
import web_prak.models.Order;

import java.util.List;

public interface OrderDAO extends CommonDAO<Order, Long> {
    List<Order> getOrdersListByClient(Client client);
}
