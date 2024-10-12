package subscribe;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
        basePackageClasses = SubscribeApplication.class,
        transactionManagerRef = DataSourceConfig.TRANSACTION_MANAGER,
        entityManagerFactoryRef = DataSourceConfig.ENTITY_MANAGER_FACTORY
)
@EntityScan(basePackageClasses = SubscribeApplication.class)
public class DataSourceConfig {

    public static final String TRANSACTION_MANAGER = "subscribeTransactionManager";
    public static final String ENTITY_MANAGER_FACTORY = "subscribeCouponEntityManagerFactory";
    public static final String PERSIST_UNIT = "subscribe";
    public static final String PROD_DATA_SOURCE = "prodDataSource";
    public static final String LOCK_DATA_SOURCE = "lockDataSource";

    @Bean(name = TRANSACTION_MANAGER)
    PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
            JpaProperties jpaProperties,
            HibernateProperties hibernateProperties
    ) {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), jpaProperties.getProperties(), null)
                .dataSource(prodDataSource())
                .properties(
                        hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(),
                                new HibernateSettings()))
                .persistenceUnit(PERSIST_UNIT)
                .packages(SubscribeApplication.class)
                .build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.jpa")
    public JpaProperties jpaProperties() {
        return new JpaProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.jpa.hibernate")
    public HibernateProperties hibernateProperties() {
        return new HibernateProperties();
    }

    @Primary
    @Bean(name = PROD_DATA_SOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.prod")
    public DataSource prodDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = LOCK_DATA_SOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.lock")
    public DataSource lockDataSource() {
        HikariDataSource hikariDataSource = DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
        hikariDataSource.setMaximumPoolSize(1);

        return hikariDataSource;
    }
}
