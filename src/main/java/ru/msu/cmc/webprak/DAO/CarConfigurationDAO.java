package ru.msu.cmc.webprak.DAO;

import ru.msu.cmc.webprak.models.CarConfiguration;
import ru.msu.cmc.webprak.models.Car;
import ru.msu.cmc.webprak.models.Model;
import java.util.List;

public interface CarConfigurationDAO extends CommonDAO<CarConfiguration, Long> {

    List<CarConfiguration> getConfigurationsByModel(Model model);
}
