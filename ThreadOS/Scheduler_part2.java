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
	synchronized( queue ) {
	    for ( int i = 0; i < queue.size( ); i++ ) {
		TCB tcb = ( TCB )queue.elementAt( i );
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
	    Thread.sleep( timeSlice );
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
    
    // A modified run of p161
    public void run( ) {
	Thread current = null;

	//this.setPriority( 6 );
	
	while ( true ) {
	    try {
		// get the next TCB and its thread
	    //if queue 1 is empty, move on
		if ( queue1.size( ) == 0 )
		    continue;// potentially need to check for further queues?
		TCB currentTCB = (TCB)queue1.firstElement( );
		if ( currentTCB.getTerminated( ) == true ) {//if finished in queue 1
		    queue1.remove( currentTCB );
		    returnTid( currentTCB.getTid( ) );
		    continue;
		}
		current = currentTCB.getThread( );
		//if current thread (current TCB) is not null and active, resume, if inactive, start.
		if ( current != null ) {
		    if ( current.isAlive( ) )
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
		// System.out.println("* * * Context Switch * * * ");
		
		
		//find out Synchronized
		//https://stackoverflow.com/questions/1085709/what-does-synchronized-mean
		/*
		 * synchronized methods enable a simple strategy for preventing thread 
		 * interference and memory consistency errors: if an object is visible to 
		 * more than one thread, all reads or writes to that object's variables are 
		 * done through synchronized methods.
		 * 
		 * 
		 * */
		synchronized ( queue1 ) {
			//
		    if ( current != null && current.isAlive( ) )
			//current.setPriority( 2 );
		    current.suspend();
		    queue1.remove( currentTCB ); // rotate this TCB to the end
		    queue1.add( currentTCB );
		    //Should rotate to end of next queue (queue2)
		    //should then activate in next queue
		    //should then synchronize for that queue
		    //Should rotate to end of third queue (queue3)
		    //should then activate in 3rd queue
		    //should then synchronize in 3rd queue.
		}
	    } catch ( NullPointerException e3 ) { };
	}
    }
}
 
/*
 * 
 * 
Need to figure out how to run for quantum amount of time?
add parameter for scheduleSleep to change timeSlice
 
1. It has three queues: 0, 1 and 2
Initialize all 3 queues

2. A new thread's TCB is always enqueued into queue 0
addThread the same? (always enqueue to queue0)

3. Your scheduler first executes all threads in queue 0. The queue 0's time quantum is timeslice/2, i.e., half of the one
used in Part 1 Round-robin scheduler
//on run, execute all of queue 0 (quantum = timeslice/2?

4. If a thread in the queue 0 does not complete its execution for queue 0's time slice, (i.e., timeSlice / 2 ), the
scheduler moves the corresponding TCB to queue 1.
Need to check if finished. (If finished, remove, return, continue, if not,)


5. If queue 0 is empty, it will execute threads in queue 1. The queue 1's time quantum is the same as the one in Part
1's round-robin scheduler, (i.e., timeSlice). However, in order to react new threads in queue 0, your scheduler
should execute a thread in queue 1 for timeSlice / 2 and then check if queue 0 has new TCBs. If so, it will execute
all threads in queue 0 first, and thereafter resume the execution of the same thread in queue 1 for another
timeSlice / 2.
//Need to recheck queue 0 after executing in queue 1 for timeSlice/2
need to keep track of how many timeslices have been executed.


6. If a thread in queue 1 does not complete its execution for queue 1's time quantum, (i.e., timeSlice ), the scheduler
then moves the TCB to queue 2.
//move to third queue


7. If both queue 0 and queue 1 is empty, it can execute threads in queue 2. The queue 2's time quantum is a double
of the one in Part 1's round-robin scheduler, (i.e., timeSlice * 2). However, in order to react threads with higher
priority in queue 0 and 1, your scheduler should execute a thread in queue 2 for timeSlice / 2 and then check if
queue 0 and 1 have new TCBs. The rest of the behavior is the same as that for queue 1.
//Need to recheck queue 0 after executing in queue 3 for timeSlice/2
need to keep track of how many timeslices have been executed.

8. If a thread in queue 2 does not complete its execution for queue 2's time slice, (i.e., timeSlice * 2 ), the scheduler
puts it back to the tail of queue 2. (This is different from the textbook example that executes threads in queue 2
with FCFS, see figure 1 below.)
just requeue if unfinished
 * */
