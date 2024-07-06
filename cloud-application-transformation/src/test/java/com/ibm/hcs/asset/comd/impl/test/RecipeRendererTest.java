package com.ibm.hcs.asset.comd.impl.test;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ibm.hcs.asset.comd.impl.ConsolePrinter;
import com.ibm.hcs.asset.comd.impl.RecipeRenderer;

public class RecipeRendererTest {

	private RecipeRenderer recipeRenderer;

	@BeforeEach
	public void setup() {
		recipeRenderer = new RecipeRenderer(new ConsolePrinter()); // Example initialization
	}

	@Test
	public void testRender() {
		List<String> testData = Arrays.asList("item1", "item2", "item3");

		recipeRenderer.render(testData);

	}
}
