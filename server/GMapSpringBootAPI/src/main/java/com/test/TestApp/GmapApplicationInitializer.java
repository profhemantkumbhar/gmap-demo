package com.test.TestApp;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ServletComponentScan
@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableWebMvc
@ComponentScan("com.test")
public class GmapApplicationInitializer  implements WebMvcConfigurer {

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipart = new CommonsMultipartResolver();
		multipart.setMaxUploadSize(100000000);
		return multipart;
	}
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

	@Override
	@Order(1)
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

		configurer.favorPathExtension(false).favorParameter(true).parameterName("mediaType").ignoreAcceptHeader(true)
				.useJaf(false).defaultContentType(MediaType.APPLICATION_JSON)
				.mediaType("xml", MediaType.APPLICATION_XML).mediaType("json", MediaType.APPLICATION_JSON)
				.mediaType("html", MediaType.TEXT_HTML).mediaType("pdf", MediaType.APPLICATION_PDF).mediaType("xlsx",
						MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
 
	}
	@Bean
	public DataSource jndiDataSource() throws IllegalArgumentException, NamingException {
		JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
		bean.setJndiName("java:/comp/env/jdbc/FetchFormsDB");
		bean.setProxyInterface(DataSource.class);
		bean.setLookupOnStartup(false);
		bean.afterPropertiesSet();
		DataSource ds = (DataSource) bean.getObject();
		return ds;
 
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		try {
			return new JdbcTemplate(jndiDataSource());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}

	@Bean(name = "transactionManager")
	public PlatformTransactionManager txManager() {
		try {
			DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(jndiDataSource());
			return transactionManager;
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return null;
	}

	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
		try {
			return new NamedParameterJdbcTemplate(jndiDataSource());
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String... args) {	
		SpringApplication.run(GmapApplicationInitializer.class, args);
	}
}
