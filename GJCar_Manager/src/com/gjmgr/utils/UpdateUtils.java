package com.gjmgr.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.gjmgr.app.R;
import com.gjmgr.data.data.Public_Param;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 1.apk��ѹ�����������
 * �����������سɹ��˱��Σ���֮ǰ���ص����ļ�����Ӱ�챾�ΰ�װ
 * ԭ��
//��һ�����أ�������hbzh.apk
   �ڶ������أ�������hbzh.apk������һ�εı�Ϊhbzh.apk.apk
   ���������أ�������hbzh.apk, ���ڶ��α�Ϊhbzh.apk.apk,  �������α�Ϊhbhz.apk.apk.apk
   ������֮��ÿ�εĶ��������һ��

   ������ԭ��������ɵ�apk�ļ�����ȷ������ȷ��apk�ļ�Ҳ�����ɣ�����������

   �����жϣ���������
   ��Ϊ�жϣ��ֶ�ȡ��
   ��̨�������
   
 ʵ�������Ӧ������������⣬��Ϊ�������;ֹͣ�ˣ��ǲ���installapk�ģ��Ͳ�����ʾ����ʧ��  
*/

/**2.����������ʹ��RandomAccessFile��,
 *     ����û���Լ�ʶ��Ļ���
 *     �������쳣��HttpClient�ͻᲶ���쳣catch(){}
 *     ����������ʱ��ֻ��ʹ��RandomAccessFile��֡����ȡ������ûʱ��*/

/**3.����֮�󣬲���װ�������.apk.apk.apk
 * */
/**apk�������*/
public class UpdateUtils{

	//������Ϣ
	//http://61.183.247.74/download/AHBZHNewVision8-22.apk
	//http://down.17huang.com/17app/17huang_3.2.apk	
	
	//����
	private static String saveFileName = "GJCar_Manager";	//apk����
	
	//���صİ�װ��url
	public String apkUrl = "";
	public String apkMD5 = "";
	
	
	/*����*/
	private Dialog downloadApkDialog;
	private TextView title,apkNameTv,total,speedText;//���ƣ�apkname�����������������ٶ�
	private ProgressBar progressBar;//������
	private Button cancleButton;
	private long apkSize;//app������
	private String apkMaxSize = "";//7.2MB
	
	private int countfront ;
	private int count;
	private float speed;
	
	/* ���ذ���װ·�� */
	public static File m_file;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final int SDCARD_CANNOT_USE = 3;
    private static final int SDCARD_SHEARED = 4;
    private static final int CREATE_FILE_FAILED = 5;
    private static final int SDCARD_NOENOUGH = 6;
    private static final int PROGRESS_SHOW = 7;
    private static final int ROM_NOENOUGH = 8;
    private static final int SERVER_ERROR = 9;
    private static final int NET_ERROR = 10;
    private static final int UPDATE_SPEED = 11;
    
    /* �������̿��� */
    private static boolean isSD;//�Ƿ����ص���sd����
    private static final int CLOSE_APP = 50;
    private byte progress;
    private Thread downLoadThread;
    private boolean interceptFlag = false;
    private Handler mHandler;
    public boolean force_update = false;	//�Ƿ�ǿ�Ƹ���
    private String server_version= "";			//�������汾��
	private String low_version;				//��Ͱ汾��
	private ProgressDialog tip;
	public boolean file_useable;
	public FileManager manager;	
	private Activity activity;	
	public boolean isJump = false;
	
	/** ���캯��*/
    public UpdateUtils(final Activity activity,String apkUrl,String size,boolean forceupdate)
    {
    	this.activity = activity;
    	this.apkUrl = apkUrl;
    	this.apkSize = Long.parseLong(size);	
    	this.force_update = forceupdate;
    	System.out.println("xxxxxxxxxxxxxxxxx"+force_update);
    	mHandler = new Handler(){
	    	public void handleMessage(Message msg){
	    		switch (msg.what) {
	    			
	    		    case UPDATE_SPEED:
	    			    speedText.setText(Float.toString(speed)+"KB/s"); 
	    		    	
					case DOWN_UPDATE:
						progressBar.setProgress(progress);
						float downSize =  new BigDecimal(((float)count)/1000000).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
						total.setText(downSize+"MB"+"/"+apkMaxSize);
						break;
						
					case DOWN_OVER:
						if(downloadApkDialog != null){
							downloadApkDialog.dismiss();
							downloadApkDialog = null;
						}
						installApk();
						break;
						
					case CLOSE_APP:
						Toast.makeText(activity, "�°汾�и�����õĹ��ܼ����ŵĽ��棬ǿ�ҽ���������", Toast.LENGTH_SHORT).show();
						break;						
					case PROGRESS_SHOW:
						checkUpdateInfoDialog();
						break;
						
					case SDCARD_CANNOT_USE:
						Toast.makeText(activity, "SD�����ڲ�����״̬", Toast.LENGTH_LONG).show();
						break;
					case SDCARD_SHEARED:
						Toast.makeText(activity, "SD������USB��״̬,������", Toast.LENGTH_LONG).show();
						break;
					case SDCARD_NOENOUGH:
						Toast.makeText(activity, "SD�����ÿռ䲻��", Toast.LENGTH_LONG).show();
						break;
					case CREATE_FILE_FAILED:
						Toast.makeText(activity, "�����ļ�ʧ��", Toast.LENGTH_LONG).show();
						break;
					case ROM_NOENOUGH:
						Toast.makeText(activity, "�����ڴ治��", Toast.LENGTH_LONG).show();
						break;
					
					case SERVER_ERROR:
						Toast.makeText(activity, "���������ϣ�sorry", Toast.LENGTH_LONG).show();
						break;
					
					case NET_ERROR:
						Toast.makeText(activity, "������ϣ�sorry", Toast.LENGTH_LONG).show();
						break;	
										
					default:
						break;
				}
	    	};
	    };
    }
	
	public interface Update_Notify{
		public void update_finish();
		public void closeApp();
	}
	private Update_Notify listener;
	
	public void setListener(Update_Notify thelistener){
    	this.listener = thelistener;
    }
		
    private void ShowProgressDialog(String msg){
    	if(tip != null){
    		tip.cancel();
    		tip.dismiss();
    		tip = null;
    	}
		tip = new ProgressDialog(activity);
		tip.setCanceledOnTouchOutside(false);
//		tip.setCancelable(false);
		tip.setMessage(msg);
		tip.show();
	}
    

    /**1.��ʼ */
	public void UpdateManager_do(){
		checkApkUpdate();
	}
    
    /** 2.�˶Ը�����Ϣ���߳�*/
    private void checkApkUpdate(){
    	checkUpdate();
    	
    }
    
    /** 3.�˶Ը�����Ϣ�ķ���*/
    private void checkUpdate(){

//    	this.apkUrl = "http://www.feeling.hpecar.com/api/appManage/file?filename=9627aa3f-f8a8-41c4-ad74-1039841b8d4e.apk";
//    	this.apkUrl = "http://p.gdown.baidu.com/2849f8378dcef29e4618d6a241dc9af86a8aee48c4c1f6eef4a664654d2de2446d15d28fbad76f4af7e8cd73021df8659dee42b71a1ee6220228996cc39e219b4dd4e14dc8d793109b7e4a6d0c732a48a0f7464af3f7c6d20a84fbb838bc86e47390b2116472f77f2f54e781cd729bd5c44f72e893b6239fd1df8546b9127392dd38c0e9f8074932ba74b2c5e8f7c4a584a11d093ef8121b";
//   	this.apkUrl = "http://www.feeling.hpecar.com/api/appManage/file?filename=9627aa3f-f8a8-41c4-ad74-1039841b8d4e.apk";
    	
//    	saveFileName = saveFileName + server_version + ".apk";
    	saveFileName = saveFileName + Public_Param.Version_Name + ".apk";
    	mHandler.sendEmptyMessage(PROGRESS_SHOW);
    	
    }
	
	/**4.�����������ʾ�Ի��� */
	public void checkUpdateInfoDialog(){
		showNoticeDialog();
	}
	
	/**4-1.�����������ʾ�Ի�����ʾ */
	private void showNoticeDialog(){
//		AlertDialog.Builder builder = new Builder(activity);
//		builder.setTitle("����汾����");
//		builder.setMessage(PublicMessage.update_hini);
//		builder.setPositiveButton("��������", new OnClickListener() {			
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				showDownloadDialog();	//???????????????????????????		
//			}
//		});
//		builder.setNegativeButton("�Ժ���˵", new OnClickListener(){			
//			public void onClick(DialogInterface dialog, int which){
//				dialog.dismiss();	
//				isJump = true;
//				if(force_update){
//					closeApplication();
//					return;
//				}
//				listener.update_finish();
//			}
//		});
//		builder.setCancelable(false).setOnKeyListener(new OnKeyListener() {
//			@Override
//			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//				return false;
//			}
//		});
//		noticeDialog = builder.create();
//		noticeDialog.setCanceledOnTouchOutside(false);
//		noticeDialog.setCancelable(false);
//		noticeDialog.show();

		final Dialog updateDialog = new Dialog(activity, R.style.delete_dialog);
		
		View view = View.inflate(activity, R.layout.dialog_update, null);
		
		TextView title = (TextView)view.findViewById(R.id.dialog_update_title);
		title.setText("�汾����:"+Public_Param.Version_Name);//����汾�ӷ�������ȡ������apk�л�ȡ(apk��û��������)
		TextView content = (TextView)view.findViewById(R.id.dialog_update_content);
		content.setText(StringHelper.getString(Public_Param.Version_Content));
		Button ok = (Button)view.findViewById(R.id.dialog_update_confirm_button);
		Button cancle = (Button)view.findViewById(R.id.dialog_update_cancel_button);
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				updateDialog.dismiss();
				showDownloadDialog();	//???????????????????????????					
			}
		});
		
		cancle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				updateDialog.dismiss();	
				isJump = true;
				if(force_update){
					closeApplication();
					return;
				}
				listener.update_finish();
			}
		});
		
		updateDialog.setOnKeyListener(new OnKeyListener(){

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
		updateDialog.getWindow().setContentView(view);
		updateDialog.setCancelable(false);
		updateDialog.setCanceledOnTouchOutside(false);
		updateDialog.show();
	}
		
	/**4-3.�����������ʾ�Ի��򣺹ر� */
	public void showDownload(){
		if(downloadApkDialog != null){
			downloadApkDialog.show();
		}else{
			showDownloadDialog();
		}
	}
	
	
	/** 5.�������ؽ��ȶԻ���*/
	private void showDownloadDialog(){
//		downloadDialog = new ProgressDialog(activity);
//		downloadDialog.setTitle("����汾����");
//		downloadDialog.setMax(100);
//		downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//		downloadDialog.setCancelable(true);
//		downloadDialog.setButton("ȡ��", new OnClickListener(){
//			public void onClick(DialogInterface arg0, int arg1) {
//				downloadDialog.dismiss();
//				interceptFlag = true;
//				if(force_update){
//					closeApplication();
//					return;
//				}
//				listener.update_finish();
//			}
//		});
//		downloadDialog.setCanceledOnTouchOutside(false);
//		downloadDialog.show();
//		downloadDialog.setCancelable(false);
//		downloadApk();
		
		downloadApkDialog = new Dialog(activity, R.style.delete_dialog);
		
		View view = View.inflate(activity, R.layout.dialog_downapk, null);
		
		/*��ʼ���ؼ�*/
		title = (TextView)view.findViewById(R.id.dialog_downapk_title);
		apkNameTv = (TextView)view.findViewById(R.id.dialog_downapk_apkname);
		total = (TextView)view.findViewById(R.id.dialog_downapk_total);
		speedText = (TextView)view.findViewById(R.id.dialog_downapk_speed);
		progressBar = (ProgressBar)view.findViewById(R.id.dialog_downapk_progressbar);
		cancleButton = (Button)view.findViewById(R.id.dialog_downapk_cancle);
		
		title.setText("������£�"+Public_Param.Version_Name);
		
		cancleButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {	
				downloadApkDialog.dismiss();
				interceptFlag = true;
				if(force_update){
					closeApplication();
					return;
				}
				listener.update_finish();
			}
		});
		
		/*�Ի��������*/
		downloadApkDialog.getWindow().setContentView(view);
		downloadApkDialog.setCancelable(false);
		downloadApkDialog.setCanceledOnTouchOutside(false);
		downloadApkDialog.show();
		downloadApk();
	}
	
	/** 6.���ص��߳�*/
	private void downloadApk(){
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	
	/** 7.���ص�runnable  */
	private Runnable mdownApkRunnable = new Runnable() {	
		public void run(){
			try{
				
				/*1.�ж��ļ�Ŀ¼�Ƿ񴴽��ɹ�*/
				file_useable = makeUseablePath();System.out.println("DDDDDDDDDDDDDDDDDDD_makeUser");
				//apkSize = getApkFileSize();//�����ȡapk�Ĵ�С
				System.out.println("DDDDDDDDDDDDDDDDDDD"+apkSize);
				float downSize =  new BigDecimal(((float)apkSize)/1000000).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
				apkMaxSize = downSize+"MB";
				if(file_useable){
					m_file = manager.getFile();
					System.out.println("sd----------------·��:"+m_file);
										
					if(manager.getAvaliableSpaceOfSDCard() < apkSize){
						
						//�������ڴ�
						mHandler.sendEmptyMessage(SDCARD_NOENOUGH);
						if(manager.getRomAvailableSize() < apkSize){//�ڴ��Ƿ��㹻
							mHandler.sendEmptyMessage(ROM_NOENOUGH);
						}else{
							downloadApkToMyDir();
						}
						
					}else{
						//������SD��
					
						downloadApkToSDCard();
					}
					
				}else{
					//�������ڴ�
					if(manager.getRomAvailableSize() < apkSize){//�ڴ��Ƿ��㹻
						mHandler.sendEmptyMessage(ROM_NOENOUGH);
					}else{
						downloadApkToMyDir();
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
	 }
	};
	
	/** 8���ж��ļ����Ƿ���ڻ��Ƿ�����*/
	private boolean makeUseablePath(){
		manager = new FileManager();
		int state = manager.makeHallPath();System.out.println("sd----------------����sd���Ƿ�ɹ�:"+state);
		switch(state){
		
			case FileManager.SD_SUCCESS://����Ŀ¼�ɹ�
				return true;
			case FileManager.SD_SHARE:
				mHandler.sendEmptyMessage(SDCARD_SHEARED);//SD�����ڹ���
				break;
			case FileManager.SDCANNOT_USE://SD���޷�ʹ��
				mHandler.sendEmptyMessage(SDCARD_CANNOT_USE);
				break;
			case FileManager.SD_FAILED://����Ŀ¼ʧ��
				mHandler.sendEmptyMessage(CREATE_FILE_FAILED);
				break;
			default:
				break;
		}
		return false;
	}
	
	/** */
	
	/**8-1������sd�� */
	private void downloadApkToSDCard() throws FileNotFoundException{
		System.out.println("����aaaaaaaaaaaaaa_8-1������sd��");
		//�����ļ�
		File ApkFile = new File(m_file,saveFileName);
		RandomAccessFile fos = new RandomAccessFile(ApkFile,"rwd");
		
		// ����Ĭ�ϵĿͻ���ʵ��  
        HttpClient httpCLient = new DefaultHttpClient();  
          
        // ����get����ʵ��  
        HttpGet httpget = new HttpGet(this.apkUrl);  
                   
        try  
        {       
            // �ͻ���ִ��get���� ������Ӧʵ��  
            HttpResponse response = httpCLient.execute(httpget);  

            // ��ȡ��Ӧ��Ϣʵ��  
            HttpEntity entity = response.getEntity();  
  
            // �ж���Ӧ�����Ƿ�Ϊnull     
            if(entity == null){  
            	mHandler.sendEmptyMessage(SERVER_ERROR);
                return;
            }
            
            // ��ȡ��Ӧ���ݳ��� 
            //long length = entity.getContentLength();
            long length = apkSize;
            // ��ȡ��Ӧ����
			if(length != -1){

				InputStream is = entity.getContent();
				count = 0;
				countfront = 0;//ǰһ�������
				byte buf[] = new byte[1024];
				int block_time = 0;
				
				/*��ȡ�ٶȵ��߳�*/
				 new Thread(new Runnable(){

						@Override
						public void run() {
							while(!interceptFlag){
								try {
									countfront = count;
									Thread.sleep(1000);
									speed = (count-countfront)/1000;
									mHandler.sendEmptyMessage(UPDATE_SPEED);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							
						}
		    	    	
		    	    }).start();
				
				 /*��ʼ����*/
				do{ 
					block_time = 0;
					int numread = is.read(buf);
		    		if(numread < 0){
		    			break;
		    		}
		    		count += numread;
		    	    progress =(byte)(((float)count / (length)) * 100);
		    	    
		    	    //���½���
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);
		    	    fos.write(buf,0,numread);
	    	    
		    	}while(!interceptFlag);//���ȡ����ֹͣ����.
					
				fos.close();
				is.close();
				
			}
			if(!interceptFlag){
				//���MD5�ǲ��ǶԵ�  ������ǶԵ� ����ʾ����ʧ��  ��ָ�� ����ַ��ȥ����
	    		//�������֪ͨ��װ
				isSD = true;//�������ص�sd��
	    		mHandler.sendEmptyMessage(DOWN_OVER);
	    		
	    	}
                          
        } catch (ClientProtocolException e){  
            e.printStackTrace();  
        } catch (IOException e){  
            e.printStackTrace(); mHandler.sendEmptyMessage(NET_ERROR); 
        }finally{  
            httpCLient.getConnectionManager().shutdown();  
        }  
  
	}
	
	/** 8-2.���أ�������
	 * */
	private void downloadApkToMyDir(){
		System.out.println("aaaaaaaaaaa��װ������8-2.���أ�������");
		// �����ڴ��ļ��������
		FileOutputStream out = null;
		try {
			out = activity.openFileOutput(saveFileName,Context.MODE_WORLD_WRITEABLE|Context.MODE_WORLD_READABLE);
		} catch (FileNotFoundException e1) {
			mHandler.sendEmptyMessage(CREATE_FILE_FAILED);
			return;
		}
		
		// ����Ĭ�ϵĿͻ���ʵ��  
        HttpClient httpCLient = new DefaultHttpClient();  
          
        // ����get����ʵ��  
        HttpGet httpget = new HttpGet(this.apkUrl);  
        
        try  
        {  
              
            // �ͻ���ִ��get���� ������Ӧʵ��  
            HttpResponse response = httpCLient.execute(httpget);  
              
            // ��ȡ��Ӧ��Ϣʵ��  
            HttpEntity entity = response.getEntity();  
            
           // �ж���Ӧ�����Ƿ�Ϊnull 
            if(entity == null){  
            	System.out.println("������ok");
            	mHandler.sendEmptyMessage(SERVER_ERROR);
            	return;
            } 
            
            // ��ȡ��Ӧ��Ϣʵ��ĳ���
            //long length = entity.getContentLength();System.out.println("��������"+length);
            long length = apkSize;
            // ��ȡ��Ӧ��Ϣʵ�������
			if(-1 != length){

				InputStream is = entity.getContent();
				count = 0;
				countfront = 0;
				byte buf[] = new byte[1024];
				int block_time = 0;
				
				/*��ȡ�ٶȵ��߳�*/
				 new Thread(new Runnable(){

						@Override
						public void run() {
							while(!interceptFlag){
								try {
									countfront = count;
									Thread.sleep(1000);
									speed = (count-countfront)/1000;
									mHandler.sendEmptyMessage(UPDATE_SPEED);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							
						}
		    	    	
		    	    }).start();
				
				 /*��ʼ����*/
				do{ 
					block_time = 0;
					int numread = is.read(buf);
		    		if(numread < 0){
		    			System.out.println("aaa");
		    			break;
		    		}System.out.println("bbb");
		    		count += numread;
		    	    progress =(byte)(((float)count / (length)) * 100);
		    	    
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);//���½���
		    	    out.write(buf,0,numread);

		    	}while(!interceptFlag);//���ȡ����ֹͣ����.
				System.out.println("ccc");
				out.close();
				is.close();
			}
			if(!interceptFlag){
				//���MD5�ǲ��ǶԵ�  ������ǶԵ� ����ʾ����ʧ��  ��ָ�� ����ַ��ȥ����
	    		//�������֪ͨ��װ
	    		mHandler.sendEmptyMessage(DOWN_OVER);
	    		isSD = false;//�������ص�sd��
	    	}
			
        } catch (ClientProtocolException e){  
            e.printStackTrace();  System.out.println("������1");
        } catch (IOException e){
        	mHandler.sendEmptyMessage(NET_ERROR);
            e.printStackTrace();  System.out.println("������2");
        }finally{  
            httpCLient.getConnectionManager().shutdown();  System.out.println("������3");
        }  
	}
		
	/**9-1.apk�Ĵ�С*/
	private int getApkFileSize(){
		HttpURLConnection conn = null;
		try{
			conn = (HttpURLConnection)(new URL(apkUrl)).openConnection();
			conn.setConnectTimeout(3000);
			conn.setRequestMethod("GET");System.out.println("DDDDDDDDDDDDDDDDDDDD_getsizea");
			int size = 0;
			if(HttpURLConnection.HTTP_OK == conn.getResponseCode()){
				size = conn.getContentLength();System.out.println("DDDDDDDDDDDDDDDDDDDD_getsizeb"+size);
			}System.out.println("DDDDDDDDDDDDDDDDDDDD_getsizec"+size);
			conn.disconnect();
			conn = null;
			return size;
		}catch(Exception e){
			return 0;
		}finally{
			if(conn != null){
				conn.disconnect();
				conn = null;
			}
		}
	}

	
	 /**
     * 10.��װapk
     */
	private void installApk(){

		if(isSD){
			File apkfile = new File(m_file,saveFileName);
	        if (!apkfile.exists()) {
	            return;
	        }   
			System.out.println("��װ"+apkfile.toString()); 		
	        Intent i = new Intent(Intent.ACTION_VIEW);
	        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
	        activity.startActivity(i);
		}else{
			System.out.println("��װ3");
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("file://" + activity.getFilesDir().getAbsolutePath() + "/" + saveFileName),"application/vnd.android.package-archive");
//			MainController.getInstance().getContext().startActivity(intent);
			activity.startActivity(intent);
		}
		//�����ǿ�Ƹ���  ��װ��ʱ��ס�����رտͻ���
//		if(force_update){
//			closeApplication();
//		}
		closeApplication();
	}
	
	/**11.����apkInfo*/
//	public void updateApkInfo(){
//		
//		if(true){
//			//1.��ȡ�ļ�
//			SharedPreferences preferences = activity.getSharedPreferences(activity.getResources().getString(R.string.apkinfo), Context.MODE_PRIVATE);
//			final Editor edit = preferences.edit();
//			
//			//2.�ӷ��������ذ汾��Ϣ
//			AsyncHttpClient httpClient = new AsyncHttpClient();
//			
//			RequestParams params = new RequestParams();//�����������
////			params.put("token", data);//���贫���������
//			System.out.println("3");
//			String url = activity.getResources().getString(R.string.website)+"IAPPInfo.cc";//���������url
//
//			httpClient.get(url, params, new AsyncHttpResponseHandler(){
//
//				@Override
//				public void onFailure(int arg0, Header[] arg1,
//						byte[] arg2, Throwable arg3) {
//					System.out.println("4");
//				}
//
//				@Override
//				public void onSuccess(int arg0, Header[] arg1,
//						byte[] arg2) {
//					System.out.println("5");
////					ApkInfo apkinfo = JSONHelper.getApkInfo(new String(arg2).trim());			
////					edit.putInt("apkId", apkinfo.getApkId());
////					edit.putString("apkName", apkinfo.getApkName());
////					edit.putString("apkVersion", apkinfo.getApkVersion());
////					edit.putString("apkNote", apkinfo.getApkNote());
////					edit.putInt("apkNumber", apkinfo.getApkNumber());
////					edit.commit();
//				}
//				
//			});
//		}
//	}
				
/**��������*******************************************************************/
	
	/**�ر�����Ӧ�ó���*/
	private void closeApplication(){
		mHandler.sendEmptyMessage(CLOSE_APP);
		listener.closeApp();		
	}
		
}
