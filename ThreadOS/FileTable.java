public class FileTable {

    private Vector table;         // the actual entity of this file table
    private Directory dir;        // the root directory 

    /*
    When a user thread opens a file, it follows these steps:   

    The user thread allocates a new entry of the user file 
    descriptor table in its TCB. 
    This entry number itself becomes a file descriptor number. 
    The entry maintains a reference to a file (structure) table entry.   

    The user thread then requests the file system to 
    allocate a new entry of the system-maintained file (structure) table. 
    This entry includes the seek pointer of this file, 
    a reference to the inode corresponding to the file, 
    the inode number, the count to maintain #threads sharing 
    this file (structure) table, and the access mode. 
    The seek pointer is set to the front or the tail of 
    this file depending on the file access mode.   

    The file system locates the corresponding inode and 
    records it in this file (structure) table entry.   

    The user thread registers a reference to this 
    file (structure) table entry in its file descriptor 
    table entry of the TCB.   
    */
 
    public FileTable( Directory directory ) { // constructor
       table = new Vector( );     // instantiate a file (structure) table
       dir = directory;           // receive a reference to the Director
    }                             // from the file system
 
    // major public methods
    public synchronized FileTableEntry falloc( String filename, String mode ) {
       // allocate a new file (structure) table entry for this file name
       short newEntiNumber = dir.ialloc(filename);
       Inode newEntiNode = new Inode(newEntiNumber);
       FileTableEntry newEnt = new FileTableEntry(newEntiNode, newEntiNumber, mode);
      //what variables? 

       // allocate/retrieve and register the corresponding inode using dir
            //ialloc does both?
       
       // increment this inode's count
            //incrementing both inode and FTE
            newEnt.count++;
            newEntiNode.count++;
       
       // immediately write back this inode to the disk
            newEntiNode.toDisk(newEntiNumber);
       
       // return a reference to this file (structure) table entry
       //table.add(newEnt);
       return newEnt;
    }
 
    public synchronized boolean ffree( FileTableEntry e ) {
       // receive a file table entry reference

       // save the corresponding inode to the disk
       e.inode.toDisk(e.iNumber);
       // free this file table entry.
         //HOW TO FREE?
         //ifree() in Directory? removes Inode
            //since in directory, can also remove references
            //in regards to fnames and fsize
         //need to remove rest of FileTableEntry
         // e = null or some shit?;
         //need 
      //ifree(e.iNumber);
      //
       // return true if this file table entry found in my table
       if(table.contains(e)){
          return true;
       }
       return false;
    }
 
    public synchronized boolean fempty( ) {
       return table.isEmpty( );  // return if table is empty 
    }                            // should be called before starting a format
 }