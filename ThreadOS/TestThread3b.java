Class  TestThread3b extends Thread{
    private String name;
    private String iter;
    private long submissionTime;
    private long responseTime;
    private long completionTime;
    
    public TestThread3b(string args[]){
        name = args[0];
        iter = args[1];
        //c
    }
    public void run(){
        SysLib.cout( "Thread[" + name + iter +"] is running\n" );
        responseTime = new Date( ).getTime( );
        byte[] buffer = new byte[512]; 
        for ( int i = 0; i < 10000; i++ ){ 
            SysLib.rawread( i, buffer ); 
        }
        completionTime = new Date( ).getTime( );
        SysLib.cout( "Thread[" + name + iter +"]:" +
                 " response time = " + (responseTime - submissionTime) +
                 " turnaround time = " + (completionTime - submissionTime)+
                 " execution time = " + (completionTime - responseTime)+
                 "\n");

        SysLib.exit();

    }
}