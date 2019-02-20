
public class SyncQueue {
	private QueueNode[] queue;
	
	
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
		queue = new QueueNode[10];
		counter = 0;
	}
	
	public SyncQueue(int condMax){
		queue = new QueueNode[condMax];
		counter = 0;
	}
	
	/*
	 * enqueues the calling thread into the queue 
	 * and sleeps it until a given condition is satisfied. 
	 * It returns the ID of a child thread that has woken the calling thread. 
	 * */
	public int enqueueAndSleep(int condition){
		//enqueue calling thread?
		
		int callingTID = 0;
		queue[counter] = new QueueNode(callingTID);
		
		synchronized(queue[counter]){
			while(!using){
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Thread.currentThread().interrupt(); 
					e.printStackTrace();
				}
			}
			using = false;
		}
		counter++;
		//wait condition is child's TID
		//while() //wait until child TID is done?
		//sleep
		return 0;
		
	}
	
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
		
		int tid = 0;
		//queue.
		counter--;
		
	}
	
	public void dequeueAndWakeup(int condition, int tid){
		
	}

}
