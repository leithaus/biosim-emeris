package biosim.client.model;

import m3.gwt.props.ApplyCodeGeneration;

@ApplyCodeGeneration
public enum MyEnum {
	
	One
	, Two
	, Three
	;

	// BEGIN_GENERATED_CODE
	
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class MyEnumContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.model.MyEnum");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        return list;
	    }
	    public MyEnum newInstance() {
	        throw new RuntimeException("cannot instantiate an interface or abstract class");
	    }
	    MyEnumContainerContext(Class<?> actualClass) {
	        super(biosim.client.model.MyEnum.class, m3.gwt.lang.ListX.create(MyEnum.One, MyEnum.Two, MyEnum.Three));
	    }
	}
	public static final MyEnumContainerContext Context = new MyEnumContainerContext(MyEnum.class);

	// END_GENERATED_CODE
}
