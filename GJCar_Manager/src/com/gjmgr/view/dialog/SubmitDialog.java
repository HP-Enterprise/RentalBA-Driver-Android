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
		
/**�ύ�Ի���
 * 1.����2.��ʾ3.��ʧ4.��ʧ�����⴦��������ϣ��ֶ�ȡ��������**********************************************************/		

		public static Dialog submitDialog = null; 
		
		/**�ύ�Ի���*/
		public static void showSubmitDialog(Context context){
			if(submitDialog != null){
				submitDialog.dismiss();				
			}
			submitDialog = new Dialog(context, R.style.delete_dialog);System.out.println("d1");
			
			View view = View.inflate(context, R.layout.dialog_submit, null);System.out.println("d2");
			
			/*����СԲȦ*/
			ImageView image= (ImageView)view.findViewById(R.id.dialog_sumbit_image);
			Animation animation = AnimationUtils.loadAnimation(context, R.anim.load);
			LinearInterpolator lir = new LinearInterpolator();  System.out.println("d3");  
			animation.setInterpolator(lir);//������ת
			image.startAnimation(animation);System.out.println("d4");
			
		    submitDialog.setContentView(view);System.out.println("d5");
		    submitDialog.setCancelable(true);
		    submitDialog.setCanceledOnTouchOutside(false);
		    submitDialog.show();System.out.println("d6");
		}
		
		/**�رնԻ���*/
		public static void closeSubmitDialog(){
			if(submitDialog != null){
				submitDialog.dismiss();;
			}
		}

}
