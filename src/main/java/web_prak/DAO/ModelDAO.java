package web_prak.DAO;

import web_prak.models.Brand;
import web_prak.models.Model;

import java.util.List;

public interface ModelDAO extends CommonDAO<Model, Long> {
    List<Model> getModelListByBrand(Brand brand);
}
