package com.example.yzwy.lprmag.view;

import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.Paint;
        import android.graphics.Rect;
        import android.graphics.drawable.Drawable;
        import android.util.AttributeSet;
        import android.view.View;

import com.example.yzwy.lprmag.R;

public class ScanView extends View{
    private static final long ANIMATION_DELAY = 30;
    private static final int  LINE_HEIGHT = 30;
    private Paint finderMaslPaint;
    private int measureedWidth;
    private int measureedHeight;

    private Rect topRect = new Rect();
    private Rect bottomRect = new Rect();
    private Rect rightRect = new Rect();
    private Rect leftRect = new Rect();
    private Rect middleRect = new Rect();
    private Rect lineRect = new Rect();
    private Drawable zx_code_kuang;
    private Drawable zx_code_line;
    private int lineHeight;

    public ScanView(Context context,AttributeSet  attrs){
        super(context, attrs);
        init(context);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        //确定矩形框的大小
        canvas.drawRect(leftRect,finderMaslPaint);
        canvas.drawRect(topRect,finderMaslPaint);
        canvas.drawRect(rightRect,finderMaslPaint);
        canvas.drawRect(bottomRect,finderMaslPaint);
        //画框
        zx_code_kuang.setBounds(middleRect);
        zx_code_kuang.draw(canvas);
        //线的循环位置
        if(lineRect.bottom <  middleRect.bottom){
            zx_code_line.setBounds(lineRect);
            lineRect.top = lineRect.top + lineHeight / 5;
            lineRect.bottom = lineRect.bottom +lineHeight /5;
        }else{
            lineRect.set(middleRect);
            lineRect.bottom = lineRect.top +lineHeight;
            zx_code_line.setBounds(lineRect);
        }
        zx_code_line.draw(canvas);
        postInvalidateDelayed(ANIMATION_DELAY, middleRect.left, middleRect.top, middleRect.right, middleRect.bottom);
    }
    private void init(Context context) {
//        // 设置底层半透明
//        int finder_mask = context.getResources().getColor(R.color.qecode_transparent_bg);
        // 设置底层半透明
        int finder_mask = context.getResources().getColor(R.color.translucent);
        finderMaslPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        finderMaslPaint.setColor(finder_mask);
//        //引入项目所需的图
//        zx_code_kuang = context.getResources().getDrawable(R.drawable.qrcode_frame_icon);
//        zx_code_line = context.getResources().getDrawable(R.drawable.qrcode_line_icon);
        //引入项目所需的图
        zx_code_kuang = context.getResources().getDrawable(R.drawable.app_logo);
        zx_code_line = context.getResources().getDrawable(R.drawable.app_logo);
        //线的高度
        lineHeight = LINE_HEIGHT;
    }

    public Rect getScanImageRect(int w, int h) {
        //先求出实际矩形
        Rect rect = new Rect();
        float temp_w = w / (float) measureedWidth;
        rect.left = (int)(middleRect.left * temp_w);
        rect.right = (int)(middleRect.right * temp_w);
        float temp_h = h / (float) measureedHeight;
        rect.top = (int) (middleRect.top * temp_h);
        rect.bottom = (int) (middleRect.bottom * temp_h);
        return rect;
    }

    //自定义View重载函数
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //屏幕初始值
        measureedWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureedHeight = MeasureSpec.getSize(heightMeasureSpec);
        int borderWidth = measureedWidth / 2 +100;
        //矩形框的位置[对应位置设置，依次为左上右下，数值越大矩形扫描位置更改越大。]
        middleRect.set((measureedWidth - borderWidth) /2,(measureedHeight - borderWidth) / 4,(measureedWidth - borderWidth) / 2 + borderWidth,(measureedHeight - borderWidth) / 4 + borderWidth);
        lineRect.set(middleRect);
        //确定矩形框大小，分别减去左上右下的多余部分，确定大小
        leftRect.set(0, middleRect.top, middleRect.left,middleRect.bottom);
        topRect.set(0,0, measureedWidth,middleRect.top);
        rightRect.set(middleRect.right, middleRect.top, measureedWidth, middleRect.bottom);
        bottomRect.set(0,  middleRect.bottom, measureedWidth,measureedHeight);



    }


}
