package biosim.client.messages.model;

public class MLabel extends MIconNode {

	public static final String defaultIconUrl = "/images/tag.png";
	
	public MLabel() {
	}

	public MLabel(String name) {
		super(name);
	}

	public MLabel(String name, BlobRef icon) {
		super(name, icon);
	}
	
	@Override
	public String getDefaultIconUrl() {
		// TODO Auto-generated method stub
		return defaultIconUrl;
	}
	// BEGIN_GENERATED_CODE
	
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class MLabelContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext icon = MIconNode.Context.icon;
	    public m3.gwt.props.PropertyContext name = MIconNode.Context.name;
	    public m3.gwt.props.PropertyContext uid = MIconNode.Context.uid;
	    public m3.gwt.props.PropertyContext linkHints = MIconNode.Context.linkHints;
	    public m3.gwt.props.PropertyContext agentServices = MIconNode.Context.agentServices;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.model.MIconNode");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.model.MNode");
	        set = set.insert("biosim.client.messages.model.MLabel");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.icon);
	        list = list.cons(this.name);
	        list = list.cons(this.uid);
	        list = list.cons(this.linkHints);
	        list = list.cons(this.agentServices);
	        return list;
	    }
	    public MLabel newInstance() {
	        return new MLabel();
	    }
	    MLabelContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.model.MLabel.class, MLabel.Context);
	    }
	}
	public static final MLabelContainerContext Context = new MLabelContainerContext(MLabel.class);

	// END_GENERATED_CODE
}
