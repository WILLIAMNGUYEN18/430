import java.util.Date; 

 

class Test4 extends Thread { 
    boolean enabled;
    int accType;


    public Test4 ( String args[] ) {
        if(args[0].equals("enabled")){
            enabled = true;
        } else{
            enabled = false;
        }

        accType = Integer.parseInt(args[1]);    
    }


    public void run(){
        //flush first

        //Random accesses: 
        //read and write many blocks randomly across the disk. 
        //Verify the correctness of your disk cache.
        String[] args1 = SysLib.stringToArgs( "TestThread4a a e" );
        if(!enabled){
            args1 = SysLib.stringToArgs( "TestThread4a a d" );
        }
        
        //Localized accesses: 
        //read and write a small selection of blocks many times to 
        //get a high ratio of cache hits. 
        String[] args2 = SysLib.stringToArgs( "TestThread4b b e" );
        if(!enabled){
            args2 = SysLib.stringToArgs( "TestThread4b b d" );
        } 
        
        //Mixed accesses: 
        //90% of the total disk operations should be localized accesses 
        //and 10% should be random accesses. 
        String[] args3 = SysLib.stringToArgs( "TestThread4c c e" );
        if(!enabled){
            args3 = SysLib.stringToArgs( "TestThread4c c d" );
        }
        
        //    Adversary accesses: 
        //generate disk accesses that do not make good use of the disk 
        //cache at all, i.e., purposely accessing blocks to create 
        //cache misses. 
        String[] args4 = SysLib.stringToArgs( "TestThread4d d e" );
        if(!enabled){
            args4 = SysLib.stringToArgs( "TestThread4d d d" );
        } 

        long startTime = (new Date( ) ).getTime( ); 
                


        //potentially 4 different TestThread4 a,b,c,d

        if(accType == 1){
            SysLib.exec(args1);

        } else if(accType ==2){
            SysLib.exec(args2);

        }  else if(accType ==3){
            SysLib.exec(args3);

        }  else{
            SysLib.exec(args4);

        }

        long endTime = (new Date( ) ).getTime( ); 
        System.out.println("Completed EXEC, Calculating time");

        SysLib.cout( "elapsed time = " + ( endTime - startTime ) + " msec.\n" ); 

        SysLib.exit( );   
    }

}