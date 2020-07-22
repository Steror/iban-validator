package ibanvalidator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IbanValidatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(IbanValidatorApplication.class, args);
		System.setProperty("java.awt.headless", "false");
		Panel panel = new Panel(new Validator());
		panel.createPanel();
	}
}
