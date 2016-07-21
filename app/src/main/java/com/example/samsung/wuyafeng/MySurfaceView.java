package com.example.samsung.wuyafeng;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

/**
 * Created by matlab_user on 2016/7/21.
 */


class Explosion {

    MySurfaceView gameView;
    private Bitmap[] bitmaps;// 位图
    float x;// x方向位移
    float y;// y方向位移
    private int anmiIndex = 0;// 爆炸动画帧索引

    public Explosion(MySurfaceView gameView, Bitmap[] bitmaps, float x, float y) {
        this.gameView = gameView;
        this.bitmaps = bitmaps;
        this.x = x;
        this.y = y;
    }

    // 绘制背景的方法
    public void drawSelf(Canvas canvas, Paint paint) {
        if (anmiIndex >= bitmaps.length - 1) {// 如果动画播放完毕，不再绘制爆炸效果
            return;
        }
        canvas.drawBitmap(bitmaps[anmiIndex], x, y, paint);// 绘制数组中某一幅图
        anmiIndex++;// 当前下标加1
    }


};

class Bullet {

    MySurfaceView gameView;
    private Bitmap bitmap;// 位图
    private Bitmap[] bitmaps;// 爆炸动画图组
    float x;// x方向位移
    float y;// y方向位移
    float vx;// x方向速度
    float vy;// y方向速度
    private float t = 0;// 时间
    private float timeSpan = 0.5f;// 时间间隔
    int size;// 子弹尺寸
    boolean explodeFlag = false;// 是否绘制子弹的标记
    Explosion mExplosion;// 爆炸对象引用

    // 构造器
    public Bullet(MySurfaceView gameView, Bitmap bitmap, Bitmap[] bitmaps,
                  float x, float y, float vx, float vy) {
        this.gameView = gameView;// 成员变量赋值
        this.bitmap = bitmap;
        this.bitmaps = bitmaps;
        this.x = x;// 成员变量赋值
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        size = bitmap.getHeight();// 获得图片的高度
    }

    // 绘制子弹的方法
    public void drawSelf(Canvas canvas, Paint paint) {
        if (explodeFlag && mExplosion != null) {// 如果已经爆炸，绘制爆炸动画
            mExplosion.drawSelf(canvas, paint);
        } else {
            go();// 子弹前进
            canvas.drawBitmap(bitmap, x, y, paint);// 绘制子弹
        }
    }

    // 子弹前进的方法
    public void go() {
        x += vx * t;// 水平方向匀速直线运动
        y += vy * t + 0.5f * Constant.G * t * t;// 竖直方向匀加速直线运动
        if (x >= Constant.EXPLOSION_X || y >= Constant.SCREEN_HEIGHT) {// 子弹在特定位置爆炸
            mExplosion = new Explosion(gameView, bitmaps, x, y);// 创建爆炸对象
            explodeFlag = true;// 不再绘制子弹
            return;
        }
        t += timeSpan;// 时间间隔
    }


}


class DrawThread extends Thread {
    private boolean flag = true;//线程工作标志位
    private int sleepSpan = 100;//线程休眠时间
    MySurfaceView gameView;//父界面引用
    SurfaceHolder surfaceHolder;//surfaceHolder引用

    public DrawThread(MySurfaceView gameView) {//构造器
        this.gameView = gameView;
        this.surfaceHolder = gameView.getHolder();
    }

    public void run() {
        Canvas c;//声明画布
        while (this.flag) {
            c = null;
            try {
                // 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
                c = this.surfaceHolder.lockCanvas(null);
                synchronized (this.surfaceHolder) {
                    gameView.onDraw(c);// 绘制
                }
            } finally {
                if (c != null) {// 并释放锁
                    this.surfaceHolder.unlockCanvasAndPost(c);
                }
            }
            try {
                Thread.sleep(sleepSpan);// 睡眠指定毫秒数
            } catch (Exception e) {
                e.printStackTrace();// 打印堆栈信息
            }
        }
    }

    public void setFlag(boolean flag) {//设置标志位的方法
        this.flag = flag;
    }
}


public class MySurfaceView extends SurfaceView implements
        SurfaceHolder.Callback {
    MainActivity activity;// activity的引用
    Paint paint;// 画笔引用
    DrawThread drawThread;// 绘制线程引用
    Bitmap bgBmp;//背景图片
    Bitmap bulletBmp;// 子弹位图
    Bitmap[] explodeBmps;//爆炸位图数组
    Bullet bullet;//子弹对象引用
    public MySurfaceView(final MainActivity activity, Button b2D, View.OnClickListener returnListener) {//构造器
        super(activity);
        this.activity = activity;
        // 获得焦点并设置为可触控

        b2D.setOnClickListener								//为打开按钮添加监听器
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置为横屏
                        activity.setContentView(activity.gameView);//将该界面设置为窗体内容


                    }
                });

        this.requestFocus();
        this.setFocusableInTouchMode(true);
        getHolder().addCallback(this);// 注册回调接口
    }

    @Override
    protected void onDraw(Canvas canvas) {//绘制界面的方法
        super.onDraw(canvas);
        canvas.drawBitmap(bgBmp, 0, 0, paint);//绘制背景
        bullet.drawSelf(canvas, paint);//绘制子弹
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {//界面变化时调用的方法
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        paint = new Paint();// 创建画笔
        paint.setAntiAlias(true);// 打开抗锯齿
        //加载图片资源
        bulletBmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.bullet);
        bgBmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);
        explodeBmps=new Bitmap[]{
                BitmapFactory.decodeResource(this.getResources(), R.drawable.explode0),
                BitmapFactory.decodeResource(this.getResources(), R.drawable.explode1),
                BitmapFactory.decodeResource(this.getResources(), R.drawable.explode2),
                BitmapFactory.decodeResource(this.getResources(), R.drawable.explode3),
                BitmapFactory.decodeResource(this.getResources(), R.drawable.explode4),
                BitmapFactory.decodeResource(this.getResources(), R.drawable.explode5),
        };
        bullet = new Bullet(this, bulletBmp,explodeBmps,0,290,1.3f,-5.9f);//创建子弹对象
        drawThread = new DrawThread(this);//创建绘制线程
        drawThread.start();//启动绘制线程
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {//界面销毁时调用的方法
        drawThread.setFlag(false);//停止绘制线程
    }
}
