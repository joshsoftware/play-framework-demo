package com.ranbhr.sample.repositories;

import javax.inject.Inject;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;


public class MyExecutionContext extends CustomExecutionContext{

	@Inject
	public MyExecutionContext(ActorSystem system) {
		super(system, "student.executor");
		// TODO Auto-generated constructor stub
	}

}
