package biosim.client.messages.model;

import biosim.client.messages.model.BlobRef;



public class MImage extends MNode {
	
	private BlobRef _blobRef;

    public MImage() {
    }
	
//	@Override
	public String getIconUrl() {
		return "/images/photo.png";
	}
	
	public void setBlob(MBlob blob) {
		setBlobRef(blob.getRef());
	}

	// BEGIN_GENERATED_CODE
	
	public biosim.client.messages.model.BlobRef getBlobRef() {
	    return _blobRef;
	}
	public void setBlobRef(biosim.client.messages.model.BlobRef blobRef0) {
	    _setBlobRef(blobRef0);
	}
	protected void _setBlobRef(biosim.client.messages.model.BlobRef blobRef0) {
	    biosim.client.messages.model.BlobRef before = _blobRef;
	     _blobRef = blobRef0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.blobRef, before, blobRef0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class MImageContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext blobRef = new m3.gwt.props.impl.AbstractPropertyContext<MImage,biosim.client.messages.model.BlobRef>(this, "blobRef", biosim.client.messages.model.BlobRef.class, 0, null, false) {
	    	    protected biosim.client.messages.model.BlobRef getImpl(MImage bean) { return bean.getBlobRef(); }
	    	    protected void setImpl(MImage bean, biosim.client.messages.model.BlobRef value ) { bean.setBlobRef(value);}
	    };
	    public m3.gwt.props.PropertyContext uid = MNode.Context.uid;
	    public m3.gwt.props.PropertyContext linkHints = MNode.Context.linkHints;
	    public m3.gwt.props.PropertyContext agentServices = MNode.Context.agentServices;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.model.MImage");
	        set = set.insert("biosim.client.messages.model.MNode");
	        set = set.insert("java.lang.Object");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.blobRef);
	        list = list.cons(this.uid);
	        list = list.cons(this.linkHints);
	        list = list.cons(this.agentServices);
	        return list;
	    }
	    public MImage newInstance() {
	        return new MImage();
	    }
	    MImageContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.model.MImage.class, MImage.Context);
	    }
	}
	public static final MImageContainerContext Context = new MImageContainerContext(MImage.class);

	// END_GENERATED_CODE
}
