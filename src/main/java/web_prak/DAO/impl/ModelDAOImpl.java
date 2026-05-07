package web_prak.DAO.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import web_prak.DAO.ModelDAO;
import web_prak.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ModelDAOImpl extends CommonDAOImpl<Model, Long> implements ModelDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public ModelDAOImpl() {
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