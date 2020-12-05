package com.altafjava.ocr;

import java.io.IOException;
import javax.script.ScriptException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class Application {

	public static void main(String[] args) throws IOException, ScriptException {
		SpringApplication.run(Application.class, args);
		log.info("============ APPLICATION STARTED SUCCESSFULLY ============");
	}

}
