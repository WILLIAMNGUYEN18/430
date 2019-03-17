import java.util.Date;
import java.util.Random;

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

    //    Localized accesses: 
    //read and write a small selection of blocks many times 
    //to get a high ratio of cache hits. 
    public void run(){
        System.out.println("Locality + Random Access Test");
        SysLib.cout( "Thread[" + name +"] is running\n" );
        System.out.println("check1");
        responseTime = new Date( ).getTime( );
        
        //enabled = SysLib.cread/cwrite
        if(able.equals("e")){
            System.out.println("enabled cache");
            for(int i = 0; i < 100; i++){
                //Random rand = new Random();
                byte[] buffer = new byte[512];
                //int rand = rand.nextInt(1000);
                SysLib.cwrite(1, buffer);
                SysLib.cread(1, buffer);
            }
        } else{
            System.out.println("disabled cache");
            //disabled = SysLib.rawread/write
            for(int i = 0; i < 100; i++){
                //Random rand = new Random();
                byte[] buffer = new byte[512];
                //int rand = rand.nextInt(1000);
                SysLib.rawwrite(1, buffer);
                SysLib.rawread(1, buffer);
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