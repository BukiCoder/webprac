package web_prak.DAO.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import web_prak.DAO.CarConfigurationDAO;
import web_prak.models.*;

import java.util.List;
@Repository
public class CarConfigurationDAOImpl extends CommonDAOImpl<CarConfiguration, Long> implements CarConfigurationDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public CarConfigurationDAOImpl() {
        super(CarConfiguration.class);
    }

    @Override
    @Transactional
    public List<CarConfiguration> getConfigurationsByModel(Model model) {
            Query<CarConfiguration> query = (Query<CarConfiguration>) entityManager.createQuery("FROM CarConfiguration WHERE model.id = :id", CarConfiguration.class)
                    .setParameter("id", model.getId());
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }

}
