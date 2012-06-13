package biosim.client.eventlist;

public class CompositeList<T> extends ObservableListImpl<T> {
	
	public CompositeList() {		
	}
	
	public void addSourceList(ObservableList<T> list) {
		list.addListener(new FineGrainedListListener<T>() {
			@Override
			public void added(ListEvent<T> event) {
				CompositeList.this.add(event.getElement());
			}
			@Override
			public void changed(ListEvent<T> event) {
			}
			@Override
			public void removed(ListEvent<T> event) {
				CompositeList.this.remove(event);
			}
		});
	}

}
