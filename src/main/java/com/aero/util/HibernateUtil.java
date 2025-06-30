package com.aero.util;

import com.aero.model.Incident;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration config = new Configuration();

            config.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            config.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/incident_db");
            config.setProperty("hibernate.connection.username", "root");
            config.setProperty("hibernate.connection.password", "Root..1234");

            config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            config.setProperty("hibernate.hbm2ddl.auto", "update");
            config.setProperty("hibernate.show_sql", "true");
            config.setProperty("hibernate.format_sql", "true");

            config.addAnnotatedClass(Incident.class);

            sessionFactory = config.buildSessionFactory(
                new StandardServiceRegistryBuilder()
                    .applySettings(config.getProperties())
                    .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
