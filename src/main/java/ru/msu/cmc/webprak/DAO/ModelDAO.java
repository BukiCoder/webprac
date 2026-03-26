package ru.msu.cmc.webprak.DAO;

import ru.msu.cmc.webprak.filters.ModelFilter;
import ru.msu.cmc.webprak.models.Model;
import ru.msu.cmc.webprak.models.Brand;
import java.util.List;

public interface ModelDAO extends CommonDAO<Model, Long> {
    List<Model> getModelListByBrand(Brand brand);
}
