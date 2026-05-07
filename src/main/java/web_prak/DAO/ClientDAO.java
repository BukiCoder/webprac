package web_prak.DAO;

import web_prak.filters.ClientFilter;
import web_prak.models.Client;

import java.util.List;

public interface ClientDAO extends CommonDAO<Client, Long> {
    Client getClientByEmail(String email);
    Client getClientByPhone(String phone);
    List<Client> serchClients(ClientFilter client);
}
