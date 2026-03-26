package ru.msu.cmc.webprak.DAO;

import ru.msu.cmc.webprak.models.Client;
import ru.msu.cmc.webprak.filters.ClientFilter;
import java.util.List;

public interface ClientDAO extends CommonDAO<Client, Long> {
    Client getClientByEmail(String email);
    Client getClientByPhone(String phone);
    List<Client> serchClients(ClientFilter client);
}
