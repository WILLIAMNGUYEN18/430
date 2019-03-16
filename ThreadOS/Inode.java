public class Inode {
   private final static int iNodeSize = 32;       // fix to 32 bytes
   private final static int directSize = 11;      // # direct pointers

   public int length;                             // file size in bytes
   public short count;                            // # file-table entries pointing to this
   public short flag;                             // 0 = unused, 1 = used, ...
   public short direct[] = new short[directSize]; // direct pointers
   public short indirect;                         // a indirect pointer

   /*    Create a Vector<Inode> object that maintains all 
   inode on memory, is shared among all threads, 
   and is exclusively access by each thread.  
   Where should I do this?
   Probably synchronized calls?
   */

   /*
   (1) the length of the corresponding file  
   (2) the number of file (structure) table entries that point to this inode  
   (3) the flag to indicate if it is unused (= 0), used(= 1), or in some other status (= 2, 3, 4, ..., i.e., what/how the file is currently being used for).   
   16 inodes can be stored in one block. 
   */
   Inode( ) {                                     // a default constructor
      length = 0;
      count = 0;
      flag = 1;
      for ( int i = 0; i < directSize; i++ )
         direct[i] = -1;
      indirect = -1;
   }

   /*
    inode number, termed inumber, 
   this constructor reads the corresponding disk block, 
   locates the corresponding inode information in that block, 
   and initializes a new inode with this information. 
   */
   Inode( short iNumber ) {                       // retrieving inode from disk
      // design it by yourself.
      //read disk block corresponding to inode?

      //locate corresponding inode information in block

      //initialize inode to new inode with information
   }

   int toDisk( short iNumber ) {                  // save to disk as the i-th inode
      // design it by yourself.
   }
}