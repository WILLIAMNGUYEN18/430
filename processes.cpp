
#include <stdlib.h>     // for exit
#include <stdio.h>      // for perror
#include <unistd.h>     // for fork, pipe
#include <sys/wait.h>   // for wait
#include <iostream>     // for cerr, cout

using namespace std;

int main(int argc, const char *argv[]){
    int n;

    //argv[1] search how many processes whose name is given are running on the system where your program has been involved

    //create root process?
    //create a child process
    //redirect pipes
    // fork another process 
    printf("Flag 0");
    fflush(stdout);
    enum {RD, WR}; // pipe fd index RD=0, WR=1
    int fd[2], fd2[2];// filedescriptor[2];
    
    //char buff[100];

    //int pid; // process ID
    pid_t pid;
    //pid = p fork();


    //if(pipe(filedescriptor) < 0){
    //    fprintf(stderr, "Pipe Failed");
    //    exit(EXIT_FAILURE);
    //}
    //pid_t pid;
    //((pid = fork()) <0)
    //create pipe
    if(pipe(fd) < 0){
        //fprintf(stderr, "Pipe Failed");
        //exit(EXIT_FAILURE);
        perror("pipe error");
    }
    printf("pid = %ld\n", (long)pid);

    //Creating secondary pipe and checking for failure
    if(pipe(fd2) < 0){
        //fprintf(stderr, "Second Pipe Failed");
        //exit(EXIT_FAILURE);
        perror("2nd pipe error");
    }

    //2nd fork (child to grandchild) for grep
    pid = fork();
    //fail case for 2nd fork
    if(pid < 0){
        fprintf(stderr, "Second Fork Failed");
        exit(EXIT_FAILURE);
        //perror("2nd fork error");        
    }


    else if(pid == 0){
        printf("Flag 2");
        fflush(stdout);

        
        /*
        //input from first pipe
        dup2(fd[0],0);
        //dup2(fd[RD],0); //Reading input from first pipe

        //output to fd2
        dup2(fd2[1],1);
        //dup2(fd[WR],1); //Outputting to second pipe

        //close all fds
        close(fd[0]);
        close(fd[1]);
        close(fd2[0]);
        close(fd2[1]);
        */
        close(fd[RD]);
        close(fd2[WR]);
        dup2(fd[WR], STDOUT_FILENO);//writing to first pipe
        dup2(fd2[RD],0); //reading from 2nd pipe
        
        //close all fds
        close(fd[0]);
        close(fd[1]);
        close(fd2[0]);
        close(fd2[1]);

        //May need to adjust
        //execlp("\Program Files\Git\usr\bin\grep","grep", argv[1],NULL);
        //wait(NULL);
        execlp("/bin/grep","grep", argv[1],NULL);
        //execlp("grep","grep", argv[1],NULL);
        
        
        //Error state for execlp
        perror("bad exec grep");
        _exit(1);
    }

    //closing unused first pipe
    close(fd[0]);
    close(fd[1]);

    //3rd fork (grandchild to great grandchild) ps -a
    pid = fork();
    if(pid < 0){
        fprintf(stderr, "Third Fork Failed");
        exit(EXIT_FAILURE);
        //perror("3rd fork error");
    }

    else if(pid == 0){
        printf("Flag 3");
        fflush(stdout);

        /*
        //input from secondary pipe
        dup2(fd2[0],0);
        //dup2(fd2[RD],0); //great grandchild receiving input from 2nd pipe
        */
        close(fd[0]);
        close(fd[1]);
        close(fd2[RD]);
        dup2(fd2[1],1); //write to 2nd pipe
        //close fds
        close(fd2[0]);
        close(fd2[1]);
        
        //execute process status of all "ps -a"
        //execlp("\Program Files\Git\usr\bin\ps","ps","-a",NULL);
        execlp("/bin/ps","ps","-A",NULL);
        //execlp("ps","ps","-a",NULL);
        
        //Error state for execlp
        perror("bad exec ps");
        _exit(1);

    }

    // ---------- PARENT SECTION ----------
    else {
        // parent will wait for the child to complete

        close(fd[WR]);
        dup2(fd[RD], STDIN_FILENO);
        close(fd[RD]);
        close(fd2[0]);
        close(fd2[1]);
        //wait(NULL);
        //n = read(filedescriptor[1], buf, 100);        wait(NULL);
        
        printf("Child Complete");
        execlp("/usr/bin/wc","wc","-l",NULL);
        //execlp("wc","wc","-l",NULL);
        
        
        //Error state for execlp
        perror("bad exec ws");


        
    }
    exit(EXIT_SUCCESS);
    return 0;   
}

