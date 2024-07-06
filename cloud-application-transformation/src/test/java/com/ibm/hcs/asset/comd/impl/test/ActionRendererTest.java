package com.ibm.hcs.asset.comd.impl.test;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ibm.hcs.asset.comd.impl.ActionRenderer;
import com.ibm.hcs.asset.comd.impl.ConsolePrinter;

@ExtendWith(MockitoExtension.class)
public class ActionRendererTest {

	@Mock
	private ConsolePrinter consolePrinter;

	@InjectMocks
	private ActionRenderer actionRenderer;

	@BeforeEach
	public void setUp() {
		actionRenderer = new ActionRenderer();
	}

	@Test
	public void testRender() {
		List<String> list = Arrays.asList("Item1", "Item2", "Item3");
		actionRenderer.render(list);

	}

}
