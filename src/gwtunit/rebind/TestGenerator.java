package gwtunit.rebind;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;


public class TestGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,	String typeName) 
			throws UnableToCompleteException {

		try {
			JClassType type = context.getTypeOracle().getType(typeName);
			String packageName = type.getPackage().getName();
			String className = type.getSimpleSourceName() + "Extended";
			
			ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory( packageName, className );
			composer.setSuperclass( type.getName() );
			composer.addImport("junit.framework.TestCase");
			
			PrintWriter writer = context.tryCreate( logger, packageName, className );
			if (writer != null) {
				SourceWriter source = composer.createSourceWriter( context, writer );
		
				generateConstructor( source, className, type );
				generateNewInstanceMethod( source, className );
				
				source.commit( logger );
			}
			return type.getParameterizedQualifiedSourceName() + "Extended";
		} 
		catch (NotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	private void generateConstructor(SourceWriter source, String className, JClassType type) {
		source.println("public " + className + "() {");
		for ( JMethod method : getTestMethods(type) ) {
			source.println("addTestMethod(\"" + method.getName() + "\", new TestMethod<" + className + ">() {");
			source.println("   public void invoke(" + className + " target) {");
			source.println("      target." + method.getName() + "();");
			source.println("   }");
			source.println("});");
		}
		source.println("}");		
	}
	
	
	private void generateNewInstanceMethod(SourceWriter source, String className) {
		source.println("public TestCase newInstance() {");
		source.println("   return new " + className + "();");
		source.println("}");		
	}
	
	
	private Iterable<JMethod> getTestMethods(JClassType type) {
		List<JMethod> methods = new ArrayList<JMethod>();
		for (JMethod method : type.getMethods()) {
			if (isTest(method)) {
				methods.add( method );
			}
		}
		return methods;
	}
	
	
	private boolean isTest(JMethod method) {
		return method.getName().startsWith("test");
	}

}
