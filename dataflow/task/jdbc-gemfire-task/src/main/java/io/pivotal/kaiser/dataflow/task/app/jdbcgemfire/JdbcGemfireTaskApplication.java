package io.pivotal.kaiser.dataflow.task.app.jdbcgemfire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.launcher.annotation.EnableTaskLauncher;

@SpringBootApplication
public class JdbcGemfireTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(JdbcGemfireTaskApplication.class, args);
	}

}
