package com.ibm.hcs.asset.comd.impl.test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ibm.hcs.asset.comd.impl.LoginCommand;
import com.ibm.hcs.asset.comd.impl.LoginRenderer;

@ExtendWith(MockitoExtension.class)
public class LoginCommandTest {

	@Mock
	private LoginRenderer loginRenderer;

	@InjectMocks
	private LoginCommand<Object> loginCommand;

	@BeforeEach
	public void setUp() {

	}

	@Test
	public void testCatWithValidUsernameAndPassword() {

		String username = "user";
		String password = "pass";

		loginCommand.cat(username, password);
		verify(loginRenderer, times(1)).render(null);

	}

	@Test
	public void testCatWithInvalidUsername() {

		String username = "";
		String password = "pass";

		loginCommand.cat(username, password);
		verify(loginRenderer, never()).render(null);
	}

	@Test
	public void testCatWithInvalidPassword() {

		String username = "user";
		String password = "";

		loginCommand.cat(username, password);
		verify(loginRenderer, never()).render(null);
	}

	@Test
	public void testCatWithNullUsername() {

		String username = null;
		String password = "pass";

		loginCommand.cat(username, password);
		verify(loginRenderer, never()).render(null);
	}

	@Test
	public void testCatWithNullPassword() {

		String username = "user";
		String password = null;

		loginCommand.cat(username, password);
		verify(loginRenderer, never()).render(null);
	}

	@Test
	public void testExecute() {

		loginCommand.execute();
		verify(loginRenderer, times(1)).render(null);
	}
}
