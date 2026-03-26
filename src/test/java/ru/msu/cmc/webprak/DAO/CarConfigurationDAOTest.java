package ru.msu.cmc.webprak.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprak.models.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class CarConfigurationDAOTest {

    @Autowired
    private CarConfigurationDAO configDAO;
    @Autowired
    private ModelDAO modelDAO;
    @Autowired
    private BrandDAO brandDAO;
    @Autowired
    private SessionFactory sessionFactory;

    private Model camryModel;
    private Model accordModel;

    @Test
    void testGetConfigurationsByModel() {
        List<CarConfiguration> camryConfigs = configDAO.getConfigurationsByModel(camryModel);
        assertEquals(2, camryConfigs.size());

        List<CarConfiguration> accordConfigs = configDAO.getConfigurationsByModel(accordModel);
        assertEquals(1, accordConfigs.size());

        assertTrue(camryConfigs.stream().allMatch(c -> c.getModel().getId().equals(camryModel.getId())));
    }

    @Test
    void testSaveConfiguration() {
        CarConfiguration config = new CarConfiguration();
        config.setModel(camryModel);
        config.setName("XLE Hybrid");
        config.setEngineType("Hybrid");
        config.setEnginePower(208);
        config.setEngineVolume(new BigDecimal("2.5"));
        config.setFuelConsumption(new BigDecimal("5.2"));
        config.setFuelType("Hybrid");
        config.setTankCapacity(new BigDecimal("49"));
        config.setHasCruiseControl(true);
        config.setBasicCost(35000);
        config.setSeatsNumber(5);
        config.setDoorsCount(4);
        config.setTransmissionType("CVT");
        config.setDriveType("FWD");
        config.setIsBasic(false);
        config.setIsSalesStopped(false);

        configDAO.save(config);

        CarConfiguration savedConfig = configDAO.getById(config.getId());
        assertNotNull(savedConfig);
        assertEquals("XLE Hybrid", savedConfig.getName());
        assertEquals(208, savedConfig.getEnginePower());

        List<CarConfiguration> camryConfigs = configDAO.getConfigurationsByModel(camryModel);
        assertEquals(3, camryConfigs.size());
    }

    @Test
    void testUpdateConfiguration() {
        List<CarConfiguration> camryConfigs = configDAO.getConfigurationsByModel(camryModel);
        CarConfiguration config = camryConfigs.get(0);

        config.setEnginePower(210);
        config.setBasicCost(33000);
        config.setHasCruiseControl(true);
        configDAO.update(config);

        CarConfiguration updatedConfig = configDAO.getById(config.getId());
        assertEquals(210, updatedConfig.getEnginePower());
        assertEquals(33000, updatedConfig.getBasicCost());
        assertTrue(updatedConfig.getHasCruiseControl());
    }

    @Test
    void testDeleteConfiguration() {
        List<CarConfiguration> accordConfigs = configDAO.getConfigurationsByModel(accordModel);
        CarConfiguration configToDelete = accordConfigs.get(0);
        Long configId = configToDelete.getId();

        configDAO.delete(configToDelete);

        CarConfiguration deletedConfig = configDAO.getById(configId);
        assertNull(deletedConfig);

        List<CarConfiguration> remainingConfigs = configDAO.getConfigurationsByModel(accordModel);
        assertEquals(null, remainingConfigs);
    }

    @Test
    void testBasicCostValidation() {
        CarConfiguration config = new CarConfiguration();
        config.setModel(camryModel);
        config.setName("LE");
        config.setEngineType("Gasoline");
        config.setEnginePower(203);
        config.setEngineVolume(new BigDecimal("2.5"));
        config.setFuelConsumption(new BigDecimal("7.8"));
        config.setFuelType("Gasoline");
        config.setTankCapacity(new BigDecimal("60"));
        config.setHasCruiseControl(true);
        config.setBasicCost(26000);
        config.setSeatsNumber(5);
        config.setDoorsCount(4);
        config.setTransmissionType("Automatic");
        config.setDriveType("FWD");

        configDAO.save(config);

        CarConfiguration savedConfig = configDAO.getById(config.getId());
        assertNotNull(savedConfig);
        assertTrue(savedConfig.getBasicCost() > 0);
    }

    @BeforeEach
    void setUp() {
        // Создаем бренд
        Brand toyota = new Brand();
        toyota.setName("Toyota");
        toyota.setCreationYear(1937);
        toyota.setContactPhone("+81-3-3817-1111");
        toyota.setContactEmail("toyota@toyota.com");
        toyota.setCountryCode("JPN");
        toyota.setLogoSrc("/images/toyota.png");
        brandDAO.save(toyota);

        Brand honda = new Brand();
        honda.setName("Honda");
        honda.setCreationYear(1948);
        honda.setContactPhone("+81-3-3423-1111");
        honda.setContactEmail("honda@honda.com");
        honda.setCountryCode("JPN");
        honda.setLogoSrc("/images/honda.png");
        brandDAO.save(honda);

        // Создаем модели
        camryModel = new Model();
        camryModel.setBrand(toyota);
        camryModel.setName("Camry");
        camryModel.setYear(2023);
        camryModel.setMinCost(26000);
        modelDAO.save(camryModel);

        accordModel = new Model();
        accordModel.setBrand(honda);
        accordModel.setName("Accord");
        accordModel.setYear(2023);
        accordModel.setMinCost(27000);
        modelDAO.save(accordModel);

        // Создаем конфигурации
        List<CarConfiguration> configs = new ArrayList<>();

        CarConfiguration camryLE = new CarConfiguration();
        camryLE.setModel(camryModel);
        camryLE.setName("LE");
        camryLE.setEngineType("Gasoline");
        camryLE.setEnginePower(203);
        camryLE.setEngineVolume(new BigDecimal("2.5"));
        camryLE.setFuelConsumption(new BigDecimal("7.8"));
        camryLE.setFuelType("Gasoline");
        camryLE.setTankCapacity(new BigDecimal("60"));
        camryLE.setHasCruiseControl(true);
        camryLE.setBasicCost(26000);
        camryLE.setSeatsNumber(5);
        camryLE.setDoorsCount(4);
        camryLE.setTransmissionType("Automatic");
        camryLE.setDriveType("FWD");

        CarConfiguration camryXLE = new CarConfiguration();
        camryXLE.setModel(camryModel);
        camryXLE.setName("XLE");
        camryXLE.setEngineType("Gasoline");
        camryXLE.setEnginePower(203);
        camryXLE.setEngineVolume(new BigDecimal("2.5"));
        camryXLE.setFuelConsumption(new BigDecimal("7.8"));
        camryXLE.setFuelType("Gasoline");
        camryXLE.setTankCapacity(new BigDecimal("60"));
        camryXLE.setHasCruiseControl(true);
        camryXLE.setBasicCost(31000);
        camryXLE.setSeatsNumber(5);
        camryXLE.setDoorsCount(4);
        camryXLE.setTransmissionType("Automatic");
        camryXLE.setDriveType("FWD");
        camryXLE.setIsBasic(false);

        CarConfiguration accordSport = new CarConfiguration();
        accordSport.setModel(accordModel);
        accordSport.setName("Sport");
        accordSport.setEngineType("Gasoline");
        accordSport.setEnginePower(192);
        accordSport.setEngineVolume(new BigDecimal("1.5"));
        accordSport.setFuelConsumption(new BigDecimal("7.5"));
        accordSport.setFuelType("Gasoline");
        accordSport.setTankCapacity(new BigDecimal("56"));
        accordSport.setHasCruiseControl(true);
        accordSport.setBasicCost(28000);
        accordSport.setSeatsNumber(5);
        accordSport.setDoorsCount(4);
        accordSport.setTransmissionType("CVT");
        accordSport.setDriveType("FWD");

        configs.add(camryLE);
        configs.add(camryXLE);
        configs.add(accordSport);

        configDAO.saveCollection(configs);
    }

    @BeforeAll
    @AfterEach
    void cleanDatabase() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE car_configuration RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE model RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE brand RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE car_configuration_id_seq RESTART WITH 1;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE model_id_seq RESTART WITH 1;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE brand_id_seq RESTART WITH 1;").executeUpdate();
            session.getTransaction().commit();
        }
    }
}