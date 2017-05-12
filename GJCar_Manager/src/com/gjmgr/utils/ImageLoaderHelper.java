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
		
	/**ImageLoader的初始化*/
	public static void initImageLoader(Context context){

		//设置缓存文件的目录
		File cacheDir = StorageUtils.getOwnCacheDirectory(context.getApplicationContext(), "imageloader/Cache");
		
		//参数设置
		ImageLoaderConfiguration config = new ImageLoaderConfiguration
				.Builder(context)
				.memoryCacheExtraOptions(480,800)//保存的每个缓存文件的最长宽度
				//.discCacheExtraOptions(480, 800, CompressFormat.PNG, 75, null)设置缓存的详细信息，最好不要设置这个
				.memoryCache(new UsingFreqLimitedMemoryCache(2*1024*1024))//你可以通过自己的内存实现
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY-2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCacheSize(2*1024*1024)
				.discCacheSize(50*1024*1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(100)//缓存的文件的数量
				.discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000))//connectionTimeout(5s),readTimeout(30 s)超时连接
				.writeDebugLogs()//Remove for release app
				.build();//开始创建
		
		//初始化
		ImageLoader.getInstance().init(config);
	}
	
	/** 使用ImageLoader进行图片加载的时候，先要实例化ImageLoader，调用以下方法进行实例化，在每个布局里面都要实例化后再使用*/
	public static DisplayImageOptions initDisplayImageOptions(){
		
		DisplayImageOptions options = new DisplayImageOptions
			.Builder()
			.showImageOnLoading(R.drawable.carlist_logo)//设置图片下载期间显示的图片
			.showImageForEmptyUri(R.drawable.carlist_logo)//设置图片uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.carlist_logo)//设置图片加载或解码过程时的错误显示的图片
			.cacheInMemory(true)//设置下载的图片是否缓存在内存中
			.cacheOnDisc(true)//设置下载的图片是否缓存在sd卡中	
			.considerExifParams(true)//是否考虑JPEG图像EXIF参数(旋转，翻转)
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
			.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
			//.decodingOptions(new Options())//设置图片的解码配置
			//.delayBeforeLoading(int delayInMillis)//下载前的延迟时间
			//设置图片加入缓存前，对bitmap进行设置
			//.preProcessor(BitmapProcessor preProcessor)
			.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
			.displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
			.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
			.build();	
		
		return options;
	}
	
	
}
