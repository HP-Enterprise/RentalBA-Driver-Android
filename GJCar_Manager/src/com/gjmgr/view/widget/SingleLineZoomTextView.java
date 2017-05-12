package com.gjmgr.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class SingleLineZoomTextView extends TextView {

    private Paint mPaint;
    private float mTextSize;

    public SingleLineZoomTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    public SingleLineZoomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    public SingleLineZoomTextView(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }
    /**
     * getTextSize ����ֵ��������(px)Ϊ��λ�ģ��� setTextSize() Ĭ���� sp Ϊ��λ
     * �������Ҫ�������ص�λ setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
     */
    private void refitText(String text, int textWidth) {
        if (textWidth > 0) {
            mTextSize = this.getTextSize();//������صĵ�λΪpx
            mPaint = new Paint();
            mPaint.set(this.getPaint());
            int drawWid = 0;//drawableLeft��Right��Top��Buttom ����ͼƬ�Ŀ�
            Drawable[] draws = getCompoundDrawables();
            for (int i = 0; i < draws.length; i++) {
                if(draws[i]!= null){
                    drawWid += draws[i].getBounds().width();
                }
            }
            //��õ�ǰTextView����Ч���
            int availableWidth = textWidth - this.getPaddingLeft()
                    - this.getPaddingRight()- getCompoundDrawablePadding()- drawWid;
            //�����ַ���ռ���ؿ��
            float textWidths = getTextLength(mTextSize, text);
            while(textWidths > availableWidth){
                mPaint.setTextSize(--mTextSize);//���ﴫ��ĵ�λ�� px
                textWidths = getTextLength(mTextSize, text);
            }
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);//�������õ�λΪ px
        }
    }

    /**
     * @param textSize
     * @param text
     * @return �ַ�����ռ���ؿ��
     */
    private float getTextLength(float textSize,String text){
        mPaint.setTextSize(textSize);
        return mPaint.measureText(text);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        refitText(getText().toString(), this.getWidth());
    }

}