package com.ibm.hcs.asset.comd.model.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.ibm.hcs.asset.comd.model.GitModel;

public class GitModelTest {

    @Test
    public void testGettersAndSetters() {
        // Create an instance of GitModel
        GitModel gitModel = new GitModel();
        
        // Set values using setters
        gitModel.setGitUrl("https://github.com/example/repository.git");
        gitModel.setUsername("testuser");
        gitModel.setToken("secretpassword");
        
        // Test getters to verify the values
        assertEquals("https://github.com/example/repository.git", gitModel.getGitUrl());
        assertEquals("testuser", gitModel.getUsername());
        assertEquals("secretpassword", gitModel.getToken());
    }
}
