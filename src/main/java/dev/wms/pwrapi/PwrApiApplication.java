package dev.wms.pwrapi;

import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PwrApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PwrApiApplication.class, args);
	}

}
