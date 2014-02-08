package medizin.server.spring;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import medizin.server.domain.Answer;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@ComponentScan(basePackages = "ch.unibas.medizin.gwt",
	excludeFilters = {@ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION),
	@ComponentScan.Filter(pattern = ".*_Roo_.*", type = FilterType.REGEX)})
@EnableSpringConfigured
public class ApplicationConfig {
	
	@Bean
    public DataSource dataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("org.h2.Driver");
            dataSource.setUrl("jdbc:h2:~/tmp/bme");
            dataSource.setUsername("sa");
            dataSource.setPassword("");

            return dataSource;
    }

    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactory() {
            HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            vendorAdapter.setDatabase(Database.H2);

            LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

            Map<String, String> jpaPropertyMap = new HashMap<String, String>();
            jpaPropertyMap.put("hibernate.hbm2ddl.auto", "create");
            jpaPropertyMap.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

            factory.setJpaPropertyMap(jpaPropertyMap);
            factory.setJpaVendorAdapter(vendorAdapter);
            factory.setPackagesToScan(Answer.class.getPackage().getName());
            factory.setDataSource(dataSource());

            factory.afterPropertiesSet();

            return factory.getObject();
    }

    @Bean
    public JpaDialect jpaDialect() {
            return new HibernateJpaDialect();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
            JpaTransactionManager txManager = new JpaTransactionManager();
            txManager.setEntityManagerFactory(entityManagerFactory());
            return txManager;
    }
    
    @Bean
    public JavaMailSender javaMailSender() {
    	JavaMailSender javaMailSender = new JavaMailSenderImpl();
    	
    	return javaMailSender;
    }

}
