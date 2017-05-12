package com.gjmgr.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
* Բ��ImageView
* 
* @author skg
* 
*/
public class ImageView_Round extends ImageView {

        public ImageView_Round(Context context, AttributeSet attrs) {
                super(context, attrs);
                init();
        }

        public ImageView_Round(Context context) {
                super(context);
                init();
        }

        private final RectF roundRect = new RectF();
        private float rect_adius = 10;//Բ�ǵİ뾶
        private final Paint maskPaint = new Paint();
        private final Paint zonePaint = new Paint();

        private void init() {
                maskPaint.setAntiAlias(true);
                maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                //
                zonePaint.setAntiAlias(true);
                zonePaint.setColor(Color.WHITE);
                //
                float density = getResources().getDisplayMetrics().density;
                rect_adius = rect_adius * density;
        }

        public void setRectAdius(float adius) {
                rect_adius = adius;
                invalidate();
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right,
                        int bottom) {
                super.onLayout(changed, left, top, right, bottom);
                
                int w = getWidth();
                int h = getHeight();
                rect_adius = w;//�Լ����õ�
                roundRect.set(0, 0, w, h);//w,h��������������4��Բ�ǵĳ�����w=h��Ϊ�����Σ�������rect_adius = w = h����ʱ�����ľ���1/4��Բ��
        }

        @Override
        public void draw(Canvas canvas) {
                canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
               // rect_adius ��layoutwidth���ʱΪԭ��
                //
                canvas.drawRoundRect(roundRect, rect_adius, rect_adius, zonePaint);
                
                canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
                super.draw(canvas);
                canvas.restore();
        }

}