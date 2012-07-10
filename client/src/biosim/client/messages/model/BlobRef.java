package biosim.client.messages.model;

import m3.gwt.props.ApplyCodeGeneration;


import com.google.gwt.core.client.GWT;

@ApplyCodeGeneration
public class BlobRef {

	private Uid _agentUid;
	private Uid _blobUid;
	private String _filename;
	
	public BlobRef() {
	}
	
	public BlobRef(Uid _agentUid, Uid _blobUid, String _filename) {
		super();
		this._agentUid = _agentUid;
		this._blobUid = _blobUid;
		this._filename = _filename;
	}

	public String getUrl() {
        String href = "/blobs/" + getAgentUid().asString() + "/" + getBlobUid().asString() + "." + getExt();
        if ( GWT.isProdMode() ) {
            return href;
        } else {
            return "http://localhost:8080" + href;
        }
	}
	
	public String getExt() {
	    int i = _filename.lastIndexOf(".");
	    if ( i == -1 ) {
	        return "";
	    } else {
	        return _filename.substring(i+1);
	    }
	        
	}


	// BEGIN_GENERATED_CODE
	
	public biosim.client.messages.model.Uid getAgentUid() {
	    return _agentUid;
	}
	public void setAgentUid(biosim.client.messages.model.Uid agentUid0) {
	    _setAgentUid(agentUid0);
	}
	protected void _setAgentUid(biosim.client.messages.model.Uid agentUid0) {
	    biosim.client.messages.model.Uid before = _agentUid;
	     _agentUid = agentUid0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.agentUid, before, agentUid0);
	}
	public biosim.client.messages.model.Uid getBlobUid() {
	    return _blobUid;
	}
	public void setBlobUid(biosim.client.messages.model.Uid blobUid0) {
	    _setBlobUid(blobUid0);
	}
	protected void _setBlobUid(biosim.client.messages.model.Uid blobUid0) {
	    biosim.client.messages.model.Uid before = _blobUid;
	     _blobUid = blobUid0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.blobUid, before, blobUid0);
	}
	public java.lang.String getFilename() {
	    return _filename;
	}
	public void setFilename(java.lang.String filename0) {
	    _setFilename(filename0);
	}
	protected void _setFilename(java.lang.String filename0) {
	    java.lang.String before = _filename;
	     _filename = filename0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.filename, before, filename0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class BlobRefContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext agentUid = new m3.gwt.props.impl.AbstractPropertyContext<BlobRef,biosim.client.messages.model.Uid>(this, "agentUid", biosim.client.messages.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.messages.model.Uid getImpl(BlobRef bean) { return bean.getAgentUid(); }
	    	    protected void setImpl(BlobRef bean, biosim.client.messages.model.Uid value ) { bean.setAgentUid(value);}
	    };
	    public m3.gwt.props.PropertyContext blobUid = new m3.gwt.props.impl.AbstractPropertyContext<BlobRef,biosim.client.messages.model.Uid>(this, "blobUid", biosim.client.messages.model.Uid.class, 1, null, false) {
	    	    protected biosim.client.messages.model.Uid getImpl(BlobRef bean) { return bean.getBlobUid(); }
	    	    protected void setImpl(BlobRef bean, biosim.client.messages.model.Uid value ) { bean.setBlobUid(value);}
	    };
	    public m3.gwt.props.PropertyContext filename = new m3.gwt.props.impl.AbstractPropertyContext<BlobRef,java.lang.String>(this, "filename", java.lang.String.class, 2, null, false) {
	    	    protected java.lang.String getImpl(BlobRef bean) { return bean.getFilename(); }
	    	    protected void setImpl(BlobRef bean, java.lang.String value ) { bean.setFilename(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.model.BlobRef");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.agentUid);
	        list = list.cons(this.blobUid);
	        list = list.cons(this.filename);
	        return list;
	    }
	    public BlobRef newInstance() {
	        return new BlobRef();
	    }
	    BlobRefContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.model.BlobRef.class);
	    }
	}
	public static final BlobRefContainerContext Context = new BlobRefContainerContext(BlobRef.class);

	// END_GENERATED_CODE
}
