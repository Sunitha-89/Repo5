package com.ibm.hcs.asset.comd.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import com.ibm.hcs.asset.comd.impl.ScanGitCommand;
import com.ibm.hcs.asset.comd.impl.ScanGitcommandRendere;
import com.ibm.hcs.asset.comd.model.GitModel;

@ExtendWith(MockitoExtension.class)
public class ScanGitCommandTest {

	@Mock
    private Logger logger;
	
	 @Mock
	    private Git gitMock;

	    @Mock
	    private UsernamePasswordCredentialsProvider credentialsProviderMock;

	    @Mock
	    private Logger loggerMock;

	    private GitModel gitModel;
	
	 @Mock
	    GitModel mockGitModel;

	    @Mock
	    ScanGitcommandRendere mockScanGitcommandRendere;

	    @InjectMocks
	    ScanGitCommand<Object> scanGitCommand;

	    @BeforeEach
	    void setUp() {
	        // Initialize any mock behaviors or setup needed
	    }


	    @Test
	    public void testPromptForInput() {
	        String simulatedUserInput = "testInput";
	        InputStream inputStream = new ByteArrayInputStream(simulatedUserInput.getBytes());
	        System.setIn(inputStream);

	        ScanGitCommand<?> scanGitCommand = new ScanGitCommand<>(mock(ScanGitcommandRendere.class), new GitModel());
	        String result = scanGitCommand.promptForInput("Enter your username: ");

	        assertEquals(simulatedUserInput, result);
	    }
	    
	    @Test
	    public void testPromptForPassword() {
	        String simulatedPassword = "testPassword";

	        // Simulate input stream
	        InputStream inputStream = new ByteArrayInputStream(simulatedPassword.getBytes());
	        System.setIn(inputStream);

	        ScanGitCommand<?> scanGitCommand = new ScanGitCommand<>(mock(ScanGitcommandRendere.class), new GitModel());
	        String result = scanGitCommand.promptForPassword("Enter your token: ");

	        assertEquals(simulatedPassword, result);
	    }
	    
	    
	    @Test
	    void execute_RenderMethodCalledOnce() {
	        // Arrange
	        String gitUrl = "https://example.com/git/repo";
	        String username = "testuser";
	        String token = "testtoken";

	        // Set mock behavior
	        when(mockGitModel.getGitUrl()).thenReturn(gitUrl);
	        when(mockGitModel.getUsername()).thenReturn(username);
	        when(mockGitModel.getToken()).thenReturn(token);

	        // Act
	        List<Object> result = scanGitCommand.execute();

	        // Assert
	        verify(mockScanGitcommandRendere, times(1)).render(Arrays.asList(gitUrl, username, token));
	        assertNull(result); // Assuming execute() returns null
	    }
	    
	    //new
	    
	    @Test
	    public void testPromptForInputWithException() {
	        // Arrange
	        System.setIn(new ByteArrayInputStream("".getBytes())); // Simulating empty input
	        

	        // Act
	        String userInput = scanGitCommand.promptForInput("Enter input: ");

	        // Assert
	        assertNull(userInput);
	       
	    }
	    
	    
	    
	    void testValidateCredentials_SuccessfulConnection() throws Exception {
	        // Arrange
	        String gitUrl = "https://example.com/gitrepo";
	        String username = "testuser";
	        String token = "testtoken";

	        // Mock LsRemoteCommand and its behavior
	        LsRemoteCommand lsRemoteCommandMock = Mockito.mock(LsRemoteCommand.class);
	        Mockito.when(lsRemoteCommandMock.setRemote(gitUrl)).thenReturn(lsRemoteCommandMock);
	        Mockito.when(lsRemoteCommandMock.setCredentialsProvider(Mockito.any(UsernamePasswordCredentialsProvider.class)))
	                .thenReturn(lsRemoteCommandMock);
	        Mockito.when(lsRemoteCommandMock.call()).thenThrow(new TransportException("https://example.com/gitrepo: 500 Internal Server Error"));

	        // Mock Git.lsRemoteRepository() to return the mocked LsRemoteCommand
	        Git gitMock = Mockito.mock(Git.class);
	        Mockito.when(gitMock.lsRemoteRepository()).thenReturn(lsRemoteCommandMock);

	        // Create an instance of the class under test
	       // ScanGitCommand scanGitCommand = new ScanGitCommand(gitMock); // Assuming you have a constructor

	        // Act
	        boolean isValid = scanGitCommand.validateCredentials(gitUrl, username, token);

	        // Assert
	        assertFalse(isValid, "Expected credentials to be invalid due to server error");
	    }

}

