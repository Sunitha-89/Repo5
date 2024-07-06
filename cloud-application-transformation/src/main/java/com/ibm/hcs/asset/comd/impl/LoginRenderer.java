package com.ibm.hcs.asset.comd.impl;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class LoginRenderer {

	private static final Logger logger = LoggerFactory.getLogger(LoginRenderer.class);
	private ConsolePrinter console;

	public LoginRenderer(ConsolePrinter console) {
		this.console = console;
	}

	public <T> void render(List<T> list) {
		Resource resource = new ClassPathResource("banner.txt");
		if (!resource.exists()) {
			String errorMessage = "banner.txt not found";
			logger.error(errorMessage);
			console.display("Error: " + errorMessage);
			return;
		}

		try {
			URI uri = resource.getURI();
			Path path = Paths.get(uri);
			if (Files.notExists(path)) {
				String errorMessage = "banner.txt not found at path: " + path.toString();
				logger.error(errorMessage);
				console.display("Error: " + errorMessage);
				return;
			}

			List<String> lines = Files.readAllLines(path);
			if (lines.isEmpty()) {
				String errorMessage = "banner.txt is empty";
				logger.error(errorMessage);
				console.display("Error: " + errorMessage);
				return;
			}

			console.display(lines.toString());
		} catch (IOException e) {
			String errorMessage = "Error reading banner.txt: " + e.getMessage();
			logger.error(errorMessage, e);
			console.display("Error: " + errorMessage);
		} catch (Exception e) {
			String errorMessage = "Unexpected error: " + e.getMessage();
			logger.error(errorMessage, e);
			console.display("Error: " + errorMessage);
		}
	}
}
