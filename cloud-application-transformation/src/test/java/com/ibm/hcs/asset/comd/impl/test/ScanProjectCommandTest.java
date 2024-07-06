package com.ibm.hcs.asset.comd.impl.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ibm.hcs.asset.comd.impl.ScanProjectCommand;
import com.ibm.hcs.asset.comd.impl.ScanProjectRender;
import com.ibm.hcs.asset.comd.model.BaseDirectoryModel;

@ExtendWith(MockitoExtension.class)
public class ScanProjectCommandTest {

	@TempDir
	Path tempDir;

	@Mock
	private ScanProjectRender scanProjectRender;

	@Mock
	private BaseDirectoryModel baseDirectoryModel;

	@InjectMocks
	private ScanProjectCommand<String> scanProjectCommand;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testScanProjectDirectory_withValidProjectName() {
		// Given
		String projectName = "TestProject";
		Path baseDirectoryPath = tempDir.resolve("base/directory/path");
		baseDirectoryPath.toFile().mkdirs(); // Ensure the directory exists

		// Mocking baseDirectoryModel to return the correct path
		when(baseDirectoryModel.getBaseDirectoryPath()).thenReturn(baseDirectoryPath.toString());

		// Check if baseDirectoryModel is not null
		assertNotNull(baseDirectoryModel);

		// Initialize scanProjectCommand
		ScanProjectCommand scanProjectCommand = new ScanProjectCommand(scanProjectRender, baseDirectoryModel);

		// Check if scanProjectCommand is not null
		assertNotNull(scanProjectCommand);

		// When
		scanProjectCommand.scanProjectDirectory(projectName);

		// Then
		verify(scanProjectRender).render(Arrays.asList(projectName, Paths.get(baseDirectoryPath.toString())));
	}

	@Test
	public void testScanProjectDirectory_withNullProjectName() {
		// Given
		String projectName = null;

		// When & Then
		assertThrows(NullPointerException.class, () -> scanProjectCommand.scanProjectDirectory(projectName));
	}

	@Test
	public void testScanProjectDirectory_withEmptyProjectName() {
		// Given
		String projectName = "";

		// When & Then
		assertThrows(NullPointerException.class, () -> scanProjectCommand.scanProjectDirectory(projectName));
	}
}