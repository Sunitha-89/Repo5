package com.ibm.hcs.asset.comd.impl;

import java.util.List;

import com.ibm.hcs.asset.comd.Command;

public abstract class AbstractCommand<T> implements Command<T> {

	@Override
	public abstract List<T> execute();

}
