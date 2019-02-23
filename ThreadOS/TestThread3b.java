import java.util.Date;
class  TestThread3b extends Thread{
    private String name;
    //private String iter;
    private long submissionTime;
    private long responseTime;
    private long completionTime;
    
    public TestThread3b(String args[]){
        name = args[0];
        //iter = args[1];
        submissionTime = new Date( ).getTime( );
    }
    
    public void run(){
        SysLib.cout( "Thread[" + name + "] is running\n" );
        System.out.println("check1.5");
        responseTime = new Date( ).getTime( );
        byte[] buffer = new byte[512]; 
        for ( int i = 0; i < 100; i++ ){ 
            SysLib.rawwrite( i, buffer );
            //System.out.println("check2.5"); 
        }
        System.out.println("check3.5");
        completionTime = new Date( ).getTime( );
        SysLib.cout( "Thread[" + name +"]:" +
                 " response time = " + (responseTime - submissionTime) +
                 " turnaround time = " + (completionTime - submissionTime)+
                 " execution time = " + (completionTime - responseTime)+
                 "\n");

        System.out.println("check 4.5");
        SysLib.exit();

    }
}