
public class QueueNode {
	/*
	 * QueueNode objects, each representing a different condition and 
	 * enqueuing all threads that wait for this condition. 
	 * You have to implement your own QueueNode.java. The size of 
	 * the queue array should be given through a constructor whose 
	 * spec is given below. 
	 * */
	
	//thread ID is tid, which is an integer
	private int TID;
	/*
	 * "waitingQueue calling thread’s ID as an independent waiting condition" 
	 * condition used by waitQueue to track the thread is equal to the thread's ID 
	 * And thread TID is an integer from what I understand. so when we pull TCB.getID(), 
	 * and pass it, would that integer effectively be the condition stored (as an integer) in the queueNode?
	 * */
	
	public QueueNode(){
		this.TID = 0;
	}
	
	public QueueNode(int id){
		this.TID = id;
	}
	
	public int getTID(){
		return this.TID;
	}
	

}
