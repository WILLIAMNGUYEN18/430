import java.util.Date;
class  TestThread3a extends Thread{
    private String name;
    //private String iter;
    private long submissionTime;
    private long responseTime;
    private long completionTime;

    public TestThread3a(String args[]){
        name = args[0];
        //iter = args[1];
        submissionTime = new Date( ).getTime( );
    }
    
    public void run(){
        SysLib.cout( "Thread[" + name +"] is running\n" );
        System.out.println("check1");
        responseTime = new Date( ).getTime( );
        for ( int i = 0; i < Integer.MAX_VALUE / 10; i++ ) {
            double ans = Math.pow( Math.sqrt( Math.sqrt( i ) * Math.sqrt( i ) ), 2.0 );
            //System.out.println("check2");
        }
        System.out.println("check3");
        completionTime = new Date( ).getTime( );
        SysLib.cout( "Thread[" + name + "]:" +
                 " response time = " + (responseTime - submissionTime) +
                 " turnaround time = " + (completionTime - submissionTime)+
                 " execution time = " + (completionTime - responseTime)+
                 "\n");
        System.out.println("check4");
        SysLib.exit();

    }

}