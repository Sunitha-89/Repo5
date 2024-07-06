package com.ibm.hcs.asset.comd.impl;

import java.util.List;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ibm.hcs.asset.comd.model.BaseDirectoryModel;
import com.ibm.hcs.asset.comd.service.Filemangement;
import com.ibm.hcs.asset.comd.service.GitServiceimpl;

@Component
public class ScanGitcommandRendere {

	private static final Logger logger = LoggerFactory.getLogger(ProjectRenderer.class);
	private final ConsolePrinter consolePrinter;
	private final GitServiceimpl gitserviceimpl;
	private Filemangement filemangement;
	private final BaseDirectoryModel basedirectorymodel;

	public ScanGitcommandRendere(ConsolePrinter consolePrinter, GitServiceimpl gitserviceimpl,
			Filemangement filemangement, BaseDirectoryModel basedirectorymodel) {
		this.consolePrinter = consolePrinter;
		this.gitserviceimpl = gitserviceimpl;
		this.filemangement = filemangement;
		this.basedirectorymodel = basedirectorymodel;
	}

	public <T> void render(List<T> list) {

		String gitUrl = (String) list.get(0);
		String username = (String) list.get(1);
		String token = (String) list.get(2);
		// path/to/local/dir
		basedirectorymodel.setBaseDirectoryPath("C:\\New folder");
		try {
			List<String> projectName = gitserviceimpl.validateAndcloneFromGit(gitUrl, username, token,
					basedirectorymodel.getBaseDirectoryPath());
			// Print directories in the specified format
			IntStream.range(0, projectName.size())
					.forEach(idx -> System.out.println((idx + 1) + ". " + projectName.get(idx)));

		} catch (Exception e) {
			logger.error("Error during repository scan: {}", e.getMessage());
			consolePrinter.display("Error during repository scan: " + e.getMessage());
		}

	}

}
