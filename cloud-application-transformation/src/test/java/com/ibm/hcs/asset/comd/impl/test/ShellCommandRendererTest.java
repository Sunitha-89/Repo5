package com.ibm.hcs.asset.comd.impl.test;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ibm.hcs.asset.comd.impl.ConsolePrinter;
import com.ibm.hcs.asset.comd.impl.ShellCommandRenderer;

@ExtendWith(MockitoExtension.class)
public class ShellCommandRendererTest {

	@Mock
	private ConsolePrinter console;

	private ShellCommandRenderer renderer;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		renderer = new ShellCommandRenderer();
	}

	@Test
	public void testRenderEmptyList() {
		List<String> emptyList = Collections.emptyList();
		renderer.render(emptyList);
		verifyNoInteractions(console);
	}

	@Test
	public void testRenderNonEmptyList() {
		List<String> list = Arrays.asList("item1", "item2");
		renderer.render(list);
		verifyNoMoreInteractions(console);
	}

	@Test
	public void testRenderNullList() {
		renderer.render(null);
		verifyNoInteractions(console);
	}
}
