package pl.sda;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
// czysci kontekst springa przed kazdym wywolaniem testu (daje calkowita
// izolacje, ale trwa dlugo)
// @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class HelloWorldControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private GreetingsRepository greetingsRepository;

	@BeforeEach
	void cleanDb() {
		greetingsRepository.deleteAll();
	}

	@DisplayName("when call GET on /greetings, then 200 status is " +
		     "returned and empty json array")
	@Test
	void test0() throws Exception {
		// when
		mockMvc.perform(get("/greetings"))

			// then
			.andExpect(status().isOk())
			.andExpect(content().string("[]"));
	}

	// @formatter:off
	@DisplayName(
		"given one message in the system, " +
		"when call GET on greetings, " +
		"then 200 status and json array of size 1 is returned"
	)
	// @formatter:on
	@Test
	void test1() throws Exception {
		// given
		postGreetingWithMessage("hello");

		// when
		mockMvc.perform(get("/greetings"))
			.andDo(MockMvcResultHandlers.print())

			// then
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].msg", is("hello")));
	}

	// @formatter:off
	@DisplayName(
		"given two greetings in the database, " +
		"when call GET on single greeting endpoint, " +
		"then status OK and matching greeting is returned"
	)
	// @formatter:on
	@Test
	void test2() throws Exception {
		// given
		postGreetingWithMessage("hello");
		int hiGreetingId = postGreetingWithMessage("hi");

		// when
		mockMvc.perform(get("/greetings/{id}", hiGreetingId))

			// then
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.msg", is("hi")))
			.andExpect(jsonPath("$.id", is(hiGreetingId)));
	}

	// @formatter:off
	@DisplayName(
		"given two greetings in the database, " +
		"when call DELETE on a single greeting endpoint, " +
		"then status is OK and matching greeting is removed"
	)
	// @formatter:on
	@Test
	void test3() throws Exception {
		// given
		int helloGreetingId = postGreetingWithMessage("hello");
		postGreetingWithMessage("hi");

		// when
		mockMvc.perform(delete("/greetings/{id}", helloGreetingId))

			// then
			.andExpect(status().isOk());
		mockMvc.perform(get("/greetings"))
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].msg", is("hi")));

	}

	private int postGreetingWithMessage(String msg) throws Exception {
		String greeting = String.format("{\"msg\": \"%s\"}", msg);
		String responseBodyAsJson = mockMvc.perform(
			// @formatter:off
			post("/greetings")
			.contentType(MediaType.APPLICATION_JSON)
			.content(greeting)
			// @formatter:on
		).andExpect(status().isOk()).andReturn().getResponse()
			.getContentAsString();
		return (int) objectMapper
			.readValue(responseBodyAsJson, Greeting.class).getId();
	}
}
