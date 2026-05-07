package web_prak.DAO;

import web_prak.models.Brand;

import java.util.List;

public interface BrandDAO extends CommonDAO<Brand, Long> {
    List<Brand> findBrandsByName(String name);
}
