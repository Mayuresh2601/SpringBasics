package com.bridgelabz.fundoonotes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

@SpringBootTest
@PropertySource("classpath:message.properties")
class FundooNotesApplicationTests{

	@Test
	void contextLoads() {
	}

}
