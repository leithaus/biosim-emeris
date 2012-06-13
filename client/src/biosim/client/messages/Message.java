package biosim.client.messages;

import java.util.List;

import m3.gwt.lang.ListX;
import m3.gwt.props.ApplyCodeGeneration;
import m3.gwt.websocket.WebSocket;
import biosim.client.Biosim;
import biosim.client.model.Uid;

@ApplyCodeGeneration
public class Message {
	
	private Uid _uid;
	private Uid _sender;
	private MessageBody _body;

	public Message() {
		_uid = Uid.random();
		_sender = Biosim.get().getSenderUid();
	}
	
	public boolean ack() {
		return true;
	}
	
	public MessageBody getBody() {
		return _body;
	}
	public void setBody(MessageBody body) {
		_body = body;
	}
	
	public Uid getUid() {
		return _uid;
	}

	public WebSocket.IncomingMessage asIncomingMessage() {
		return new WebSocket.IncomingMessage() {
			@Override
			public String id() {
				return _uid.asString();
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public <T> T delegate() {
				return (T) Message.this;
			}
			
			@Override
			public List<String> acks() {
				return ListX.create();
			}
			@Override
			public boolean ack() {
				return Message.this.ack();
			}
		}; 
	}

	// BEGIN_GENERATED_CODE
	
	public void setUid(biosim.client.model.Uid uid0) {
	    _setUid(uid0);
	}
	protected void _setUid(biosim.client.model.Uid uid0) {
	    biosim.client.model.Uid before = _uid;
	     _uid = uid0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.uid, before, uid0);
	}
	public biosim.client.model.Uid getSender() {
	    return _sender;
	}
	public void setSender(biosim.client.model.Uid sender0) {
	    _setSender(sender0);
	}
	protected void _setSender(biosim.client.model.Uid sender0) {
	    biosim.client.model.Uid before = _sender;
	     _sender = sender0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.sender, before, sender0);
	}
	protected void _setBody(biosim.client.messages.MessageBody body0) {
	    biosim.client.messages.MessageBody before = _body;
	     _body = body0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.body, before, body0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class MessageContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext uid = new m3.gwt.props.impl.AbstractPropertyContext<Message,biosim.client.model.Uid>(this, "uid", biosim.client.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.model.Uid getImpl(Message bean) { return bean.getUid(); }
	    	    protected void setImpl(Message bean, biosim.client.model.Uid value ) { bean.setUid(value);}
	    };
	    public m3.gwt.props.PropertyContext sender = new m3.gwt.props.impl.AbstractPropertyContext<Message,biosim.client.model.Uid>(this, "sender", biosim.client.model.Uid.class, 1, null, false) {
	    	    protected biosim.client.model.Uid getImpl(Message bean) { return bean.getSender(); }
	    	    protected void setImpl(Message bean, biosim.client.model.Uid value ) { bean.setSender(value);}
	    };
	    public m3.gwt.props.PropertyContext body = new m3.gwt.props.impl.AbstractPropertyContext<Message,biosim.client.messages.MessageBody>(this, "body", biosim.client.messages.MessageBody.class, 2, null, false) {
	    	    protected biosim.client.messages.MessageBody getImpl(Message bean) { return bean.getBody(); }
	    	    protected void setImpl(Message bean, biosim.client.messages.MessageBody value ) { bean.setBody(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.Message");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uid);
	        list = list.cons(this.sender);
	        list = list.cons(this.body);
	        return list;
	    }
	    public Message newInstance() {
	        return new Message();
	    }
	    MessageContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.Message.class);
	    }
	}
	public static final MessageContainerContext Context = new MessageContainerContext(Message.class);

	// END_GENERATED_CODE
}
