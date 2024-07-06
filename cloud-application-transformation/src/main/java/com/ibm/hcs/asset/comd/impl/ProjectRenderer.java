package com.ibm.hcs.asset.comd.impl;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ibm.hcs.asset.comd.service.Filemangement;

@Component
public class ProjectRenderer {

	private static final Logger logger = LoggerFactory.getLogger(ProjectRenderer.class);
	private ConsolePrinter console;
	private Filemangement filemangement;

	public ProjectRenderer(ConsolePrinter console, Filemangement filemangement) {
		this.filemangement = filemangement;
		this.console = console;
	}

	public <T> void render(List<T> list) {
		// This method normalizes a path to a standard format.
		String baseDirectory = (String) list.get(0);
		Path path = filemangement.validateAndReturnAbsolutePath(baseDirectory);

		try {
			List<String> directories = filemangement.findProjectFolders(path);

			// Print directories in the specified format
			IntStream.range(0, directories.size())
					.forEach(idx -> System.out.println((idx + 1) + ". " + directories.get(idx)));

		} catch (IllegalArgumentException e) {
			// Handle specific exception (e.g., invalid arguments)
			System.out.println("Invalid base directory: " + e.getMessage());
		} catch (SecurityException e) {
			// Handle security-related exceptions (e.g., access denied)
			System.out.println("Access denied: " + e.getMessage());
		} catch (Exception e) {
			// Handle general exceptions
			System.out.println("Error scanning directory: " + e.getMessage());
			logger.error("Error scanning directory", e); // Log the error for debugging
		}
	}
}
