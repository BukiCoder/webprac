package ru.msu.cmc.webprak.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprak.models.Manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class ManagerDAOTest {

    @Autowired
    private ManagerDAO managerDAO;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testSimpleManipulations() {
        Collection<Manager> all = managerDAO.getAll();
        assertEquals(2, all.size());

        Manager m1 = managerDAO.getById(1L);
        assertNotNull(m1);
        assertEquals("John Doe", m1.getName());
        assertEquals("john@dealership.com", m1.getEmail());

        Manager nonExistent = managerDAO.getById(999L);
        assertNull(nonExistent);
    }

    @Test
    void testSave() {
        Manager newManager = new Manager();
        newManager.setName("Jane Smith");
        newManager.setEmail("jane@dealership.com");
        newManager.setPhone("+1234567890");
        newManager.setPasswordHash("hashed123");

        managerDAO.save(newManager);

        Manager saved = managerDAO.getById(newManager.getId());
        assertNotNull(saved);
        assertEquals("Jane Smith", saved.getName());

        Collection<Manager> all = managerDAO.getAll();
        assertEquals(3, all.size());
    }

    @Test
    void testUpdate() {
        Manager m = managerDAO.getById(1L);
        m.setName("Johnathan Doe");
        m.setPhone("+9876543210");
        managerDAO.update(m);

        Manager updated = managerDAO.getById(1L);
        assertEquals("Johnathan Doe", updated.getName());
        assertEquals("+9876543210", updated.getPhone());
    }

    @Test
    void testDelete() {
        Manager toDelete = managerDAO.getById(2L);
        managerDAO.delete(toDelete);

        assertNull(managerDAO.getById(2L));
        Collection<Manager> all = managerDAO.getAll();
        assertEquals(1, all.size());
    }

    @Test
    void testDeleteById() {
        managerDAO.deleteById(1L);
        assertNull(managerDAO.getById(1L));
    }

    @Test
    void testSaveCollection() {
        List<Manager> managers = new ArrayList<>();
        Manager m1 = new Manager();
        m1.setName("Alice");
        m1.setEmail("alice@dealership.com");
        m1.setPhone("111");
        m1.setPasswordHash("hash1");

        Manager m2 = new Manager();
        m2.setName("Bob");
        m2.setEmail("bob@dealership.com");
        m2.setPhone("222");
        m2.setPasswordHash("hash2");

        managers.add(m1);
        managers.add(m2);
        managerDAO.saveCollection(managers);

        Collection<Manager> all = managerDAO.getAll();
        assertEquals(4, all.size());
    }

    @BeforeEach
    void setUp() {
        List<Manager> managers = new ArrayList<>();

        Manager m1 = new Manager();
        m1.setName("John Doe");
        m1.setEmail("john@dealership.com");
        m1.setPhone("+123456789");
        m1.setPasswordHash("secret1");

        Manager m2 = new Manager();
        m2.setName("Sarah Connor");
        m2.setEmail("sarah@dealership.com");
        m2.setPhone("+987654321");
        m2.setPasswordHash("secret2");

        managers.add(m1);
        managers.add(m2);
        managerDAO.saveCollection(managers);
    }

    @BeforeAll
    @AfterEach
    void cleanDatabase() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE manager RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE manager_id_seq RESTART WITH 1;").executeUpdate();
            session.getTransaction().commit();
        }
    }
}