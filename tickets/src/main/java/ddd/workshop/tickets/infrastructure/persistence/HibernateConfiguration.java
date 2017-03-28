package ddd.workshop.tickets.infrastructure.persistence;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement(order = 0)
@Profile("persistence-hibernate")
public class HibernateConfiguration {

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMappingResources("ddd/workshop/tickets/infrastructure/persistence/mapping.hbm.xml");
        sessionFactory.setHibernateProperties(loadProperties());
        return sessionFactory;
    }

    private Properties loadProperties() {
        try {
            Properties properties = new Properties();
            properties.load(HibernateConfiguration.class.getResourceAsStream("hibernate.properties"));
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Could not load Hibernate properties", e);
        }
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {

        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "prototype")
    public Session session(SessionFactory factory) {
        return factory.getCurrentSession();
    }

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "prototype")
    public SessionImplementor sessionImplementor(SessionFactory factory) {
        return (SessionImplementor) session(factory);
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

}