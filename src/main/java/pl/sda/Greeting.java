package pl.sda;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Greeting {
	private String msg;
	@Id
	@GeneratedValue
	private long id;

	public Greeting() {
	}

	public Greeting(String s) {
		msg = s;
	}
}
