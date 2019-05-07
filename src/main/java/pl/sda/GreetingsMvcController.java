package pl.sda;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app/greetings")
public class GreetingsMvcController {

	private GreetingsRepository repository;

	public GreetingsMvcController(GreetingsRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public String helloView(Model model) {
		model.addAttribute("allGreetings", repository.findAll());
		return "hello";
	}

	@PostMapping
	public String addGreeting(Greeting greeting) {
		repository.save(greeting);
		return "redirect:/app/greetings";
	}
}
