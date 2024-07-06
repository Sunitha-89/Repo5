
package com.ibm.hcs.asset.comd.impl.test;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ibm.hcs.asset.comd.impl.ProjectRenderer;
import com.ibm.hcs.asset.comd.impl.ScanCommand;
import com.ibm.hcs.asset.comd.model.BaseDirectoryModel;

@ExtendWith(MockitoExtension.class)
public class ScanCommandTest {

	@Mock
	private ProjectRenderer projectRenderer;

	@InjectMocks
	private ScanCommand<String> scanCommand;

	private Path testBaseDirectory;

	private Path yamlFilePath;

	@Mock
	private BaseDirectoryModel baseDirectoryModel;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		scanCommand = new ScanCommand<>(projectRenderer, baseDirectoryModel);
	}

	@Test
	public void testScanBaseDirectory() {
		String testBaseDirectory = "/test/path";

		// Mock behavior of basedirectorymodel.getBaseDirectoryPath()
		when(baseDirectoryModel.getBaseDirectoryPath()).thenReturn(testBaseDirectory);

		// Invoke the method under test
		scanCommand.scanBaseDirectory(testBaseDirectory);

		// Verify that basedirectorymodel.setBaseDirectoryPath() was called with the
		// correct argument
		verify(baseDirectoryModel).setBaseDirectoryPath(testBaseDirectory);

	}

	@Test
	public void testScanBaseDirectory_NullPath() {
		assertThrows(NullPointerException.class, () -> {
			scanCommand.scanBaseDirectory(null);
		});
	}

	@Test
	public void testExecute() {
		String testBaseDirectory = "/test/path";

		// Mock behavior of basedirectorymodel.getBaseDirectoryPath()
		when(baseDirectoryModel.getBaseDirectoryPath()).thenReturn(testBaseDirectory);

		// Mock behavior of projectRenderer.render()
		doNothing().when(projectRenderer).render(Collections.singletonList(testBaseDirectory));

		// Call the method under test
		List<String> result = scanCommand.execute();

		// Verify the result
		assertNull(result); // Since execute() returns null
	}

}