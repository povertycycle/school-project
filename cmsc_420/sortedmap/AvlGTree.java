package cmsc420.sortedmap;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

/* ================================================================================================================= */
/* Code adaptation from https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/AVLTreeST.java.html
/* and https://gist.github.com/nandor/9518116, and https://www.geeksforgeeks.org/avl-tree-set-1-insertion/.
/* ================================================================================================================= */

public class AvlGTree<K, V> extends AbstractMap<K, V> implements SortedMap<K, V> {
	/* ============================================================================================================= */
	/* AVLGTree Fields. 
	/* ============================================================================================================= */
	/* Root node. */
	public AVLGNode root;
	/* Maximum height difference by g. */
	private final int g;
	/* Number of structural modification made to the tree. */
	private int modCount = 0;
	/* Comparator that is used for the tree. */
	private Comparator<? super K> comp;
	/* Entryset */
	private transient EntrySet entrySet = null;
	/* Tree size. */
	private int size;
	/* ============================================================================================================= */
	/* Inner class AVLG nodes. 
	/* ============================================================================================================= */
	@SuppressWarnings("serial")
	public class AVLGNode extends AbstractMap.SimpleEntry<K, V> implements SortedMap.Entry<K,V> {
		private AVLGNode left, right, parent;
		private int height;
		/* Constructor */
		public AVLGNode(K key, V value, int h, AVLGNode parent) {	
			super(key, value);
			this.parent = parent;
			this.height = h;
		}
		/* Return the height of the node. */
		public int height() {
			return this.height; 
		}	
		/* Returns the left child. */
		public AVLGNode getLeft() { return this.left; }
		/* Returns the right child. */
		public AVLGNode getRight() { return this.right; }
		/* Returns the parent. */
		public AVLGNode getParent() { return this.parent; }
	}
	/* ============================================================================================================= */	
	/* Constructor AVLGTree.
	/* ============================================================================================================= */
	public AvlGTree() { 
		this.g = 1;
		this.comp = defaultComp;
	}
	public AvlGTree(final Comparator<? super K> c) {
		this.g = 1;
		if (c == null) this.comp = defaultComp; 
		else this.comp = c;
	}
	public AvlGTree(final int g) {
		this.g = g;
		this.comp = defaultComp;
	}
	/* Constructor with comparator */
	public AvlGTree(final Comparator<? super K> comp, final int g) {
		this.g = g;
		this.comp = comp;
	}
	/* Comparator */
	public Comparator<K> defaultComp = new Comparator<K>() {
		@SuppressWarnings("unchecked")
		public int compare(K k1, K k2) {
			return ((Comparable<? super K>) k1).compareTo(k2);
		}
	};
	/* ============================================================================================================= */
	/* These are the methods that we do not need to implement.
	/* ============================================================================================================= */
	public SortedMap<K, V> headMap(K arg0) { throw new UnsupportedOperationException();	}
	public SortedMap<K, V> tailMap(K arg0) { throw new UnsupportedOperationException();	}
	public Set<K> keySet() { throw new UnsupportedOperationException(); }	
	public Collection<V> values() { throw new UnsupportedOperationException(); }
	public void remove(AVLGNode x) { throw new UnsupportedOperationException(); }
	/* ============================================================================================================= */
	/* SubMap and EntrySet as backing structure of AVLGTree.
	/* ============================================================================================================= */	
	public class SubMap extends AbstractMap<K, V> implements SortedMap<K, V> {
		K lo, hi;
		/* Constructor for submap. */
		public SubMap(K fromKey, K toKey) {
			if (fromKey == null || toKey == null) { throw new NullPointerException(); }
			this.lo = fromKey;
			this.hi = toKey;
		}
		/* ========================================================================================================= */
		/* Public methods.
		/* ========================================================================================================= */
		public void clear() { AvlGTree.this.clear(); }
		public Comparator<? super K> comparator() { return comp; }
		public boolean containsKey(Object key) { 
			if (key == null) throw new NullPointerException();
			return helperInRange((K) key) && AvlGTree.this.containsKey(key); 
		}
		public Set entrySet() { 
			return new SubMapEntrySet(this.lo, this.hi); 
		}
		public K firstKey() { 
			Set es = this.entrySet();
			Iterator iter = es.iterator();
			AVLGNode first = (AVLGNode) iter.next();
			if (first == null) throw new NullPointerException();
			while (!helperInRange(first.getKey()) && iter.hasNext()) {
				first = (AVLGNode) iter.next();
			}
			return first.getKey();			
		}
		public V get(Object key) { 
			if (key == null) throw new NullPointerException();
			if (helperInRange((K) key) == false) return null;
			else {
				return AvlGTree.this.get(key); 
			}
		}
		public K lastKey() { 
			Set es = this.entrySet();
			Iterator iter = es.iterator();
			AVLGNode last = (AVLGNode) iter.next();
			if (last == null) throw new NullPointerException();
			while (helperInRange(last.getKey()) && iter.hasNext()) {
				last = (AVLGNode) iter.next();
			}
			return last.getKey();	
		}
		public V put(K key, V value) {
			if (key == null || value == null) throw new NullPointerException();
			if (!helperInRange(key)) throw new IllegalArgumentException();
			return AvlGTree.this.put(key, value);
		}
		public SortedMap<K, V> subMap(K fromKey, K toKey) { 
			if (fromKey == null || toKey == null) throw new NullPointerException();
			if (comp.compare(fromKey, toKey) > 0) throw new IllegalArgumentException();
			if (!helperInRange(fromKey) || !helperInRange(toKey)) throw new IllegalArgumentException();
			return new SubMap(fromKey, toKey); 
		}
		/* ========================================================================================================= */
		/* Helper methods.
		/* ========================================================================================================= */
		/* Checks if the key is in the range of the submap. */
		public boolean helperInRange(K key) {
			if (key == null) throw new NullPointerException();
			int cmpFromKey = comp.compare(key, this.lo);
			int cmpToKey = comp.compare(this.hi, key);
			if (cmpFromKey >= 0 && cmpToKey > 0) { return true;	} 
			else { return false; }
		}
		/* ========================================================================================================= */
		/* Inner class of the submap class for entry set.
		/* ========================================================================================================= */
		protected class SubMapEntrySet extends AbstractSet<Map.Entry<K, V>> {
			K lo, hi;
			/* Constructor. */
			public SubMapEntrySet(K lo, K hi) { this.lo = lo; this.hi = hi; }
			/* ===================================================================================================== */
			/* Public methods.
			/* ===================================================================================================== */
			public void clear() { AvlGTree.this.clear(); }
			public Iterator iterator() { 
				return new EntryIterator(getFirstSubEntry(this.lo, this.hi)); 
			}
			public int size() { return size(root); }
			public int size(AVLGNode node) {
				if (node != null) {
					return helperInRange(node.getKey()) ? 1 + size(node.left) + size(node.right) : size(node.left) + size(node.right); 
				}
				else {
					return 0;
				}
			}
			/* ===================================================================================================== */
			/* These are the methods that we do not need to implement.
			/* ===================================================================================================== */
			public SortedSet<AVLGNode> headSet(Map.Entry<K, V> arg0) { throw new UnsupportedOperationException(); }
			public SortedSet<AVLGNode> subSet(Map.Entry<K, V> arg0, AVLGNode arg1) { throw new UnsupportedOperationException(); }
			public SortedSet<AVLGNode> tailSet(Map.Entry<K, V> arg0) {	throw new UnsupportedOperationException(); }
			public Set<K> keySet() { throw new UnsupportedOperationException(); }	
			public Collection<V> values() { throw new UnsupportedOperationException(); }
			public void remove(Map.Entry<K, V> x) { throw new UnsupportedOperationException(); }
			public boolean add(Map.Entry<K, V> e) { throw new UnsupportedOperationException();	}
			public boolean addAll(Collection<? extends Map.Entry<K, V>> c) { throw new UnsupportedOperationException(); }		
			/* ===================================================================================================== */
			/* Inner class for the iterator helper.
			/* ===================================================================================================== */
			final class EntryIterator implements Iterator<Map.Entry<K, V>> {
				AVLGNode next, lastreturned = null;
				int expectedModCount;
				
				EntryIterator(AVLGNode first) {
					this.expectedModCount = modCount;
					this.next = first;
				}
				public final boolean hasNext() {
					return next != null;
				}
				public final Map.Entry<K, V> next() {
					Map.Entry<K, V> e = next; 
					if (e == null) throw new NoSuchElementException(); 
					if (modCount != expectedModCount) throw new ConcurrentModificationException(); 
					if (successor((AVLGNode) e) != null && comp.compare(successor((AVLGNode) e).getKey(), hi) < 0) { 
						next = successor((AVLGNode) e); 
						return e; 
					} else { 
						next = null; 
						return e; 
					}
				}
			}
		}
		/* ========================================================================================================= */
		/* These are the methods that we do not need to implement.
		/* ========================================================================================================= */
		public SortedMap<K, V> headMap(K toKey) { throw new UnsupportedOperationException(); }
		public Set<K> keySet() { throw new UnsupportedOperationException(); }
		public SortedMap<K, V> tailMap(K fromKey) { throw new UnsupportedOperationException(); }
		public Collection<V> values() { throw new UnsupportedOperationException(); }
		public V remove(Object key) { throw new UnsupportedOperationException(); }		
	}
	/* Follows above documentation */
	/* ============================================================================================================= */
	/* Inner class for EntrySet for the tree.
	/* ============================================================================================================= */
	class EntrySet extends AbstractSet<Map.Entry<K, V>> {
		/* ========================================================================================================= */
		/* Public methods.
		/* ========================================================================================================= */
		public void clear() { AvlGTree.this.clear(); }
		public Iterator<Map.Entry<K, V>> iterator() { 
			return new EntryIterator(firstEntry()); 
		}
		public int size() { return AvlGTree.this.size; }
		/* ========================================================================================================= */
		/* Inner class EntrySet Iterator. 
		/* ========================================================================================================= */
		final class EntryIterator implements Iterator<Map.Entry<K, V>> {
			AVLGNode next, lastreturned = null;
			int expectedModCount;
			
			EntryIterator(AVLGNode first) {
				this.expectedModCount = modCount;
				this.next = first;
			}
			public final boolean hasNext() {
				return next != null;
			}
			public final Map.Entry<K, V> next() {
				if (next == null) throw new NoSuchElementException();
				if (modCount != expectedModCount) throw new ConcurrentModificationException();
				lastreturned = next;
				this.next = successor(next);
				return lastreturned;
			}
		}
	}
	/* ============================================================================================================= */
	/* Tree methods.
	/* ============================================================================================================= */ 
	/* Returns the maxImbalance. */
	public int g() { return g; }
	/* Return the height of a Tree root. */
	public int height() { return height(root); }
	/* Return the height of a Node. */
	public int height(AVLGNode node) { if (node == null) return -1; return node.height; }
	/* The difference between the children heights. */
	private int balanceFactor(AVLGNode node) {
		if (node == null) return 0;
		return height(node.left) - height(node.right);
	}  
	/* Rotate the tree to the right. */
	private AVLGNode rotateRight(AVLGNode y) {	        
		 AVLGNode x = y.left;
		 AVLGNode T2 = x.right;
		 x.parent = y.parent;
		 y.parent = x;
		 if (T2 != null) T2.parent = y;
		 x.right = y;
		 y.left = T2;
	     y.height = 1 + Math.max(height(y.left), height(y.right));
	     x.height = 1 + Math.max(height(x.left), height(x.right));
	     if (T2 != null) T2.height = 1 + Math.max(height(T2.left), height(T2.right));
		 return x;
	}
	/* Rotate the tree to the right. */
	private AVLGNode rotateLeft(AVLGNode x) {		
		 AVLGNode y = x.right;	
		 AVLGNode T2 = y.left; 
		 y.parent = x.parent;
		 x.parent = y;
		 if (T2 != null) T2.parent = x;
		 y.left = x;	        
		 x.right = T2;
	     x.height = 1 + Math.max(height(x.left), height(x.right));
	     y.height = 1 + Math.max(height(y.left), height(y.right));
	     if (T2 != null) T2.height = 1 + Math.max(height(T2.left), height(T2.right));
		 return y;
	}		
        
	/* Put the Key Value pair into the AVLGTree. */
	public AVLGNode insert(AVLGNode where, K key, V val, AVLGNode parent) {
        if (where == null) { this.size++; return new AVLGNode(key, val, 0, parent); } 
        int cmp = comp.compare(key, where.getKey());
        if (cmp > 0) {
        	where.right = insert(where.right, key, val, where);
        }
        else if (cmp < 0) {
        	where.left = insert(where.left, key, val, where);
        }
        else {
        	where.setValue(val);
        	return where;
        }                
        int balance = this.balanceFactor(where);
		// 4 cases.
		// Left left case.
		if (balance > g && comp.compare(where.left.getKey(), key) > 0) {
			return rotateRight(where);
		}
        // Right Right Case 
        if (balance < -g && comp.compare(key, where.right.getKey()) > 0) {
        	return rotateLeft(where);
        }
        // Left Right Case 
        if (balance > g && comp.compare(where.left.getKey(), key) < 0) {
        	where.left = rotateLeft(where.left);
        	return rotateRight(where);
        } 
        // Right Left Case 
        if (balance < -g && comp.compare(key, where.right.getKey()) < 0) { 
        	where.right = rotateRight(where.right);
            return rotateLeft(where); 
        } 

        where.height = 1 + Math.max(height(where.left), height(where.right));
        /* return the (unchanged) node pointer */
        return where;
    }
	/* ============================================================================================================= */
	/* Methods inherited from SortedMap; the methods below are adapted from the docjar documentation.
	/* ============================================================================================================= */
	/* Return comparator. */
	public Comparator<? super K> comparator() { return this.comp; }	
	/* Returns an entryset of the tree. */
	public Set<Map.Entry<K, V>> entrySet() { 
		if (entrySet == null) entrySet = new EntrySet();
		return entrySet;
	}
	/* Return the key of the first in the tree. */
	public K firstKey() { 
		return key(firstEntry()); 
	}
	/* Return the key of the last in the tree. */
	public K lastKey() {
		return key(lastEntry()); 
	}
	/* Return the submap of the tree. */
	public SortedMap<K, V> subMap(K fromKey, K toKey) { 
		if (fromKey == null || toKey == null) throw new NullPointerException();
		if (comp.compare(fromKey, toKey) > 0) throw new IllegalArgumentException();
		return new SubMap(fromKey, toKey); 
	}
	/* Clears the tree. */
	public void clear() { this.root = null; modCount++;	this.size = 0; }
	/* Checks if the tree contains the key. */
	@SuppressWarnings("unchecked")
	public boolean containsKey(Object key) { 
		return getEntry(key) != null;
	}
	public boolean containsValue(Object value) {
		for (AVLGNode e = firstEntry(); e != null; e = successor(e)) {
			if (valEquals(value, e.getValue())) return true;
		}
		return false;
	}
	/* Get the value of a key in the tree. */
	public V get(Object key) { 
		AVLGNode p = getEntry(key);
		return (p == null ? null : p.getValue());
	}
	/* Puts the key value pair into the tree. */
	public V put(K key, V value) {
		V oldVal = get(key);
		this.root = insert(this.root, key, value, root);
		modCount++;	
		return oldVal;
	}
	/* Returns the size of the tree. */
	public int size() { return this.size; }
	/* ===================================================================================================== */
	/* Helper methods.
	/* ===================================================================================================== */
	/* Return the node of the first in the tree. */
	final AVLGNode firstEntry() {
		AVLGNode p = root;	
		if (p != null)		  
			while (p.left != null) p = p.left;
		return p;
	}
	/* Return the node of the last in the tree. */
	final AVLGNode lastEntry() {
		AVLGNode p = root;
        if (p != null)
        	while (p.right != null) p = p.right;
        return p;
	}
	/* Checks if the value is within range. */
	private boolean helperInRange(K key, K lo, K hi) {
		if (key == null) throw new NullPointerException();
		int cmpFromKey = comp.compare(key, lo);
		int cmpToKey = comp.compare(hi, key);
		if (cmpFromKey >= 0 && cmpToKey > 0) { return true;	} 
		else { return false; }
	}
	/* Get first entry within a range. */
	final AVLGNode getFirstSubEntry(K lo, K hi) {
		Set es = this.entrySet();
		Iterator iter = es.iterator();
		AVLGNode first = null;
		if (iter.hasNext()) {
			first = (AVLGNode) iter.next();
		}
		if (first != null) {
			while (!helperInRange(first.getKey(), lo, hi) && iter.hasNext()) {
				first = (AVLGNode) iter.next();
			}
		}
		return first;
	}
	/* Get first entry within a range. */
	final AVLGNode getLastSubEntry(K lo, K hi) {
		Set es = this.entrySet();
		Iterator iter = es.iterator();
		AVLGNode last = null;
		if (iter.hasNext()) {
			last = (AVLGNode) iter.next();
		}
		if (last != null) {
			while (helperInRange(last.getKey(), lo, hi) && iter.hasNext()) {
				last = (AVLGNode) iter.next();
			}
		}
		return last;	
	}
	/* Return the successor of a node. */
	AVLGNode successor(AVLGNode t) {
		if (t == null) return null;
		else if (t.right != null) {
			AVLGNode p = t.right;
			while (p.left != null)
				p = p.left;
			return p;
		} else {
			AVLGNode p = t.parent;
			AVLGNode ch = t;
			while (p != null && ch == p.right) {
				ch = p;
				p = p.parent;
			}
			return p;
		}
	}
	/* Get the Node from the tree with a key. */
	final AVLGNode getEntry(Object key) {
		if (key == null) throw new NullPointerException();
		K k = (K) key;
		AVLGNode p = this.root;
		while (p != null) {
			int cmp = comp.compare(k, p.getKey());
			if (cmp < 0)
				p = p.left;
			else if (cmp > 0)
				p = p.right;
			else
				return p;
		}
		return null;
	}
	/* Checks if two values are equal. */
	static final boolean valEquals(Object o1, Object o2) {
		return (o1==null ? o2==null : o1.equals(o2));
	}
	/* Get the key. */
	static <K> K key(Entry<K,?> e) {
		if (e==null)
			throw new NoSuchElementException();
		return e.getKey();
	}
	
}
