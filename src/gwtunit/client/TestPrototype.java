package gwtunit.client;

import junit.framework.TestCase;


public interface TestPrototype {

	String getTestClassName();
	TestCase newInstance();
	Iterable<String> getTestMethodNames();
	TestMethod<?> getTestMethod(String name);
	
	interface TestMethod<T extends TestCase> {
		void invoke(T target);
	}
}
