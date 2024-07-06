package com.ibm.hcs.asset.comd.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

@Service
public class Filemangement {

	@Value("${yaml.file.path}")
	private Resource yamlFilePath;

	private static final Logger logger = LoggerFactory.getLogger(Filemangement.class);

	public Path validateAndReturnAbsolutePath(String inputPath) throws InvalidPathException {
		if (inputPath == null || inputPath.trim().isEmpty()) {
			throw new NullPointerException("Path cannot be null or empty");
		}

		// This method normalizes a path to a standard format.
		String normalizedPath = FilenameUtils.normalizeNoEndSeparator(inputPath);
		if (normalizedPath == null) {
			throw new InvalidPathException(inputPath, "Invalid path format");
		}

		Path path = Paths.get(normalizedPath).toAbsolutePath();

		// Check if the path exists
		if (!Files.exists(path)) {
			throw new InvalidPathException(normalizedPath, "Path does not exist");
		}

		return path;
	}

	public List<String> findProjectFolders(Path directoryPath) {
		try {
			if (!Files.isDirectory(directoryPath)) {
				logger.warn("Provided path is not a directory: {}", directoryPath);
				return Collections.emptyList();
			}

			try (Stream<Path> paths = Files.walk(directoryPath, 2)) {
				List<String> projectFolders = paths.filter(Files::isDirectory)
						.filter(path -> !path.equals(directoryPath)) // Exclude the root directory itself
						.filter(this::isMavenProject).map(path -> path.getFileName().toString())
						.collect(Collectors.toList());

				if (!projectFolders.isEmpty()) {

					return projectFolders;
				} else {
					logger.warn("No Maven project folders found in directory: {}", directoryPath);
					return Collections.emptyList();
				}
			} catch (IOException e) {
				logger.error("Error while searching for project folders.", e);
				return Collections.emptyList();
			}
		} catch (InvalidPathException e) {
			logger.error("Invalid path provided: {}", e.getMessage());
			return Collections.emptyList();
		}
	}

	public boolean isMavenProject(Path directoryPath) {
		if (!Files.isDirectory(directoryPath)) {
			logger.warn("Provided path is not a directory: {}", directoryPath);
			return false;
		}

		Path pomFilePath = directoryPath.resolve("pom.xml");
		if (!Files.exists(pomFilePath)) {
			return false;
		}

		Path srcMainJava = directoryPath.resolve("src").resolve("main").resolve("java");
		Path srcTestJava = directoryPath.resolve("src").resolve("test").resolve("java");

		boolean hasMavenStructure = Files.isDirectory(srcMainJava) && Files.isDirectory(srcTestJava);

		if (hasMavenStructure) {

		} else {
			logger.warn("Directory does not follow Maven structure: {}", directoryPath);
		}

		return hasMavenStructure;
	}

	public String findRecipe(String projectName, Path directoryPath) throws IOException {

		InputStream yamlContent = yamlFilePath.getInputStream();
		Yaml yaml = new Yaml();
		Map<String, List<Map<String, Object>>> yamlData = yaml.load(yamlContent);
		boolean isSpringBoot = isSpringBootProject(projectName, directoryPath);
		boolean containsXmlFiles = containsServerFiles(directoryPath, projectName);
		String javaVersion = getJavaVersion(projectName, directoryPath);

		StringBuilder recipes = new StringBuilder();

		List<Map<String, Object>> journeys = yamlData.get("journeys");
		if (journeys != null) {
			for (Map<String, Object> journey : journeys) {
				String journeyName = (String) journey.get("name");

				// Determine if this journey is applicable based on project characteristics
				boolean isValidRecipe = false;

				if ("1.8".equals(javaVersion)) {
					if (journeyName.contains("8") || (journeyName.contains("Java"))) {
						isValidRecipe = true;
					}
				}
				if (isSpringBoot) {
					// SpringBoot project without server files
					if (journeyName.contains("SpringBoot")) {
						isValidRecipe = true;
					}
				}

				if (containsXmlFiles) {
					// Project contains only server files
					if (journeyName.contains("WAS") || (journeyName.contains("Ejb"))) {
						isValidRecipe = true;
					}
				}
				if (isSpringBoot && containsXmlFiles) {
					// SpringBoot project with server files
					if (journeyName.contains("SpringBoot") || journeyName.contains("WAS")
							|| (journeyName.contains("Ejb"))) {
						isValidRecipe = true;
					}
				}

				if (isValidRecipe) {
					recipes.append(journeyName).append("\n");

					String journeyDetails = (String) journey.get("details");

				}
			}
		}

		if (recipes.length() > 0) {
			return recipes.toString();
		} else {
			return "No suitable recipe found";
		}
	}

	public boolean isSpringBootProject(String projectName, Path directoryPath) throws IOException {
		try (Stream<Path> paths = Files.walk(directoryPath, Integer.MAX_VALUE)) {
			return paths.filter(Files::isDirectory)
					.anyMatch(path -> path.getFileName().toString().equals(projectName) && containsSpringBootPom(path));
		} catch (IOException e) {

			return false;
		}
	}

	public boolean containsSpringBootPom(Path projectPath) {
		Path pomFilePath = projectPath.resolve("pom.xml");
		if (Files.exists(pomFilePath)) {
			try {
				String content = Files.readString(pomFilePath);
				return content.contains("spring-boot-starter");
			} catch (IOException e) {

			}
		}
		return false;
	}

	public boolean containsServerFiles(Path projectPath, String projectName) throws IOException {

		Path specificProjectPath = projectPath.resolve(projectName);

		boolean result = Files.walk(specificProjectPath)

				.anyMatch(path -> path.getFileName().toString().equals("server.xml"));

		return result;
	}

	public String getJavaVersion(String projectName, Path directoryPath) {
		try {
			Path projectDir = directoryPath.resolve(projectName);
			Path pomFilePath = projectDir.resolve("pom.xml");
			if (Files.exists(pomFilePath)) {
				List<String> lines = Files.readAllLines(pomFilePath);
				for (String line : lines) {
					if (line.contains("<maven.compiler.source>") || line.contains("<maven.compiler.target>")) {
						return extractVersion(line);
					}
				}
			}

		} catch (IOException e) {
			logger.error("Error reading pom.xml for Java version", e);
		}

		return "Unknown";
	}

	private String extractVersion(String line) {
		return line.replaceAll(".*<.*>(.*)<.*>.*", "$1").trim();
	}

}
