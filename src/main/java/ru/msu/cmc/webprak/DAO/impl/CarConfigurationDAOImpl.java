package ru.msu.cmc.webprak.DAO.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprak.DAO.CarConfigurationDAO;
import ru.msu.cmc.webprak.models.*;

import java.util.List;

import org.hibernate.query.Query;
@Repository
public class CarConfigurationDAOImpl extends CommonDAOImpl<CarConfiguration, Long> implements CarConfigurationDAO {

    public CarConfigurationDAOImpl() {
        super(CarConfiguration.class);
    }

    @Override
    public List<CarConfiguration> getConfigurationsByModel(Model model) {
        try (Session session = sessionFactory.openSession()) {
            Query<CarConfiguration> query = session.createQuery("FROM CarConfiguration WHERE model_id = :id", CarConfiguration.class)
                    .setParameter("id", model.getId());
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    }
}
