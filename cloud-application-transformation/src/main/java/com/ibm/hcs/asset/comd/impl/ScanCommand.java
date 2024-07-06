package com.ibm.hcs.asset.comd.impl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.ibm.hcs.asset.comd.model.BaseDirectoryModel;

@ShellComponent
public class ScanCommand<T> extends AbstractCommand<T> {

	private static final Logger logger = LoggerFactory.getLogger(ScanCommand.class);

	private final ProjectRenderer projectRenderer;
	private final BaseDirectoryModel basedirectorymodel;

	public ScanCommand(ProjectRenderer projectRenderer, BaseDirectoryModel basedirectorymodel) {
		this.projectRenderer = projectRenderer;
		this.basedirectorymodel = basedirectorymodel;
	}

	@ShellMethod(key = "scan", value = "scan for valid project directories")
	public void scanBaseDirectory(@ShellOption(value = "--basedir", help = "Base Directory") String baseDirectory) {
		try {
			validateBaseDirectory(baseDirectory);
			basedirectorymodel.setBaseDirectoryPath(baseDirectory);
			execute();
		} catch (IllegalArgumentException e) {
			logger.error("Invalid command format: " + e.getMessage());
			logger.info("Usage: scan --basedir <path>");
		}
	}

	private void validateBaseDirectory(String baseDirectory) {
		if (baseDirectory == null || baseDirectory.trim().isEmpty()) {
			throw new IllegalArgumentException("Path cannot be null or empty");
		}
	}

	@Override
	public List<T> execute() {
		projectRenderer.render(Arrays.asList(basedirectorymodel.getBaseDirectoryPath()));
		return null;
	}
}
