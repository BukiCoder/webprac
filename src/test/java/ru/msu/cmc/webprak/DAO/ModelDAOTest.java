package ru.msu.cmc.webprak.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprak.filters.ModelFilter;
import ru.msu.cmc.webprak.models.Brand;
import ru.msu.cmc.webprak.models.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class ModelDAOTest {

    @Autowired
    private ModelDAO modelDAO;
    @Autowired
    private BrandDAO brandDAO;
    @Autowired
    private SessionFactory sessionFactory;

    private Brand toyota;
    private Brand honda;

    @Test
    void testGetModelListByBrand() {
        List<Model> toyotaModels = modelDAO.getModelListByBrand(toyota);
        assertEquals(3, toyotaModels.size());

        List<Model> hondaModels = modelDAO.getModelListByBrand(honda);
        assertEquals(2, hondaModels.size());

        assertTrue(toyotaModels.stream().allMatch(m -> m.getBrand().getId().equals(toyota.getId())));
    }

    @Test
    void testsearchByFilter() {
        ModelFilter filtermin = new ModelFilter();
        filtermin.setYear(2020);
        ModelFilter filtermax = new ModelFilter();
        filtermax.setYear(2022);

        List<Model> recentModels = modelDAO.searchByFilter(filtermin, filtermax);
        assertEquals(3, recentModels.size());
        assertTrue(recentModels.stream().allMatch(m -> m.getYear() >= 2020 && m.getYear() <= 2022));
    }

    @Test
    void testsearchByFilterByName() {
        ModelFilter filter = new ModelFilter();
        filter.setName("Camry");

        List<Model> models = modelDAO.searchByFilter(filter, filter);
        assertEquals(1, models.size());
        assertEquals("Camry", models.get(0).getName());
    }

    @Test
    void testsearchByFilterByPriceRange() {
        ModelFilter maxfilter = new ModelFilter();
        maxfilter.setMinCost(25000);
        ModelFilter minfilter = new ModelFilter();
        minfilter.setMinCost(40000);

        List<Model> models = modelDAO.searchByFilter(maxfilter, minfilter);
        assertEquals(3, models.size());
        assertTrue(models.stream().allMatch(m -> m.getMinCost() >= 25000 && m.getMinCost() <= 40000));
    }

    @Test
    void testSaveModel() {
        Model newModel = new Model();
        newModel.setBrand(toyota);
        newModel.setName("Supra");
        newModel.setYear(2023);
        newModel.setMinCost(45000);
        newModel.setImageSrc("/images/supra.png");

        modelDAO.save(newModel);

        Model savedModel = modelDAO.getById(newModel.getId());
        assertNotNull(savedModel);
        assertEquals("Supra", savedModel.getName());
        assertEquals(2023, savedModel.getYear());
        assertEquals(toyota.getId(), savedModel.getBrand().getId());

        List<Model> toyotaModels = modelDAO.getModelListByBrand(toyota);
        assertEquals(4, toyotaModels.size());
    }

    @Test
    void testUpdateModel() {
        Model model = modelDAO.getModelListByBrand(toyota).get(0);
        model.setMinCost(28000);
        model.setYear(2024);
        modelDAO.update(model);

        Model updatedModel = modelDAO.getById(model.getId());
        assertEquals(28000, updatedModel.getMinCost());
        assertEquals(2024, updatedModel.getYear());
    }

    @Test
    void testDeleteModel() {
        List<Model> toyotaModels = modelDAO.getModelListByBrand(toyota);
        Model modelToDelete = toyotaModels.get(0);
        Long modelId = modelToDelete.getId();

        modelDAO.delete(modelToDelete);

        Model deletedModel = modelDAO.getById(modelId);
        assertNull(deletedModel);

        List<Model> remainingModels = modelDAO.getModelListByBrand(toyota);
        assertEquals(2, remainingModels.size());
    }

    @Test
    void testsearchByFilterByYearRange() {
        ModelFilter filterMin = new ModelFilter();
        filterMin.setYear(2022);
        ModelFilter filterMax = new ModelFilter();
        filterMax.setYear(2023);

        List<Model> models = modelDAO.searchByFilter(filterMin, filterMax);
        assertEquals(4, models.size());
        assertTrue(models.stream().allMatch(m -> m.getYear() >= 2022 && m.getYear() <= 2023));
    }

    @Test
    void testsearchByFilterByMinCost() {
        ModelFilter filter = new ModelFilter();
        filter.setMinCost(22000);

        List<Model> models = modelDAO.searchByFilter(filter, filter);
        assertEquals(1, models.size());

    }

    @Test
    void testsearchByFilterCombinedFilters() {
        ModelFilter filterMin = new ModelFilter();
        filterMin.setMinCost(25000);
        filterMin.setYear(2022);
        ModelFilter filterMax = new ModelFilter();
        filterMax.setMinCost(35000);
        filterMax.setYear(2023);

        List<Model> models = modelDAO.searchByFilter(filterMin, filterMax);
        assertEquals(3, models.size());
        assertTrue(models.stream().allMatch(m -> m.getMinCost() >= 25000 && m.getMinCost() <= 35000
                && m.getYear() >= 2022 && m.getYear() <= 2023));
    }

    @BeforeEach
    void setUp() {
        toyota = new Brand();
        toyota.setName("Toyota");
        toyota.setCreationYear(1937);
        toyota.setContactPhone("+81-3-3817-1111");
        toyota.setContactEmail("toyota@toyota.com");
        toyota.setCountryCode("JPN");
        toyota.setLogoSrc("/images/toyota.png");
        brandDAO.save(toyota);

        honda = new Brand();
        honda.setName("Honda");
        honda.setCreationYear(1948);
        honda.setContactPhone("+81-3-3423-1111");
        honda.setContactEmail("honda@honda.com");
        honda.setCountryCode("JPN");
        honda.setLogoSrc("/images/honda.png");
        brandDAO.save(honda);

        List<Model> models = new ArrayList<>();

        Model camry = new Model();
        camry.setBrand(toyota);
        camry.setName("Camry");
        camry.setYear(2023);
        camry.setMinCost(28000);
        camry.setImageSrc("/images/camry.png");

        Model rav4 = new Model();
        rav4.setBrand(toyota);
        rav4.setName("RAV4");
        rav4.setYear(2022);
        rav4.setMinCost(30000);
        rav4.setImageSrc("/images/rav4.png");

        Model corolla = new Model();
        corolla.setBrand(toyota);
        corolla.setName("Corolla");
        corolla.setYear(2021);
        corolla.setMinCost(22000);
        corolla.setImageSrc("/images/corolla.png");

        Model accord = new Model();
        accord.setBrand(honda);
        accord.setName("Accord");
        accord.setYear(2023);
        accord.setMinCost(27000);
        accord.setImageSrc("/images/accord.png");

        Model civic = new Model();
        civic.setBrand(honda);
        civic.setName("Civic");
        civic.setYear(2022);
        civic.setMinCost(24000);
        civic.setImageSrc("/images/civic.png");

        models.add(camry);
        models.add(rav4);
        models.add(corolla);
        models.add(accord);
        models.add(civic);

        modelDAO.saveCollection(models);
    }

    @BeforeAll
    @AfterEach
    void cleanDatabase() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE model RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE brand RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE model_id_seq RESTART WITH 1;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE brand_id_seq RESTART WITH 1;").executeUpdate();
            session.getTransaction().commit();
        }
    }
}