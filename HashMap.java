//Michael Molina
//CSE 373 Section A, Winter 2013 2/8/2013
//Homework #4 HashMaps vs. Zombies
//This program implements a hashMap using a hash table array.
//The hash table uses separate chaining (a linked list in each hash bucket
// index) to resolve collisions.

//import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HashMap<K, V> implements Map<K, V> {
	private static final double MAX_LOAD = 0.75;
	
	private Node[] elements;
	private int size; 
	
	//Constructs a new empty map with a hash table capacity of 10
	@SuppressWarnings("unchecked")
	public HashMap() {
		elements = (Node[]) new HashMap.Node[10];
		size = 0;
	}
	
	//Removes all key/value pairs from the map
	public void clear() {
		for (int i = 0; i < elements.length; i++) {
			elements[i] = null;
		}
		size = 0; 
	}
	
	//Returns true if the map contains the key passed in
	//Throws a NullPointerException if the key passed in is null
	public boolean containsKey(K key) {
		if(key.equals(null)) {
			throw new NullPointerException();
		}
		return get(key) != null;
	}
	
	//Returns the value associated with the passed in key and 
	//if the key is not in the map then this returns null
	//Throws a NullPointerException if the key passed in is null
	public V get(K key) {
		if(key.equals(null)) {
			throw new NullPointerException();
		}
		Node current = foundNode(key);
		if (current != null) {
			return current.value;
		} else {
			return null;
		}
	}
	
	//Returns true if there are now elements in the map otherwise false
	public boolean isEmpty() {
		return size == 0;
	}

	//Returns a new HashSet of keys that are in the map
	public Set<K> keySet() {
		Set<K> keys = new HashSet<K>();
		for(int i = 0; i < elements.length; i++) {
			Node current = elements[i];
			while (current != null) {
				keys.add(current.key);
				current = current.next;
			}
		}
		return keys;
	}
	
	//Adds the given key/value pair to the map and if the key exists already
	//then its value is changed to the new passed in value
	//Throws a NullPointerException if key or value passed in is null.
	public void put(K key, V value) {
		if(key.equals(null) || value.equals(null)) {
			throw new NullPointerException();
		}
		Node current = foundNode(key);
		if (current != null) {
			//key exists already
			current.value = value;
		} else {
			//key doesn't exist so add newNode to map
			int h = hash(key);
			Node newNode = new Node(key, value);
			newNode.next = elements[h];
			elements[h] = newNode;
			size++;
		}
		//resize if necessary
		if (loadFactor() > MAX_LOAD) {
			rehash();
		}	
	}
	
	//Removes the passed in key and its associated value if its in the map
	//Throws a NullPointerException if passed in key is null
	public void remove(K key) {
		if(key.equals(null)) {
			throw new NullPointerException();
		}
		int h = hash(key);
		if(elements[h] != null) {
			if(elements[h].key.equals(key))	{
				//front case
				elements[h] = elements[h].next; 
				size--;
			} else {
				//non-front case
				Node current = elements[h];
				while (current.next != null && !current.next.key.equals(key)) {
					current = current.next;
				}
				//current is not null if we found the key
				if (current.next != null) {
					current.next = current.next.next;
					size--;
				}
			} 
		}
	}

	//Returns an integer representing the number of elements in the map
	public int size() {
		return size;
	}
	
	//Returns a string of all the elements in the map (its keys and its values)
	//For example: {key1=value1, key2=value2}
	public String toString() {
		String s = "{";
		Boolean first = true; //prints the first element differently
		for (int i = 0; i < elements.length; i++) {
			Node current = elements[i];
			while (current != null) {
				if(first) {
					s += current.key + "=" + current.value;
					first = false;
				} else {
					s += ", " +  current.key + "=" + current.value;
				}
				current = current.next;
			}
		}
		return s + "}";
	}
	
	//Returns a node that has the passed in key in it or null if not
	private Node foundNode(K key) {
		int h = hash(key);
		Node current = elements[h];
		while (current != null && !current.key.equals(key)) {
			current = current.next;
		}
		return current;
	}
	
	// Returns the hash table's "load factor", its ratio of size to capacity.
	private double loadFactor() {
		return (double) size / elements.length;
	}
	
	// hash function for mapping values to indexes
	private int hash(K key) {
		return Math.abs(key.hashCode()) % elements.length;
	}
	
	// Resizes the hash table to twice its original capacity.
	@SuppressWarnings("unchecked")
	private void rehash() {
		Node[] newElements = (Node[]) new HashMap.Node[2 * elements.length];
		Node[] old = elements;
		elements = newElements;
		size = 0;
		for (Node node : old) {
			while (node != null) {
				put(node.key, node.value);
				node = node.next;
			}
		}
	}
	
	//Node class that stores a key, value, and a next Node (linked list)
	private class Node {
		public K key;
		public V value;
		public Node next;
		
		public Node(K k, V val) {
			this.key = k;
			this.value = val;
			this.next = null;
		}
		
	}
}