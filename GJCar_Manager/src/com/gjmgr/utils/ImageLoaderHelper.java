package com.gjmgr.utils;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;

import com.gjmgr.app.R;
import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;


/** www:http://blog.csdn.net/vipzjyno1/article/details/23206387*/
public class ImageLoaderHelper {
		
	/**ImageLoader�ĳ�ʼ��*/
	public static void initImageLoader(Context context){

		//���û����ļ���Ŀ¼
		File cacheDir = StorageUtils.getOwnCacheDirectory(context.getApplicationContext(), "imageloader/Cache");
		
		//��������
		ImageLoaderConfiguration config = new ImageLoaderConfiguration
				.Builder(context)
				.memoryCacheExtraOptions(480,800)//�����ÿ�������ļ�������
				//.discCacheExtraOptions(480, 800, CompressFormat.PNG, 75, null)���û������ϸ��Ϣ����ò�Ҫ�������
				.memoryCache(new UsingFreqLimitedMemoryCache(2*1024*1024))//�����ͨ���Լ����ڴ�ʵ��
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY-2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCacheSize(2*1024*1024)
				.discCacheSize(50*1024*1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())//�������ʱ���URI������MD5����
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(100)//������ļ�������
				.discCache(new UnlimitedDiscCache(cacheDir))//�Զ��建��·��
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000))//connectionTimeout(5s),readTimeout(30 s)��ʱ����
				.writeDebugLogs()//Remove for release app
				.build();//��ʼ����
		
		//��ʼ��
		ImageLoader.getInstance().init(config);
	}
	
	/** ʹ��ImageLoader����ͼƬ���ص�ʱ����Ҫʵ����ImageLoader���������·�������ʵ��������ÿ���������涼Ҫʵ��������ʹ��*/
	public static DisplayImageOptions initDisplayImageOptions(){
		
		DisplayImageOptions options = new DisplayImageOptions
			.Builder()
			.showImageOnLoading(R.drawable.carlist_logo)//����ͼƬ�����ڼ���ʾ��ͼƬ
			.showImageForEmptyUri(R.drawable.carlist_logo)//����ͼƬuriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
			.showImageOnFail(R.drawable.carlist_logo)//����ͼƬ���ػ�������ʱ�Ĵ�����ʾ��ͼƬ
			.cacheInMemory(true)//�������ص�ͼƬ�Ƿ񻺴����ڴ���
			.cacheOnDisc(true)//�������ص�ͼƬ�Ƿ񻺴���sd����	
			.considerExifParams(true)//�Ƿ���JPEGͼ��EXIF����(��ת����ת)
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//����ͼƬ����εı��뷽ʽ��ʾ
			.bitmapConfig(Bitmap.Config.RGB_565)//����ͼƬ�Ľ�������
			//.decodingOptions(new Options())//����ͼƬ�Ľ�������
			//.delayBeforeLoading(int delayInMillis)//����ǰ���ӳ�ʱ��
			//����ͼƬ���뻺��ǰ����bitmap��������
			//.preProcessor(BitmapProcessor preProcessor)
			.resetViewBeforeLoading(true)//����ͼƬ������ǰ�Ƿ����ã���λ
			.displayer(new RoundedBitmapDisplayer(20))//�Ƿ�����ΪԲ�ǣ�����Ϊ����
			.displayer(new FadeInBitmapDisplayer(100))//�Ƿ�ͼƬ���غú���Ķ���ʱ��
			.build();	
		
		return options;
	}
	
	
}
