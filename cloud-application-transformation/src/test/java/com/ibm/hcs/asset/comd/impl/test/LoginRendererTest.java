package com.ibm.hcs.asset.comd.impl.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.ibm.hcs.asset.comd.impl.ConsolePrinter;
import com.ibm.hcs.asset.comd.impl.LoginRenderer;

@ExtendWith(MockitoExtension.class)
@Disabled
public class LoginRendererTest {

    @Mock
    private ConsolePrinter consoleMock;

    private LoginRenderer loginRenderer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        loginRenderer = new LoginRenderer(consoleMock);
    }

    @Test
    public void testRenderWithValidFile() throws IOException {
        // Mock the behavior of ClassPathResource and Files for banner.txt
        Resource resourceMock = mock(ClassPathResource.class);
        when(resourceMock.exists()).thenReturn(true);
        URI uri = URI.create("file:/path/to/banner.txt");
        when(resourceMock.getURI()).thenReturn(uri);

        // Mock reading lines from banner.txt
        List<String> lines = new ArrayList<>();
        lines.add("[Welcome to spring Shell Application.]");
        when(Files.readAllLines(Paths.get(uri))).thenReturn(lines);

        // Call the method under test
        loginRenderer.render(new ArrayList<>()); // Pass an empty list for simplicity

        // Verify console.display() method calls
        verify(consoleMock).display("[Welcome to the application!]");
    }

    @Test
    public void testRenderWithMissingFile() throws IOException {
        // Mock the behavior of ClassPathResource for a missing banner.txt
        Resource resourceMock = mock(ClassPathResource.class);
        when(resourceMock.exists()).thenReturn(false);

        // Call the method under test
        loginRenderer.render(new ArrayList<>()); // Pass an empty list for simplicity

        // Verify console.display() method calls for error message
        verify(consoleMock).display("Error: banner.txt not found");
    }

    @Test
    public void testRenderWithIOException() throws IOException {
        // Mock the behavior of ClassPathResource and Files for banner.txt
        Resource resourceMock = mock(ClassPathResource.class);
        when(resourceMock.exists()).thenReturn(true);
        URI uri = URI.create("file:/path/to/banner.txt");
        when(resourceMock.getURI()).thenReturn(uri);

        // Mock IOException when reading lines from banner.txt
        when(Files.readAllLines(Paths.get(uri))).thenThrow(new IOException("Simulated IO exception"));

        // Call the method under test
        loginRenderer.render(new ArrayList<>()); // Pass an empty list for simplicity

        // Verify console.display() method calls for error message
        verify(consoleMock).display("Error reading banner.txt: Simulated IO exception");
    }
}
