package web_prak.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web_prak.DAO.CarConfigurationDAO;
import web_prak.models.CarConfiguration;

@Controller
@RequestMapping("/configurations")
@PreAuthorize("hasRole('MANAGER')")
@RequiredArgsConstructor
public class ConfigurationController {

    private final CarConfigurationDAO configDAO;

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("config", configDAO.getById(id));
        return "configurations/edit";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @ModelAttribute("config") CarConfiguration formConfig) {
        CarConfiguration existing = configDAO.getById(id);
        existing.setName(formConfig.getName());
        existing.setEngineType(formConfig.getEngineType());
        existing.setEnginePower(formConfig.getEnginePower());
        existing.setEngineVolume(formConfig.getEngineVolume());
        existing.setFuelConsumption(formConfig.getFuelConsumption());
        existing.setFuelType(formConfig.getFuelType());
        existing.setTankCapacity(formConfig.getTankCapacity());
        existing.setTransmissionType(formConfig.getTransmissionType());
        existing.setDriveType(formConfig.getDriveType());
        existing.setHasCruiseControl(formConfig.getHasCruiseControl());
        existing.setBasicCost(formConfig.getBasicCost());
        existing.setDoorsCount(formConfig.getDoorsCount());
        existing.setSeatsNumber(formConfig.getSeatsNumber());
        existing.setIsBasic(formConfig.getIsBasic());
        existing.setIsSalesStopped(formConfig.getIsSalesStopped());
        if(formConfig.getAdditionalProperties() != "")
           existing.setAdditionalProperties(formConfig.getAdditionalProperties());
        configDAO.update(existing);
        return "redirect:/models/" + existing.getModel().getId() + "/edit";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        CarConfiguration config = configDAO.getById(id);
        Long modelId = config.getModel().getId();
        configDAO.deleteById(id);
        return "redirect:/models/" + modelId + "/edit";
    }
}