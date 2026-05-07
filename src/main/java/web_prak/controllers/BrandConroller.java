package web_prak.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web_prak.DAO.BrandDAO;
import web_prak.models.Brand;

@Controller
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandConroller {

    private final BrandDAO brandDAO;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("brands", brandDAO.getAll());
        return "brands/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("brand", brandDAO.getById(id));
        return "brands/view";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("brand", new Brand());
        return "brands/add";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/add")
    public String add(@ModelAttribute Brand brand) {
        brandDAO.save(brand);
        return "redirect:/brands/" + brand.getId();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("brand", brandDAO.getById(id));
        return "brands/edit";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @ModelAttribute Brand brand) {
        brand.setId(id);
        brandDAO.update(brand);
        return "redirect:/brands/" + id;
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        brandDAO.deleteById(id);
        return "redirect:/brands";
    }
}