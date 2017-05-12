package com.gjmgr.utils;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

/** �ļ��������*/
public class FileManager {
	public String PATH = File.separator + "ganjiao" + File.separator;
	public final static int SD_SUCCESS = 10;		//sd�������ļ��ɹ�
	public final static int SD_FAILED = 20;			//sd�������ļ����ɹ�---δ֪����
	public static final int SDCAN_USE = 1;			//sd������
	public static final int SD_SHARE = 2;				//sd�����ڹ���״̬
	public static final int SDCANNOT_USE = 3;		//sd��������
	public File m_File;
	
	/**��ȡ�ļ�*/
	public File getFile(){
		return m_File;
	}
	
	/**����SD���ļ�*/
	public int makeHallPath(){
		int state = isSDCardMounted();System.out.println("sd---------------�Ƿ���ã�"+state);
		if(1 != state){
			return state;
		}
		
		PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + PATH;System.out.println("sd---------------·����"+PATH);
		m_File = new File(PATH);
		System.out.println("sd---------------�Ƿ���ڣ�"+m_File.exists());
		if(!m_File.exists()){
			if(m_File.mkdirs()){
				return SD_SUCCESS;
			}else{
				return SD_FAILED;
			}
		}
		return SD_SUCCESS;
	} 
	
	/**���������ڴ��ļ�*/
	public boolean makeRomPath(){
		String filePath = Environment.getDataDirectory().getAbsolutePath() + PATH;
		m_File = new File(filePath);
		System.out.println("·����������������������������������������������"+filePath);
		if(!m_File.exists()){
			if(m_File.mkdirs()){
				return true;
			}else{
				System.out.println("sdf ��ɪɪ������ ɵɵ�ط�����������������");
				return false;
			}
		}
		
		return true;
	}
	
	

	
	/**�ж�SD����״̬*/
	public int isSDCardMounted(){
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)){			//sd������
			return SDCAN_USE;	
		}else if(state.equals(Environment.MEDIA_SHARED)){		//sd�����ڹ���״̬
			return SD_SHARE;
		}else{
			return SDCANNOT_USE;								//sd��������
		}
	}
	
	/**���SD�������ڴ�*/
	public long getAvaliableSpaceOfSDCard(){
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return blockSize * availableBlocks;
	}
	
	/**��û����ڴ��ڴ�*/
    public long getRomTotalSize() {  
        File path = Environment.getDataDirectory();  
        StatFs stat = new StatFs(path.getPath());  
        long blockSize = stat.getBlockSize();  
        long totalBlocks = stat.getBlockCount();  
        return blockSize * totalBlocks; 
    }  
  
    /**��û�������ڴ�*/ 
    public long getRomAvailableSize() {  
        File path = Environment.getDataDirectory();  
        StatFs stat = new StatFs(path.getPath());  
        long blockSize = stat.getBlockSize();  
        long availableBlocks = stat.getAvailableBlocks();  
        return blockSize * availableBlocks;  
    }  
}