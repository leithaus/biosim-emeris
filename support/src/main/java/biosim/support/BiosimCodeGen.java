package biosim.support;

import groovy.lang.Script;

import java.io.PrintStream;
import java.util.List;
import java.util.Set;

import m3.gwt.props.ApplyCodeGeneration;
import m3.gwt.props.OmitProperty;
import net.model3.collections.ListX;
import net.model3.collections.SetX;
import net.model3.lang.StringX;
import net.model3.logging.SimpleLoggingConfigurator;
import net.model3.newfile.Directory;
import net.model3.newfile.File;
import codegen.CodegenMain;
import codegen.SourceHolder;

import com.thoughtworks.qdox.model.AbstractBaseJavaEntity;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.Type;



public class BiosimCodeGen {

	public static void main(String[] args) {
		new SimpleLoggingConfigurator().addConsoleAppender();
		BiosimCodeGen codeGen = new BiosimCodeGen();
		codeGen.run();
	}

	Set<String> _collectionClasses = SetX.create(
		List.class.getName()
		, "m3.fj.data.FList"
    ); 

	Set<SourceHolder> _codeGeneratedClasses = SetX.create();
	
	protected Directory _root = new Directory("../client/").getCanonical();
	
	public List<Directory> getSourceDirectories() {
		return ListX.create(
				_root.subDirectory( "src/biosim/" )
		);
	}

	public Directory getOutputDirectory() {
		return _root.subDirectory("src" );		
	}

	public Directory getBackupDirectory() {
		return _root.subDirectory("purgatory" );
	}
	
	public void run() {

		CodegenMain codeGenerator = new CodegenMain() {
			@Override
			public void addScriptVariables(SourceHolder source, Script compiledTemplate) {
				compiledTemplate.setProperty("codeGen", BiosimCodeGen.this);
			}
		};
		
		codeGenerator.addGroovyTemplate(new File("src/main/resources/template.groovy"));
		codeGenerator.setBackupDirectory(getBackupDirectory());
		for ( Directory sd : getSourceDirectories() ) {
			codeGenerator.getSourceDirectories().add(sd.getCanonical());
		}
		codeGenerator.setOutputDirectory(getOutputDirectory());
		
		codeGenerator.run();

		outputUberContext();
		
	}

	private void outputUberContext() {
		PrintStream out = getOutputDirectory().file("biosim/client/BiosimUberContext.java").createPrintStream();

		List<String> header = ListX.create(
				"package biosim.client;"
				,""
				,"public class BiosimUberContext extends m3.gwt.props.AbstractUberContext {"
				,""
				,"    private static final m3.gwt.props.UberContext _instance = new BiosimUberContext();"
				,"    public static m3.gwt.props.UberContext get() { return _instance; }"
				,""
				,"    private BiosimUberContext() {}"
				,""
				,"    {"
		);
		List<String> footer = ListX.create(
				"    }"
				, "}"
		);
		
		for ( String line : header ) {
			out.println(line);
		}
		
		for ( SourceHolder sh : _codeGeneratedClasses ) {
			out.println("        addContainerContext(" + sh.getClassname() + ".Context);");
		}
		
		for ( String line : footer ) {
			out.println(line);
		}
		
		out.close();
		
	}
    
    public boolean isInstanceOf(Type type, Class<?> exprType) {
    	final JavaClass javaClass = type.getJavaClass();
		if ( javaClass != null ) {
    		return isInstanceOf(javaClass, exprType);
    	}
		return false;
    }

	public boolean isInstanceOf(final JavaClass javaClass, Class<?> exprType) {
		if (javaClass.getFullyQualifiedName().equals(exprType.getName()) ) {
			return true;
		}
		Type superClass = javaClass.getSuperClass();
		if ( superClass != null ) {
			if ( isInstanceOf(superClass, exprType) ) {
				return true;
			}
		}
		for ( Type interfaceType : javaClass.getImplements() ) {
			if ( isInstanceOf(interfaceType, exprType) ) {
				return true;
			}
		}
		return false;
	}
    
    public String getPropertyTypeAsString(Type type) {
   		return getTypeAsString(type.getActualTypeArguments()[0]);
    }
    
    public String getTypeAsString(Type type) {
    	StringBuilder sb = new StringBuilder(type.getFullyQualifiedName());
    	if ( type.getActualTypeArguments() != null && type.getActualTypeArguments().length > 0 ) {
    		sb.append("<");
    		for ( int i=0 ; i<type.getActualTypeArguments().length ; i++ ) {
    			if ( i > 0 ) {
    				sb.append(",");
    			}
    			sb.append(getTypeAsString(type.getActualTypeArguments()[i]));
    		}
    		sb.append(">");
    	}
    	return sb.toString();
    }
    
    public boolean isCodeGenerationNeeded(SourceHolder holder) {
        return isCodeGenerationNeeded(holder.getJavaClass());
    }

    public boolean isCodeGenerationNeeded(JavaClass jc) {
        return hasAnnotation(jc, ApplyCodeGeneration.class);
    }
    
    public boolean hasAnnotation(AbstractBaseJavaEntity je, Class<?> annotationClass) {
    	if ( je == null ) {
    		return false;
    	} else {
	        for ( Annotation  anno : je.getAnnotations() ) {
	            if ( isInstanceOf(anno.getType(), annotationClass) ) {
	                return true;
	            }
	        }
	        if ( je instanceof JavaClass ) {
	        	JavaClass jc = (JavaClass) je;
		        if ( jc.getSuperJavaClass() != null ) {
		            return hasAnnotation(jc.getSuperJavaClass(), annotationClass);
		        } else {
		            return false;
		        }
	        }
    	}
    	return false;
    }
    
    String parseFieldName(String name) {
    	if ( name.startsWith("_") ) {
    		return name.substring(1);
    	} else {
    		return name;
    	}
    }
    
    String getShortClassname(String classname) {
    	int dot = classname.lastIndexOf(".");
    	return classname.substring(dot+1);
    }

    Type toReferenceType(Type type) {
    	if ( type.isPrimitive() ) {
        	String fqn = type.getFullyQualifiedName();
        	if ( fqn.equals("int") ) {
        		return new Type("Integer");
        	} else if ( fqn.equals("boolean") ) {
        		return new Type("Boolean");
        	} else {
        		throw new RuntimeException("don't know how to handle " + fqn);
        	}
    	} else {
    		return type;
    	}
    }
    
    public List<Property> getProperties(JavaClass jc) {
    	List<Property> l = ListX.create();
    	if ( jc != null && !jc.isInterface() && isCodeGenerationNeeded(jc) && !jc.isEnum() ) {
        	for ( JavaField field : jc.getFields() ) {
        		boolean skipField = 
        			field.isStatic() || hasAnnotation(field, OmitProperty.class);
        		if ( skipField ) {
        			continue;
        		}
                Property prop = new Property();
                prop.field = field;
                prop.name = parseFieldName(field.getName());
        		l.add(prop);
        	}
    	}
    	return l;    	
    }
    
    List<String> getEnumNames(JavaClass jc) {
    	if ( jc.isEnum() ) {
    		List<String> values = ListX.create();
    		for ( JavaField f : jc.getFields() ) {
    			if ( f.getType().getJavaClass().equals(jc) ) {
    				values.add(jc.getName() + "." + f.getName());
    			}
    		}
    		return values;
    	} else {
    		return null;
    	}
    }
    
    String getCollectionElementTypeName(Type t) {
    	if ( _collectionClasses.contains(t.getFullyQualifiedName()) ) {
    		return t.getActualTypeArguments()[0].getFullyQualifiedName() + ".class";
    	} else {
    		return "null";
    	}
    }
    
    public String generateContainerContext(SourceHolder sourceHolder) {

    	List<String> lines = ListX.create();
    	    	
    	String shortClassName = getShortClassname(sourceHolder.getClassname());
    	String contextClassname = shortClassName + "ContainerContext";

    	JavaClass superJavaClass = sourceHolder.getJavaClass().getSuperJavaClass();
    	boolean superClassHasContext = isCodeGenerationNeeded(superJavaClass);
   		lines.add("public static class " + contextClassname + " extends m3.gwt.props.impl.AbstractContainerContext {");
    	
    	int index = 0;
    	for ( Property prop : getProperties(sourceHolder.getJavaClass()) ) {
    	    JavaField field = prop.field;
    		Type type = field.getType();
    		String typeName = getTypeAsString(toReferenceType(type));
    		String elementTypeName = getCollectionElementTypeName(type);
    	    lines.add("    public m3.gwt.props.PropertyContext " + prop.name + " = new m3.gwt.props.impl.AbstractPropertyContext<" + shortClassName + "," + typeName + ">(this, \"" + prop.name + "\", " + type.getFullyQualifiedName() + ".class, " + index + ", " + elementTypeName + ", " + field.isTransient() + ") {");
    	    lines.add("    	    protected " + typeName + " getImpl(" + shortClassName + " bean) { return bean." + generateGetAndSetString(field, "get"));
	    	if ( !field.isFinal() ) {
	    		lines.add("    	    protected void setImpl(" + shortClassName + " bean, " + typeName + " value ) { bean." + generateGetAndSetString(field, "set"));
	    	} else {
	    		lines.add("    	    protected void setImpl(" + shortClassName + " bean, " + typeName + " value ) { throw new RuntimeException(\"cannot set final fields\"); }");	    		
	    	}
            lines.add("    };");
    		index += 1;
    	}

    	if ( superClassHasContext ) {
    		for ( Property prop : getProperties(superJavaClass) ) {
        	    lines.add("    public m3.gwt.props.PropertyContext " + prop.name + " = " + superJavaClass.getName() + ".Context." + prop.name + ";");
    		}
    	}
    	
        lines.add("    protected m3.fj.data.FSet<String> createImplementsList() {");
        lines.add("        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();");
        for ( String classname : getImplementedClasses(sourceHolder.getJavaClass()) ) {
            lines.add("        set = set.insert(\"" + classname + "\");");
        }
        lines.add("        return set;");
        lines.add("    }");

        lines.add("    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {");
        lines.add("        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();");
        
        List<Property> props = ListX.create();
        if ( superClassHasContext ) {        	
        	props.addAll(getProperties(superJavaClass));
        }
        props.addAll(getProperties(sourceHolder.getJavaClass()));
        for ( Property prop : props ) {
            lines.add("        list = list.cons(this." + prop.name + ");");
        }
        lines.add("        return list;");
        lines.add("    }");

        lines.add("    public " + shortClassName + " newInstance() {");
        if ( sourceHolder.getJavaClass().isInterface() || sourceHolder.getJavaClass().isAbstract() || sourceHolder.getJavaClass().isEnum() ) {
        	lines.add("        throw new RuntimeException(\"cannot instantiate an interface or abstract class\");");        	
        } else {
        	lines.add("        return new " + shortClassName + "();");
        }
        lines.add("    }");

		lines.add("    " + contextClassname + "(Class<?> actualClass) {");
    	if ( superClassHasContext ) {
    		String superClassname = getShortClassname(sourceHolder.getClassname());
    		lines.add("        super(" + sourceHolder.getClassname() + ".class, " + superClassname + ".Context);");    	
    	} else if ( sourceHolder.getJavaClass().isEnum() ) {
    		List<String> enumNames = getEnumNames(sourceHolder.getJavaClass());
    		String enumListCode = "m3.gwt.lang.ListX.create(" + StringX.join(enumNames, ", ") + ")";
    		lines.add("        super(" + sourceHolder.getClassname() + ".class, " + enumListCode + ");");
    	} else {
    		lines.add("        super(" + sourceHolder.getClassname() + ".class);");
    	}
    	
    	lines.add("    }");
    	
    	if ( sourceHolder.getJavaClass().isEnum() ) {
    		JavaClass jc = sourceHolder.getJavaClass();
    		jc.toString();
    	}
    	
    	lines.add("}");

    	lines.add("public static final " + contextClassname + " Context = new " + contextClassname + "(" + shortClassName + ".class);");    	  
    	
		return StringX.join(lines, "\n");
    }
    
    public Set<String> getImplementedClasses(JavaClass jc) {
        Set<String> set = SetX.create();
        set.add(jc.getFullyQualifiedName());
        for ( JavaClass iface : jc.getImplementedInterfaces() ) {
            set.addAll(getImplementedClasses(iface));
        }
        if ( jc.getSuperJavaClass() != null ) {
            set.addAll(getImplementedClasses(jc.getSuperJavaClass()));
        }
        return set;
    }

    public String generateGetAndSetString(JavaField field, String getOrSet){
    	String fieldMethod = getOrSet + field.getName().replaceFirst("_" + field.getName().charAt(1), "" + field.getName().toUpperCase().charAt(1));
    	if(getOrSet == "get"){
    		fieldMethod += "(); }";
    	}else if( getOrSet == "set"){
    		fieldMethod += "(value);}";
    	}
    	return fieldMethod;
    }

    public String generateGettersAndSetters(SourceHolder sourceHolder) {
        JavaClass javaClass = sourceHolder.getJavaClass();
    	_codeGeneratedClasses.add(sourceHolder);
    	List<String> lines = ListX.create();
    	for ( Property prop : getProperties(sourceHolder.getJavaClass()) ) {
    		JavaField field = prop.field;
    		if ( !field.isPrivate() ) {
    			System.err.println(field + " is not private");
    		}
    		
    		String propertyType = getTypeAsString(field.getType());
    		String fieldName = field.getName();
    		String propertyName = fieldName;
    		if ( propertyName.startsWith("_") ) {
    			propertyName = propertyName.substring(1);
    		}
    		
    		String upperedPropertyName = Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
    		
    		if ( !sourceHolder.hasGetterMethod(propertyName) ) {
    			lines.add("public " + propertyType + " get" + upperedPropertyName + "() {");
   				lines.add("    return " + fieldName + ";");
    			lines.add("}");
    		}
    		
    		if ( !field.isFinal() ) {
    			
    			String setterParmName = propertyName + "0";
    			if ( !sourceHolder.hasSetterMethod(propertyName) ) {
	    			lines.add("public void set" + upperedPropertyName + "(" + propertyType + " " + setterParmName + ") {");
	    			lines.add("    _set" + upperedPropertyName + "(" + setterParmName + ");");
	    			lines.add("}");
    			}

				lines.add("protected void _set" + upperedPropertyName + "(" + propertyType + " " + setterParmName + ") {");
				lines.add("    " + propertyType + " before = _" + propertyName + ";");
				lines.add("     _" + propertyName + " = " + propertyName + "0;" );
				lines.add("    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context." + propertyName + ", before, " + propertyName + "0);");
				lines.add("}");
    		}
			
    	}
    	
    	if ( !javaClass.isInterface() && javaClass.getMethodBySignature("toString", new Type[0]) == null ) {
    	    lines.add("@Override");
    	    lines.add("public String toString() {");
    	    lines.add("    return m3.gwt.props.ToStringBuilder.toString(this, Context);");
    	    lines.add("}");
    	}
    	
		return StringX.join(lines, "\n");
    }
    
    class Property {
        String name;
        JavaField field;
        public String toString() {
        	return field.toString();
        }
    }

}
