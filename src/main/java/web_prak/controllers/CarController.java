package web_prak.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web_prak.DAO.*;
import web_prak.filters.CarConfigurationFilter;
import web_prak.filters.CarFilter;
import web_prak.filters.ModelFilter;
import web_prak.models.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarDAO carDAO;
    private final CarConfigurationDAO configDAO;
    private final ModelDAO modelDAO;
    private final BrandDAO brandDAO;


    @GetMapping
    public String list(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String VIN,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String interiorColor,
            @RequestParam(required = false) String seatUpholstery,
            @RequestParam(required = false) String additionalProperties,
            @RequestParam(required = false) String imageSrc,
            @RequestParam(required = false) Car.CarStatus status,
            @RequestParam(required = false) Boolean isNew,
            @RequestParam(required = false) Boolean isTestDriveAvailable,
            @RequestParam(required = false) Integer costMin,
            @RequestParam(required = false) Integer costMax,
            @RequestParam(required = false) Integer yearMin,
            @RequestParam(required = false) Integer yearMax,
            @RequestParam(required = false) Integer mileageMin,
            @RequestParam(required = false) Integer mileageMax,
            @RequestParam(required = false) LocalDate lastLtoDateMin,
            @RequestParam(required = false) LocalDate lastLtoDateMax,
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
            @RequestParam(name = "configuration.model.id", required = false) Long modelId,
            @RequestParam(name = "configuration.model.name", required = false) String modelName,
            @RequestParam(name = "configuration.model.brand.id", required = false) Long brandId,
            @RequestParam(name = "configuration.model.brand.name", required = false) String brandName,
            @RequestParam(defaultValue = "0") int offset,
            Model model, HttpServletRequest request) {

        CarFilter minFilter = new CarFilter();
        CarFilter maxFilter = new CarFilter();

        minFilter.setId(id); maxFilter.setId(id);
        minFilter.setVIN(VIN); maxFilter.setVIN(VIN);
        minFilter.setColor(color); maxFilter.setColor(color);
        minFilter.setInteriorColor(interiorColor); maxFilter.setInteriorColor(interiorColor);
        minFilter.setSeatUpholstery(seatUpholstery); maxFilter.setSeatUpholstery(seatUpholstery);
        minFilter.setImageSrc(imageSrc); maxFilter.setImageSrc(imageSrc);
        minFilter.setStatus(status); maxFilter.setStatus(status);
        minFilter.setIsNew(isNew); maxFilter.setIsNew(isNew);
        minFilter.setIsTestDriveAvailable(isTestDriveAvailable); maxFilter.setIsTestDriveAvailable(isTestDriveAvailable);

        minFilter.setCost(costMin); maxFilter.setCost(costMax);
        minFilter.setYear(yearMin); maxFilter.setYear(yearMax);
        minFilter.setMileage(mileageMin); maxFilter.setMileage(mileageMax);
        minFilter.setLastLtoDate(lastLtoDateMin); maxFilter.setLastLtoDate(lastLtoDateMax);

        CarConfigurationFilter configMin = null;
        ModelFilter modelObj = null;
        if (configId != null || configName != null || enginePowerMin != null || engineVolumeMin != null ||
                engineType != null || fuelConsumptionMin != null || fuelType != null || tankCapacityMin != null ||
                hasCruiseControl != null || basicCostMin != null || seatsNumberMin != null || doorsCountMin != null ||
                transmissionType != null || driveType != null || isBasic != null || isSalesStopped != null ||
                modelId != null || modelName != null || brandId != null || brandName != null) {
            configMin = new CarConfigurationFilter();
            configMin.setId(configId);
            configMin.setName(configName);
            configMin.setEnginePower(enginePowerMin);
            configMin.setEngineVolume(engineVolumeMin);
            configMin.setEngineType(engineType);
            configMin.setFuelConsumption(fuelConsumptionMin);
            configMin.setFuelType(fuelType);
            configMin.setTankCapacity(tankCapacityMin);
            configMin.setHasCruiseControl(hasCruiseControl);
            configMin.setBasicCost(basicCostMin);
            configMin.setSeatsNumber(seatsNumberMin);
            configMin.setDoorsCount(doorsCountMin);
            configMin.setTransmissionType(transmissionType);
            configMin.setDriveType(driveType);
            configMin.setIsBasic(isBasic);
            configMin.setIsSalesStopped(isSalesStopped);
            modelObj = new ModelFilter();
            if (modelId != null || modelName != null || brandId != null || brandName != null) {
                modelObj.setId(modelId);

                modelObj.setName(modelName);

            }
        }

        CarConfigurationFilter configMax = null;
        if (configId != null || configName != null || enginePowerMax != null || engineVolumeMax != null ||
                engineType != null || fuelConsumptionMax != null || fuelType != null || tankCapacityMax != null ||
                hasCruiseControl != null || basicCostMax != null || seatsNumberMax != null || doorsCountMax != null ||
                transmissionType != null || driveType != null || isBasic != null || isSalesStopped != null ||
                modelId != null || modelName != null || brandId != null || brandName != null) {
            configMax = new CarConfigurationFilter();
            configMax.setId(configId);
            configMax.setName(configName);
            configMax.setEnginePower(enginePowerMax);
            configMax.setEngineVolume(engineVolumeMax);
            configMax.setEngineType(engineType);
            configMax.setFuelConsumption(fuelConsumptionMax);
            configMax.setFuelType(fuelType);
            configMax.setTankCapacity(tankCapacityMax);
            configMax.setHasCruiseControl(hasCruiseControl);
            configMax.setBasicCost(basicCostMax);
            configMax.setSeatsNumber(seatsNumberMax);
            configMax.setDoorsCount(doorsCountMax);
            configMax.setTransmissionType(transmissionType);
            configMax.setDriveType(driveType);
            configMax.setIsBasic(isBasic);
            configMax.setIsSalesStopped(isSalesStopped);
            // Модель и марка (точные, одинаковы для min/max)
        }

        if (!request.isUserInRole("MANAGER")) {
            minFilter.status = maxFilter.status = Car.CarStatus.available;
        }
        List<Car> cars = carDAO.searchByFilter(minFilter, maxFilter);
        List<CarConfiguration> configs = configDAO.searchByFilter(configMin, configMax);

        List<web_prak.models.Model> models = modelDAO.searchByFilter(modelObj, modelObj).stream().filter(x -> (brandId == null || x.getBrand().getId() == brandId) && (brandName == null || brandName=="" || x.getBrand().getName().equals(brandName))).toList();
        var tt = modelDAO.searchByFilter(modelObj, modelObj);
        for (int i = 0; i < tt.stream().count(); i++)  {
            System.out.println(tt.get(i).getBrand().getName());
            System.out.println(tt.get(i).getBrand().getName().equals(brandName));
        }
        List<CarConfiguration> intersectConfigs = configs.stream()
                .filter(obj -> models.stream().map(web_prak.models.Model::getId).anyMatch(x -> x == obj.getModel().getId()))
                .toList();

        List<Car> intersect = cars.stream()
                .filter(obj -> intersectConfigs.stream().map(CarConfiguration::getId).anyMatch(x -> x == obj.getConfiguration().getId()))
                .toList();
        model.addAttribute("cars", intersect);
        model.addAttribute("offset", offset);
        return "cars/list";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}/update")
    @ResponseBody
    public Car updatePartial(@PathVariable Long id, @RequestBody Car updatedCar) {
        Car existing = carDAO.getById(id);
        if(updatedCar.getVIN() != null)
            existing.setVIN(updatedCar.getVIN());
        if(updatedCar.getCost() != null)
            existing.setCost(updatedCar.getCost());
        if(updatedCar.getYear() != null)
            existing.setYear(updatedCar.getYear());
        if(updatedCar.getColor() != null)
            existing.setColor(updatedCar.getColor());
        if(updatedCar.getInteriorColor() != null)
            existing.setInteriorColor(updatedCar.getInteriorColor());
        if(updatedCar.getSeatUpholstery() != null)
            existing.setSeatUpholstery(updatedCar.getSeatUpholstery());
        if(updatedCar.getMileage() != null)
            existing.setMileage(updatedCar.getMileage());
        if(updatedCar.getLastLtoDate() != null)
            existing.setLastLtoDate(updatedCar.getLastLtoDate());
        if(updatedCar.getIsNew() != null)
            existing.setIsNew(updatedCar.getIsNew());
        if(updatedCar.getIsTestDriveAvailable() != null)
            existing.setIsTestDriveAvailable(updatedCar.getIsTestDriveAvailable());
        if(updatedCar.getStatus() != null)
            existing.setStatus(updatedCar.getStatus());
        carDAO.update(existing);
        return existing;
    }
    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("car", carDAO.getById(id));
        model.addAttribute("statuses", Car.CarStatus.values()); // список статусов
        return "cars/view";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("configurations", configDAO.getAll());
        return "cars/add";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/add")
    public String add(@ModelAttribute Car car) {
        if (car.getConfiguration() != null && car.getConfiguration().getId() != null) {
            CarConfiguration fullConfig = configDAO.getById(car.getConfiguration().getId());
            car.setConfiguration(fullConfig);
        }
        carDAO.save(car);
        return "redirect:/cars/" + car.getId();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("car", carDAO.getById(id));
        model.addAttribute("configurations", configDAO.getAll());
        return "cars/edit";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @ModelAttribute Car car) {
        car.setId(id);
        carDAO.update(car);
        return "redirect:/cars/" + id;
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        carDAO.deleteById(id);
        return "redirect:/cars";
    }
}