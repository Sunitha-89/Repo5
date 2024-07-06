package com.ibm.hcs.asset.comd.service.test;


import static org.hamcrest.CoreMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibm.hcs.asset.comd.service.Filemangement;

@Disabled
public class FilemangementTest {

    @Autowired
    private Filemangement filemangement;

    @TempDir
    Path tempFolder;

    @BeforeEach
    public void setUp() {
        filemangement = new Filemangement();
    }
    
    
    

    @Test
    public void testValidateAndReturnAbsolutePath_NullPath() {
        assertThrows(NullPointerException.class, () -> {
            filemangement.validateAndReturnAbsolutePath(null);
        });
    }

    @Test
    public void testValidateAndReturnAbsolutePath_EmptyPath() {
        assertThrows(NullPointerException.class, () -> {
            filemangement.validateAndReturnAbsolutePath("");
        });
    }

    @Test
    public void testValidateAndReturnAbsolutePath_InvalidPath() {
        assertThrows(InvalidPathException.class, () -> {
            filemangement.validateAndReturnAbsolutePath("invalid:path");
        });
    }

    @Test
    public void testValidateAndReturnAbsolutePath_NonexistentPath() {
        assertThrows(InvalidPathException.class, () -> {
            filemangement.validateAndReturnAbsolutePath("nonexistent/path");
        });
    }

    @Test
    public void testValidateAndReturnAbsolutePath_ValidPath() throws IOException {
        Path tempFile = Files.createFile(tempFolder.resolve("testFile.txt"));
        Path result = filemangement.validateAndReturnAbsolutePath(tempFile.toString());
        assertEquals(tempFile.toAbsolutePath(), result);
    }

    @Test
    public void testFindProjectFolders_NoMavenProjects() throws IOException {
        Path tempDir = Files.createDirectory(tempFolder.resolve("emptyDir"));
        List<String> result = filemangement.findProjectFolders(tempDir);
        assertTrue(result.isEmpty());
    }

	/*
	 * @Test public void testFindProjectFolders_ValidMavenProjects() throws
	 * IOException { Path tempDir =
	 * Files.createDirectory(tempFolder.resolve("rootDir")); Path mavenProjectDir =
	 * Files.createDirectories(tempDir.resolve("mavenProject/src/main/java"));
	 * Files.createDirectories(mavenProjectDir.resolve("src/main/resources"));
	 * Files.createDirectories(mavenProjectDir.resolve("src/test/java"));
	 * Files.createFile(mavenProjectDir.resolve("pom.xml"));
	 * 
	 * List<String> result = filemangement.findProjectFolders(tempDir);
	 * System.out.println("Found project folders: " + result); // Debug line
	 * 
	 * // Debug print statement result.forEach(System.out::println);
	 * 
	 * assertTrue(result.contains("mavenProject")); }
	 */
    @Test
    public void testIsMavenProject_ValidMavenProject() throws IOException {
        Path mavenProjectDir = Files.createDirectories(tempFolder.resolve("mavenProject"));
        Files.createDirectories(mavenProjectDir.resolve("src/main/java"));
        Files.createDirectories(mavenProjectDir.resolve("src/main/resources"));
        Files.createDirectories(mavenProjectDir.resolve("src/test/java"));
        Files.createFile(mavenProjectDir.resolve("pom.xml"));

        assertTrue(filemangement.isMavenProject(mavenProjectDir));
    }


    @Test
    public void testIsMavenProject_InvalidMavenProject() throws IOException {
        Path nonMavenProjectDir = Files.createDirectories(tempFolder.resolve("nonMavenProject/src/main/java"));
        Files.createDirectories(nonMavenProjectDir.resolve("src/main/resources"));
        Files.createDirectories(nonMavenProjectDir.resolve("src/test/java"));

        assertFalse(filemangement.isMavenProject(nonMavenProjectDir));
    }

    @Test
    public void testFindRecipe_NoRecipes() throws IOException {
        // Create tempDir and emptyDir
        Path tempDir = Files.createDirectory(tempFolder.resolve("emptyDir"));
        Path nonExistentProjectDir = Files.createDirectories(tempDir.resolve("NonExistentProject"));

        String result = filemangement.findRecipe("NonExistentProject", tempDir);
        assertEquals("No suitable recipe found", result);
    }

    @Test
    public void testContainsSpringBootPom(@TempDir Path tempDir) throws IOException {
        // Create a temporary pom.xml file with spring-boot-starter content
        Path pomFilePath = tempDir.resolve("pom.xml");
        String pomContent = "<project>\n" +
                "    <dependencies>\n" +
                "        <dependency>\n" +
                "            <groupId>org.springframework.boot</groupId>\n" +
                "            <artifactId>spring-boot-starter</artifactId>\n" +
                "            <version>2.5.2</version>\n" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "</project>";
        Files.writeString(pomFilePath, pomContent);

        // Test containsSpringBootPom method
        assertTrue(filemangement.containsSpringBootPom(tempDir));

        // Clean up - optionally delete the temporary file
        Files.deleteIfExists(pomFilePath);
    }
    
    @Test
    public void testContainsServerFiles(@TempDir Path tempDir) throws IOException {
        // Create a temporary directory for the project
        Path projectPath = tempDir.resolve("my-project");
        Files.createDirectories(projectPath);

        // Create a server.xml file inside the project directory
        Path serverXmlPath = projectPath.resolve("server.xml");
        Files.createFile(serverXmlPath);

        // Test containsServerFiles method
        assertTrue(filemangement.containsServerFiles(tempDir, "my-project"));

        // Clean up - optionally delete the temporary files and directory
        Files.deleteIfExists(serverXmlPath);
        Files.deleteIfExists(projectPath);
    }

    
    @Test
    public void testGetJavaVersion(@TempDir Path tempDir) throws IOException {
        // Create a temporary project directory
        Path projectPath = tempDir.resolve("my-project");
        Files.createDirectories(projectPath);

        // Create a temporary pom.xml file with Maven compiler configuration
        String pomContent = "<project>\n" +
                "    <build>\n" +
                "        <plugins>\n" +
                "            <plugin>\n" +
                "                <groupId>org.apache.maven.plugins</groupId>\n" +
                "                <artifactId>maven-compiler-plugin</artifactId>\n" +
                "                <configuration>\n" +
                "                   <maven.compiler.source>1.8</maven.compiler.source>\n" +
                "                </configuration>\n" +
                "            </plugin>\n" +
                "        </plugins>\n" +
                "    </build>\n" +
                "</project>";
        Path pomFilePath = projectPath.resolve("pom.xml");
        Files.writeString(pomFilePath, pomContent);

        
        String javaVersion = filemangement.getJavaVersion("my-project", tempDir);

        // Assert the expected Java version
        assertEquals("1.8", javaVersion);

        // Clean up - optionally delete the temporary files and directory
        Files.deleteIfExists(pomFilePath);
        Files.deleteIfExists(projectPath);
    }
}
