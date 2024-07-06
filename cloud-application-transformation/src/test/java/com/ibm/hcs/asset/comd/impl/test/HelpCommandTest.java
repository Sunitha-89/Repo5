package com.ibm.hcs.asset.comd.impl.test;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ibm.hcs.asset.comd.impl.HelpCommand;

@ExtendWith(MockitoExtension.class)
public class HelpCommandTest {

	@Test
	public void testExecute() {

		HelpCommand<String> helpCommand = new HelpCommand<>();
		List<String> result = helpCommand.execute();
		assertNull(result);

	}
}
