package com.ibm.hcs.asset.comd.service.test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ibm.hcs.asset.comd.service.GitServiceimpl;
import com.ibm.hcs.asset.comd.service.Filemangement;
@ExtendWith(MockitoExtension.class)
@Disabled

public class GitServiceimplTest {

    @Mock
    private Filemangement filemangement;

    private GitServiceimpl gitServiceimpl;
    
    @Mock
    private Git git;
    
    @Mock
    private LsRemoteCommand lsRemoteCommand;

    @Mock
    private CloneCommand cloneCommand;

    @Mock
    private StoredConfig storedConfig;

    private UsernamePasswordCredentialsProvider upc;

    @BeforeEach
    void setUp() {
    	MockitoAnnotations.initMocks(this);
        upc = new UsernamePasswordCredentialsProvider("user", "token");
        MockitoAnnotations.openMocks(this);
        gitServiceimpl = new GitServiceimpl(filemangement);
    }
        
    @Test
    void testValidateAndcloneFromGit_Unauthorized() throws Exception {
        String gitUrl = "https://github.com/example/repo.git";
        String user = "invalidUser";
        String token = "invalidToken";
        String localDirPath = "/path/to/local/dir";

        

        // Call the method under test
        Exception exception = assertThrows(Exception.class, () -> {
            gitServiceimpl.validateAndcloneFromGit(gitUrl, user, token, localDirPath);
        });

        // Verify the exception message or behavior as needed
        assertTrue(exception.getMessage().contains("User does not have read access to the repository"));
    } 
    

    @Test
    void testGetDefaultBranch_NoBranches() throws Exception {
        String gitUrl = "https://github.com/example/repo.git";

       

        Exception exception = assertThrows(TransportException.class, () -> {
            gitServiceimpl.getDefaultBranch(gitUrl, upc);
        });

        assertTrue(exception.getMessage().contains("not authorized"));
    }
    
    
    @Test
    void testCommitToBranch() throws Exception {
        // Mocking parameters
        String gitUrl = "https://example.com/git/repo";
        String user = "username";
        String token = "password";

        // Mocking Git operations
        UsernamePasswordCredentialsProvider upc = new UsernamePasswordCredentialsProvider(user, token);
        when(GitServiceimpl.hasWriteAccessToGitRepo(gitUrl,upc)).thenReturn(true);

        PushCommand pushCommandMock = mock(PushCommand.class);
        when(git.push()).thenReturn(pushCommandMock);
        when(pushCommandMock.setCredentialsProvider(upc)).thenReturn(pushCommandMock);

        PushResult pushResultMock = mock(PushResult.class);
        when(pushCommandMock.call()).thenReturn(List.of(pushResultMock));

        RemoteRefUpdate remoteRefUpdateMock = mock(RemoteRefUpdate.class);
        when(pushResultMock.getRemoteUpdates()).thenReturn(List.of(remoteRefUpdateMock));
        when(remoteRefUpdateMock.getStatus()).thenReturn(RemoteRefUpdate.Status.OK);

        // Calling the method under test
        gitServiceimpl.commitToBranch(gitUrl, git, user, token);

        // Verifying interactions
        verify(git.checkout()).setCreateBranch(true).setName(toString());
        verify(git.add()).addFilepattern(".");
        verify(git.commit()).setMessage("test commit");
        
    }

    

    @Test
    void testHasWriteAccessToGitRepo_failure() throws GitAPIException, IOException {
        // Arrange
        String repoUrl = "https://example.com/gitrepo.git";
        UsernamePasswordCredentialsProvider mockCredentialsProvider = mock(UsernamePasswordCredentialsProvider.class);
        
        // Mock the Git and PushCommand objects
        Git git = mock(Git.class);
        PushCommand pushCommand = mock(PushCommand.class);
        
        // Setup mock behavior
        when(git.push()).thenReturn(pushCommand);
        when(pushCommand.setRemote(ArgumentMatchers.eq("origin")))
                .thenReturn(pushCommand); // Ensure setRemote() returns pushCommand
        when(pushCommand.setCredentialsProvider(ArgumentMatchers.eq(mockCredentialsProvider)))
                .thenReturn(pushCommand);
        when(pushCommand.setDryRun(ArgumentMatchers.eq(true)))
                .thenReturn(pushCommand);
        when(pushCommand.add(ArgumentMatchers.eq("refs/heads/temp-branch")))
                .thenReturn(pushCommand);
        // You may need to mock other methods like call() as well

        // Act
        boolean hasWriteAccess = gitServiceimpl.hasWriteAccessToGitRepo(repoUrl, mockCredentialsProvider);
        System.out.println("hasWriteAccess: " + hasWriteAccess);
        // Assert
        assertTrue(hasWriteAccess);
        
    }

}
