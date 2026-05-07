package web_prak.DAO;

import web_prak.models.CarConfiguration;
import web_prak.models.Model;

import java.util.List;

public interface CarConfigurationDAO extends CommonDAO<CarConfiguration, Long> {

    List<CarConfiguration> getConfigurationsByModel(Model model);
}
