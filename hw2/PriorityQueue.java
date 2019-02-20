import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// ng22457
// sa43897



public class PriorityQueue {
	
	int capacity;
	
	Node highest = null;
	
	AtomicInteger currentSize = new AtomicInteger(0);

	ReentrantLock globalLock = new ReentrantLock();
	Condition isFull = globalLock.newCondition();
	Condition isEmpty = globalLock.newCondition();

	public PriorityQueue(int maxSize) {
        // Creates a Priority queue with maximum allowed size as capacity
		
		capacity = maxSize;
		
	}

	public int add(String name, int priority) {
        // Adds the name with its priority to this queue.
        // Returns the current position in the list where the name was inserted;
        // otherwise, returns -1 if the name is already present in the list.
        // This method blocks when the list is full.
		
		try {
		while(currentSize.get() == capacity) {
			globalLock.lock();
			isFull.await();
			globalLock.unlock();
		}
		} catch(Exception e) {
			
		}
		
		Node newNode = new Node(name,priority);
		int return_index = -1;
		Node insert_current = null;
		Node insert_current2 = null;
		
		if(highest == null) {
			highest = newNode;
			currentSize.incrementAndGet();
			globalLock.lock();
			isEmpty.signal();
			globalLock.unlock();
			//System.out.println("0");
			return 0;
		}
		
		Node current = highest;
		
		current.nodeLock.lock();
		
		if(current.priority < priority && !current.name.equals(name)) {
			/*newNode.next = current;
			highest = newNode;
			current.nodeLock.unlock();
			currentSize.incrementAndGet();
			globalLock.lock();
			isEmpty.signal();
			globalLock.unlock();
			return 0;*/
			insert_current = highest;
			return_index = 0;
		} else if(current.name.equals(name)) {
			return -1;
		}
		
		Node current2 = current.next;
		
		if(current2 == null && return_index != 0) {
			current.next = newNode;
			current.nodeLock.unlock();
			currentSize.incrementAndGet();
			//System.out.println("1");
			return 1;
		} else if(current2 == null) {
			highest = newNode;
			newNode.next = current;
			current.nodeLock.unlock();
			currentSize.incrementAndGet();
			return 0;
		}
		current2.nodeLock.lock();
		
		int index = 1;
		
		boolean same_name = false;
		boolean stop_looking = false;
		
		for(int i=0;i<currentSize.get();i++) {
			
			index++;
			
			if(current2.priority < priority && return_index != 0 && !stop_looking) {
				insert_current = current;
				insert_current2 = current2;
				return_index = index;
				stop_looking = true;
			}
			
			current.nodeLock.unlock();
			current = current2.next;
			
			if(current == null) {
				if(return_index == 0) {
					break;
				}
				if(!stop_looking) {
					current2.next = newNode;
					current2.nodeLock.unlock();
					currentSize.incrementAndGet();
					return index;
				} else {
					break;
				}
			}
			
			if(current2.name.equals(name) || current.name.equals(name)) {
				same_name = true;
				break;
			}
			
			current.nodeLock.lock();
			Node temp = current2;
			current2 = current;
			current = temp;
		}
		
		if(same_name) {
			//current.nodeLock.unlock();
			current2.nodeLock.unlock();
			return -1;
		} else if(return_index == 0){
			//current.nodeLock.unlock();
			current2.nodeLock.unlock();
			newNode.next = highest;
			highest = newNode;
			currentSize.incrementAndGet();
			return 0;
		} else {
		
			insert_current.next = newNode;
			newNode.next = insert_current2;
			//current.nodeLock.unlock();
			current2.nodeLock.unlock();
			currentSize.incrementAndGet();
			return return_index-1;
		}
		
	}

	public int search(String name) {
        // Returns the position of the name in the list;
        // otherwise, returns -1 if the name is not found.
		
		int index = 0;
		
		Node current = highest;
		
		current.nodeLock.lock();
		
		Node current2 = highest.next;
		
		current2.nodeLock.lock();
		
		while(index < currentSize.get()) {
			
			if(current.name.equals(name)) {
				current.nodeLock.unlock();
				current2.nodeLock.unlock();
				return index;
			}else if(current2.name.equals(name)) {
				current.nodeLock.unlock();
				current2.nodeLock.unlock();
				return index+1;
			}else {
				current.nodeLock.unlock();
				current = current2.next;
				if(current == null) {
					break;
				}
				current.nodeLock.lock();
				current2.nodeLock.unlock();
				current2 = current.next;
				if(current2 == null) {
					if(current.name.equals(name)) {
						return index+2;
					}else {
						break;
					}
				}
				current2.nodeLock.lock();
				index += 2;
			}
		}
		
		return -1;
	}

	public String getFirst() {
        // Retrieves and removes the name with the highest priority in the list,
        // or blocks the thread if the list is empty.
		
		try {
		while(currentSize.get() == 0) {
			globalLock.lock();
			isEmpty.await();
			globalLock.unlock();
		}
		}catch(Exception e) {
			
		}
		
		Node current = highest;
		
		current.nodeLock.lock();
		
		current.next = null;
		
		currentSize.decrementAndGet();
		
		if(currentSize.get() == capacity-1) {
			globalLock.lock();
			isFull.signal();
			globalLock.unlock();
		}
		
		String result = current.name;
		
		current.nodeLock.unlock();	
		
		return result;
	}
	
	public static void main(String[] args) {
		
		PriorityQueue test = new PriorityQueue(10);
		
		System.out.println(test.add("thread 0", 0));
		System.out.println(test.add("thread 1", 1));
		System.out.println(test.add("thread 2", 3));
		System.out.println(test.add("thread 6", 0));
		System.out.println(test.add("thread 4", 2));
		System.out.println(test.add("thread 5", 3));
		System.out.println(test.add("thread 6", 1));
		System.out.println(test.add("thread 7", 0));
		
		Node current = test.highest;
		for(int i=0;i<test.currentSize.get();i++) {
			System.out.println(current.name + ": " + current.priority);
			current = current.next;
		}
		System.out.println();
		
		System.out.println(test.search("thread 0"));
		System.out.println(test.search("thread 1"));
		System.out.println(test.search("thread 2"));
		System.out.println(test.search("thread 3"));
		System.out.println(test.search("thread 4"));
		System.out.println(test.search("thread 5"));
		System.out.println(test.search("thread 6"));
		System.out.println(test.search("thread 7"));
		
	}
	
	
}

class Node {
	
	String name;
	int priority;
	Node next = null;
	
	ReentrantLock nodeLock;
	
	public Node(String name, int priority) {
		
		this.name = name;
		this.priority = priority;
		
		nodeLock = new ReentrantLock();
		
	}
	
}
