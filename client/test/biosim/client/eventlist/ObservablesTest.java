package biosim.client.eventlist;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import m3.gwt.lang.Function1;

import org.junit.Test;


public class ObservablesTest {

	@Test
	public void testFilterListeners() {
		
		ObservableList<String> source = Observables.create();

		ObservableList<String> filtered = source.filter(new Function1<String, Boolean>() {
			@Override
			public Boolean apply(String t) {
				return true;
			}
		});

		ObservableList<Integer> mapped = filtered.map(new Function1<String, Integer>() {
			@Override
			public Integer apply(String s) {
				return Integer.parseInt(s);
			}
		});
		
		source.add("100");
		
		assertEquals("mapped should have 1 element", 1, mapped.size());
		assertEquals("element should be \"100\"", (Integer) 100, mapped.get(0));
		
		source.remove(0);
		
		assertEquals("mapped should have 0 elements", 0, mapped.size());
		
	}
	

	@Test
	public void testRefilter1() {

		ObservableList<AtomicInteger> source = Observables.create();

		FilteredList<AtomicInteger> filtered = source.filter(new Function1<AtomicInteger, Boolean>() {
			@Override
			public Boolean apply(AtomicInteger t) {
				return t.get() % 2 == 0;
			}
		});

		for ( int i = 0 ; i < 10 ; i++ ) {
			source.add(new AtomicInteger(i));
		}

		for ( int i = 0 ; i < 7 ; i += 2 ) {
			source.get(i).addAndGet(11);
		}
		
		filtered.reapply();
		
		assertEquals(1, filtered.size());
		assertEquals(8, filtered.get(0).get());
		
		
	}
	

	@Test
	public void testRefilter2() {

		ObservableList<AtomicInteger> source = Observables.create();

		FilteredList<AtomicInteger> filtered = source.filter(new Function1<AtomicInteger, Boolean>() {
			@Override
			public Boolean apply(AtomicInteger t) {
				return t.get() % 2 == 0;
			}
		});

		for ( int i = 0 ; i < 10 ; i++ ) {
			source.add(new AtomicInteger(i));
		}

		source.get(0).addAndGet(11);
		filtered.reapply();
		source.remove(1);
		
		assertEquals(4, filtered.size());
		assertEquals(2, filtered.get(0).get());
		assertEquals(4, filtered.get(1).get());
		assertEquals(6, filtered.get(2).get());
		assertEquals(8, filtered.get(3).get());
		
		
	}

	@Test
	public void testRefilter3() {

		ObservableList<AtomicInteger> source = Observables.create();

		FilteredList<AtomicInteger> filtered = source.filter(new Function1<AtomicInteger, Boolean>() {
			@Override
			public Boolean apply(AtomicInteger t) {
				return t.get() % 5 == 0;
			}
		});

		for ( int i = 0 ; i < 10 ; i++ ) {
			source.add(new AtomicInteger(i));
		}

		// the next line was blowing up with an index out of bounds
		source.remove(7); 
		
		filtered.toString();
	
		
	}
	
}
