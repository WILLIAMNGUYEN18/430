import java.util.Date;
class  TestThread4b extends Thread{
    private String name;
    private String able;
    private long submissionTime;
    private long responseTime;
    private long completionTime;

    public TestThread4b(String args[]){
        name = args[0];
        able = args[1];
        submissionTime = new Date( ).getTime( );
    }

    //    Adversary accesses: 
    //generate disk accesses that do not make good use of the 
    //disk cache at all, i.e., purposely accessing blocks to 
    //create cache misses.  
    public void run(){
        System.out.println("Locality Test");
        SysLib.cout( "Thread[" + name +"] is running\n" );
        System.out.println("check1");
        responseTime = new Date( ).getTime( );
        
        //enabled = SysLib.cread/cwrite
        if(able.equals("e")){
            System.out.println("enabled cache");
            for(int i = 0; i < 50; i++){
                //Random rand = new Random();
                byte[] buffer = new byte[512];
                //int rand = rand.nextInt(1000);
                SysLib.cwrite(1, buffer);
                SysLib.cread(100-i, buffer);
            }
        } else{
            System.out.println("disabled cache");
            //disabled = SysLib.rawread/write
            for(int i = 0; i < 50; i++){
                //Random rand = new Random();
                byte[] buffer = new byte[512];
                //int rand = rand.nextInt(1000);
                SysLib.rawwrite(i, buffer);
                SysLib.rawread(100 - i, buffer);
            }

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