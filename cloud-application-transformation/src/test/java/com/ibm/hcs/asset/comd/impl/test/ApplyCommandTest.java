package com.ibm.hcs.asset.comd.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ibm.hcs.asset.comd.impl.ApplyCommand;

@ExtendWith(MockitoExtension.class)
public class ApplyCommandTest {

	private ApplyCommand<String> applyCommand;

	@BeforeEach
	public void setUp() {
		applyCommand = new ApplyCommand<>();
	}

	@Test
	public void testExecute() {

		List<String> result = applyCommand.execute();
		assertEquals(null, result);
	}

}
