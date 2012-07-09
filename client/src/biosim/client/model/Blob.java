package biosim.client.model;

import biosim.client.messages.model.BlobRef;


public class Blob extends Node {

	private String _dataInBase64;
	private BlobRef _ref;

    private Blob() {
    }

	public Blob(DataSet dataSet, Uid agentUid, String filename) {
		super(dataSet);
		_ref = new BlobRef(agentUid, getUid(), filename);
	}

	public String getDataInBase64() {
        return _dataInBase64;
    }
	public void setDataInBase64(String dataInBase64) {
        _dataInBase64 = dataInBase64;
    }
	
	@Override
	public String getIconUrl() {
		return "/images/photo.png";
	}

	@Override
	public String getName() {
		return null;
	}
	
	@Override
	public String toHtmlString() {
		return null;
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
	public static class BlobContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext dataInBase64 = new m3.gwt.props.impl.AbstractPropertyContext<Blob,java.lang.String>(this, "dataInBase64", java.lang.String.class, 0, null, false) {
	    	    protected java.lang.String getImpl(Blob bean) { return bean.getDataInBase64(); }
	    	    protected void setImpl(Blob bean, java.lang.String value ) { bean.setDataInBase64(value);}
	    };
	    public m3.gwt.props.PropertyContext ref = new m3.gwt.props.impl.AbstractPropertyContext<Blob,biosim.client.messages.model.BlobRef>(this, "ref", biosim.client.messages.model.BlobRef.class, 1, null, false) {
	    	    protected biosim.client.messages.model.BlobRef getImpl(Blob bean) { return bean.getRef(); }
	    	    protected void setImpl(Blob bean, biosim.client.messages.model.BlobRef value ) { bean.setRef(value);}
	    };
	    public m3.gwt.props.PropertyContext uid = Node.Context.uid;
	    public m3.gwt.props.PropertyContext created = Node.Context.created;
	    public m3.gwt.props.PropertyContext dataSet = Node.Context.dataSet;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.model.Blob");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.model.Node");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uid);
	        list = list.cons(this.created);
	        list = list.cons(this.dataSet);
	        list = list.cons(this.dataInBase64);
	        list = list.cons(this.ref);
	        return list;
	    }
	    public Blob newInstance() {
	        return new Blob();
	    }
	    BlobContainerContext(Class<?> actualClass) {
	        super(biosim.client.model.Blob.class, Blob.Context);
	    }
	}
	public static final BlobContainerContext Context = new BlobContainerContext(Blob.class);

	// END_GENERATED_CODE
}
