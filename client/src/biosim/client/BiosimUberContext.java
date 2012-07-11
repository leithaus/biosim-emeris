package biosim.client;

public class BiosimUberContext extends m3.gwt.props.AbstractUberContext {

    private static final m3.gwt.props.UberContext _instance = new BiosimUberContext();
    public static m3.gwt.props.UberContext get() { return _instance; }

    private BiosimUberContext() {}

    {
        addContainerContext(biosim.client.messages.model.FilterAcceptCriteria.Context);
        addContainerContext(biosim.client.messages.protocol.RootLabelsResponse.Context);
        addContainerContext(biosim.client.messages.model.Uid.Context);
        addContainerContext(biosim.client.messages.model.MConnection.Context);
        addContainerContext(biosim.client.messages.protocol.Request.Context);
        addContainerContext(biosim.client.messages.model.BlobRef.Context);
        addContainerContext(biosim.client.messages.model.MLabel.Context);
        addContainerContext(biosim.client.messages.protocol.CreateNodesRequest.Context);
        addContainerContext(biosim.client.messages.model.UidAndName.Context);
        addContainerContext(biosim.client.messages.protocol.SelectResponse.Context);
        addContainerContext(biosim.client.messages.model.MBlob.Context);
        addContainerContext(biosim.client.messages.model.MImage.Context);
        addContainerContext(biosim.client.messages.protocol.CreateNodesResponse.Context);
        addContainerContext(biosim.client.messages.protocol.QueryResponse.Context);
        addContainerContext(biosim.client.messages.protocol.SelectRequest.Context);
        addContainerContext(biosim.client.messages.model.MLink.Context);
        addContainerContext(biosim.client.messages.protocol.FetchRequest.Context);
        addContainerContext(biosim.client.messages.model.MAgent.Context);
        addContainerContext(biosim.client.messages.model.MNode.Context);
        addContainerContext(biosim.client.messages.protocol.RequestBody.Context);
        addContainerContext(biosim.client.messages.protocol.RootLabelsRequest.Context);
        addContainerContext(biosim.client.messages.protocol.FetchResponse.Context);
        addContainerContext(biosim.client.messages.protocol.QueryRequest.Context);
        addContainerContext(biosim.client.messages.protocol.RemoveNodesRequest.Context);
        addContainerContext(biosim.client.messages.model.MText.Context);
        addContainerContext(biosim.client.messages.protocol.Response.Context);
        addContainerContext(biosim.client.messages.protocol.ResponseBody.Context);
    }
}
