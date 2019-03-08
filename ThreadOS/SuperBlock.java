class Superblock {
   private final int defaultInodeBlocks = 64;
   public int totalBlocks; // the number of disk blocks
   public int totalInodes; // the number of inodes
   public int freeList;    // the block number of the free list's head
   
   //totalBlocks = SysLib.bytes2int(superBlock, offset);
   //superBlock is a byte array of 512 (the maximum disk block size).    

   /*
   SysLib.int2bytes: Converts the totalBlocks data to 
   bytes and store in toDisk at the specified offset  
   In order to sync the contents of the superblock to disk, 
   it is necessary to convert all necessary data to byte format 
   within a temporary byte array, which then gets written to disk 
   */
   
   // (the offset needs to be multiples of 4 because that is the number of bytes in an int).
   public SuperBlock( int diskSize ) {
      //read superBlock from disk
      byte[] superBlock = new byte(Disk.blockSize);
      SysLib.rawread(0, superBlock);
      totalBlocks = SysLib.bytes2int(superBlock, 0);
      totalInodes = SysLib.bytes2int(superBlock, 4);
      freeList = SysLib.bytes2int(superBlock, 8);
      
      if( totalBlocks = diskSize && totalInodes > 0 && freeList >=2){
         //if disk contents are valid
         return;
      }
      else{
         //need to format disk
         totalBlocks = diskSize;
         format(defaultInodeBlocks);
      }

   }
}