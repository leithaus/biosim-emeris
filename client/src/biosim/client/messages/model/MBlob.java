package biosim.client.messages.model;



public class MBlob extends MNode {

	private String _dataInBase64;
	private BlobRef _ref;

    private MBlob() {
    }

	public MBlob(Uid agentUid, String filename) {
		_ref = new BlobRef(agentUid, getUid(), filename);
	}

	public String getDataInBase64() {
        return _dataInBase64;
    }
	public void setDataInBase64(String dataInBase64) {
        _dataInBase64 = dataInBase64;
    }

	// BEGIN_GENERATED_CODE
	
	protected void _setDataInBase64(java.lang.String dataInBase640) {
	    java.lang.String before = _dataInBase64;
	     _dataInBase64 = dataInBase640;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.dataInBase64, before, dataInBase640);
	}
	public biosim.client.messages.model.BlobRef getRef() {
	    return _ref;
	}
	public void setRef(biosim.client.messages.model.BlobRef ref0) {
	    _setRef(ref0);
	}
	protected void _setRef(biosim.client.messages.model.BlobRef ref0) {
	    biosim.client.messages.model.BlobRef before = _ref;
	     _ref = ref0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.ref, before, ref0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class MBlobContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext dataInBase64 = new m3.gwt.props.impl.AbstractPropertyContext<MBlob,java.lang.String>(this, "dataInBase64", java.lang.String.class, 0, null, false) {
	    	    protected java.lang.String getImpl(MBlob bean) { return bean.getDataInBase64(); }
	    	    protected void setImpl(MBlob bean, java.lang.String value ) { bean.setDataInBase64(value);}
	    };
	    public m3.gwt.props.PropertyContext ref = new m3.gwt.props.impl.AbstractPropertyContext<MBlob,biosim.client.messages.model.BlobRef>(this, "ref", biosim.client.messages.model.BlobRef.class, 1, null, false) {
	    	    protected biosim.client.messages.model.BlobRef getImpl(MBlob bean) { return bean.getRef(); }
	    	    protected void setImpl(MBlob bean, biosim.client.messages.model.BlobRef value ) { bean.setRef(value);}
	    };
	    public m3.gwt.props.PropertyContext uid = MNode.Context.uid;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.model.MNode");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.model.MBlob");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.dataInBase64);
	        list = list.cons(this.ref);
	        list = list.cons(this.uid);
	        return list;
	    }
	    public MBlob newInstance() {
	        return new MBlob();
	    }
	    MBlobContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.model.MBlob.class, MBlob.Context);
	    }
	}
	public static final MBlobContainerContext Context = new MBlobContainerContext(MBlob.class);

	// END_GENERATED_CODE
}
