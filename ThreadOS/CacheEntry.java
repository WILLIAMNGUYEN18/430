import java.util.*;
import java.lang.reflect.*;
import java.io.*;


public class CacheEntry {
    
    public int blockFrame;
    public int refBit;
    public int dirtBit;
    public byte[] cacheBlock;
    //Each cache Block should contain a blockSize-byte of data. 

    public CacheEntry(){
        this.blockFrame = -1;
        this.dirtBit = 0;
        this.refBit = 0;
    }
    public CacheEntry(byte[] blockSize){
        
        this.blockFrame = -1;
        this.dirtBit = 0;
        this.refBit = 0;
        cacheBlock = byte[blockSize];
    }


    
    
}