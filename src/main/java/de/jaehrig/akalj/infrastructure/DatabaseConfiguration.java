package de.jaehrig.akalj.infrastructure;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class DatabaseConfiguration {

    private static final String HIBERNATE_HBM_2_DDL_AUTO = "hibernate.hbm2ddl.auto";
    private static final String HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private final Environment env;

    @Autowired
    DatabaseConfiguration(final Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("driverClassName", ""));
        dataSource.setUrl(env.getProperty("url"));
        dataSource.setUsername(env.getProperty("user"));
        dataSource.setPassword(env.getProperty("password"));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("de.jaehrig.akalj.domain.model");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(additionalProperties());
        return em;
    }

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        if (env.getProperty(HIBERNATE_HBM_2_DDL_AUTO) != null) {
            hibernateProperties.setProperty(HIBERNATE_HBM_2_DDL_AUTO, env.getProperty(HIBERNATE_HBM_2_DDL_AUTO));
        }
        if (env.getProperty(HIBERNATE_DIALECT) != null) {
            hibernateProperties.setProperty(HIBERNATE_DIALECT, env.getProperty(HIBERNATE_DIALECT));
        }
        if (env.getProperty(HIBERNATE_SHOW_SQL) != null) {
            hibernateProperties.setProperty(HIBERNATE_SHOW_SQL, env.getProperty(HIBERNATE_SHOW_SQL));
        }
        hibernateProperties.setProperty("hibernate.id.new_generator_mappings", "true");

        return hibernateProperties;
    }

    @Configuration
    @PropertySource("classpath:persistence-sqlite.properties")
    static class SqliteConfig {

    }
}
