package A1;

import static org.junit.Assert.*;
import org.junit.Test;


public class ListeImplTest {

	@Test
	public void testCons() {
		Liste list = new ListeImpl();
		list.cons(1);
		list.cons(2);

		assertEquals(2, list.head());
		assertEquals(1, list.head());
	}

	@Test
	public void testHead() {
		Liste list = new ListeImpl();
		list.cons(1);
		list.cons(2);

		assertEquals(2, list.head());
		assertEquals(1, list.length());
		assertEquals(1, list.head());
		assertEquals(0, list.length());
	}

	@Test(expected = NullPointerException.class)
	public void testHeadNeg() {
		Liste list = new ListeImpl();
		list.head();
	}

	@Test
	public void testTop() {
		Liste list = new ListeImpl();
		list.cons(1);
		list.cons(2);

		assertEquals(2, list.top());
		assertEquals(2, list.length());

		list.head();

		assertEquals(1, list.top());
		assertEquals(1, list.length());
	}

	@Test(expected = NullPointerException.class)
	public void testTopNeg() {
		Liste list = new ListeImpl();
		list.top();
	}

	@Test
	public void testLength() {
		Liste list = new ListeImpl();
		list.cons(1);
		list.cons(2);
		assertEquals(2, list.length());
		list.head();
		list.head();
		assertEquals(0, list.length());
	}

	@Test
	public void testIsempty() {
		Liste list = new ListeImpl();
		assertEquals(true, list.isempty());
		list.cons(1);
		assertEquals(false, list.isempty());
	}

	@Test
	public void testInsert() {
		Liste list = new ListeImpl();
		list.cons(1);
		list.cons(2);
		assertEquals(true, list.insert(3, 1));
		list.head();
		assertEquals(3, list.head());
		list.insert(4, 0);
		assertEquals(4, list.head());
		assertEquals(false, list.insert(5, 10));
		assertEquals(1, list.length());
	}

}
