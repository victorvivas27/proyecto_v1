package com.proyecto_v1.proyecto_v1;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProyectoV1Application {
private  final  static Logger  LOGGER= LoggerFactory.getLogger(ProyectoV1Application.class);

	public static void main(String[] args) {
		SpringApplication.run(ProyectoV1Application.class, args);
		LOGGER.info("App corriendo en el PUERTO :  http://localhost:9090");
	}

}
