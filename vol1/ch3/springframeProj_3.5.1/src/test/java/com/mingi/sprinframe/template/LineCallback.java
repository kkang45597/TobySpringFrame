package com.mingi.sprinframe.template;

public interface LineCallback<T> {
	T doSomethingWithLine(String line, T value);
}