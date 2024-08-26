package com.mingi.springframe.service;

public class HelloTarget implements Hello {

	@Override
	public String sayHello(String name) {
		System.out.println("ori:Hello " + name);
		return "Hello " + name;
	}

	@Override
	public String sayHi(String name) {		
		System.out.println("ori:Hi " + name);
		return "Hi " + name;
	}

	@Override
	public String sayThanks(String name) {
		System.out.println("ori:Thanks " + name);
		return "Thanks " + name;
	}

}