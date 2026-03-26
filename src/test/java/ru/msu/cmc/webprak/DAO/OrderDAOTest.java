package ru.msu.cmc.webprak.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprak.filters.OrderFilter;
import ru.msu.cmc.webprak.models.*;
import ru.msu.cmc.webprak.models.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class OrderDAOTest {

    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private CarDAO carDAO;
    @Autowired
    private ClientDAO clientDAO;
    @Autowired
    private ManagerDAO managerDAO;
    @Autowired
    private CarConfigurationDAO configDAO;
    @Autowired
    private ModelDAO modelDAO;
    @Autowired
    private BrandDAO brandDAO;
    @Autowired
    private SessionFactory sessionFactory;

    private Car testCar;
    private Client testClient;
    private Manager testManager;

    @Test
    void testGetOrdersListByClient() {
        List<Order> orders = orderDAO.getOrdersListByClient(testClient);
        assertEquals(2, orders.size());
        assertTrue(orders.stream().allMatch(o -> o.getClient().getId().equals(testClient.getId())));

        Client dummy = new Client();
        dummy.setId(999L);
        List<Order> empty = orderDAO.getOrdersListByClient(dummy);
        assertTrue(empty.isEmpty());
    }

    @Test
    void testSaveOrder() {
        Order newOrder = new Order();
        newOrder.setCar(testCar);
        newOrder.setClient(testClient);
        newOrder.setManager(testManager);
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setStatus(Order.OrderStatus.in_process);

        orderDAO.save(newOrder);

        Order saved = orderDAO.getById(newOrder.getId());
        assertNotNull(saved);
        assertEquals(testCar.getId(), saved.getCar().getId());

        List<Order> clientOrders = orderDAO.getOrdersListByClient(testClient);
        assertEquals(3, clientOrders.size());
    }

    @Test
    void testUpdateOrder() {
        List<Order> orders = orderDAO.getOrdersListByClient(testClient);
        Order order = orders.get(0);
        order.setStatus(Order.OrderStatus.canceled);
        order.setDeliveryAddress("New Address");
        orderDAO.update(order);

        Order updated = orderDAO.getById(order.getId());
        assertEquals(Order.OrderStatus.canceled, updated.getStatus());
        assertEquals("New Address", updated.getDeliveryAddress());
    }

    @Test
    void testDeleteOrder() {
        List<Order> orders = orderDAO.getOrdersListByClient(testClient);
        Order toDelete = orders.get(0);
        orderDAO.delete(toDelete);

        assertNull(orderDAO.getById(toDelete.getId()));
        assertEquals(1, orderDAO.getOrdersListByClient(testClient).size());
    }

    @Test
    void testsearchByFilterByStatus() {
        OrderFilter filter = new OrderFilter();
        filter.setStatus(Order.OrderStatus.completed);

        List<Order> completedOrders = orderDAO.searchByFilter(filter, filter);
        assertEquals(1, completedOrders.size());
        assertEquals(Order.OrderStatus.completed, completedOrders.get(0).getStatus());

        filter.setStatus(Order.OrderStatus.in_process);
        List<Order> inProcessOrders = orderDAO.searchByFilter(filter, filter);
        assertEquals(1, inProcessOrders.size());
        assertEquals(Order.OrderStatus.in_process, inProcessOrders.get(0).getStatus());
    }

    @BeforeEach
    void setUp() {
        Brand brand = new Brand();
        brand.setName("Test Brand");
        brand.setCreationYear(2000);
        brand.setContactPhone("+123456789");
        brand.setContactEmail("test@brand.com");
        brand.setCountryCode("TST");
        brand.setLogoSrc("/images/test.png");
        brandDAO.save(brand);
        Model model = new Model();
        model.setBrand(brand);
        model.setName("Test Model");
        model.setYear(2023);
        model.setMinCost(30000);
        modelDAO.save(model);

        CarConfiguration config = new CarConfiguration();
        config.setModel(model);
        config.setName("Base");
        config.setEngineType("Petrol");
        config.setEnginePower(150);
        config.setEngineVolume(new BigDecimal("2.0"));
        config.setFuelConsumption(new BigDecimal("8.5"));
        config.setFuelType("Petrol");
        config.setTankCapacity(new BigDecimal("50"));
        config.setHasCruiseControl(true);
        config.setBasicCost(30000);
        config.setSeatsNumber(5);
        config.setDoorsCount(4);
        config.setTransmissionType("Manual");
        config.setDriveType("FWD");
        configDAO.save(config);

        testCar = new Car();
        testCar.setConfiguration(config);
        testCar.setVIN("TESTVIN123456789");
        testCar.setCost(32000);
        testCar.setColor("Red");
        testCar.setInteriorColor("Black");
        testCar.setIsNew(true);
        testCar.setYear(2023);
        testCar.setStatus(Car.CarStatus.available);
        carDAO.save(testCar);

        testClient = new Client();
        testClient.setName("Test Client");
        testClient.setEmail("client@test.com");
        testClient.setAddress("Test Address");
        testClient.setPhone("+111111111");
        testClient.setPasswordHash("hash");
        clientDAO.save(testClient);

        testManager = new Manager();
        testManager.setName("Test Manager");
        testManager.setEmail("manager@test.com");
        testManager.setPhone("+222222222");
        testManager.setPasswordHash("hash");
        managerDAO.save(testManager);

        List<Order> orders = new ArrayList<>();

        Order order1 = new Order();
        order1.setCar(testCar);
        order1.setClient(testClient);
        order1.setManager(testManager);
        order1.setOrderDate(LocalDate.now());
        order1.setStatus(Order.OrderStatus.completed);
        order1.setDeliveryAddress("Address 1");

        Order order2 = new Order();
        order2.setCar(testCar);
        order2.setClient(testClient);
        order2.setManager(testManager);
        order2.setOrderDate(LocalDate.now());
        order2.setStatus(Order.OrderStatus.in_process);
        order2.setTestDriveDate(LocalDateTime.now());

        orders.add(order1);
        orders.add(order2);
        orderDAO.saveCollection(orders);
    }

    @BeforeAll
    @AfterEach
    void cleanDatabase() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE \"order\" RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE car RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE car_configuration RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE model RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE brand RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE client RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE manager RESTART IDENTITY CASCADE;").executeUpdate();

            session.createSQLQuery("ALTER SEQUENCE \"order_id_seq\" RESTART WITH 1;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE car_id_seq RESTART WITH 1;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE car_configuration_id_seq RESTART WITH 1;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE model_id_seq RESTART WITH 1;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE brand_id_seq RESTART WITH 1;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE client_id_seq RESTART WITH 1;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE manager_id_seq RESTART WITH 1;").executeUpdate();

            session.getTransaction().commit();
        }
    }
}