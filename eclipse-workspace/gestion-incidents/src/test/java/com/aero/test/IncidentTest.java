package com.aero.test;

import com.aero.model.Incident;
import com.aero.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IncidentTest {

    private Session session;

    @BeforeEach
    public void setup() {
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
    }

    @AfterEach
    public void tearDown() {
        session.getTransaction().commit();
        session.close();
    }
	@Test
    public void testInsertIncident() {
        Incident incident = new Incident("Test moteur", "Grave", "Piste A", "Signalé");
        session.persist(incident);
        assertTrue(incident.getId() > 0);
    }
}
