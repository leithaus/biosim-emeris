package biosim.client.messages;


import m3.gwt.props.ApplyCodeGeneration;


@ApplyCodeGeneration
public abstract class MessageBody {

	public MessageBody() {
	}

	public Message createMessage() {
		Message message = new Message();
		message.setBody(this);
		return message;
	}
	
	public boolean ack() {
		return true;
	}

	// BEGIN_GENERATED_CODE
	
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class MessageBodyContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.MessageBody");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        return list;
	    }
	    public MessageBody newInstance() {
	        throw new RuntimeException("cannot instantiate an interface or abstract class");
	    }
	    MessageBodyContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.MessageBody.class);
	    }
	}
	public static final MessageBodyContainerContext Context = new MessageBodyContainerContext(MessageBody.class);

	// END_GENERATED_CODE
}
