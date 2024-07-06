package com.ibm.hcs.asset.comd.model.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.ibm.hcs.asset.comd.model.BaseDirectoryModel;

public class BaseDirectoryModelTest {

    @Test
    public void testGetBaseDirectoryPath() {
        // Arrange
        BaseDirectoryModel model = new BaseDirectoryModel();
        String expectedPath = "/path/to/directory";
        model.setBaseDirectoryPath(expectedPath);

        // Act
        String actualPath = model.getBaseDirectoryPath();

        // Assert
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testSetBaseDirectoryPath() {
        // Arrange
        BaseDirectoryModel model = new BaseDirectoryModel();
        String expectedPath = "/another/path";

        // Act
        model.setBaseDirectoryPath(expectedPath);
        String actualPath = model.getBaseDirectoryPath();

        // Assert
        assertEquals(expectedPath, actualPath);
    }
}
