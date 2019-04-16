package pl.sda;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SpringDemoTarr4ApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void contextLoads() {
		HelloWorldRestController controller = applicationContext
			.getBean(HelloWorldRestController.class);
		Iterable<Greeting> allGreetings = controller.getAllGreetings();
		assertThat(allGreetings).isNotNull();
	}

}
