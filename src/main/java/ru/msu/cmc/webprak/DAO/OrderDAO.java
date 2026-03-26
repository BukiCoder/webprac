package ru.msu.cmc.webprak.DAO;

import ru.msu.cmc.webprak.filters.OrderFilter;
import ru.msu.cmc.webprak.models.Order;
import ru.msu.cmc.webprak.models.Client;
import java.util.List;

public interface OrderDAO extends CommonDAO<Order, Long> {
    List<Order> getOrdersListByClient(Client client);
}
