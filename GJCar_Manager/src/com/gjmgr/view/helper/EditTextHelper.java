package com.gjmgr.view.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;

public class EditTextHelper {

	public void setEditText_Clear(final EditText edit, final ImageView delete){
		
		/*文本*/
		edit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				
				if(s.length() == 0){
					delete.setVisibility(View.GONE);
				}else{
					delete.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}
			
			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
		
		/*焦点*/
		edit.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				
				if(hasFocus){
					if(edit.getText().toString().length() == 0){
						delete.setVisibility(View.GONE);
					}else{
						delete.setVisibility(View.VISIBLE);
					}
				}else{
					delete.setVisibility(View.GONE);
				}
			}
		});
		
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				edit.setText("");
			}
		});
	}
	
	public void setEditText_Password(final EditText edit, final ImageView delete, final ImageView show, final int[] imageIds){
		
		/*文本*/
		edit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				
				if(s.length() == 0){
					delete.setVisibility(View.GONE);
				}else{
					delete.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}
			
			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
		
		/*焦点*/
		edit.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				
				if(hasFocus){
					if(edit.getText().toString().length() == 0){
						delete.setVisibility(View.GONE);
					}else{
						delete.setVisibility(View.VISIBLE);
					}
				}else{
					delete.setVisibility(View.GONE);
				}
			}
		});
		
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				edit.setText("");
			}
		});
		
		show.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(edit.getInputType() == 0x81){//密文
					edit.setInputType(0x90);
					show.setImageResource(imageIds[0]);
				}else{//明文0x90
					edit.setInputType(0x81);
					show.setImageResource(imageIds[1]);
				}
			}
		});
		
		
	}
}
