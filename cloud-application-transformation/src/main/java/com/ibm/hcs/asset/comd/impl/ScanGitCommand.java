package com.ibm.hcs.asset.comd.impl;

import java.io.Console;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.ibm.hcs.asset.comd.model.GitModel;

@ShellComponent
public class ScanGitCommand<T> extends AbstractCommand<T> {

	private static final Logger logger = LoggerFactory.getLogger(ScanCommand.class);
	private final ScanGitcommandRendere ScanGitcommandRendere;
	private GitModel gitmodel;

	public ScanGitCommand(ScanGitcommandRendere ScanGitcommandRendere, GitModel gitModel) {
		this.ScanGitcommandRendere = ScanGitcommandRendere;
		this.gitmodel = gitModel;
	}

	@ShellMethod(key = "scangit", value = "Scan Git repository for projects")
	public void scanGitRepository(@ShellOption(value = "-g", help = "Git URL") String gitUrl) {
		// Prompt for username and token

		gitmodel.setUsername(promptForInput("Enter your username: "));
		gitmodel.setToken(promptForPassword("Enter your token: "));

		if (!validateCredentials(gitUrl, gitmodel.getUsername(), gitmodel.getToken())) {
			throw new IllegalArgumentException("Invalid username or token");
		}
		if (gitUrl == null || gitUrl.trim().isEmpty()) {
			throw new NullPointerException("Url cannot be null or empty");
		}

		if (null != gitUrl && !gitUrl.trim().isEmpty()) {
			gitmodel.setGitUrl(gitUrl);
			execute();
		} else {
			logger.info("Invalid command format. Usage: scan --basedir <path>");
		}

	}

	@Override
	public List<T> execute() {
		ScanGitcommandRendere.render(Arrays.asList(gitmodel.getGitUrl(), gitmodel.getUsername(), gitmodel.getToken()));
		return null;
	}

	public String promptForInput(String prompt) {
		String userInput = null;
		try {
			System.out.print(prompt);
			userInput = new java.util.Scanner(System.in).nextLine();
		} catch (Exception e) {
			logger.error("Error reading input", e);
		}
		return userInput;
	}

	public String promptForPassword(String prompt) {
		String password;
		Console console = System.console();
		if (console == null) {
			// Fallback for environments where console is not available
			System.out.print(prompt);
			password = new java.util.Scanner(System.in).nextLine();
		} else {
			char[] passwordChars = console.readPassword(prompt);
			password = new String(passwordChars);
		}
		return password;
	}

	public boolean validateCredentials(String gitUrl, String username, String token) {
		try {
			// Attempt to connect to the repository to validate credentials
			Git.lsRemoteRepository().setRemote(gitUrl)
					.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, token)).call();
			return true; // If successful, credentials are valid
		} catch (GitAPIException e) {
			logger.error("Failed to authenticate with provided username and token: {}", e.getMessage());
			return false; // Authentication failed
		}

	}
}
