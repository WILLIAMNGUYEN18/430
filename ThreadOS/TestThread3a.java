Class  TestThread3a extends Thread{
    private String name;
    private String iter;
    private long submissionTime;
    private long responseTime;
    private long completionTime;

    public TestThread3a(string args[]){
        name = args[0];
        iter = args[1];
        //c
    }
    public void run(){
        SysLib.cout( "Thread[" + name + iter + "] is running\n" );
        responseTime = new Date( ).getTime( );
        for ( int i = 0; i < Integer.MAX_VALUE / 2; i++ ) {
            double ans = Math.pow( Math.sqrt( Math.sqrt( i ) * Math.sqrt( i ) ), 2.0 );
        }
        completionTime = new Date( ).getTime( );
        SysLib.cout( "Thread[" + name + "]:" +
                 " response time = " + (responseTime - submissionTime) +
                 " turnaround time = " + (completionTime - submissionTime)+
                 " execution time = " + (completionTime - responseTime)+
                 "\n");

        SysLib.exit();

    }

}