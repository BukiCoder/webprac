package web_prak.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import web_prak.DAO.ClientDAO;
import web_prak.DAO.ManagerDAO;
import web_prak.DAO.ModelDAO;
import web_prak.DAO.impl.ClientDAOImpl;
import web_prak.models.Client;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class MainController {

    @Autowired
    private ClientDAO clDAO;

    @Autowired
    private ManagerDAO mnDAO;

    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("client", new Client());
        return "register";
    }
    @PostMapping("/register")
    public String registerP(Client d) {
        System.out.println(d.getEmail());
        if (clDAO.getClientByEmail(d.getEmail()) != null || mnDAO.getManagerByEmail(d.getEmail()) != null)
        {
            return "redirect:error?msg=" + URLEncoder.encode("Пользователь с таким e-mail уже существует", StandardCharsets.UTF_8);
        }
        if (clDAO.getClientByPhone(d.getPhone()) != null || mnDAO.getManagerByPhone(d.getPhone()) != null)
        {
            return  "redirect:error?msg=" + URLEncoder.encode("Пользователь с таким номером телефона уже существует", StandardCharsets.UTF_8);
        }
        clDAO.save(d);
        return "login";
    }
}