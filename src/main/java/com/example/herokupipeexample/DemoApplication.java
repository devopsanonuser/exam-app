package com.example.herokupipeexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Primary;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class DemoApplication {

	@Bean
  @Primary
  // https://devcenter.heroku.com/articles/connecting-to-relational-databases-on-heroku-with-java
  public BasicDataSource dataSource() throws URISyntaxException {
      URI dbUri = new URI(System.getenv("DATABASE_URL"));

      String username = dbUri.getUserInfo().split(":")[0];
      String password = dbUri.getUserInfo().split(":")[1];
      String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

      BasicDataSource basicDataSource = new BasicDataSource();
      basicDataSource.setUrl(dbUrl);
      basicDataSource.setUsername(username);
      basicDataSource.setPassword(password);

      System.out.println("####VAR####");
      System.out.println(dbUrl);

      return basicDataSource;
  }

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
