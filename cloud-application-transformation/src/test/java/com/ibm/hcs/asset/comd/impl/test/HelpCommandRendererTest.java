package com.ibm.hcs.asset.comd.impl.test;

import static org.mockito.Mockito.verifyNoInteractions;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ibm.hcs.asset.comd.impl.ConsolePrinter;
import com.ibm.hcs.asset.comd.impl.HelpCommandRenderer;

@ExtendWith(MockitoExtension.class)
public class HelpCommandRendererTest {

	@Mock
	private ConsolePrinter consoleMock;
	private HelpCommandRenderer renderer;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		renderer = new HelpCommandRenderer();
	}

	@Test
	public void render_emptyList_shouldNotPrint() {

		List<String> emptyList = Arrays.asList();
		renderer.render(emptyList);
		verifyNoInteractions(consoleMock);
	}
}
