package biosim.client.model;



public class Image extends Node {
	
	private BlobRef _blobRef;

    private Image() {
    }

	public Image(DataSet dataSet) {
		super(dataSet);
	}
	
	@Override
	public String getIconUrl() {
		return "/images/photo.png";
	}
	
	public void setBlob(Blob blob) {
		setBlobRef(blob.getRef());
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
	
	public biosim.client.model.BlobRef getBlobRef() {
	    return _blobRef;
	}
	public void setBlobRef(biosim.client.model.BlobRef blobRef0) {
	    _setBlobRef(blobRef0);
	}
	protected void _setBlobRef(biosim.client.model.BlobRef blobRef0) {
	    biosim.client.model.BlobRef before = _blobRef;
	     _blobRef = blobRef0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.blobRef, before, blobRef0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class ImageContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext blobRef = new m3.gwt.props.impl.AbstractPropertyContext<Image,biosim.client.model.BlobRef>(this, "blobRef", biosim.client.model.BlobRef.class, 0, null, false) {
	    	    protected biosim.client.model.BlobRef getImpl(Image bean) { return bean.getBlobRef(); }
	    	    protected void setImpl(Image bean, biosim.client.model.BlobRef value ) { bean.setBlobRef(value);}
	    };
	    public m3.gwt.props.PropertyContext uid = Node.Context.uid;
	    public m3.gwt.props.PropertyContext created = Node.Context.created;
	    public m3.gwt.props.PropertyContext dataSet = Node.Context.dataSet;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.model.Node");
	        set = set.insert("biosim.client.model.Image");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uid);
	        list = list.cons(this.created);
	        list = list.cons(this.dataSet);
	        list = list.cons(this.blobRef);
	        return list;
	    }
	    public Image newInstance() {
	        return new Image();
	    }
	    ImageContainerContext(Class<?> actualClass) {
	        super(biosim.client.model.Image.class, Image.Context);
	    }
	}
	public static final ImageContainerContext Context = new ImageContainerContext(Image.class);

	// END_GENERATED_CODE
}
