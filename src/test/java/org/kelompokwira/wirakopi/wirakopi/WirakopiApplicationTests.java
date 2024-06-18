package org.kelompokwira.wirakopi.wirakopi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ContextConfiguration
class WirakopiApplicationTests {
    @Autowired
    private MockMvc mvc;

    @Test
	void contextLoads() {

	}
}
