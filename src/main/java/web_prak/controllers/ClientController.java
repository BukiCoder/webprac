package web_prak.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web_prak.DAO.ClientDAO;
import web_prak.DAO.OrderDAO;
import web_prak.filters.ClientFilter;
import web_prak.models.Client;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientDAO clientDAO;
    private final OrderDAO orderDAO;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
    public String list(@ModelAttribute ClientFilter filter, Model model) {
        List<Client> clients = clientDAO.serchClients(filter);
        model.addAttribute("clients", clients);
        model.addAttribute("filter", filter);
        return "clients/list";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model,
                       @RequestParam(value = "edit", required = false) Boolean edit) {
        Client client = clientDAO.getById(id);
        model.addAttribute("client", client);
        model.addAttribute("orders", orderDAO.getOrdersListByClient(client));
        model.addAttribute("editMode", edit != null && edit);
        return "clients/view";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("client", new Client());
        return "clients/add";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/add")
    public String add(@ModelAttribute Client client) {
        client.setPasswordHash(passwordEncoder.encode(client.getPasswordHash()));
        client.setRegistrationDate(LocalDate.now());
        clientDAO.save(client);
        return "redirect:/clients/" + client.getId();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @ModelAttribute Client client) {
        client.setId(id);
        Client existing = clientDAO.getById(id);
        if (existing.getPasswordHash().isEmpty()) {
            client.setPasswordHash(existing.getPasswordHash());
        } else {
            client.setPasswordHash(passwordEncoder.encode(existing.getPasswordHash()));
        }
        clientDAO.update(client);
        return "redirect:/clients/" + id;
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        clientDAO.deleteById(id);
        return "redirect:/clients";
    }
}