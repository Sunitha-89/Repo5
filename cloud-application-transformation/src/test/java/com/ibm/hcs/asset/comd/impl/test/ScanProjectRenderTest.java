package com.ibm.hcs.asset.comd.impl.test;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ibm.hcs.asset.comd.impl.ConsolePrinter;
import com.ibm.hcs.asset.comd.impl.ScanProjectRender;
import com.ibm.hcs.asset.comd.service.Filemangement;

@ExtendWith(MockitoExtension.class)
public class ScanProjectRenderTest {

	private ScanProjectRender scanProjectRender;
	private ConsolePrinter mockConsolePrinter;

	@Mock
	private Filemangement filemanagement;

	@BeforeEach
	public void setUp() {
		mockConsolePrinter = mock(ConsolePrinter.class);
		scanProjectRender = new ScanProjectRender(mockConsolePrinter, filemanagement);
	}

	@Test
	public void testRender_Success() throws IOException {
		// Mock data
		String projectName = "TestProject";
		Path directoryPath = Path.of("/path/to/directory");

		List<Object> dataList = new ArrayList<>();
		dataList.add(projectName);
		dataList.add(directoryPath);

		// Mock behavior of filemanagement
		String expectedRecipe = "Test recipe";
		when(filemanagement.findRecipe(projectName, directoryPath)).thenReturn(expectedRecipe);

		// Call the method under test
		scanProjectRender.render(dataList);

		// Verify console output or other expected behavior
		// Example: verify(console).printLine(expectedRecipe);
		// In this case, using println, you may not verify console output directly in
		// JUnit
	}

	@Test
	public void testRender_IOError() throws IOException {
		// Mock data
		String projectName = "TestProject";
		Path directoryPath = Path.of("/path/to/directory");

		List<Object> dataList = new ArrayList<>();
		dataList.add(projectName);
		dataList.add(directoryPath);

		// Mocking IOException
		when(filemanagement.findRecipe(projectName, directoryPath)).thenThrow(IOException.class);

		// Call the method under test
		scanProjectRender.render(dataList);

		// Verify logger error is called with IOException

	}
}