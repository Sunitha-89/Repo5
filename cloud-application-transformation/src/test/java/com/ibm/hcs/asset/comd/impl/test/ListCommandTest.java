package com.ibm.hcs.asset.comd.impl.test;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ibm.hcs.asset.comd.impl.ListCommand;

@ExtendWith(MockitoExtension.class)
public class ListCommandTest {

	@Test
	public void testExecuteReturnsNull() {
		ListCommand<String> command = new ListCommand<>();
		List<String> result = command.execute();
		Assertions.assertNull(result);
	}
}
