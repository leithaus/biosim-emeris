package biosim.client.messages;

import m3.gwt.props.ApplyCodeGeneration;

@ApplyCodeGeneration
public class TestA {

	private String _a;
	
	// BEGIN_GENERATED_CODE
	
	public java.lang.String getA() {
	    return _a;
	}
	public void setA(java.lang.String a0) {
	    _setA(a0);
	}
	protected void _setA(java.lang.String a0) {
	    java.lang.String before = _a;
	     _a = a0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.a, before, a0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class TestAContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext a = new m3.gwt.props.impl.AbstractPropertyContext<TestA,java.lang.String>(this, "a", java.lang.String.class, 0, null, false) {
	    	    protected java.lang.String getImpl(TestA bean) { return bean.getA(); }
	    	    protected void setImpl(TestA bean, java.lang.String value ) { bean.setA(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.TestA");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.a);
	        return list;
	    }
	    public TestA newInstance() {
	        return new TestA();
	    }
	    TestAContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.TestA.class);
	    }
	}
	public static final TestAContainerContext Context = new TestAContainerContext(TestA.class);

	// END_GENERATED_CODE
}
