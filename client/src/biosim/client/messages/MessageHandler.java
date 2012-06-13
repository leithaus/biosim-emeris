package biosim.client.messages;



public abstract class MessageHandler {

	public void process(Message message) {
		MessageBody body = message.getBody();
		
		if ( body instanceof ClearDataSet ) {
			processClearDataSet();
			
		} else if ( body instanceof CreateNodes ) {
			processCreateNodes((CreateNodes)body);

		} else if ( body instanceof RemoveNodes ) {
			processRemoveNodes((RemoveNodes)body);

		} else {
			throw new RuntimeException("don't know how to handle type " + body.getClass());
		}
		
	}
	
	public abstract void connect();

	public abstract void processClearDataSet();
	
	public abstract void processRemoveNodes(RemoveNodes rn);

	public abstract void processCreateNodes(CreateNodes cn);
	
}
