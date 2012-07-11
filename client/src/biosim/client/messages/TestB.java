package biosim.client.messages;


public class TestB extends TestA {

	private String _b;
	
	// BEGIN_GENERATED_CODE
	
	public java.lang.String getB() {
	    return _b;
	}
	public void setB(java.lang.String b0) {
	    _setB(b0);
	}
	protected void _setB(java.lang.String b0) {
	    java.lang.String before = _b;
	     _b = b0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.b, before, b0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class TestBContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext b = new m3.gwt.props.impl.AbstractPropertyContext<TestB,java.lang.String>(this, "b", java.lang.String.class, 0, null, false) {
	    	    protected java.lang.String getImpl(TestB bean) { return bean.getB(); }
	    	    protected void setImpl(TestB bean, java.lang.String value ) { bean.setB(value);}
	    };
	    public m3.gwt.props.PropertyContext a = TestA.Context.a;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.TestA");
	        set = set.insert("biosim.client.messages.TestB");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.b);
	        list = list.cons(this.a);
	        return list;
	    }
	    public TestB newInstance() {
	        return new TestB();
	    }
	    TestBContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.TestB.class, TestB.Context);
	    }
	}
	public static final TestBContainerContext Context = new TestBContainerContext(TestB.class);

	// END_GENERATED_CODE
}
