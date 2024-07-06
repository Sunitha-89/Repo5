package com.ibm.hcs.asset.comd.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GitServiceimpl {

	private static final Logger logger = LoggerFactory.getLogger(GitServiceimpl.class);
	private static final String HTTPS = "https";
	private static final String HTTP = "http";
	private Filemangement filemangement;

	public GitServiceimpl(Filemangement filemangement) {
		this.filemangement = filemangement;
	}

	public List<String> validateAndcloneFromGit(String gitUrl, String user, String token, String localDirPath)
			throws Exception {
		UsernamePasswordCredentialsProvider upc = new UsernamePasswordCredentialsProvider(user, token);
		File localDir = new File(localDirPath);

		if (!localDir.exists()) {
			localDir.mkdirs();
		}

		// Check if user has access to the repository
		try {
			Git.lsRemoteRepository().setRemote(gitUrl).setCredentialsProvider(upc).call();
		} catch (GitAPIException e) {
			logger.error("User does not have read access to the repository: {}", e.getMessage());
			throw new Exception("User does not have read access to the repository.", e);
		}

		String branch = getDefaultBranch(gitUrl, upc);

		try {
			Collection<Ref> refs = Git.lsRemoteRepository().setHeads(true).setRemote(gitUrl).setCredentialsProvider(upc)
					.call();

			Map<String, Ref> gitBranchMap = new HashMap<>();
			refs.forEach(ref -> {
				String branchName = ref.getName().substring(ref.getName().lastIndexOf("/") + 1);
				gitBranchMap.put(branchName, ref);
			});

			if (!gitBranchMap.containsKey(branch)) {
				logger.error("Branch does not exist.");
				throw new Exception("Branch does not exist.");
			}

			try (Git git = Git.cloneRepository().setURI(gitUrl).setDirectory(localDir).setBranch(branch)
					.setCredentialsProvider(upc).call()) {

				StoredConfig config = git.getRepository().getConfig();
				config.setBoolean(HTTP, null, "sslVerify", false);
				config.setBoolean(HTTPS, null, "sslVerify", false);
				config.save();

				// Branch exists. Cloned to local repository successfully.
			} catch (GitAPIException | IOException e) {
				logger.error("Error during Git operations: {}", e.getMessage());
				throw new Exception("Git operation failed.", e);
			}
		} catch (GitAPIException | IOException e) {
			logger.error("Error during remote branch listing: {}", e.getMessage());
			throw new Exception("Remote branch listing failed.", e);
		}

		// Get the project names from the subdirectories created after cloning
		List<String> projectNames = filemangement.findProjectFolders(localDir.toPath());
		if (projectNames.isEmpty()) {
			throw new Exception("No valid Maven projects found in the repository.");
		}

		return projectNames;
	}

	public String getDefaultBranch(String gitUrl, UsernamePasswordCredentialsProvider upc)
			throws GitAPIException, IOException {
		Collection<Ref> refs = Git.lsRemoteRepository().setHeads(true).setRemote(gitUrl).setCredentialsProvider(upc)
				.call();

		for (Ref ref : refs) {
			String branchName = ref.getName().substring(ref.getName().lastIndexOf("/") + 1);
			if (branchName.equals("main") || branchName.equals("master")) {
				return branchName;
			}
		}

		// If no main or master branch is found, just return the first branch found
		if (!refs.isEmpty()) {
			Ref firstRef = refs.iterator().next();
			return firstRef.getName().substring(firstRef.getName().lastIndexOf("/") + 1);
		}

		throw new IOException("No branches found in the repository.");
	}

	private String findSubdirectoryName(File parentDir) {
		File[] files = parentDir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory() && !file.getName().equals(".git")) {
					// Return the first subdirectory that is not ".git"
					return file.getName();
				}
			}
		}
		// Handle the case where no suitable subdirectory is found
		return parentDir.getName(); // Default to parent directory name
	}

	public void commitToBranch(String gitUrl, Git git, String user, String token) throws Exception {

		UsernamePasswordCredentialsProvider upc = new UsernamePasswordCredentialsProvider(user, token);
		// Check if the user has write access to the repository
		if (!hasWriteAccessToGitRepo(gitUrl, upc)) {
			logger.error("User does not have write access to the repository.");
			throw new Exception("User does not have write access to the repository.");
		}

		try {

			// Create a temporary branch
			String tempBranch = "temp_branch"; // Generate a unique branch name
			git.checkout().setCreateBranch(true).setName(tempBranch).call();
			logger.info("Created temporary branch {}", tempBranch);

			// Stage all files
			git.add().addFilepattern(".").call();

			// Commit changes to the temporary branch
			git.commit().setMessage("test commit").call();
			logger.info("Committed changes to temporary branch {}", tempBranch);

			Iterable<PushResult> pushResults = git.push().setCredentialsProvider(upc).setRemote("origin")
					.setRefSpecs(new RefSpec(tempBranch)).call();

			for (PushResult pushResult : pushResults) {
				for (RemoteRefUpdate update : pushResult.getRemoteUpdates()) {
					if (update.getStatus() == RemoteRefUpdate.Status.OK) {
						logger.info("Pushed changes to temporary branch {}", tempBranch);
					} else {
						logger.error("Failed to push changes to temporary branch {}: {}", tempBranch,
								update.getStatus());
						throw new Exception(
								"Failed to push changes to temporary branch " + tempBranch + ": " + update.getStatus());
					}
				}
			}

		} catch (GitAPIException | IOException e) {
			logger.error("Error committing changes: {}", e.getMessage());
			throw new Exception("Commit operation failed.", e);
		}
	}

	public static boolean hasWriteAccessToGitRepo(String repoUrl, UsernamePasswordCredentialsProvider upc) {
		File tempDir = null;
		try {
			// Clone the repository to a temporary directory
			tempDir = Files.createTempDirectory("tempRepo").toFile();
			try (Git git = Git.cloneRepository().setURI(repoUrl).setCredentialsProvider(upc).setDirectory(tempDir)
					.call()) {

				// Create a temporary branch and attempt to push it to the remote repository
				// with dry-run
				git.branchCreate().setName("temp-branch").call();
				git.push().setRemote("origin").setCredentialsProvider(upc).setDryRun(true).add("refs/heads/temp-branch")
						.call();
			}

			// Clean up the temporary directory
			FileUtils.deleteDirectory(tempDir);

			// If the dry-run push succeeds, return true indicating write access
			return true;
		} catch (GitAPIException | IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (tempDir != null && tempDir.exists()) {
				try {
					FileUtils.deleteDirectory(tempDir);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
