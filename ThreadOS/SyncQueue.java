
public class SyncQueue {
	private QueueNode[] queue;
	
	/*
	 * one QueueNode object per condition supported by the SyncQueue.
	 * */
	
	// You can use the java monitor support 
	//(wait() / notify()) to implement this class.  
	public SyncQueue(){
		queue = new QueueNode[10];
	}
	
	public SyncQueue(int condMax){
		queue = new QueueNode[condMax];
	}
	
	public Thread enqueueAndSleep(){
		
		return null;
		
	}
	
	public void dequeueAndWakeup(int condition){
		
	}
	
	public void dequeueAndWakuep(int condition, int tid){
		
	}

}
