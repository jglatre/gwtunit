package gwtunit.client;

import junit.framework.TestCase;
import junit.framework.TestResult;


public class AsynchTestCase extends TestCase {
	
	private TestResult result;
	private Expectations expectations;
	private Object asynchResult;
	
	
	public void run(TestResult result) {
		this.result = result;
		super.run(result);		
	}
	
	
	public <T> void setExpectations(Expectations<T> expectations) {
		this.expectations = expectations;
	}
	

	public void check(Object asynchResult) {
		this.asynchResult = asynchResult;
		run(result);
	}

	
	protected void runTest() throws Throwable {
		if (expectations == null) {
			super.runTest();
		}
		else {
			expectations.run(asynchResult);
		}
	}
	
	
	public interface Expectations<T> {
		void run(T asynchResult);
	}
}
