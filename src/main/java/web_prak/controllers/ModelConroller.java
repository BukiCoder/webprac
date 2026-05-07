package web_prak.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web_prak.DAO.*;
import web_prak.filters.CarConfigurationFilter;
import web_prak.filters.ModelFilter;
import web_prak.models.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/models")
@RequiredArgsConstructor
public class ModelConroller {

    private final ModelDAO modelDAO;
    private final CarConfigurationDAO configDAO;
    private final BrandDAO brandDAO;

    @GetMapping
    public String list(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer yearMin,
            @RequestParam(required = false) Integer yearMax,
            @RequestParam(required = false) Integer minCostMin,
            @RequestParam(required = false) Integer minCostMax,
            @RequestParam(name = "brand.id", required = false) Long brandId,
            @RequestParam(name = "brand.name", required = false) String brandName,
            @RequestParam(name = "configuration.id", required = false) Long configId,
            @RequestParam(name = "configuration.name", required = false) String configName,
            @RequestParam(name = "configuration.enginePowerMin", required = false) Integer enginePowerMin,
            @RequestParam(name = "configuration.enginePowerMax", required = false) Integer enginePowerMax,
            @RequestParam(name = "configuration.engineVolumeMin", required = false) BigDecimal engineVolumeMin,
            @RequestParam(name = "configuration.engineVolumeMax", required = false) BigDecimal engineVolumeMax,
            @RequestParam(name = "configuration.engineType", required = false) String engineType,
            @RequestParam(name = "configuration.fuelConsumptionMin", required = false) BigDecimal fuelConsumptionMin,
            @RequestParam(name = "configuration.fuelConsumptionMax", required = false) BigDecimal fuelConsumptionMax,
            @RequestParam(name = "configuration.fuelType", required = false) String fuelType,
            @RequestParam(name = "configuration.tankCapacityMin", required = false) BigDecimal tankCapacityMin,
            @RequestParam(name = "configuration.tankCapacityMax", required = false) BigDecimal tankCapacityMax,
            @RequestParam(name = "configuration.hasCruiseControl", required = false) Boolean hasCruiseControl,
            @RequestParam(name = "configuration.basicCostMin", required = false) Integer basicCostMin,
            @RequestParam(name = "configuration.basicCostMax", required = false) Integer basicCostMax,
            @RequestParam(name = "configuration.seatsNumberMin", required = false) Integer seatsNumberMin,
            @RequestParam(name = "configuration.seatsNumberMax", required = false) Integer seatsNumberMax,
            @RequestParam(name = "configuration.doorsCountMin", required = false) Integer doorsCountMin,
            @RequestParam(name = "configuration.doorsCountMax", required = false) Integer doorsCountMax,
            @RequestParam(name = "configuration.transmissionType", required = false) String transmissionType,
            @RequestParam(name = "configuration.driveType", required = false) String driveType,
            @RequestParam(name = "configuration.isBasic", required = false) Boolean isBasic,
            @RequestParam(name = "configuration.isSalesStopped", required = false) Boolean isSalesStopped,
            Model model,
            HttpServletRequest request) {

        if (!request.isUserInRole("MANAGER")) {
            id = null;
            configId = null;
        }

        ModelFilter minModelFilter = new ModelFilter();
        ModelFilter maxModelFilter = new ModelFilter();

        minModelFilter.setId(id);
        maxModelFilter.setId(id);
        minModelFilter.setName(name);
        maxModelFilter.setName(name);

        minModelFilter.setYear(yearMin);
        maxModelFilter.setYear(yearMax);
        minModelFilter.setMinCost(minCostMin);
        maxModelFilter.setMinCost(minCostMax);

        CarConfigurationFilter configMin = null;
        CarConfigurationFilter configMax = null;
        boolean hasConfigFilter = configId != null || configName != null ||
                enginePowerMin != null || enginePowerMax != null ||
                engineVolumeMin != null || engineVolumeMax != null ||
                engineType != null ||
                fuelConsumptionMin != null || fuelConsumptionMax != null ||
                fuelType != null ||
                tankCapacityMin != null || tankCapacityMax != null ||
                hasCruiseControl != null ||
                basicCostMin != null || basicCostMax != null ||
                seatsNumberMin != null || seatsNumberMax != null ||
                doorsCountMin != null || doorsCountMax != null ||
                transmissionType != null || driveType != null ||
                isBasic != null || isSalesStopped != null;

        if (hasConfigFilter) {
            configMin = new CarConfigurationFilter();
            configMax = new CarConfigurationFilter();

            configMin.setId(configId);          configMax.setId(configId);
            configMin.setName(configName);      configMax.setName(configName);
            configMin.setEnginePower(enginePowerMin); configMax.setEnginePower(enginePowerMax);
            configMin.setEngineVolume(engineVolumeMin); configMax.setEngineVolume(engineVolumeMax);
            configMin.setEngineType(engineType); configMax.setEngineType(engineType);
            configMin.setFuelConsumption(fuelConsumptionMin); configMax.setFuelConsumption(fuelConsumptionMax);
            configMin.setFuelType(fuelType);    configMax.setFuelType(fuelType);
            configMin.setTankCapacity(tankCapacityMin); configMax.setTankCapacity(tankCapacityMax);
            configMin.setHasCruiseControl(hasCruiseControl); configMax.setHasCruiseControl(hasCruiseControl);
            configMin.setBasicCost(basicCostMin); configMax.setBasicCost(basicCostMax);
            configMin.setSeatsNumber(seatsNumberMin); configMax.setSeatsNumber(seatsNumberMax);
            configMin.setDoorsCount(doorsCountMin); configMax.setDoorsCount(doorsCountMax);
            configMin.setTransmissionType(transmissionType); configMax.setTransmissionType(transmissionType);
            configMin.setDriveType(driveType); configMax.setDriveType(driveType);
            configMin.setIsBasic(isBasic);      configMax.setIsBasic(isBasic);
            configMin.setIsSalesStopped(isSalesStopped); configMax.setIsSalesStopped(isSalesStopped);
        }

        List<web_prak.models.Model> models = modelDAO.searchByFilter(minModelFilter, maxModelFilter);

        if (brandId != null) {
            models = models.stream()
                    .filter(m -> m.getBrand().getId().equals(brandId))
                    .collect(Collectors.toList());
        }
        if (brandName != null && !brandName.isBlank()) {
            models = models.stream()
                    .filter(m -> m.getBrand().getName().equalsIgnoreCase(brandName))
                    .collect(Collectors.toList());
        }

        if (hasConfigFilter) {
            List<CarConfiguration> configs = configDAO.searchByFilter(configMin, configMax);
            Set<Long> modelIdsWithConfig = configs.stream()
                    .map(c -> c.getModel().getId())
                    .collect(Collectors.toSet());
            models = models.stream()
                    .filter(m -> modelIdsWithConfig.contains(m.getId()))
                    .collect(Collectors.toList());
        }

        model.addAttribute("models", models);
        return "models/list";
    }
    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        web_prak.models.Model m = modelDAO.getById(id);
        List<CarConfiguration> configs = configDAO.getConfigurationsByModel(m);

        CarConfiguration basicConfig = configs.stream()
                .filter(CarConfiguration::getIsBasic)
                .findFirst().orElse(null);
        CarConfiguration selected = basicConfig != null ? basicConfig :
                (configs.isEmpty() ? null : configs.get(0));
        Map<Long, List<String>> differencesMap = computeDifferences(configs, basicConfig);
        model.addAttribute("model", m);
        model.addAttribute("configurations", configs);
        model.addAttribute("selectedConfig", selected);
        model.addAttribute("differencesMap", differencesMap);
        return "models/view";
    }

    @GetMapping("/{id}/config")
    public String viewConfig(@PathVariable Long id, @RequestParam Long configId, Model model) {
        web_prak.models.Model m = modelDAO.getById(id);
        List<CarConfiguration> configs = configDAO.getConfigurationsByModel(m);
        CarConfiguration basicConfig = configs.stream()
                .filter(CarConfiguration::getIsBasic)
                .findFirst().orElse(null);
        CarConfiguration selected = configDAO.getById(configId);
        model.addAttribute("model", m);
        model.addAttribute("configurations", configs);
        model.addAttribute("selectedConfig", selected);
        model.addAttribute("differencesMap", computeDifferences(configs, basicConfig));
        return "models/view";
    }

    private Map<Long, List<String>> computeDifferences(List<CarConfiguration> configs, CarConfiguration base) {
        Map<Long, List<String>> map = new HashMap<>();
        if (base == null) return map;
        for (CarConfiguration cfg : configs) {
            if (cfg.getId().equals(base.getId())) continue;
            List<String> diffs = new ArrayList<>();
            if (!cfg.getEnginePower().equals(base.getEnginePower()))
                diffs.add(cfg.getEnginePower() + " л.с.");
            if (!cfg.getEngineVolume().equals(base.getEngineVolume()))
                diffs.add(cfg.getEngineVolume() + " л");
            if (!cfg.getFuelType().equals(base.getFuelType()))
                diffs.add(cfg.getFuelType());
            if (!cfg.getTransmissionType().equals(base.getTransmissionType()))
                diffs.add(cfg.getTransmissionType());
            if (!cfg.getDriveType().equals(base.getDriveType()))
                diffs.add(cfg.getDriveType());
            if (!cfg.getDoorsCount().equals(base.getDoorsCount()))
                diffs.add(cfg.getDoorsCount() + " дв.");
            if (!cfg.getSeatsNumber().equals(base.getSeatsNumber()))
                diffs.add(cfg.getSeatsNumber() + " мест");
            if (!cfg.getHasCruiseControl().equals(base.getHasCruiseControl()))
                diffs.add(cfg.getHasCruiseControl() ? "круиз" : "без круиза");
            if (cfg.getFuelConsumption().compareTo(base.getFuelConsumption()) != 0)
                diffs.add(cfg.getFuelConsumption() + " л/100км");
            if (cfg.getTankCapacity().compareTo(base.getTankCapacity()) != 0)
                diffs.add(cfg.getTankCapacity() + " л");
            map.put(cfg.getId(), diffs);
        }
        return map;
    }
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("model", new web_prak.models.Model());
        model.addAttribute("brands", brandDAO.getAll());
        return "models/add";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/add")
    public String add(@ModelAttribute web_prak.models.Model model1,
                      Model model) {
        CarConfiguration config = new CarConfiguration();
        config.setModel(model1);
        config.setName("new_conf");
        config.setEngineType("бензин");
        config.setEnginePower(1);
        config.setEngineVolume(BigDecimal.valueOf(1));
        config.setFuelConsumption(BigDecimal.valueOf(1));
        config.setFuelType("АИ-95");
        config.setTankCapacity(BigDecimal.valueOf(1));
        config.setTransmissionType("МКПП");
        config.setDriveType("FWD");
        config.setHasCruiseControl(false);
        config.setBasicCost(100000);
        config.setDoorsCount(4);
        config.setSeatsNumber(5);
        config.setIsBasic(false);
        config.setIsSalesStopped(false);

        modelDAO.save(model1);
        configDAO.save(config);

        return "redirect:/models/" + model1.getId();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("model", modelDAO.getById(id));
        model.addAttribute("brands", brandDAO.getAll());
        model.addAttribute("configurations", configDAO.getConfigurationsByModel(modelDAO.getById(id)));
        return "models/edit";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{id}/edit")
    public String updateModel(@PathVariable Long id, @ModelAttribute("model") web_prak.models.Model model) {
        model.setId(id);
        modelDAO.update(model);
        return "redirect:/models/" + id;
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        modelDAO.deleteById(id);
        return "redirect:/models";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{id}/addConfig")
    public String addConfig(@PathVariable Long id) {
        web_prak.models.Model model = modelDAO.getById(id);
        CarConfiguration config = new CarConfiguration();
        config.setModel(model);
        config.setName("new_conf");
        config.setEngineType("бензин");
        config.setEnginePower(1);
        config.setEngineVolume(BigDecimal.valueOf(1));
        config.setFuelConsumption(BigDecimal.valueOf(1));
        config.setFuelType("АИ-95");
        config.setTankCapacity(BigDecimal.valueOf(1));
        config.setTransmissionType("МКПП");
        config.setDriveType("FWD");
        config.setHasCruiseControl(false);
        config.setBasicCost(100000);
        config.setDoorsCount(4);
        config.setSeatsNumber(5);
        config.setIsBasic(false);
        config.setIsSalesStopped(false);
        configDAO.save(config);
        return "redirect:/models/" + id + "/edit";
    }
}