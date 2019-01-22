/*
Code a C++ program, named processes.cpp that receives one argument, 
(i.e., argv[1]) upon its invocation and searches how many processes 
whose name is given in argv[1] are running on the system where your program has been invoked. 
To be specific, your program should demonstrate the same behavior as: 
$ ps -A | grep argv[1] | wc - l 


our parent process spawns a child that spawns a grand-child that spawns a great-grand-child. Each process should execute a different command as follows: 

Parent, wait for child      stdin no change                                 stdout no chage

Child wc -l                 stdin redirected from a grand-child's           stdout no change 

Grand-child grep argv[1]    stdin redirected from a great-grand-child's     stdout  redirected to a child's stdin

Great-grand-child ps -A     stdin no change                                 stdout redirected to a grand-child's

https://stackoverflow.com/questions/7861093/fork-execlp-in-linux
https://gist.github.com/mplewis/5279108


https://superuser.com/questions/21067/windows-equivalent-of-whereis
https://stackoverflow.com/questions/15250008/third-process-wc-wont-work
https://stackoverflow.com/questions/21558937/i-do-not-understand-how-execlp-works-in-linux
https://linux.die.net/man/3/execlp
https://stackoverflow.com/questions/40451305/how-to-know-if-a-process-is-a-parent-or-a-child

http://www.csl.mtu.edu/cs4411.ck/www/NOTES/process/fork/create.html

http://man7.org/linux/man-pages/man2/dup.2.html


*/

/*
pid_t fork( void ); 
	creates a child process that differs from the parent process only in terms of their process IDs. 

int execlp( const char *file, const char *arg, ..., (char *)0 ); 
	replaces the current process image with a new process image that will be loaded from file. The first argument arg must be the same as file 

int pipe( int filedes[2] ); 
	creates a pair of file descriptors (which point to a pipe structure), and places them in the array pointed to by filedes. filedes[0] is for reading data from the pipe, filedes[1] is for writing data to the pipe. 

int dup2( int oldfd, int newfd ); 
	creates in newfd a copy of the file descriptor oldfd. This system call redirects the flow of standard input and output to be input and output into the pipe. Oldfd is the file descriptor that points to the pipe, and newfd is the standard input and output fd that you want to redirect to the pipe. 

pid_t wait( int *status ); 
    waits for process termination. 

int close( int fd ); 
    closes a file descriptor.  

*/

#include <stdlib.h>     // for exit
#include <stdio.h>      // for perror
#include <unistd.h>     // for fork, pipe
#include <sys/wait.h>   // for wait
#include <iostream>     // for cerr, cout

using namespace std;

/*
our parent process spawns a child that spawns a grand-child that spawns a great-grand-child. Each process should execute a different command as follows: 

Parent, wait for child      stdin no change                                 stdout no chage

Child wc -l                 stdin redirected from a grand-child's           stdout no change 

Grand-child grep argv[1]    stdin redirected from a great-grand-child's     stdout  redirected to a child's stdin

Great-grand-child ps -A     stdin no change                                 stdout redirected to a grand-child's
*/
//(int argc, char *argv[]) to receive arguments
int main((int argc, char *argv[]){

    int count = 0;// the number of instances
    //argv[1] search how many processes whose name is given are running on the system where your program has been involved

    //create root process?
    //create a child process
    //redirect pipes
    // fork another process 

    enum {RD, WR}; // pipe fd index RD=0, WR=1
    int fd[2], fd2[2];
    
    //char buff[100];

    int pid; // process ID
    //pid_t p;
    //pid = p fork();

    if(pipe(fd) < 0){
        fprintf(stderr, "Pipe Failed");
        exit(EXIT_FAILURE);
    }
    pid = fork();

    if (pid < 0) { // error occurred
        fprintf(stderr, "Fork Failed");
        exit(EXIT_FAILURE);
    }

    // ---------- CHILD SECTION -----------   
    else if (pid == 0) {
        sleep(2);
        
        //close(1);dup(f1[1]); == dup2(f1[1], 1);

        dup2(fd[1],1)
        

        //different exec
        
        //where wc
        //C:\Program Files\Git\usr\bin\wc.exe
        execlp("\Program Files\Git\usr\bin\wc","wc","-l",NULL);
    }
    // execlp("\Program Files\Git\usr\bin\ps","ps","-a",NULL);
    // execlp("\Program Files\Git\usr\bin\grep","grep","-l",NULL);

    //need to do multiple children sections?
    // ---------- PARENT SECTION ----------
    else {
        // parent will wait for the child to complete
        wait(NULL);
        sleep(5);
        printf("Child Complete");
        exit(EXIT_SUCCESS);
    }


    exit(EXIT_SUCCESS);
}

/*
   if( pipe(fd) < 0 ) // 1: pipe created
      perror("pipe error");
   else if ((pid = fork()) < 0) // 2: child forked
      perror("fork error");
   else if (pid == 0) {
      // ---------- CHILD SECTION ----------
      close(fd[WR]);// 4: child's fd[1] closed
      dup2(fd[RD], 0); // stdin(0) --> childs pipe read 

      char buf[256];
      n = read(fd[RD], buf, 256); // use this raw read!
      // cin >> buf; <-- *caution with cin and white space!
      cout << buf;      // write to stdout
      cout << "Child Done!" << endl;
   }
   else {
      // ---------- PARENT SECTION -----------
      close(fd[RD]); // 3: close parent's read end of pipe
      dup2(fd[WR], 2); // stderr(2) --> parent's pipe write 

      cerr <<  "Hello my child" << endl;
      wait( NULL );
      cout << "Parent Done!" << endl;
   }
*/