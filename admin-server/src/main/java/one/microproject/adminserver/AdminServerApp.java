package one.microproject.adminserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import de.codecentric.boot.admin.server.config.EnableAdminServer;

@EnableAdminServer
@SpringBootApplication
public class AdminServerApp {

	public static void main(String[] args) {
		SpringApplication.run(AdminServerApp.class, args);
	}

}
