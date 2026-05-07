package web_prak.DAO;

import web_prak.models.Client;
import web_prak.models.Manager;

public interface ManagerDAO extends CommonDAO<Manager, Long> {
    Manager getManagerByEmail(String email);
    Manager getManagerByPhone(String phone);
}
