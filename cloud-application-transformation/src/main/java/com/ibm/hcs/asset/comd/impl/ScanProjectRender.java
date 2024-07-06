package com.ibm.hcs.asset.comd.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ibm.hcs.asset.comd.service.Filemangement;

@Component
public class ScanProjectRender {

	private static final Logger logger = LoggerFactory.getLogger(ProjectRenderer.class);
	private ConsolePrinter console;
	private final Filemangement filemanagement;

	public ScanProjectRender(ConsolePrinter console, Filemangement filemanagement) {
		this.filemanagement = filemanagement;
		this.console = console;
	}

	public <T> void render(List<T> list) {

		String projectName = (String) list.get(0);
		Path directoryPath = (Path) list.get(1);

		try {

			String recipe = filemanagement.findRecipe(projectName, directoryPath);
			// Pass baseDirectoryPath to findRecipe
			System.out.println(recipe);

		} catch (IOException e) {
			logger.error("Error reading YAML file or processing recipes", e);
		}

	}

}
