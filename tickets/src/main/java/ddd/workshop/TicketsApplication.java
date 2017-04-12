package ddd.workshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude=HibernateJpaAutoConfiguration.class)
public class TicketsApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TicketsApplication.class);
		app.setAdditionalProfiles("persistence-hibernate");
		app.run(args);
	}
}
