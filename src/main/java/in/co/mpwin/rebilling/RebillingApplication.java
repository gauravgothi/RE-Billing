package in.co.mpwin.rebilling;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class RebillingApplication {

	//private static final Logger logger = LoggerFactory.getLogger(RebillingApplication.class);
	@Bean
	public ModelMapper modelMapper()	{
		return new ModelMapper();
	}

	public static void main(String[] args) {

		SpringApplication.run(RebillingApplication.class, args);
		//logger.info("Project REBilling have been started");
	}

}
