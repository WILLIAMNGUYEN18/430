import java.util.*;

public class Scheduler extends Thread
{
    private Vector queue1;
    
    private Vector queue2;
    private Vector queue3;
    private int timeSlice;
    private static final int DEFAULT_TIME_SLICE = 1000;
    //timeslice/2 
    //timeslice
    //timeslice * 2
    
    
    
    // New data added to p161 
    private boolean[] tids; // Indicate which ids have been used
    private static final int DEFAULT_MAX_THREADS = 10000;

    // A new feature added to p161 
    // Allocate an ID array, each element indicating if that id has been used
    private int nextId = 0;
    private void initTid( int maxThreads ) {
	tids = new boolean[maxThreads];
	for ( int i = 0; i < maxThreads; i++ )
	    tids[i] = false;
    }

    // A new feature added to p161 
    // Search an available thread ID and provide a new thread with this ID
    private int getNewTid( ) {
	for ( int i = 0; i < tids.length; i++ ) {
	    int tentative = ( nextId + i ) % tids.length;
	    if ( tids[tentative] == false ) {
		tids[tentative] = true;
		nextId = ( tentative + 1 ) % tids.length;
		return tentative;
	    }
	}
	return -1;
    }

    // A new feature added to p161 
    // Return the thread ID and set the corresponding tids element to be unused
    private boolean returnTid( int tid ) {
	if ( tid >= 0 && tid < tids.length && tids[tid] == true ) {
	    tids[tid] = false;
	    return true;
	}
	return false;
    }

    // A new feature added to p161 
    // Retrieve the current thread's TCB from the queue
    public TCB getMyTcb( ) {
	Thread myThread = Thread.currentThread( ); // Get my thread object
	synchronized( queue1 ) {
	    for ( int i = 0; i < queue1.size( ); i++ ) {
	    	TCB tcb = ( TCB )queue1.elementAt( i );
	    	Thread thread = tcb.getThread( );
	    	if ( thread == myThread ) // if this is my TCB, return it
	    		return tcb;
	    }
	    
	    for(int i = 0; i < queue2.size(); i++){
	    	TCB tcb = ( TCB )queue2.elementAt( i );
	    	Thread thread = tcb.getThread( );
	    	if ( thread == myThread ) // if this is my TCB, return it
	    		return tcb;
	    }
	    
	    for(int i = 0; i < queue3.size(); i++){
	    	TCB tcb = ( TCB )queue3.elementAt( i );
	    	Thread thread = tcb.getThread( );
	    	if ( thread == myThread ) // if this is my TCB, return it
	    		return tcb;
	    }
	}
	return null;
    }

    // A new feature added to p161 
    // Return the maximal number of threads to be spawned in the system
    public int getMaxThreads( ) {
	return tids.length;
    }

    public Scheduler( ) {
	timeSlice = DEFAULT_TIME_SLICE;
	queue1 = new Vector( );
	queue2 = new Vector();
	queue3 = new Vector();
	initTid( DEFAULT_MAX_THREADS );
    }

    public Scheduler( int quantum ) {
	timeSlice = quantum;
	queue1 = new Vector( );
	queue2 = new Vector();
	queue3 = new Vector();
	initTid( DEFAULT_MAX_THREADS );
    }

    // A new feature added to p161 
    // A constructor to receive the max number of threads to be spawned
    public Scheduler( int quantum, int maxThreads ) {
	timeSlice = quantum;
	queue1 = new Vector( );
	queue2 = new Vector();
	queue3 = new Vector();	
	initTid( maxThreads );
    }
    
    
    private void schedulerSleep( ) {
	try {
	    Thread.sleep( timeSlice / 2 );
	} catch ( InterruptedException e ) {
	}
    }

    // A modified addThread of p161 example
    public TCB addThread( Thread t ) {
	//t.setPriority( 2 );
	TCB parentTcb = getMyTcb( ); // get my TCB and find my TID
	int pid = ( parentTcb != null ) ? parentTcb.getTid( ) : -1;
	int tid = getNewTid( ); // get a new TID
	if ( tid == -1)
	    return null;
	TCB tcb = new TCB( t, tid, pid ); // create a new TCB
	queue1.add( tcb );
	return tcb;
    }

    // A new feature added to p161
    // Removing the TCB of a terminating thread
    public boolean deleteThread( ) {
	TCB tcb = getMyTcb( ); 
	if ( tcb!= null )
	    return tcb.setTerminated( );
	else
	    return false;
    }

    public void sleepThread( int milliseconds ) {
	try {
	    sleep( milliseconds );
	} catch ( InterruptedException e ) { }
    }
    

 
    
    /*
     * Private method utilized during the 3rd queue in which the 2nd queue
     * is checked if empty. If not, it will run through until queue 2 is clear.
     * It parallels the structure of run
     * */
    private void checkQTwo(){
    	Thread current = null;
	    while(queue2.size() > 0) {
	    	try{
	    		if ( queue2.size( ) == 0 ){
	    			return;
	    		}
	    		TCB currentTCB = (TCB)queue2.firstElement( );
				if ( currentTCB.getTerminated( ) == true ) {//if finished in queue 1
				    queue2.remove( currentTCB );
				    returnTid( currentTCB.getTid( ) );
				    return;
				}
				current = currentTCB.getThread( );
				//if current thread (current TCB) is not null and active, resume, if inactive, start.
				if ( current != null ) {
				    if ( current.isAlive( ) ){
				    	//the resume() method resumes (moves the thread to a ready state) a suspended thread.
				    	current.resume();
				    	//current.setPriority( 4 );
				    }else {
					// Spawn must be controlled by Scheduler
					// Scheduler must start a new thread
					current.start( ); 
					//current.setPriority( 4 );
				    
				    }
				}
				
				//thread is put to sleep
				schedulerSleep( );
				if(currentTCB.getTerminated() == true){
					queue2.remove(currentTCB);
					returnTid(currentTCB.getTid());
					return;
				}
				//if ( current != null && !current.isAlive( ) ){
				//}
				
				//need a way to repeat check for queue 1
				//for each sleep that allows CPU performance
				//can build a method for checking
				schedulerSleep( );
				if(currentTCB.getTerminated() == true){
					queue2.remove(currentTCB);
					returnTid(currentTCB.getTid());
					return;
				}
				
				synchronized ( this ) {
					//
				    if ( current != null && current.isAlive( ) )
					//current.setPriority( 2 );
				    //The suspend() method suspends a target thread,  
				    current.suspend();
				    queue2.remove( currentTCB ); // rotate this TCB to the end
				    //queue1.add( currentTCB );
				    queue3.add(currentTCB);
				    
				}
	    		
	    	} catch ( NullPointerException e3 ) { };
	    }
    }
    
    /*
     * Private method utilized during the 2nd and 3rd queue in which the first queue
     * is checked if empty. If not, it will run through until queue 1 is clear.
     * It parallels the structure of run
     * */
    private void checkQOne(){
    	Thread current = null;
    	while(queue1.size() > 0) {
	    	try{
	    		if ( queue1.size( ) == 0 ){
	    			return;
	    		}
	    		TCB currentTCB = (TCB)queue1.firstElement( );
				if ( currentTCB.getTerminated( ) == true ) {//if finished in queue 1
				    queue1.remove( currentTCB );
				    returnTid( currentTCB.getTid( ) );
				    return;
				}
				current = currentTCB.getThread( );
				//if current thread (current TCB) is not null and active, resume, if inactive, start.
				if ( current != null ) {
				    if ( current.isAlive( ) ){
				    	//the resume() method resumes (moves the thread to a ready state) a suspended thread.
				    	current.resume();
				    	//current.setPriority( 4 );
				    }else {
					// Spawn must be controlled by Scheduler
					// Scheduler must start a new thread
					current.start( ); 
					//current.setPriority( 4 );
				    
				    }
				}
				
				//thread is put on CPU activation for set timeslice
				schedulerSleep( );
		
				synchronized ( this ) {
					
				    if ( current != null && current.isAlive( ) )
				    //The suspend() method suspends a target thread,  
				    current.suspend();
				    queue1.remove( currentTCB ); // rotate this TCB to the end
				    //queue1.add( currentTCB );
				    queue2.add(currentTCB);
				    
				}
	    		
	    	} catch ( NullPointerException e3 ) { };
    	}
    }



    /*
     * Run deals with checking if queues are empty, 
     * if the TCB is finished, If the thread is active, 
     * and executing for 500ms, checking for higher queues inbetween multiple instances.
     * */
    // A modified run of p161
    /*
     * This is the heart of Scheduler. 
     * The difference from the lecture slide includes: 
     * (1) retrieving a next available TCB rather than a thread from the active thread list, 
     * (2) deleting it if it has been marked as "terminated", and 
     * (3) starting the thread if it has not yet been started. 
     * Other than this difference, the Scheduler repeats retrieving a next available TCB from the list, 
     * raising up the corresponding thread's priority, 
     * yielding CPU to this thread with sleep( ), and lowering the thread's priority. 
     * 
     * */
    public void run( ) {
	Thread current = null;

	//this.setPriority( 6 );

	while ( true ) {
	    try {
		// get the next TCB and its thread
	    //if queue 1 is empty, move on
	    int q = 1;
	    TCB currentTCB = null;
		if ( queue1.size( ) == 0 ){
			q++;//q == 2
			 //continue;// potentially need to check for further queues?
			if(queue2.size() == 0){
				q++;//q == 3
				if(queue3.size() == 0){
					continue;
				}
				if(q == 3){
					//if queue 1 & 2 are empty but queue 3 isn't, do this.
					currentTCB = (TCB)queue3.firstElement();
					if(currentTCB.getTerminated() == true){
						queue3.remove(currentTCB);
						returnTid(currentTCB.getTid());
						continue;
					}
					current = currentTCB.getThread();
					
					if( current != null){
						if(current.isAlive()){
							current.resume();
						}
						else{
							current.start();
						}
					}
					

					schedulerSleep( );
					checkQTwo();
					checkQOne();
					if(currentTCB.getTerminated() == true){
						queue3.remove(currentTCB);
						returnTid(currentTCB.getTid());
						continue;
					}
					
					
					schedulerSleep( );
					//checkQOne();
					checkQTwo();
					checkQOne();
					if(currentTCB.getTerminated() == true){
						queue3.remove(currentTCB);
						returnTid(currentTCB.getTid());
						continue;
					}
					
					schedulerSleep( );
					//checkQOne();
					checkQTwo();
					checkQOne();
					if(currentTCB.getTerminated() == true){
						queue3.remove(currentTCB);
						returnTid(currentTCB.getTid());
						continue;
					}
					
					schedulerSleep( );
					//checking queue 2 recursively checks queue 1, don't need both.
					//checkQOne();
					checkQTwo();
					checkQOne();
					if(currentTCB.getTerminated() == true){
						queue3.remove(currentTCB);
						returnTid(currentTCB.getTid());
						continue;
					}

				}
				
				
			}
			//if queue 1 is empty and queue 2 isn't, do this
			if(q == 2){
				currentTCB = (TCB)queue2.firstElement();
				if(currentTCB.getTerminated() == true){
					queue2.remove(currentTCB);
					returnTid(currentTCB.getTid());
					continue;
				}
				current = currentTCB.getThread();
				
				if( current != null){
					if(current.isAlive()){
						current.resume();
					}
					else{
						current.start();
					}
				}
				schedulerSleep( );
				if(currentTCB.getTerminated() == true){
					queue2.remove(currentTCB);
					returnTid(currentTCB.getTid());
					continue;
				}
				checkQOne();

				schedulerSleep( );
				checkQOne();
				if(currentTCB.getTerminated() == true){
					queue2.remove(currentTCB);
					returnTid(currentTCB.getTid());
					continue;
				}

			}
		}
		//if queue 1 isn't empty, do this   
		if(q == 1){
			currentTCB = (TCB)queue1.firstElement( );
			if ( currentTCB.getTerminated( ) == true ) {//if finished in queue 1
			    queue1.remove( currentTCB );
			    returnTid( currentTCB.getTid( ) );
			    continue;
			}
			current = currentTCB.getThread( );
			//if current thread (current TCB) is not null and active, resume, if inactive, start.
			if ( current != null ) {
			    if ( current.isAlive( ) )
			    	//the resume() method resumes (moves the thread to a ready state) a suspended thread.
			    	current.resume();
			    	//current.setPriority( 4 );
			    else {
				// Spawn must be controlled by Scheduler
				// Scheduler must start a new thread
				current.start( ); 
				//current.setPriority( 4 );
			    
			    }
			}
			
			//thread is put to sleep
			schedulerSleep( );
	
			
		}
		synchronized ( this ) {
			//
		    if ( current != null && current.isAlive( )  && q == 1)
		    {

		    //The suspend() method suspends a target thread,  
		    current.suspend();
		    queue1.remove( currentTCB ); // rotate this TCB to the end
		    //queue1.add( currentTCB );
		    queue2.add(currentTCB);
		    }
		    else if ( current != null && current.isAlive( )  && q == 3)
		    {

		    //The suspend() method suspends a target thread,  
		    current.suspend();
		    queue3.remove( currentTCB ); // rotate this TCB to the end
		    queue3.add(currentTCB);
		    }

		    else if ( current != null && current.isAlive( )  && q == 2)
		    {

			    //The suspend() method suspends a target thread,  
			    current.suspend();
			    queue2.remove( currentTCB ); // rotate this TCB to the end
			    //queue1.add( currentTCB );
			    queue3.add(currentTCB);
		    }
		    
		    	
		}
		
	    } catch ( NullPointerException e3 ) { };
	}
    }
}
 
