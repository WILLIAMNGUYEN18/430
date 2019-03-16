import java.util.*;
import java.lang.reflect.*;
import java.io.*;

public class Cache{
    public int blockSize;
    public int cacheBlocks;
    public Vector<CacheEntry> pageTable;
    
    public Cache(int blockSize, int cacheBlocks){
        this.blockSize = blockSize;
        this.cacheBlocks = cacheBlocks;
        pageTable = new Vector<CacheEntry>(cacheBlocks);

    }


    /*
        Scans the page table for blockId to determine whether the data is in memory 

        If it is in memory: reads into the buffer[ ] the contents of the cache block specified by blockId  

        If it is not in memory: reads the corresponding disk block from the ThreadOS disk (Disk.java) and load it into the main memory and add it to the page table (i.e., is in cache). 

        Return true if no errors occur (See below). 
        Note: The ThreadOS disk contains 1000 blocks.  You should only return false in the case of an out of bounds blockId. 
    */
    public boolean read(int blockId, byte buffer[]){
        if(blockId >=1000){
            return false;
        }
        boolean inMemory = false;
        int indblk = 0;

        for(int i = 0; i < pageTable.size(); i++){
            if(pageTable.get(i) != null){
                //assuming block frame number = blockId
                if(pageTable.get(i).blockFrame == blockId){
                    inMemory = true;
                    indblk = i;
                    break;
                }
            }
        }
        //If it is in memory: reads into the buffer[ ] 
        //the contents of the cache block specified by blockId  
        if(inMemory){
            //reading contents FROM cache?
            //not reading buffer to cache?
            buffer = pageTable.get(blockId).cacheBlock;
            pageTable.get(blockId).refBit = 1;

        } else{
            //If it is not in memory: reads the 
            //corresponding disk block from the 
            //ThreadOS disk (Disk.java) and load it 
            //into the main memory and add it to the 
            //page table (i.e., is in cache).

            //syslib.rawread for corresponding blockId and byte
            //buffer is changed through rawread (passed as reference)
            //can use that to change cacheBlock.

            //this changes what buffers contains (which is output of read)
            //while simultaneously providing a new byte[] to cache
            SysLib.rawread(blockId, buffer);

            //need to create an object and put buffer inside
            CacheEntry newBlock = new CacheEntry();
            newBlock.cacheBlock = buffer;
            newBlock.blockFrame = blockId;

            //block was accessed, change reference bit to 1;
            newBlock.refBit = 1;
            
            //need to fix this. Need to do enhanced second chance
            //need to check for a free block (-1 on blockFrame val)

            //loop through first time
            //no changes to bits
            //look for if access and bit = 0;
            for(int j = 0; j < pageTable.size(); j++){
                if(pageTable.get(i) != null){
                    if(pageTable.get(j).refBit = 0 && pageTable.get(j).dirtBit = 0){
                        //replace
                        pageTable.set(j, newBlock);
                        break;
                    }
                }//else do nothing
            }
            //scan looking for not accessed, but modified recently
            //refBit = 0, dirtBit = 1;
            //if so, write back to disk, then replace
            //at end, also set all refBits to 0
            for(int k = 0; k < pageTable.size(); k++){
                if(pageTable.get(i) != null){
                    if(pageTable.get(k).refBit = 0 && pageTable.get(k).dirtBit = 1){
                        //write back to disk
                        //SysLib.rawwrite(blkNumber, byte [] b)
                        SysLib.rawwrite(pageTable.get(k).blockFrame, pageTable.get(k).cacheBlock);
                        
                        //replace
                        pageTable.set(k, newBlock);
                        break;
                    }
                }
                pageTable.get(k).refBit = 0;
            }

            //3rd Loop, repeating step 1
            for(int l = 0; l < pageTable.size(); l++){
                if(pageTable.get(i) != null){
                    if(pageTable.get(l).refBit = 0 && pageTable.get(l).dirtBit = 0){
                        //replace
                        pageTable.set(l, newBlock);
                        break;
                    }
                }
            }

            //4th Loop, repeating step 2, replacement should be secured here.
            for(int k = 0; k < pageTable.size(); k++){
                if(pageTable.get(i) != null){
                    if(pageTable.get(k.refBit = 0 && pageTable.get(k).dirtBit = 1){
                        //write back to disk
                        //SysLib.rawwrite(blkNumber, byte [] b)
                        SysLib.rawwrite(pageTable.get(k).blockFrame, pageTable.get(k).cacheBlock);
                        
                        //replace
                        pageTable.set(k, newBlock);
                        break;
                    }
                }

                pageTable.get(k).refBit = 0;
            }
            
            //read from cache to buffer?
            //already do with SysLib.rawread (using same buffer reference)

        }
        
        return true;

    }

    /*
        Writes the contents of buffer[ ] array to the cache block specified by blockId  

        If the block specified by blockId is not in cache, find a free cache block and writes the buffer [ ] contents to it.  

        If the block is not in cache and there are no free blocks, you’ll have to find a victim for replacement. 

    (Note that these are the relevant options for write). 

    Return true unless there is an error (Specified below). 

    Method does not read from or write to ThreadOS disk.  

    Note: The ThreadOS disk contains 1000 blocks.  You should only return false in the case of an out of bounds blockId. 
    */
    public boolean write(int blockId, byte buffer[]){
        if(blockId >=1000){
            return false;
        }

        boolean inMemory = false;
        int indblk = 0;

        for (int i = 0; i < pageTable.size(); i ++){
            if(pageTable.get(i) != null){
                if(pageTable.get(i.blockFrame == blockId)){
                    inMemory = true;
                    indblk = i;
                    break;
                }
            }

        }

        if(inMemory){
            pageTable.get(indblk).cacheBlock = buffer;
            pageTable.get(indblk).dirtBit = 1;
            pageTable.get(indblk).refBit = 1;
            
        } else{
            CacheEntry newBlock = new CacheEntry();
            newBlock.blockFrame = blockId;
            newBlock.cacheBlock = buffer;
            newBlock.refBit = 1;

            //repeat read's enhance second chance algo

            for(int j = 0; j < pageTable.size(); j++){
                if(pageTable.get(i) != null){
                    if(pageTable.get(j).refBit = 0 && pageTable.get(j).dirtBit = 0){
                        //replace
                        pageTable.set(j, newBlock);
                        break;
                    }
                }//else do nothing
            }
            //scan looking for not accessed, but modified recently
            //refBit = 0, dirtBit = 1;
            //if so, write back to disk, then replace
            //at end, also set all refBits to 0
            for(int k = 0; k < pageTable.size(); k++){
                if(pageTable.get(i) != null){
                    if(pageTable.get(k).refBit = 0 && pageTable.get(k).dirtBit = 1){
                        //write back to disk
                        //SysLib.rawwrite(blkNumber, byte [] b)
                        SysLib.rawwrite(pageTable.get(k).blockFrame, pageTable.get(k).cacheBlock);
                        
                        //replace
                        pageTable.set(k, newBlock);
                        break;
                    }
                }
                pageTable.get(k).refBit = 0;
            }

            //3rd Loop, repeating step 1
            for(int l = 0; l < pageTable.size(); l++){
                if(pageTable.get(i) != null){
                    if(pageTable.get(l).refBit = 0 && pageTable.get(l).dirtBit = 0){
                        //replace
                        pageTable.set(l, newBlock);
                        break;
                    }
                }
            }

            //4th Loop, repeating step 2, replacement should be secured here.
            for(int k = 0; k < pageTable.size(); k++){
                if(pageTable.get(i) != null){
                    if(pageTable.get(k.refBit = 0 && pageTable.get(k).dirtBit = 1){
                        //write back to disk
                        //SysLib.rawwrite(blkNumber, byte [] b)
                        SysLib.rawwrite(pageTable.get(k).blockFrame, pageTable.get(k).cacheBlock);
                        
                        //replace
                        pageTable.set(k, newBlock);
                        break;
                    }
                }

                pageTable.get(k).refBit = 0;
            }
            

        
        }
        

    }

    /*
    Writes back all dirty blocks to Disk.java  

    Forces Disk.java to write back all contents to the DISK file.  

    Still maintains clean block copies in Cache.java. Must be called when shutting down ThreadOS.  
    */
    public void sync(){
        for(int i = 0; i < pageTable.size()){
            if(pageTable.get(i) != null){
                if(pageTable.get(i).dirtBit == 1){
                    SysLib.rawwrite(pageTable.get(i).blockFrame, pageTable.get(i).cacheBlock)
                    pageTable.get(i).dirtBit = 0;
                }
            }

        }

    }
    /*
    Writes back all dirty blocks to Disk.java  

    Forces Disk.java to write back all contents to the DISK file.  

    Wipes all cached blocks.   

    Should be called when you choose to run a different test case that doesn’t   include the cached data from the previous test. 
    */
    public void flush(){
        for(int i = 0; i < pageTable.size()){
            if(pageTable.get(i) != null){
                if(pageTable.get(i).dirtBit == 1){
                    SysLib.rawwrite(pageTable.get(i).blockFrame, pageTable.get(i).cacheBlock)
                    pageTable.get(i).dirtBit = 0;
                }
            }

        }
    }



}

/*
Kernel_org.java uses your Cache.java in its interrupt( ) method, so that you don't have to modify the kernel.  

    BOOT: instantiates a Cache.java object, (say cache) with 10 cache blocks. 

    CREAD: is called from the SysLib.cread( ) library call. It invokes cache.read( ). 

    CWRITE: is called from the SysLib.cwrite( ) library call. It invokes cache.write( ). 

    CSYNC: is called from the SysLib.csync( ) library call. It invokes cache.sync( ). 

    CFLUSH: is called from the SysLib.flush( ) library call. It invokes cache.flush( ). 

 

NOTE: To read from the Disk you can use SysLib.rawread(), and to write use SysLib.rawwrite() (hint: look at Kernel.java) 
*/

/*
block frame number
reference bit
dirty bit
3 vectors?
*/