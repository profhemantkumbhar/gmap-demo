package com.test.TestApp;

import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(0)
public class GmapAppConfiguration {
	String env;

	public GmapAppConfiguration() {
	System.out.println("tomcat config :");
		// TODO Auto-generated constructor stub
	}
	
	@Bean
	public TomcatServletWebServerFactory tomcatFactory() {
		return new TomcatServletWebServerFactory() {
			@Override
			protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
				// TODO Auto-generated method stub
				tomcat.enableNaming();
				 
				return super.getTomcatWebServer(tomcat);
			}

			@Override
			protected void postProcessContext(Context context) {
				ContextResource resource = context.getNamingResources().findResource("jdbc/FetchFormsDB");
				System.out.println("Server Port : " + context.getBaseName());
				
				if (resource == null) {
					resource = new ContextResource();

					resource.setType(DataSource.class.getName());
					resource.setName("jdbc/FetchFormsDB");
					resource.setProperty("factory", "org.apache.tomcat.jdbc.pool.DataSourceFactory");
					resource.setProperty("driverClassName", "com.mysql.jdbc.Driver");
					
				
					resource.setProperty("url",
						"jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false");
					resource.setProperty("username", "root");
					resource.setProperty("password", "mysql");
				
					context.getNamingResources().addResource(resource);
					System.out.println("JNDI added in tomcat : " +resource.getName());
				}else {
					System.out.println("JNDI already in tomcat : " +resource.getName());
				}
			}
		};
	}
}