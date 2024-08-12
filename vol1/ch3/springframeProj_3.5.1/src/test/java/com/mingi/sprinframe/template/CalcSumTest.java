package com.mingi.sprinframe.template;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalcSumTest {
	Calculator calculator;
	String numFilepath;
	
	@BeforeEach 
	public void setUp() {
		this.calculator = new Calculator();
		this.numFilepath = getClass().getResource("/numbers.txt").getPath();
	}
	
	@Test 
	public void sumOfNumbers() throws IOException {
		assertEquals(calculator.calcSum(this.numFilepath), Integer.valueOf(10));
	}
	
	@Test 
	public void multiplyOfNumbers() throws IOException {
		assertEquals(calculator.calcMultiply(this.numFilepath), Integer.valueOf(24));
	}
	
	@Test 
	public void concatenateStrings() throws IOException {
		assertEquals(calculator.concatenate(this.numFilepath), "1234");
	}
}
