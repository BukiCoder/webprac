package web_prak.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web_prak.DAO.ClientDAO;
import web_prak.DAO.OrderDAO;
import web_prak.models.Client;

@Controller
@RequestMapping("/cabinet")
@RequiredArgsConstructor
public class CabinetController {

    private final ClientDAO clientDAO;
    private final OrderDAO orderDAO;

    @GetMapping
    public String cabinet(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientDAO.getClientByEmail(email);
        model.addAttribute("client", client);
        model.addAttribute("orders", orderDAO.getOrdersListByClient(client));
        return "cabinet";
    }
}