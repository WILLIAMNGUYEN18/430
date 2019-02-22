
import java.util.*;
public class SyncQueue {
	//private QueueNode[] queue;
	private Vector<QueueNode> queue;
	//can be Vector<QueueNode> queue;
	private int size;
	
	private int counter;
	private boolean using = true;
	
	/*
	 * one QueueNode object per condition supported by the SyncQueue.
	 * */
	
	// You can use the java monitor support 
	//(wait() / notify()) to implement this class.
	//https://www.baeldung.com/java-wait-notify
	
	/*
	 * each condition in the wait queue corresponds to a ThreadOS thread, 
	 * which can sleep on its corresponding condition variable.   
	 * */
	
	/*
	 * Constructors that create a queue and allow threads to wait for 
	 * a default condition number (of 10) or a condMax number of condition/event types. 
	 * */
	public SyncQueue(){
		size = 10;
		queue = new Vector<QueueNode>(size);
		counter = 0;
	}
	
	public SyncQueue(int condMax){
		//size = condMax;
		size = condMax;
		queue = new Vector<QueueNode>(size);
		counter = 0;
	}
	
	/*
	 * enqueues the calling thread into the queue 
	 * and sleeps it until a given condition is satisfied. 
	 * It returns the ID of a child thread that has woken the calling thread. 
	 * */
	//condition is calling thread and PID
	public int enqueueAndSleep(int condition){
		//enqueue calling thread?
		
		QueueNode temp = new QueueNode(condition);
		queue.add(temp);
		counter++;
		int ind = queue.indexOf(temp);
		System.out.println("ENQUEUE CAP:" + queue.size());
		System.out.println(queue);
		System.out.println("COUNTER: " + counter);
		
		
		try {
			synchronized(queue.get(ind)){
			//no need for while?
				queue.get(ind).wait();
				
			}
		} catch (InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		//java monitor is using synchronized
		//We are trying to create our own ThreadOS monitor?
		
		
		//wait condition is child's TID
		//while() //wait until child TID is done?
		//sleep
		return 0;
		
	}
	/*
	 * 
synchronized( lockObject )
{ 
	while( ! condition )
	{ 
		lockObject.wait();
	}
	
	//take the action here;
}
	 * */
	
	/*
	 * dequeues and wakes up a thread waiting for a given condition. 
	 * If there are two or more threads waiting for the same condition, 
	 * only one thread is dequeued and resumed. 
	 * The FCFS (first-come-first-service) order does not matter. 
	 * This function can receive the calling thread's ID, (tid) as the 2nd argument. 
	 * This tid will be passed to the thread that has been woken up from enqueueAndSleep. 
	 * If no 2nd argument is given, you may regard tid as 0. 
	 * */
	public void dequeueAndWakeup(int condition){
		
		dequeueAndWakeup(condition, 0);
	}

	public void dequeueAndWakeup(int condition, int tid){
		System.out.println("DEQUEUE REACHED");
		System.out.println("COUNTER: " + counter);
		//checking for thread waiting on given condition
		//how do I find correct node?
		//int ind = queue.indexOf(QueueNode(condition));
		
		//have to iterate?
		//for(int i = 0; i < counter; i++) {
		//for(int i = 0; i < queue.capacity(); i++) {
		for(int i = 0; i < queue.size(); i++) {
			System.out.println("FUCK" + i);
			//System.out.println("QUEUE" + queue.capacity());
			System.out.println("QUEUE" + queue.size());
			System.out.println(queue);
			synchronized(queue.get(i)){
				System.out.println("QUEUE COND AT I: " + queue.get(i).getCOND());
				System.out.println("CONDITION: " + condition);

				//if(queue.get(i) != null) { //BREAKS ON THIS LINE, ARRAY INDEX OUT OF RANGE: 0
					if(queue.get(i).getCOND() == condition) {
						//synchronized(queue.get(i)){
						System.out.println("CONDITION REACHED");
						System.out.println("COUNTER: " + counter);
						System.out.println(queue);
						QueueNode temp = queue.get(i);
						//TID passed to thread that has been woken up?
						temp.setTID(tid);
						//dequeue
						queue.remove(temp);
						//resume with notify
						System.out.println("REMOVAL ATTEMPTED");
						System.out.println(queue);
						temp.notify();
						counter--;
						System.out.println("COUNTER: " + counter);
							//return directly or change value in queueNode?
							//or externally, child thread already knows? THIS ONE
							//}
						break;//only do 1
					}
				//}
			}

		}

	}
	/*
	 * 
	 * 
		for(int i = 0; i < counter; i++) {
			//if there is a thread for condition
			if (queue[i].getTID() == condition) {
				//dequeue, assuming by removing from array and shuffling the array
				synchronized (queue[i]) {
				//do I need to remove?
				
				//or do I need to shuffle?
				for(int j = i; j < counter; j++) {
					if(j+1 < queue.length) {
						queue[j] = queue[j+1];
					}
				}
				
				//resume thread
				queue[i].notify();
				}
				break;
			}
		}
		counter--;
	 * */
}
