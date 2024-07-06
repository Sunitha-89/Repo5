package com.ibm.hcs.asset.comd.model;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class BaseDirectoryModel {
	private String baseDirectoryPath;

	public String getBaseDirectoryPath() {
		return baseDirectoryPath;
	}

	public void setBaseDirectoryPath(String baseDirectoryPath) {
		this.baseDirectoryPath = baseDirectoryPath;
	}
}
