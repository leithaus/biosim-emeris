package biosim.client.model;


import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ModelTest {

	
	@Test
	public void delinkLabelShouldRemoveChildLabel() {
		
		DataSet dataSet = new DataSet();
		
		Label home = new Label(dataSet, "home");
		Label dude = new Label(dataSet, "dude");
		
		dataSet.nodes.add(home);
		dataSet.nodes.add(dude);
		
		Link link = dataSet.addLink(home, dude);
		
		assertEquals("should have 1 child label", 1, dataSet.links.size());
		
		dataSet.removeLink(link);
		
		assertEquals("should have 0 child labels", 0, dataSet.links.size());
		
	}
	
	
}
