package pl.sda;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/greetings")
public class HelloWorldRestController {

	private GreetingsRepository repository;

	public HelloWorldRestController(GreetingsRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public Iterable<Greeting> getAllGreetings() {
		return repository.findAll();
	}

	@PostMapping
	public Greeting addGreeting(@RequestBody Greeting greeting) {
		return repository.save(greeting);
	}

	@DeleteMapping("/{id}")
	public void deleteGreeting(@PathVariable("id") long id) {
		repository.deleteById(id);
	}

	@GetMapping("/{id}")
	public Optional<Greeting> getById(@PathVariable long id) {
		return repository.findById(id);
	}

}
