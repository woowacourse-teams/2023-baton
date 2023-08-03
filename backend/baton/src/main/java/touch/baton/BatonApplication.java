package touch.baton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class BatonApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatonApplication.class, args);
	}

}
