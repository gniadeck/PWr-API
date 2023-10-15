package dev.wms.pwrapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@OpenAPIDefinition(servers = @Server(url = "/", description = "Default Server URL"))
@EnableAsync(proxyTargetClass = true)
@EnableFeignClients
public class PwrApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PwrApiApplication.class, args);
	}

}
