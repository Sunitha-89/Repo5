package com.ibm.hcs.asset.comd.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.ibm.hcs.asset.comd.model.BaseDirectoryModel;

@ShellComponent
public class ScanProjectCommand<T> extends AbstractCommand<T> {

	private final ScanProjectRender scanprojectrender;
	private String projectname;
	private Path directoryPath;
	private final BaseDirectoryModel basedirectorymodel;

	public ScanProjectCommand(ScanProjectRender scanprojectrender, BaseDirectoryModel basedirectorymodel) {
		this.scanprojectrender = scanprojectrender;
		this.basedirectorymodel = basedirectorymodel;
	}

	@ShellMethod(key = "scanproject", value = "scan for a specific project")
	public void scanProjectDirectory(@ShellOption(value = "ProjectName", help = "Project Name") String projectName) {

		System.out.println(projectName);
		if (projectName == null || projectName.trim().isEmpty()) {
			throw new NullPointerException("Project name cannot be null or empty");
		}

		projectname = projectName;
		directoryPath = Paths.get(basedirectorymodel.getBaseDirectoryPath());
		execute();
	}

	@Override
	public List<T> execute() {
		try {
			scanprojectrender.render(Arrays.asList(projectname, directoryPath));
		} catch (Exception e) {
			System.err.println("Error executing command: " + e.getMessage());
			// Handle or log the exception as needed
		}
		return null; // Adjust return type and value as per your actual implementation
	}
}
