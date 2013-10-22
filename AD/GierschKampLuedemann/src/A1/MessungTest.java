package A1;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;


public class MessungTest {

	@Test
	public void testAdd() {
		List<Double> list1 = Arrays.asList(1.0, 2.0, 3.0);
		List<Double> list2 = Arrays.asList();

		Messung mssg1 = new Messung();
		Messung mssg2 = new Messung();

		mssg1.add(1);
		mssg1.add(2);
		mssg1.add(3);

		assertEquals(list1, mssg1.getMessungen());
		assertEquals(list2, mssg2.getMessungen());
	}

	@Test
	public void testAverage() {
		Messung mssg1 = new Messung();
		Messung mssg2 = new Messung();

		mssg1.add(1);
		mssg1.add(2);
		mssg1.add(3);

		assertEquals(2.0, mssg1.average(), 0.01);
		assertEquals(0.0, mssg2.average(), 0.01);
	}

	@Test
	public void testVarianz() {
		Messung mssg1 = new Messung();
		Messung mssg2 = new Messung();
		Messung mssg3 = new Messung();

		mssg1.add(1);
		mssg1.add(2);
		mssg1.add(3);

		mssg3.add(2);
		mssg3.add(2);
		mssg3.add(5);

		assertEquals(1.0, mssg1.varianz(), 0.01);
		assertEquals(0.0, mssg2.varianz(), 0.01);
		assertEquals(Math.sqrt(3), mssg3.varianz(), 0.01);
	}
}
