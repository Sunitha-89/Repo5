package com.ibm.hcs.asset.comd.model;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class GitModel {

	private String gitUrl;
	private String username;
	private String token;

}
