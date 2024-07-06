package com.ibm.hcs.asset.comd.impl;

import org.springframework.stereotype.Component;

@Component
public class ConsolePrinter {

	public <T> void display(T message) {
		System.out.println(message);
	}
}
