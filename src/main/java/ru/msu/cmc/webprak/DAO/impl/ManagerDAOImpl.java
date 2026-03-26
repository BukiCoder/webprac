package ru.msu.cmc.webprak.DAO.impl;

import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprak.DAO.BrandDAO;
import ru.msu.cmc.webprak.DAO.ManagerDAO;
import ru.msu.cmc.webprak.models.*;

@Repository
public class ManagerDAOImpl extends CommonDAOImpl<Manager, Long> implements ManagerDAO {

    public ManagerDAOImpl() {
        super(Manager.class);
    }
}