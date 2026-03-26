package ru.msu.cmc.webprak.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprak.models.Brand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class BrandDAOTest {

    @Autowired
    private BrandDAO brandDAO;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testSimpleManipulations() {
        Collection<Brand> allBrands = brandDAO.getAll();
        assertEquals(3, allBrands.size());

        Brand toyota = brandDAO.getById(1L);
        assertNotNull(toyota);
        assertEquals("Toyota", toyota.getName());
        assertEquals("JPN", toyota.getCountryCode());

        Brand nonExistent = brandDAO.getById(999L);
        assertNull(nonExistent);
    }

    @Test
    void testSave() {
        Brand newBrand = new Brand();
        newBrand.setName("Tesla");
        newBrand.setCreationYear(2003);
        newBrand.setContactPhone("+1-650-681-5000");
        newBrand.setContactEmail("info@tesla.com");
        newBrand.setDescription("American electric vehicle manufacturer");
        newBrand.setAddress("Palo Alto, California");
        newBrand.setCountryCode("USA");
        newBrand.setLogoSrc("/images/tesla.png");

        brandDAO.save(newBrand);

        Brand savedBrand = brandDAO.getById(newBrand.getId());
        assertNotNull(savedBrand);
        assertEquals("Tesla", savedBrand.getName());
        assertEquals("USA", savedBrand.getCountryCode());

        Collection<Brand> allBrands = brandDAO.getAll();
        assertEquals(4, allBrands.size());
    }

    @Test
    void testUpdate() {
        Brand brand = brandDAO.getById(1L);
        brand.setName("Toyota Motor Corporation");
        brand.setContactEmail("contact@toyota.com");
        brandDAO.update(brand);

        Brand updatedBrand = brandDAO.getById(1L);
        assertEquals("Toyota Motor Corporation", updatedBrand.getName());
        assertEquals("contact@toyota.com", updatedBrand.getContactEmail());
    }

    @Test
    void testDelete() {
        Brand brandToDelete = brandDAO.getById(3L);
        assertNotNull(brandToDelete);

        brandDAO.delete(brandToDelete);

        Brand deletedBrand = brandDAO.getById(3L);
        assertNull(deletedBrand);

        Collection<Brand> allBrands = brandDAO.getAll();
        assertEquals(2, allBrands.size());
    }

    @Test
    void testDeleteById() {
        brandDAO.deleteById(2L);

        Brand deletedBrand = brandDAO.getById(2L);
        assertNull(deletedBrand);

        Collection<Brand> allBrands = brandDAO.getAll();
        assertEquals(2, allBrands.size());
    }
    @Test
    void testFindBrandsByName_WithValidName_NoResults() {
        List<Brand> brands = brandDAO.findBrandsByName("NonExistentBrand");
        assertNull(brands);
    }

    @Test
    void testSaveCollection() {
        List<Brand> brands = new ArrayList<>();

        Brand bmw = new Brand();
        bmw.setName("BMW");
        bmw.setCreationYear(1916);
        bmw.setContactPhone("+49-89-382-0");
        bmw.setContactEmail("info@bmw.com");
        bmw.setCountryCode("DEU");
        bmw.setLogoSrc("/images/bmw.png");

        Brand audi = new Brand();
        audi.setName("Audi");
        audi.setCreationYear(1909);
        audi.setContactPhone("+49-841-89-0");
        audi.setContactEmail("info@audi.com");
        audi.setCountryCode("DEU");
        audi.setLogoSrc("/images/audi.png");

        brands.add(bmw);
        brands.add(audi);

        brandDAO.saveCollection(brands);

        Collection<Brand> allBrands = brandDAO.getAll();
        assertEquals(5, allBrands.size());
    }
    @Test
    void testFindBrandsByNameExactMatch() {
        List<Brand> brands = brandDAO.findBrandsByName("Toyota");
        assertNotNull(brands);
        assertEquals(1, brands.size());
        assertEquals("Toyota", brands.get(0).getName());
        assertEquals("JPN", brands.get(0).getCountryCode());
    }

    @Test
    void testFindBrandsByNamePartialMatch() {
        List<Brand> brands = brandDAO.findBrandsByName("Toy");
        assertNotNull(brands);
        assertEquals(1, brands.size());
        assertEquals("Toyota", brands.get(0).getName());
    }

    @BeforeEach
    void setUp() {
        List<Brand> brands = new ArrayList<>();

        Brand toyota = new Brand();
        toyota.setName("Toyota");
        toyota.setCreationYear(1937);
        toyota.setContactPhone("+81-3-3817-1111");
        toyota.setContactEmail("toyota@toyota.com");
        toyota.setDescription("Japanese automotive manufacturer");
        toyota.setAddress("Toyota City, Aichi");
        toyota.setCountryCode("JPN");
        toyota.setLogoSrc("/images/toyota.png");

        Brand honda = new Brand();
        honda.setName("Honda");
        honda.setCreationYear(1948);
        honda.setContactPhone("+81-3-3423-1111");
        honda.setContactEmail("honda@honda.com");
        honda.setDescription("Japanese automobile and motorcycle manufacturer");
        honda.setAddress("Tokyo, Japan");
        honda.setCountryCode("JPN");
        honda.setLogoSrc("/images/honda.png");

        Brand ford = new Brand();
        ford.setName("Ford");
        ford.setCreationYear(1903);
        ford.setContactPhone("+1-800-392-3673");
        ford.setContactEmail("ford@ford.com");
        ford.setDescription("American automaker");
        ford.setAddress("Dearborn, Michigan");
        ford.setCountryCode("USA");
        ford.setLogoSrc("/images/ford.png");

        brands.add(toyota);
        brands.add(honda);
        brands.add(ford);

        brandDAO.saveCollection(brands);
    }

    @BeforeAll
    @AfterEach
    void cleanDatabase() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE brand RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE brand_id_seq RESTART WITH 1;").executeUpdate();
            session.getTransaction().commit();
        }
    }
}