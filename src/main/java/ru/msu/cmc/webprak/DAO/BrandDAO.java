package ru.msu.cmc.webprak.DAO;

import ru.msu.cmc.webprak.models.Brand;
import ru.msu.cmc.webprak.models.Client;

import java.util.List;

public interface BrandDAO extends CommonDAO<Brand, Long> {
    List<Brand> findBrandsByName(String name);
}
