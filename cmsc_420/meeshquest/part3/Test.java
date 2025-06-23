package cmsc420.meeshquest.part3;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import cmsc420.sortedmap.AvlGTree;

public class Test {

	@org.junit.Test
	public void test() {
		TreeMap<Integer, Integer> a = new TreeMap<Integer, Integer>();
		AvlGTree<Integer, Integer> b = new AvlGTree<Integer, Integer>();
		SortedMap<Integer,Integer> a1 = a.subMap(3, 10);
		SortedMap<Integer, Integer> b1 = b.subMap(3, 10);
		for (int i = 0; i < 10; i++) {
			a.put(i, i);
			b.put(i, i);
		}
		a.remove(9);
		b.remove(9);
		System.out.println(a1.hashCode());
		System.out.println(b1.hashCode());
		Iterator iter1 = a.entrySet().iterator();
		Iterator iter2 = b.entrySet().iterator();
		while (iter1.hasNext()) {
			System.out.println(iter1.next());
		}
		while (iter2.hasNext()) {
			System.out.println(iter2.next());
		}
		
		assertTrue(iter1.equals(iter2));
		assertTrue(iter2.equals(iter1));
		assertTrue(a.equals(b));
		assertTrue(b.equals(a));
		assertTrue(a1.equals(b1));
		assertTrue(b1.equals(a1));
	}

}
