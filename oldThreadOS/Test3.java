import java.util.Date; 

 

class Test3 extends Thread { 

 

    private int pairs; 

 

    public Test3 ( String args[] ) { 

        pairs = Integer.parseInt( args[0] ); 

    } 

 

    public void run( ) { 

        String[] args1 = SysLib.stringToArgs( "TestThread3a a" );//… // parse arguments for computationally intensive test program, e.g., "TestThread3 comp" 

        String[] args2 = SysLib.stringToArgs( "TestThread3b b" );//… // parse arguments for disk intensive test program  
        /*
        String[] args1 = 
	    String[] args2 = SysLib.stringToArgs( "TestThread2 b 1000 0" );
	
        */
        long startTime = (new Date( ) ).getTime( ); 

        //System.out.println(args1);
        //System.out.println(args2);
        
        
        for(int i = 0; i < pairs; i++){

            SysLib.exec( args1 );
            SysLib.exec( args2 );
        
        }



        // write code to wait for each test to finish (i.e., you need to join pairs*2 times) 
        System.out.println("Completed Execution, Joining");
        for(int j = 0; j < pairs; j++){
            SysLib.join();
            SysLib.join();
        }
        long endTime = (new Date( ) ).getTime( ); 
        System.out.println("Completed Joins, Calculating time");
    

    

        SysLib.cout( "elapsed time = " + ( endTime - startTime ) + " msec.\n" ); 

        SysLib.exit( );     

    } 

} 