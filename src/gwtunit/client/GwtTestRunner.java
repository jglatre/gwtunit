package gwtunit.client;

import java.util.Enumeration;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public abstract class GwtTestRunner implements EntryPoint {
	
	public void onModuleLoad() {
		RootPanel panel = RootPanel.get("root");
		
		for (Enumeration<Test> tests = suite().tests(); tests.hasMoreElements();) {
			Test test = tests.nextElement();
			
 			/*if (test instanceof TestCase) {
 				TestCase testCase = (TestCase) test;
 				showResult( testCase.run(), testCase.getName(), panel );
 			}
 			else*/
			if (test instanceof TestSuite) {
 				TestSuite testSuite = (TestSuite) test;
 				
 				DisclosurePanel disclosurePanel = new DisclosurePanel();
 				disclosurePanel.setHeader( new Label(testSuite.getName() + " " + testSuite.countTestCases()) );
 				disclosurePanel.setOpen(true);
 				
 				TestResultsPanel resultsPanel = new TestResultsPanel();
 				disclosurePanel.setContent(resultsPanel);
 				
 				TestResult result = new TestResult();
 				result.addListener(resultsPanel);
 				
 				testSuite.run(result);
 				
 				if (result.wasSuccessful()) {
 					disclosurePanel.getHeader().setStyleName("test-ok");
 				}
 				else {
 					disclosurePanel.getHeader().setStyleName("test-failure");
 				}
 				panel.add(disclosurePanel);
// 				showResult(result, testSuite.getName(), panel);
 			}
 			/*else {
 				TestResult result = new TestResult();
				test.run(result);
				showResult(result, "anonymous", panel);
 			}*/
		}
	}
	
	
	
/*	protected void showResult(TestResult r, String name, RootPanel panel) {
		DisclosurePanel result = new DisclosurePanel();
		result.setHeader(new Label(name));
		result.setContent(new VerticalPanel());
		if (r.wasSuccessful()) {
			result.getHeader().setStyleName("test-ok");
		}
		else {
			result.getHeader().setStyleName("test-failure");
			for (Enumeration<TestFailure> e = r.failures(); e.hasMoreElements();) {
				TestFailure failure = e.nextElement();
				((Panel) result.getContent()).add( new Label(failure.exceptionMessage()) );
			}
		}
		panel.add(result);		
	}*/
	
	
	protected abstract TestSuite suite();

	
	static class TestResultsPanel extends VerticalPanel implements TestListener {
		Label current;
		
		public void startTest(Test test) {
			if (test instanceof TestCase) {
				current = new Label( test.toString() );
				current.setStyleName("test-ok");
				add(current);
			}
		}		

		public void endTest(Test test) {
		}

		public void addError(Test test, Throwable t) {
			if (current != null) {
				current.setText( current.getText() + ": " + t.getMessage() );
				current.setStyleName("test-error");
			}
		}

		public void addFailure(Test test, AssertionFailedError t) {
			if (current != null) {
				current.setText( current.getText() + ": " + t.getMessage() );
				current.setStyleName("test-failure");
			}
		}
	}
}
