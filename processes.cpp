/*
Code a C++ program, named processes.cpp that receives one argument, 
(i.e., argv[1]) upon its invocation and searches how many processes 
whose name is given in argv[1] are running on the system where your program has been invoked. 
To be specific, your program should demonstrate the same behavior as: 
$ ps -A | grep argv[1] | wc - l 


our parent process spawns a child that spawns a grand-child that spawns a great-grand-child. Each process should execute a different command as follows: 

Parent, wait for child
    base on code we saw in class?
Child wc -l redirected from a grand-child's stdout no change 

Grand-child grep argv[1] redirected from a great-grand-child's stdout redirected to a child's stdin
Great-grand-child ps -A no change redirected to a grand-child's stdin
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

//(int argc, char *argv[]) to receive arguments
int main((int argc, char *argv[]){

    int count = 0;// the number of instances
    //argv[1] search how many processes whose name is given are running on the system where your program has been involved

    //create root process?
    //create a child process
    //redirect pipes



}