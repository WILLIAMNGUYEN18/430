import java.util.Date; 

 

class Test3 extends Thread { 

 

    private int pairs; 

 

    public Test3 ( String args[] ) { 

        pairs = Integer.parseInt( args[0] ); 

    } 

 

    public void run( ) { 

        String[] args1 = … // parse arguments for computationally intensive test program, e.g., "TestThread3 comp" 

        String[] args2 = … // parse arguments for disk intensive test program  

        long startTime = (new Date( ) ).getTime( ); 

        
        for(int i = 0; i < pairs; i++){
            SysLib.exec( args1 );
            SysLib.exec( args2 );
        
        }

        // write code to exec pairs number of computationally intensive and disk intensive  

        // so you will have pairs*2 threads 
        /*
    public static String[] stringToArgs( String s ) {
        StringTokenizer token = new StringTokenizer( s," " );
        String[] progArgs = new String[ token.countTokens( ) ];
        for ( int i = 0; token.hasMoreTokens( ); i++ ) {
            progArgs[i] = token.nextToken( );
        }
	return progArgs;
    }
        */

        

        // write code to wait for each test to finish (i.e., you need to join pairs*2 times) 

        for(int j = 0; j < pairs; j++){
            SysLib.join();
            SysLib.join();
        }
        long endTime = (new Date( ) ).getTime( ); 

    

    

        SysLib.cout( "elapsed time = " + ( endTime - startTime ) + " msec.\n" ); 

        SysLib.exit( );     

    } 

} 