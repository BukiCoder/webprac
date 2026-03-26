package ru.msu.cmc.webprak.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprak.filters.CarFilter;
import ru.msu.cmc.webprak.models.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class CarDAOTest {

    @Autowired
    private CarDAO carDAO;
    @Autowired
    private CarConfigurationDAO configDAO;
    @Autowired
    private ModelDAO modelDAO;
    @Autowired
    private BrandDAO brandDAO;
    @Autowired
    private SessionFactory sessionFactory;

    private CarConfiguration camryLEConfig;
    private CarConfiguration camryXLEConfig;

    @Test
    void testsearchByFilter() {
        CarFilter filter = new CarFilter();
        filter.setStatus(Car.CarStatus.available);

        List<Car> availableCars = carDAO.searchByFilter(filter, filter);
        assertEquals(2, availableCars.size());
        assertTrue(availableCars.stream().allMatch(c -> c.getStatus() == Car.CarStatus.available));
    }

    @Test
    void testsearchByFilterByColor() {
        CarFilter filter = new CarFilter();
        filter.setColor("Red");

        List<Car> redCars = carDAO.searchByFilter(filter, filter);
        assertEquals(1, redCars.size());
        assertEquals("Red", redCars.get(0).getColor());
    }

    @Test
    void testsearchByFilterByPriceRange() {
        CarFilter filter1 = new CarFilter();
        filter1.setCost(27000);
        CarFilter filter2 = new CarFilter();
        filter2.setCost(32000);

        List<Car> carsInRange = carDAO.searchByFilter(filter1, filter2);
        assertEquals(3, carsInRange.size());
        assertTrue(carsInRange.stream().allMatch(c ->c.getCost() >= 27000 && c.getCost() <= 32000));
    }

    @Test
    void testsearchByFilterByYearRange() {
        CarFilter filter1 = new CarFilter();
        filter1.setYear(2022);
        CarFilter filter2 = new CarFilter();
        filter2.setYear(2023);

        List<Car> carsByYear = carDAO.searchByFilter(filter1, filter2);
        assertEquals(4, carsByYear.size());
        assertTrue(carsByYear.stream().allMatch(c -> c.getYear() >= 2022 && c.getYear() <= 2023));
    }

    @Test
    void testSearchNewCars() {
        CarFilter filter = new CarFilter();
        filter.setIsNew(true);

        List<Car> newCars = carDAO.searchByFilter(filter, filter);
        assertEquals(2, newCars.size());
        assertTrue(newCars.stream().allMatch(Car::getIsNew));
    }


    @Test
    void testSaveCar() {
        Car newCar = new Car();
        newCar.setConfiguration(camryLEConfig);
        newCar.setVIN("1HGCM82633A123457");
        newCar.setCost(28000);
        newCar.setColor("Blue");
        newCar.setMileage(0);
        newCar.setInteriorColor("Black");
        newCar.setIsNew(true);
        newCar.setYear(2024);
        newCar.setStatus(Car.CarStatus.available);
        newCar.setIsTestDriveAvailable(true);

        carDAO.save(newCar);

        Car savedCar = carDAO.getById(newCar.getId());
        assertNotNull(savedCar);
        assertEquals("1HGCM82633A123457", savedCar.getVIN());
        assertEquals(28000, savedCar.getCost());

        CarFilter filter = new CarFilter();
        filter.setColor("Blue");
        List<Car> blueCars = carDAO.searchByFilter(filter, filter);
        assertEquals(2, blueCars.size());
        assertTrue(blueCars.get(0).getColor().equals("Blue") && blueCars.get(1).getColor().equals("Blue"));

    }

    @Test
    void testUpdateCar() {
        CarFilter filter = new CarFilter();
        filter.setColor("Red");
        List<Car> redCars = carDAO.searchByFilter(filter, filter);
        Car carToUpdate = redCars.get(0);

        carToUpdate.setStatus(Car.CarStatus.sold);
        carToUpdate.setCost(29000);
        carDAO.update(carToUpdate);

        Car updatedCar = carDAO.getById(carToUpdate.getId());
        assertEquals(Car.CarStatus.sold, updatedCar.getStatus());
        assertEquals(29000, updatedCar.getCost());
    }

    @Test
    void testDeleteCar() {
        CarFilter filter = new CarFilter();
        filter.setColor("White");
        List<Car> whiteCars = carDAO.searchByFilter(filter, filter);
        Car carToDelete = whiteCars.get(0);
        Long carId = carToDelete.getId();

        carDAO.delete(carToDelete);

        Car deletedCar = carDAO.getById(carId);
        assertNull(deletedCar);

        filter.setColor("White");
        List<Car> remainingWhiteCars = carDAO.searchByFilter(filter, filter);
        assertEquals(1, remainingWhiteCars.size());
        assertTrue(remainingWhiteCars.get(0).getColor().equals("White"));
    }

    @Test
    void testCarWithTestDrive() {
        CarFilter filter = new CarFilter();
        filter.setIsTestDriveAvailable(true);

        List<Car> carsWithTestDrive = carDAO.searchByFilter(filter, filter);
        assertEquals(2, carsWithTestDrive.size());
        assertTrue(carsWithTestDrive.stream().allMatch(Car::getIsTestDriveAvailable));
    }


    @Test
    void testsearchByFilterByStatus() {
        CarFilter filter = new CarFilter();
        filter.setStatus(Car.CarStatus.available);

        List<Car> availableCars = carDAO.searchByFilter(filter, filter);
        assertEquals(2, availableCars.size());
        assertTrue(availableCars.stream().allMatch(c -> c.getStatus() == Car.CarStatus.available));

        filter.setStatus(Car.CarStatus.sold);
        List<Car> soldCars = carDAO.searchByFilter(filter, filter);
        assertEquals(1, soldCars.size());
        assertEquals(Car.CarStatus.sold, soldCars.get(0).getStatus());
    }


    @Test
    void testsearchByFilterByMileageRange() {
        CarFilter filterMin = new CarFilter();
        filterMin.setMileage(5000);
        CarFilter filterMax = new CarFilter();
        filterMax.setMileage(10000);

        List<Car> carsWithMileage = carDAO.searchByFilter(filterMin, filterMax);
        assertEquals(1, carsWithMileage.size());
        assertTrue(carsWithMileage.get(0).getMileage() >= 5000 && carsWithMileage.get(0).getMileage() <= 10000);
    }

    @Test
    void testsearchByFilterByInteriorColor() {
        CarFilter filter = new CarFilter();
        filter.setInteriorColor("Black");

        List<Car> blackInteriorCars = carDAO.searchByFilter(filter, filter);
        assertEquals(3, blackInteriorCars.size());
        assertTrue(blackInteriorCars.stream().allMatch(c -> c.getInteriorColor().equals("Black")));
    }

    @Test
    void testsearchByFilterBySeatUpholsteryNoResult() {
        CarFilter filter = new CarFilter();
        filter.setSeatUpholstery("Beige");

        List<Car> beigeUpholsteryCars = carDAO.searchByFilter(filter, filter);
        assertEquals(0, beigeUpholsteryCars.size());
    }

    @Test
    void testsearchByFilterByVinNoResult() {
        CarFilter filter = new CarFilter();
        filter.setVIN("12345");

        List<Car> carsWithVin = carDAO.searchByFilter(filter, filter);
        assertEquals(0, carsWithVin.size());

    }

    @Test
    void testsearchByFilterByIsNew() {
        CarFilter filter = new CarFilter();
        filter.setIsNew(true);

        List<Car> newCars = carDAO.searchByFilter(filter, filter);
        assertEquals(2, newCars.size());
        assertTrue(newCars.stream().allMatch(Car::getIsNew));

        filter.setIsNew(false);
        List<Car> usedCars = carDAO.searchByFilter(filter, filter);
        assertEquals(2, usedCars.size());
        assertTrue(usedCars.stream().noneMatch(Car::getIsNew));
    }

    @Test
    void testsearchByFilterByCombinedFilters() {
        CarFilter filterMin = new CarFilter();
        filterMin.setCost(25000);
        filterMin.setYear(2023);
        filterMin.setStatus(Car.CarStatus.available);
        CarFilter filterMax = new CarFilter();
        filterMax.setCost(32000);
        filterMax.setYear(2023);
        filterMax.setStatus(Car.CarStatus.available);

        List<Car> filteredCars = carDAO.searchByFilter(filterMin, filterMax);
        assertEquals(1, filteredCars.size());
        assertTrue(filteredCars.get(0).getCost() >= 25000 && filteredCars.get(0).getCost() <= 32000);
        assertTrue(filteredCars.get(0).getYear() == 2023);
        assertEquals(Car.CarStatus.available, filteredCars.get(0).getStatus());
    }

    @Test
    void testSearchBranchesSearch() {
        CarFilter filter1 = new CarFilter();
        filter1.setCost(25000);
        filter1.setColor("Red");
        filter1.setInteriorColor("Black");
        CarFilter filter2 = new CarFilter();
        filter2.setYear(2028);
        filter2.setColor("Red");
        filter2.setInteriorColor("Black");

        List<Car> carsInRange = carDAO.searchByFilter(filter1, filter2);
        assertEquals(1, carsInRange.size());
        assertTrue(carsInRange.get(0).getVIN().equals("1HGCM82633A123450"));
    }

    @BeforeEach
    void setUp() {
        Brand toyota = new Brand();
        toyota.setName("Toyota");
        toyota.setCreationYear(1937);
        toyota.setContactPhone("+81-3-3817-1111");
        toyota.setContactEmail("toyota@toyota.com");
        toyota.setCountryCode("JPN");
        toyota.setLogoSrc("/images/toyota.png");
        brandDAO.save(toyota);

        Model camryModel = new Model();
        camryModel.setBrand(toyota);
        camryModel.setName("Camry");
        camryModel.setYear(2023);
        camryModel.setMinCost(26000);
        modelDAO.save(camryModel);

        camryLEConfig = new CarConfiguration();
        camryLEConfig.setModel(camryModel);
        camryLEConfig.setName("LE");
        camryLEConfig.setEngineType("Gasoline");
        camryLEConfig.setEnginePower(203);
        camryLEConfig.setEngineVolume(new BigDecimal("2.5"));
        camryLEConfig.setFuelConsumption(new BigDecimal("7.8"));
        camryLEConfig.setFuelType("Gasoline");
        camryLEConfig.setTankCapacity(new BigDecimal("60"));
        camryLEConfig.setHasCruiseControl(true);
        camryLEConfig.setBasicCost(26000);
        camryLEConfig.setSeatsNumber(5);
        camryLEConfig.setDoorsCount(4);
        camryLEConfig.setTransmissionType("Automatic");
        camryLEConfig.setDriveType("FWD");
        configDAO.save(camryLEConfig);

        camryXLEConfig = new CarConfiguration();
        camryXLEConfig.setModel(camryModel);
        camryXLEConfig.setName("XLE");
        camryXLEConfig.setEngineType("Gasoline");
        camryXLEConfig.setEnginePower(203);
        camryXLEConfig.setEngineVolume(new BigDecimal("2.5"));
        camryXLEConfig.setFuelConsumption(new BigDecimal("7.8"));
        camryXLEConfig.setFuelType("Gasoline");
        camryXLEConfig.setTankCapacity(new BigDecimal("60"));
        camryXLEConfig.setHasCruiseControl(true);
        camryXLEConfig.setBasicCost(31000);
        camryXLEConfig.setSeatsNumber(5);
        camryXLEConfig.setDoorsCount(4);
        camryXLEConfig.setTransmissionType("Automatic");
        camryXLEConfig.setDriveType("FWD");
        camryXLEConfig.setIsBasic(false);
        configDAO.save(camryXLEConfig);

        List<Car> cars = new ArrayList<>();

        Car car1 = new Car();
        car1.setConfiguration(camryLEConfig);
        car1.setVIN("1HGCM82633A123450");
        car1.setCost(26500);
        car1.setColor("Red");
        car1.setMileage(0);
        car1.setInteriorColor("Black");
        car1.setIsNew(true);
        car1.setYear(2023);
        car1.setStatus(Car.CarStatus.available);
        car1.setIsTestDriveAvailable(true);

        Car car2 = new Car();
        car2.setConfiguration(camryLEConfig);
        car2.setVIN("1HGCM82633A123451");
        car2.setCost(27000);
        car2.setColor("White");
        car2.setMileage(15000);
        car2.setInteriorColor("Beige");
        car2.setLastLtoDate(LocalDate.of(2023, 6, 15));
        car2.setIsNew(false);
        car2.setYear(2022);
        car2.setStatus(Car.CarStatus.available);
        car2.setIsTestDriveAvailable(true);

        Car car3 = new Car();
        car3.setConfiguration(camryXLEConfig);
        car3.setVIN("1HGCM82633A123452");
        car3.setCost(31500);
        car3.setColor("Blue");
        car3.setMileage(0);
        car3.setInteriorColor("Black");
        car3.setIsNew(true);
        car3.setYear(2023);
        car3.setStatus(Car.CarStatus.on_test_drive);
        car3.setIsTestDriveAvailable(false);

        Car car4 = new Car();
        car4.setConfiguration(camryXLEConfig);
        car4.setVIN("1HGCM82633A123453");
        car4.setCost(30500);
        car4.setColor("White");
        car4.setMileage(5000);
        car4.setInteriorColor("Black");
        car4.setIsNew(false);
        car4.setYear(2023);
        car4.setStatus(Car.CarStatus.sold);
        car4.setIsTestDriveAvailable(false);

        cars.add(car1);
        cars.add(car2);
        cars.add(car3);
        cars.add(car4);

        carDAO.saveCollection(cars);
    }

    @BeforeAll
    @AfterEach
    void cleanDatabase() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE car RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE car_configuration RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE model RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE brand RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE car_id_seq RESTART WITH 1;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE car_configuration_id_seq RESTART WITH 1;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE model_id_seq RESTART WITH 1;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE brand_id_seq RESTART WITH 1;").executeUpdate();
            session.getTransaction().commit();
        }
    }
}