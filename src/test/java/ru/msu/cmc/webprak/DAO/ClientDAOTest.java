package ru.msu.cmc.webprak.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprak.filters.ClientFilter;
import ru.msu.cmc.webprak.models.Client;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class ClientDAOTest {

    @Autowired
    private ClientDAO clientDAO;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testGetByEmail() {
        Client client = clientDAO.getClientByEmail("john@example.com");
        assertNotNull(client);
        assertEquals("John", client.getName());

        assertNull(clientDAO.getClientByEmail("nonexistent@example.com"));
    }

    @Test
    void testGetByPhone() {
        Client client = clientDAO.getClientByPhone("+123456789");
        assertNotNull(client);
        assertEquals("John", client.getName());

        assertNull(clientDAO.getClientByPhone("+000000000"));
    }

    @Test
    void testSearchClients() {
        ClientFilter filter = new ClientFilter();
        filter.setName("John");
        List<Client> results = clientDAO.serchClients(filter);
        assertEquals(1, results.size());
        assertEquals("John", results.get(0).getName());

        filter.setEmail("john");
        results = clientDAO.serchClients(filter);
        assertEquals(1, results.size());
        assertEquals("John", results.get(0).getName());

        filter.setPhone("999");
        results = clientDAO.serchClients(filter);
        assertTrue(results.isEmpty());
    }

    @Test
    void testSave() {
        Client newClient = new Client();
        newClient.setName("Test User");
        newClient.setEmail("test@example.com");
        newClient.setAddress("Test Address");
        newClient.setPhone("+999999999");
        newClient.setPasswordHash("hash");

        clientDAO.save(newClient);

        Client saved = clientDAO.getById(newClient.getId());
        assertNotNull(saved);
        assertEquals("Test User", saved.getName());

        Collection<Client> all = clientDAO.getAll();
        assertEquals(3, all.size());
    }

    @Test
    void testUpdate() {
        Client client = clientDAO.getById(1L);
        client.setName("Updated Name");
        client.setAddress("New Address");
        clientDAO.update(client);

        Client updated = clientDAO.getById(1L);
        assertEquals("Updated Name", updated.getName());
        assertEquals("New Address", updated.getAddress());
    }

    @Test
    void testDelete() {
        Client toDelete = clientDAO.getById(2L);
        clientDAO.delete(toDelete);

        assertNull(clientDAO.getById(2L));
        assertEquals(1, clientDAO.getAll().size());
    }

    @Test
    void testSaveCollection() {
        List<Client> clients = new ArrayList<>();
        Client c1 = new Client();
        c1.setName("Alice");
        c1.setEmail("alice@example.com");
        c1.setAddress("Street 1");
        c1.setPhone("111");
        c1.setPasswordHash("hash1");

        Client c2 = new Client();
        c2.setName("Bob");
        c2.setEmail("bob@example.com");
        c2.setAddress("Street 2");
        c2.setPhone("222");
        c2.setPasswordHash("hash2");

        clients.add(c1);
        clients.add(c2);
        clientDAO.saveCollection(clients);

        assertEquals(4, clientDAO.getAll().size());
    }

    @Test
    void testGetClientsByRegistrationDate() {
        ClientFilter filter1 = new ClientFilter();
        filter1.setRegistrationDate(LocalDate.now());
        ClientFilter filter2 = new ClientFilter();
        List<Client> results = clientDAO.searchByFilter(filter1, filter2);
        assertEquals(2, results.size());
    }

    @BeforeEach
    void setUp() {
        List<Client> clients = new ArrayList<>();

        Client john = new Client();
        john.setName("John");
        john.setEmail("john@example.com");
        john.setAddress("123 Main St");
        john.setPhone("+123456789");
        john.setPasswordHash("hash1");
        john.setRegistrationDate(LocalDate.now());

        Client jane = new Client();
        jane.setName("Jane");
        jane.setEmail("jane@example.com");
        jane.setAddress("456 Oak Ave");
        jane.setPhone("+987654321");
        jane.setPasswordHash("hash2");
        jane.setRegistrationDate(LocalDate.now());

        clients.add(john);
        clients.add(jane);
        clientDAO.saveCollection(clients);
    }
    @Test
    void testSearchByFilter_BothNull() {
        List<Client> clients = clientDAO.searchByFilter(null, null);
        assertEquals(2, clients.size());
    }

    @Test
    void testSearchByFilter_Equal() {
        ClientFilter minFilter = new ClientFilter();
        minFilter.setName("John");
        ClientFilter maxFilter = new ClientFilter();
        maxFilter.setName("John");

        List<Client> clients = clientDAO.searchByFilter(minFilter, maxFilter);
        assertEquals(1, clients.size());
        assertEquals("John", clients.get(0).getName());
    }

    @Test
    void testSearchByFilter_RangeDates() {
        ClientFilter minFilter = new ClientFilter();
        minFilter.setRegistrationDate(LocalDate.now().minusDays(1));
        ClientFilter maxFilter = new ClientFilter();
        maxFilter.setRegistrationDate(LocalDate.now().plusDays(1));

        List<Client> clients = clientDAO.searchByFilter(minFilter, maxFilter);
        assertEquals(2, clients.size());
        assertTrue(clients.stream().allMatch(c ->
                c.getRegistrationDate().isAfter(LocalDate.now().minusDays(2)) &&
                        c.getRegistrationDate().isBefore(LocalDate.now().plusDays(2))));
    }

    @Test
    void testSearchByFilter_OnlyMin() {
        ClientFilter minFilter = new ClientFilter();
        minFilter.setRegistrationDate(LocalDate.now().minusDays(1));
        ClientFilter maxFilter = new ClientFilter();

        List<Client> clients = clientDAO.searchByFilter(minFilter, maxFilter);
        assertEquals(2, clients.size());
        assertTrue(clients.stream().allMatch(c ->
                c.getRegistrationDate().isAfter(LocalDate.now().minusDays(2))));
    }

    @Test
    void testSearchByFilter_OnlyMax() {
        ClientFilter minFilter = new ClientFilter();
        ClientFilter maxFilter = new ClientFilter();
        maxFilter.setRegistrationDate(LocalDate.now().plusDays(1));

        List<Client> clients = clientDAO.searchByFilter(minFilter, maxFilter);
        assertEquals(2, clients.size());
        assertTrue(clients.stream().allMatch(c ->
                c.getRegistrationDate().isBefore(LocalDate.now().plusDays(2))));
    }

    @Test
    void testSearchByFilter_StringEqual() {
        ClientFilter minFilter = new ClientFilter();
        minFilter.setEmail("john@example.com");
        ClientFilter maxFilter = new ClientFilter();
        maxFilter.setEmail("john@example.com");

        List<Client> clients = clientDAO.searchByFilter(minFilter, maxFilter);
        assertEquals(1, clients.size());
        assertEquals("John", clients.get(0).getName());
    }

    @Test
    void testSearchByFilter_StringRange() {
        ClientFilter minFilter = new ClientFilter();
        minFilter.setName("A");
        ClientFilter maxFilter = new ClientFilter();
        maxFilter.setName("Z");

        List<Client> clients = clientDAO.searchByFilter(minFilter, maxFilter);
        assertEquals(2, clients.size());
    }

    @Test
    void testSearchByFilter_NoFields_ButFiltersNotNull() {
        ClientFilter minFilter = new ClientFilter();
        ClientFilter maxFilter = new ClientFilter();

        List<Client> clients = clientDAO.searchByFilter(minFilter, maxFilter);
        assertEquals(2, clients.size());
    }

    @Test
    void testSearchByFilter_CombinedFields() {
        ClientFilter minFilter = new ClientFilter();
        minFilter.setName("John");
        minFilter.setRegistrationDate(LocalDate.now().minusDays(1));
        ClientFilter maxFilter = new ClientFilter();
        maxFilter.setName("John");
        maxFilter.setRegistrationDate(LocalDate.now().plusDays(1));

        List<Client> clients = clientDAO.searchByFilter(minFilter, maxFilter);
        assertEquals(1, clients.size());
        assertEquals("John", clients.get(0).getName());
    }
    @Test
    void testSearchClientsByNamePattern() {
        ClientFilter filter = new ClientFilter();
        filter.setName("Joh");

        List<Client> results = clientDAO.serchClients(filter);
        assertEquals(1, results.size());
        assertEquals("John", results.get(0).getName());

        filter.setName("Jane");
        results = clientDAO.serchClients(filter);
        assertEquals(1, results.size());
        assertEquals("Jane", results.get(0).getName());
    }

    @Test
    void testSearchClientsByEmailPattern() {
        ClientFilter filter = new ClientFilter();
        filter.setEmail("john");

        List<Client> results = clientDAO.serchClients(filter);
        assertEquals(1, results.size());
        assertTrue(results.get(0).getEmail().contains("john"));

        filter.setEmail("jane");
        results = clientDAO.serchClients(filter);
        assertEquals(1, results.size());
        assertTrue(results.get(0).getEmail().contains("jane"));
    }

    @Test
    void testSearchClientsByPhonePattern() {
        ClientFilter filter = new ClientFilter();
        filter.setPhone("123");

        List<Client> results = clientDAO.serchClients(filter);
        assertEquals(1, results.size());
        assertTrue(results.get(0).getPhone().contains("123"));
    }

    @Test
    void testSearchClientsByAddressPattern() {
        ClientFilter filter = new ClientFilter();
        filter.setAddress("Main");

        List<Client> results = clientDAO.serchClients(filter);
        assertEquals(0, results.size());
    }

    @Test
    void testSearchClientsByCombinedFilters() {
        ClientFilter filter = new ClientFilter();
        filter.setName("John");
        filter.setEmail("john");
        filter.setAddress("123 Main St");

        List<Client> results = clientDAO.serchClients(filter);
        assertEquals(1, results.size());
        assertEquals("John", results.get(0).getName());
    }

    @Test
    void testSearchClientsWithNoResults() {
        ClientFilter filter = new ClientFilter();
        filter.setName("NonExistent");
        filter.setEmail("nonexistent");

        List<Client> results = clientDAO.serchClients(filter);
        assertTrue(results.isEmpty());
    }

    @Test
    void testGetClientsByEmailNoResults() {
        Client results = clientDAO.getClientByEmail("NonExistent");
        assertNull(results);
    }

    @Test
    void testGetClientsByPhoneNoResults() {
        Client results = clientDAO.getClientByPhone("NonExistent");
        assertNull(results);
    }

    @BeforeAll
    @AfterEach
    void cleanDatabase() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE client RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE client_id_seq RESTART WITH 1;").executeUpdate();
            session.getTransaction().commit();
        }
    }
}