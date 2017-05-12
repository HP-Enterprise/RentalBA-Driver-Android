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
* 圆角ImageView
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
        private float rect_adius = 10;//圆角的半径
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
                rect_adius = w;//自己设置的
                roundRect.set(0, 0, w, h);//w,h就是你所画出的4个圆角的长宽，当w=h，为正方形，如果你把rect_adius = w = h，这时画出的就是1/4的圆形
        }

        @Override
        public void draw(Canvas canvas) {
                canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
               // rect_adius 和layoutwidth相等时为原型
                //
                canvas.drawRoundRect(roundRect, rect_adius, rect_adius, zonePaint);
                
                canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
                super.draw(canvas);
                canvas.restore();
        }

}