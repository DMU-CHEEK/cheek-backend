package dmu.cheek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing //JPA Auditing 활성화
@EnableScheduling
@SpringBootApplication
public class CheekApplication {

	public static void main(String[] args) {
		SpringApplication.run(CheekApplication.class, args);
	}

}
