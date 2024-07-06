package com.ibm.hcs.asset.comd.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import com.ibm.hcs.asset.comd.impl.ConsolePrinter;

public class ConsolePrinterTest {

	@Test
	public void testDisplay() {
		// Redirect System.out to capture printed output
		ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStreamCaptor));

		// Create an instance of ConsolePrinter
		ConsolePrinter consolePrinter = new ConsolePrinter();

		// Test message
		String message = "Hello, JUnit!";

		// Call display method
		consolePrinter.display(message);

		// Verify the output
		assertEquals(message, outputStreamCaptor.toString().trim());

		// Reset System.out
		System.setOut(System.out);
	}
}
