import java.util.Date;
import java.util.Random;
class  TestThread4a extends Thread{
    private String name;
    private String able;
    private long submissionTime;
    private long responseTime;
    private long completionTime;

    public TestThread4a(String args[]){
        name = args[0];
        able = args[1];
        submissionTime = new Date( ).getTime( );
    }

    //read and write many blocks randomly across the disk. 
    //Verify the correctness of your disk cache.
    //need 2 
    public void run(){
        System.out.println("Random Access Test");
        SysLib.cout( "Thread[" + name +"] is running\n" );
        System.out.println("check1");
        responseTime = new Date( ).getTime( );
        
        //enabled = SysLib.cread/cwrite
        if(able.equals("e")){
            System.out.println("enabled cache");
            for(int i = 0; i < 100; i++){
                Random rand = new Random();
                byte[] buffer = new byte[512];
                int rando = rand.nextInt(1000);
                int rand2 = rand.nextInt(1000);
                SysLib.cwrite(rando, buffer);
                SysLib.cread(rand2, buffer);
            }
        } else{
            System.out.println("disabled cache");
            //disabled = SysLib.rawread/write
            for(int i = 0; i < 100; i++){
                Random rand = new Random();
                byte[] buffer = new byte[512];
                int rando = rand.nextInt(1000);
                int rand2 = rand.nextInt(1000);
                SysLib.rawwrite(rando, buffer);
                SysLib.rawread(rand2, buffer);
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