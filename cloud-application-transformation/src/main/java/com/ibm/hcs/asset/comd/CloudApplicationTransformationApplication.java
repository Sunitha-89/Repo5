package com.ibm.hcs.asset.comd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.ibm.hcs.asset.comd.*" })
public class CloudApplicationTransformationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudApplicationTransformationApplication.class, args);
	}
}