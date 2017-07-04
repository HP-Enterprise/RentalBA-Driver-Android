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
 * 1.apk解压包出错的问题
 * 现象：我是下载成功了本次，而之前下载的破文件，会影响本次安装
 * 原因：
//第一次下载，会生成hbzh.apk
   第二次下载，会生成hbzh.apk，而第一次的变为hbzh.apk.apk
   第三次下载，会生成hbzh.apk, 而第二次变为hbzh.apk.apk,  而第三次变为hbhz.apk.apk.apk
   第三次之后，每次的都会替代第一个

   而以下原因造成生成的apk文件不正确，不正确的apk文件也会生成，但解析出错

   网络中断：不会生成
   人为中断：手动取消
   后台软件错误
   
 实际情况：应该是软件的问题，因为如果是中途停止了，是不会installapk的，就不会显示解析失败  
*/

/**2.断线重连：使用RandomAccessFile类,
 *     网络没有自己识别的机制
 *     当网络异常，HttpClient就会捕获异常catch(){}
 *     当网络重连时，只能使用RandomAccessFile跳帧来读取，不过没时间*/

/**3.下载之后，不安装，会出现.apk.apk.apk
 * */
/**apk软件更新*/
public class UpdateUtils{

	//测试信息
	//http://61.183.247.74/download/AHBZHNewVision8-22.apk
	//http://down.17huang.com/17app/17huang_3.2.apk	
	
	//名称
	private static String saveFileName = "GJCar_Manager";	//apk名字
	
	//返回的安装包url
	public String apkUrl = "";
	public String apkMD5 = "";
	
	
	/*下载*/
	private Dialog downloadApkDialog;
	private TextView title,apkNameTv,total,speedText;//名称，apkname，总下载量，下载速度
	private ProgressBar progressBar;//进度条
	private Button cancleButton;
	private long apkSize;//app的名称
	private String apkMaxSize = "";//7.2MB
	
	private int countfront ;
	private int count;
	private float speed;
	
	/* 下载包安装路径 */
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
    
    /* 下载流程控制 */
    private static boolean isSD;//是否下载到了sd卡中
    private static final int CLOSE_APP = 50;
    private byte progress;
    private Thread downLoadThread;
    private boolean interceptFlag = false;
    private Handler mHandler;
    public boolean force_update = false;	//是否强制更新
    private String server_version= "";			//服务器版本号
	private String low_version;				//最低版本号
	private ProgressDialog tip;
	public boolean file_useable;
	public FileManager manager;	
	private Activity activity;	
	public boolean isJump = false;
	
	/** 构造函数*/
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
						Toast.makeText(activity, "新版本有更多更好的功能及更炫的界面，强烈建议您更新", Toast.LENGTH_SHORT).show();
						break;						
					case PROGRESS_SHOW:
						checkUpdateInfoDialog();
						break;
						
					case SDCARD_CANNOT_USE:
						Toast.makeText(activity, "SD卡处于不可用状态", Toast.LENGTH_LONG).show();
						break;
					case SDCARD_SHEARED:
						Toast.makeText(activity, "SD卡处于USB打开状态,不可用", Toast.LENGTH_LONG).show();
						break;
					case SDCARD_NOENOUGH:
						Toast.makeText(activity, "SD卡可用空间不足", Toast.LENGTH_LONG).show();
						break;
					case CREATE_FILE_FAILED:
						Toast.makeText(activity, "创建文件失败", Toast.LENGTH_LONG).show();
						break;
					case ROM_NOENOUGH:
						Toast.makeText(activity, "机身内存不足", Toast.LENGTH_LONG).show();
						break;
					
					case SERVER_ERROR:
						Toast.makeText(activity, "服务器故障，sorry", Toast.LENGTH_LONG).show();
						break;
					
					case NET_ERROR:
						Toast.makeText(activity, "网络故障，sorry", Toast.LENGTH_LONG).show();
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
    

    /**1.开始 */
	public void UpdateManager_do(){
		checkApkUpdate();
	}
    
    /** 2.核对更新信息的线程*/
    private void checkApkUpdate(){
    	checkUpdate();
    	
    }
    
    /** 3.核对更新信息的方法*/
    private void checkUpdate(){

//    	this.apkUrl = "http://www.feeling.hpecar.com/api/appManage/file?filename=9627aa3f-f8a8-41c4-ad74-1039841b8d4e.apk";
//    	this.apkUrl = "http://p.gdown.baidu.com/2849f8378dcef29e4618d6a241dc9af86a8aee48c4c1f6eef4a664654d2de2446d15d28fbad76f4af7e8cd73021df8659dee42b71a1ee6220228996cc39e219b4dd4e14dc8d793109b7e4a6d0c732a48a0f7464af3f7c6d20a84fbb838bc86e47390b2116472f77f2f54e781cd729bd5c44f72e893b6239fd1df8546b9127392dd38c0e9f8074932ba74b2c5e8f7c4a584a11d093ef8121b";
//   	this.apkUrl = "http://www.feeling.hpecar.com/api/appManage/file?filename=9627aa3f-f8a8-41c4-ad74-1039841b8d4e.apk";
    	
//    	saveFileName = saveFileName + server_version + ".apk";
    	saveFileName = saveFileName + Public_Param.Version_Name + ".apk";
    	mHandler.sendEmptyMessage(PROGRESS_SHOW);
    	
    }
	
	/**4.更新软件的提示对话框 */
	public void checkUpdateInfoDialog(){
		showNoticeDialog();
	}
	
	/**4-1.更新软件的提示对话框：显示 */
	private void showNoticeDialog(){
//		AlertDialog.Builder builder = new Builder(activity);
//		builder.setTitle("软件版本更新");
//		builder.setMessage(PublicMessage.update_hini);
//		builder.setPositiveButton("立即更新", new OnClickListener() {			
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				showDownloadDialog();	//???????????????????????????		
//			}
//		});
//		builder.setNegativeButton("稍后再说", new OnClickListener(){			
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
		title.setText("版本更新:"+Public_Param.Version_Name);//软件版本从服务器获取，不从apk中获取(apk还没下载下来)
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
		
	/**4-3.更新软件的提示对话框：关闭 */
	public void showDownload(){
		if(downloadApkDialog != null){
			downloadApkDialog.show();
		}else{
			showDownloadDialog();
		}
	}
	
	
	/** 5.弹出下载进度对话框*/
	private void showDownloadDialog(){
//		downloadDialog = new ProgressDialog(activity);
//		downloadDialog.setTitle("软件版本更新");
//		downloadDialog.setMax(100);
//		downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//		downloadDialog.setCancelable(true);
//		downloadDialog.setButton("取消", new OnClickListener(){
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
		
		/*初始化控件*/
		title = (TextView)view.findViewById(R.id.dialog_downapk_title);
		apkNameTv = (TextView)view.findViewById(R.id.dialog_downapk_apkname);
		total = (TextView)view.findViewById(R.id.dialog_downapk_total);
		speedText = (TextView)view.findViewById(R.id.dialog_downapk_speed);
		progressBar = (ProgressBar)view.findViewById(R.id.dialog_downapk_progressbar);
		cancleButton = (Button)view.findViewById(R.id.dialog_downapk_cancle);
		
		title.setText("软件更新："+Public_Param.Version_Name);
		
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
		
		/*对话框的设置*/
		downloadApkDialog.getWindow().setContentView(view);
		downloadApkDialog.setCancelable(false);
		downloadApkDialog.setCanceledOnTouchOutside(false);
		downloadApkDialog.show();
		downloadApk();
	}
	
	/** 6.下载的线程*/
	private void downloadApk(){
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	
	/** 7.下载的runnable  */
	private Runnable mdownApkRunnable = new Runnable() {	
		public void run(){
			try{
				
				/*1.判断文件目录是否创建成功*/
				file_useable = makeUseablePath();System.out.println("DDDDDDDDDDDDDDDDDDD_makeUser");
				//apkSize = getApkFileSize();//这里获取apk的大小
				System.out.println("DDDDDDDDDDDDDDDDDDD"+apkSize);
				float downSize =  new BigDecimal(((float)apkSize)/1000000).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
				apkMaxSize = downSize+"MB";
				if(file_useable){
					m_file = manager.getFile();
					System.out.println("sd----------------路径:"+m_file);
										
					if(manager.getAvaliableSpaceOfSDCard() < apkSize){
						
						//下载至内存
						mHandler.sendEmptyMessage(SDCARD_NOENOUGH);
						if(manager.getRomAvailableSize() < apkSize){//内存是否足够
							mHandler.sendEmptyMessage(ROM_NOENOUGH);
						}else{
							downloadApkToMyDir();
						}
						
					}else{
						//下载至SD卡
					
						downloadApkToSDCard();
					}
					
				}else{
					//下载至内存
					if(manager.getRomAvailableSize() < apkSize){//内存是否足够
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
	
	/** 8：判断文件夹是否存在或是否生成*/
	private boolean makeUseablePath(){
		manager = new FileManager();
		int state = manager.makeHallPath();System.out.println("sd----------------创建sd卡是否成功:"+state);
		switch(state){
		
			case FileManager.SD_SUCCESS://创建目录成功
				return true;
			case FileManager.SD_SHARE:
				mHandler.sendEmptyMessage(SDCARD_SHEARED);//SD卡处于共享
				break;
			case FileManager.SDCANNOT_USE://SD卡无法使用
				mHandler.sendEmptyMessage(SDCARD_CANNOT_USE);
				break;
			case FileManager.SD_FAILED://创建目录失败
				mHandler.sendEmptyMessage(CREATE_FILE_FAILED);
				break;
			default:
				break;
		}
		return false;
	}
	
	/** */
	
	/**8-1下载至sd卡 */
	private void downloadApkToSDCard() throws FileNotFoundException{
		System.out.println("下载aaaaaaaaaaaaaa_8-1下载至sd卡");
		//创建文件
		File ApkFile = new File(m_file,saveFileName);
		RandomAccessFile fos = new RandomAccessFile(ApkFile,"rwd");
		
		// 创建默认的客户端实例  
        HttpClient httpCLient = new DefaultHttpClient();  
          
        // 创建get请求实例  
        HttpGet httpget = new HttpGet(this.apkUrl);  
                   
        try  
        {       
            // 客户端执行get请求 返回响应实体  
            HttpResponse response = httpCLient.execute(httpget);  

            // 获取响应消息实体  
            HttpEntity entity = response.getEntity();  
  
            // 判断响应内容是否为null     
            if(entity == null){  
            	mHandler.sendEmptyMessage(SERVER_ERROR);
                return;
            }
            
            // 获取响应内容长度 
            //long length = entity.getContentLength();
            long length = apkSize;
            // 获取响应内容
			if(length != -1){

				InputStream is = entity.getContent();
				count = 0;
				countfront = 0;//前一秒的流量
				byte buf[] = new byte[1024];
				int block_time = 0;
				
				/*获取速度的线程*/
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
				
				 /*开始下载*/
				do{ 
					block_time = 0;
					int numread = is.read(buf);
		    		if(numread < 0){
		    			break;
		    		}
		    		count += numread;
		    	    progress =(byte)(((float)count / (length)) * 100);
		    	    
		    	    //更新进度
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);
		    	    fos.write(buf,0,numread);
	    	    
		    	}while(!interceptFlag);//点击取消就停止下载.
					
				fos.close();
				is.close();
				
			}
			if(!interceptFlag){
				//检测MD5是不是对的  如果不是对的 就提示下载失败  到指定 的网址那去下载
	    		//下载完成通知安装
				isSD = true;//不是下载到sd卡
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
	
	/** 8-2.下载：至机身
	 * */
	private void downloadApkToMyDir(){
		System.out.println("aaaaaaaaaaa安装至机身8-2.下载：至机身");
		// 创建内存文件的输出流
		FileOutputStream out = null;
		try {
			out = activity.openFileOutput(saveFileName,Context.MODE_WORLD_WRITEABLE|Context.MODE_WORLD_READABLE);
		} catch (FileNotFoundException e1) {
			mHandler.sendEmptyMessage(CREATE_FILE_FAILED);
			return;
		}
		
		// 创建默认的客户端实例  
        HttpClient httpCLient = new DefaultHttpClient();  
          
        // 创建get请求实例  
        HttpGet httpget = new HttpGet(this.apkUrl);  
        
        try  
        {  
              
            // 客户端执行get请求 返回响应实体  
            HttpResponse response = httpCLient.execute(httpget);  
              
            // 获取响应消息实体  
            HttpEntity entity = response.getEntity();  
            
           // 判断响应内容是否为null 
            if(entity == null){  
            	System.out.println("至机身ok");
            	mHandler.sendEmptyMessage(SERVER_ERROR);
            	return;
            } 
            
            // 获取响应消息实体的长度
            //long length = entity.getContentLength();System.out.println("至机身长度"+length);
            long length = apkSize;
            // 获取响应消息实体的内容
			if(-1 != length){

				InputStream is = entity.getContent();
				count = 0;
				countfront = 0;
				byte buf[] = new byte[1024];
				int block_time = 0;
				
				/*获取速度的线程*/
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
				
				 /*开始下载*/
				do{ 
					block_time = 0;
					int numread = is.read(buf);
		    		if(numread < 0){
		    			System.out.println("aaa");
		    			break;
		    		}System.out.println("bbb");
		    		count += numread;
		    	    progress =(byte)(((float)count / (length)) * 100);
		    	    
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);//更新进度
		    	    out.write(buf,0,numread);

		    	}while(!interceptFlag);//点击取消就停止下载.
				System.out.println("ccc");
				out.close();
				is.close();
			}
			if(!interceptFlag){
				//检测MD5是不是对的  如果不是对的 就提示下载失败  到指定 的网址那去下载
	    		//下载完成通知安装
	    		mHandler.sendEmptyMessage(DOWN_OVER);
	    		isSD = false;//不是下载到sd卡
	    	}
			
        } catch (ClientProtocolException e){  
            e.printStackTrace();  System.out.println("至机身1");
        } catch (IOException e){
        	mHandler.sendEmptyMessage(NET_ERROR);
            e.printStackTrace();  System.out.println("至机身2");
        }finally{  
            httpCLient.getConnectionManager().shutdown();  System.out.println("至机身3");
        }  
	}
		
	/**9-1.apk的大小*/
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
     * 10.安装apk
     */
	private void installApk(){

		if(isSD){
			File apkfile = new File(m_file,saveFileName);
	        if (!apkfile.exists()) {
	            return;
	        }   
			System.out.println("安装"+apkfile.toString()); 		
	        Intent i = new Intent(Intent.ACTION_VIEW);
	        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
	        activity.startActivity(i);
		}else{
			System.out.println("安装3");
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("file://" + activity.getFilesDir().getAbsolutePath() + "/" + saveFileName),"application/vnd.android.package-archive");
//			MainController.getInstance().getContext().startActivity(intent);
			activity.startActivity(intent);
		}
		//如果是强制更新  安装的时候住主动关闭客户端
//		if(force_update){
//			closeApplication();
//		}
		closeApplication();
	}
	
	/**11.更新apkInfo*/
//	public void updateApkInfo(){
//		
//		if(true){
//			//1.获取文件
//			SharedPreferences preferences = activity.getSharedPreferences(activity.getResources().getString(R.string.apkinfo), Context.MODE_PRIVATE);
//			final Editor edit = preferences.edit();
//			
//			//2.从服务器加载版本信息
//			AsyncHttpClient httpClient = new AsyncHttpClient();
//			
//			RequestParams params = new RequestParams();//设置请求参数
////			params.put("token", data);//无需传递请求参数
//			System.out.println("3");
//			String url = activity.getResources().getString(R.string.website)+"IAPPInfo.cc";//设置请求的url
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
				
/**辅助方法*******************************************************************/
	
	/**关闭整个应用程序*/
	private void closeApplication(){
		mHandler.sendEmptyMessage(CLOSE_APP);
		listener.closeApp();		
	}
		
}
