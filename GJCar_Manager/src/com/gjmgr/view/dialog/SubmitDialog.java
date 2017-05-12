package com.gjmgr.view.dialog;

import com.gjmgr.app.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class SubmitDialog {
		
/**提交对话框：
 * 1.布局2.显示3.消失4.消失的特殊处理：网络故障，手动取消，回收**********************************************************/		

		public static Dialog submitDialog = null; 
		
		/**提交对话框*/
		public static void showSubmitDialog(Context context){
			if(submitDialog != null){
				submitDialog.dismiss();				
			}
			submitDialog = new Dialog(context, R.style.delete_dialog);System.out.println("d1");
			
			View view = View.inflate(context, R.layout.dialog_submit, null);System.out.println("d2");
			
			/*滚动小圆圈*/
			ImageView image= (ImageView)view.findViewById(R.id.dialog_sumbit_image);
			Animation animation = AnimationUtils.loadAnimation(context, R.anim.load);
			LinearInterpolator lir = new LinearInterpolator();  System.out.println("d3");  
			animation.setInterpolator(lir);//匀速旋转
			image.startAnimation(animation);System.out.println("d4");
			
		    submitDialog.setContentView(view);System.out.println("d5");
		    submitDialog.setCancelable(true);
		    submitDialog.setCanceledOnTouchOutside(false);
		    submitDialog.show();System.out.println("d6");
		}
		
		/**关闭对话框*/
		public static void closeSubmitDialog(){
			if(submitDialog != null){
				submitDialog.dismiss();;
			}
		}

}
