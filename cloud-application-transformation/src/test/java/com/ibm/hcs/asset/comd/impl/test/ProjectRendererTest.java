package com.ibm.hcs.asset.comd.impl.test;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ibm.hcs.asset.comd.impl.ConsolePrinter;
import com.ibm.hcs.asset.comd.impl.ProjectRenderer;
import com.ibm.hcs.asset.comd.service.Filemangement;

@ExtendWith(MockitoExtension.class)
public class ProjectRendererTest {

	@Mock
	private ConsolePrinter consoleMock;

	@Mock
	private Filemangement filemangementMock;

	@InjectMocks
	private ProjectRenderer projectRenderer;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testRender() throws IOException {
		// Mock input list and base directory
		String baseDirectory = "test-directory";
		List<String> list = Arrays.asList(baseDirectory);

		// Mock behavior of filemangement
		Path tempDir = Files.createTempDirectory("tempDir");
		when(filemangementMock.validateAndReturnAbsolutePath(baseDirectory)).thenReturn(tempDir);

		// Mock list of project folders
		List<String> mockDirectories = Arrays.asList("folder1", "folder2");
		when(filemangementMock.findProjectFolders(tempDir)).thenReturn(mockDirectories);

		// Test render method
		projectRenderer.render(list);

	}
}
