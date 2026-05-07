package web_prak.DAO.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import web_prak.DAO.CarDAO;
import web_prak.models.*;


@Repository
public class CarDAOImpl extends CommonDAOImpl<Car, Long>  implements CarDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public CarDAOImpl() {
        super(Car.class);
    }
}