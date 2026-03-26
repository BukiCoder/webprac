package ru.msu.cmc.webprak.DAO.impl;

import org.hibernate.Session;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprak.DAO.ModelDAO;
import ru.msu.cmc.webprak.models.*;
import ru.msu.cmc.webprak.filters.ModelFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class ModelDAOImpl extends CommonDAOImpl<Model, Long> implements ModelDAO {

    public ModelDAOImpl(LocalSessionFactoryBean sessionFactory) {
        super(Model.class);
    }

    @Override
    public List<Model> getModelListByBrand(Brand brand) {
        List<Model> res = new ArrayList<>();
        for(Model m : getAll()) {
            if (Objects.equals(m.getBrand().getId(), brand.getId())) {
                res.add(m);
            }
        }
        return res;
    }
}