package biosim.client.messages;


public class TestC extends TestB {

	private String _c;
	
	// BEGIN_GENERATED_CODE
	
	public java.lang.String getC() {
	    return _c;
	}
	public void setC(java.lang.String c0) {
	    _setC(c0);
	}
	protected void _setC(java.lang.String c0) {
	    java.lang.String before = _c;
	     _c = c0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.c, before, c0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class TestCContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext c = new m3.gwt.props.impl.AbstractPropertyContext<TestC,java.lang.String>(this, "c", java.lang.String.class, 0, null, false) {
	    	    protected java.lang.String getImpl(TestC bean) { return bean.getC(); }
	    	    protected void setImpl(TestC bean, java.lang.String value ) { bean.setC(value);}
	    };
	    public m3.gwt.props.PropertyContext b = TestB.Context.b;
	    public m3.gwt.props.PropertyContext a = TestB.Context.a;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.TestA");
	        set = set.insert("biosim.client.messages.TestB");
	        set = set.insert("biosim.client.messages.TestC");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.c);
	        list = list.cons(this.b);
	        list = list.cons(this.a);
	        return list;
	    }
	    public TestC newInstance() {
	        return new TestC();
	    }
	    TestCContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.TestC.class, TestC.Context);
	    }
	}
	public static final TestCContainerContext Context = new TestCContainerContext(TestC.class);

	// END_GENERATED_CODE
}
