package ru.msu.cmc.webprak.DAO.impl;

import org.hibernate.Session;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprak.DAO.CarDAO;
import ru.msu.cmc.webprak.models.*;


@Repository
public class CarDAOImpl extends CommonDAOImpl<Car, Long>  implements CarDAO {
    public CarDAOImpl(LocalSessionFactoryBean sessionFactory) {
        super(Car.class);
    }
}