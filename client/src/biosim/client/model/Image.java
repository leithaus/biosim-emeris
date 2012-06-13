package biosim.client.model;

import com.google.gwt.core.client.GWT;


public class Image extends Node {

	private Uid _agent;
	private String _dataInBase64;
	private String _filename;

    private Image() {
    }

	public Image(DataSet dataSet) {
		super(dataSet);
	}

	public String getDataInBase64() {
        return _dataInBase64;
    }
	public void setDataInBase64(String dataInBase64) {
        _dataInBase64 = dataInBase64;
    }
	
	public String getUrl() {
	    String ext = getExtension();
	    if ( ext.length() > 0 ) {
	        ext = "." + ext;
	    }
	    
        String href = "/blobs/" + _agent.asString() + "/" + getUid().asString() + ext;
        if ( GWT.isProdMode() ) {
            return href;
        } else {
            return "http://localhost:8080" + href;
        }
	}
	
	public String getExtension() {
	    int i = _filename.lastIndexOf(".");
	    if ( i == -1 ) {
	        return "";
	    } else {
	        return _filename.substring(i+1);
	    }
	        
	}
	
	public String getFilename() {
        return _filename;
    }
	public void setFilename(String filename) {
        _filename = filename;
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
	
	public biosim.client.model.Uid getAgent() {
	    return _agent;
	}
	public void setAgent(biosim.client.model.Uid agent0) {
	    _setAgent(agent0);
	}
	protected void _setAgent(biosim.client.model.Uid agent0) {
	    biosim.client.model.Uid before = _agent;
	     _agent = agent0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.agent, before, agent0);
	}
	protected void _setDataInBase64(java.lang.String dataInBase640) {
	    java.lang.String before = _dataInBase64;
	     _dataInBase64 = dataInBase640;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.dataInBase64, before, dataInBase640);
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
	public static class ImageContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext agent = new m3.gwt.props.impl.AbstractPropertyContext<Image,biosim.client.model.Uid>(this, "agent", biosim.client.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.model.Uid getImpl(Image bean) { return bean.getAgent(); }
	    	    protected void setImpl(Image bean, biosim.client.model.Uid value ) { bean.setAgent(value);}
	    };
	    public m3.gwt.props.PropertyContext dataInBase64 = new m3.gwt.props.impl.AbstractPropertyContext<Image,java.lang.String>(this, "dataInBase64", java.lang.String.class, 1, null, false) {
	    	    protected java.lang.String getImpl(Image bean) { return bean.getDataInBase64(); }
	    	    protected void setImpl(Image bean, java.lang.String value ) { bean.setDataInBase64(value);}
	    };
	    public m3.gwt.props.PropertyContext filename = new m3.gwt.props.impl.AbstractPropertyContext<Image,java.lang.String>(this, "filename", java.lang.String.class, 2, null, false) {
	    	    protected java.lang.String getImpl(Image bean) { return bean.getFilename(); }
	    	    protected void setImpl(Image bean, java.lang.String value ) { bean.setFilename(value);}
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
	        list = list.cons(this.agent);
	        list = list.cons(this.dataInBase64);
	        list = list.cons(this.filename);
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
