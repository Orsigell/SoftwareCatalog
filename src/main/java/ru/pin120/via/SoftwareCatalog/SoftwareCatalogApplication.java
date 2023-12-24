package ru.pin120.via.SoftwareCatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class SoftwareCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoftwareCatalogApplication.class, args);
	}
}
