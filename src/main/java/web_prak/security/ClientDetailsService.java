package web_prak.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import web_prak.DAO.ClientDAO;
import web_prak.DAO.ManagerDAO;
import web_prak.models.Client;
import web_prak.models.Manager;

@Service
@RequiredArgsConstructor
public class ClientDetailsService implements UserDetailsService {

    private final ClientDAO clientDAO;
    private final ManagerDAO managerDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = null;
        Manager manager = null;
        if (username != null && username.contains("@")) {
            client = clientDAO.getClientByEmail(username);
            if(client == null)
                manager = managerDAO.getManagerByEmail(username);
        } else {
            client = clientDAO.getClientByPhone(username);
            if(client == null)
                manager = managerDAO.getManagerByPhone(username);
        }

        if (client == null && manager == null) {
            throw new UsernameNotFoundException("Пользователь с указанным email/телефоном не найден");
        }
        return new ClientUserDetails(client, manager);
    }
}