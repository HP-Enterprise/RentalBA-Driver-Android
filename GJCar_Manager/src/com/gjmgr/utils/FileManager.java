package com.gjmgr.utils;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

/** 文件管理的类*/
public class FileManager {
	public String PATH = File.separator + "ganjiao" + File.separator;
	public final static int SD_SUCCESS = 10;		//sd卡创建文件成功
	public final static int SD_FAILED = 20;			//sd卡创建文件不成功---未知错误
	public static final int SDCAN_USE = 1;			//sd卡可用
	public static final int SD_SHARE = 2;				//sd卡处于共享状态
	public static final int SDCANNOT_USE = 3;		//sd卡不可用
	public File m_File;
	
	/**获取文件*/
	public File getFile(){
		return m_File;
	}
	
	/**创建SD卡文件*/
	public int makeHallPath(){
		int state = isSDCardMounted();System.out.println("sd---------------是否可用："+state);
		if(1 != state){
			return state;
		}
		
		PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + PATH;System.out.println("sd---------------路径："+PATH);
		m_File = new File(PATH);
		System.out.println("sd---------------是否存在："+m_File.exists());
		if(!m_File.exists()){
			if(m_File.mkdirs()){
				return SD_SUCCESS;
			}else{
				return SD_FAILED;
			}
		}
		return SD_SUCCESS;
	} 
	
	/**创建机身内存文件*/
	public boolean makeRomPath(){
		String filePath = Environment.getDataDirectory().getAbsolutePath() + PATH;
		m_File = new File(filePath);
		System.out.println("路径身啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊"+filePath);
		if(!m_File.exists()){
			if(m_File.mkdirs()){
				return true;
			}else{
				System.out.println("sdf 啊瑟瑟发抖撒 傻傻地法啊啊啊啊啊啊啊啊");
				return false;
			}
		}
		
		return true;
	}
	
	

	
	/**判断SD卡的状态*/
	public int isSDCardMounted(){
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)){			//sd卡可用
			return SDCAN_USE;	
		}else if(state.equals(Environment.MEDIA_SHARED)){		//sd卡处于共享状态
			return SD_SHARE;
		}else{
			return SDCANNOT_USE;								//sd卡不可用
		}
	}
	
	/**获得SD卡可用内存*/
	public long getAvaliableSpaceOfSDCard(){
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return blockSize * availableBlocks;
	}
	
	/**获得机身内存内存*/
    public long getRomTotalSize() {  
        File path = Environment.getDataDirectory();  
        StatFs stat = new StatFs(path.getPath());  
        long blockSize = stat.getBlockSize();  
        long totalBlocks = stat.getBlockCount();  
        return blockSize * totalBlocks; 
    }  
  
    /**获得机身可用内存*/ 
    public long getRomAvailableSize() {  
        File path = Environment.getDataDirectory();  
        StatFs stat = new StatFs(path.getPath());  
        long blockSize = stat.getBlockSize();  
        long availableBlocks = stat.getAvailableBlocks();  
        return blockSize * availableBlocks;  
    }  
}