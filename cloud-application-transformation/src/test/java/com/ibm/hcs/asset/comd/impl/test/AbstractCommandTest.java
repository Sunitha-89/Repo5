package com.ibm.hcs.asset.comd.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ibm.hcs.asset.comd.impl.AbstractCommand;

@ExtendWith(MockitoExtension.class)
class AbstractCommandTest {

	private AbstractCommand<String> command;

	@BeforeEach
	void setUp() {
		command = new AbstractCommand<String>() {
			@Override
			public List<String> execute() {
				return Arrays.asList("value1", "value2");
			}
		};
	}

	@Test
	void testExecute() {
		List<String> result = command.execute();
		assertEquals(2, result.size());
		assertEquals("value1", result.get(0));
		assertEquals("value2", result.get(1));
	}
}
