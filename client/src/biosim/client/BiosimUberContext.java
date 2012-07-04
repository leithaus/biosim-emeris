package biosim.client;

public class BiosimUberContext extends m3.gwt.props.AbstractUberContext {

    private static final m3.gwt.props.UberContext _instance = new BiosimUberContext();
    public static m3.gwt.props.UberContext get() { return _instance; }

    private BiosimUberContext() {}

    {
        addContainerContext(biosim.client.model.Offer.Context);
        addContainerContext(biosim.client.model.Address.Context);
        addContainerContext(biosim.client.messages.CreateNodes.Context);
        addContainerContext(biosim.client.messages.MessageBody.Context);
        addContainerContext(biosim.client.messages.RemoveNodes.Context);
        addContainerContext(biosim.client.model.MyEnum.Context);
        addContainerContext(biosim.client.model.Connection.Context);
        addContainerContext(biosim.client.messages.UidAndName.Context);
        addContainerContext(biosim.client.messages.ClearDataSet.Context);
        addContainerContext(biosim.client.model.Need.Context);
        addContainerContext(biosim.client.model.Label.Context);
        addContainerContext(biosim.client.model.Agent.Context);
        addContainerContext(biosim.client.model.Node.Context);
        addContainerContext(biosim.client.model.TextMessage.Context);
        addContainerContext(biosim.client.model.Link.Context);
        addContainerContext(biosim.client.model.Phone.Context);
        addContainerContext(biosim.client.model.Uid.Context);
        addContainerContext(biosim.client.model.Image.Context);
        addContainerContext(biosim.client.messages.Message.Context);
        addContainerContext(biosim.client.model.UidList.Context);
        addContainerContext(biosim.client.model.Blob.Context);
        addContainerContext(biosim.client.model.BlobRef.Context);
    }
}
