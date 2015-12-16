package org.me.myandroidstuff;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

/**
 * Created by Graham on 15/12/2015.
 */
public class myDrawThread extends Thread
{
    private int canvasWidth;
    private int canvasHeight;
    private float xPos = 0.0f;
    private float yPos = 0.0f;
    private int i;

    private float HalfAppletHeight;
    private float HalfAppletWidth;

    private boolean first = true;
    private boolean run = false;

    private SurfaceHolder mySurfaceHolder;
    private Paint paint;
    private mySurfaceView my_SurfaceView;

    public myDrawThread(SurfaceHolder surfaceHolder, mySurfaceView my_SurfaceView) {
        this.mySurfaceHolder = surfaceHolder;
        this.my_SurfaceView = my_SurfaceView;
        paint = new Paint();
    }

    public void doStart() {
        synchronized (mySurfaceHolder) {
            first = false;
        }
    }

    public void run() {
        while (run) {
            Canvas c = null;
            try {
                c = mySurfaceHolder.lockCanvas(null);
                synchronized (mySurfaceHolder) {
                    svDraw(c);
                }
            } finally {
                if (c != null) {
                    mySurfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    public void setRunning(boolean b) {
        run = b;
    }
    public void setSurfaceSize(int width, int height) {
        synchronized (mySurfaceHolder) {
            canvasWidth = width;
            canvasHeight = height;
            HalfAppletHeight = canvasHeight / 2;
            HalfAppletWidth  = canvasWidth / 32;
            doStart();
        }
    }


    private void svDraw(Canvas canvas) {
        if(run) {
            canvas.save();
            canvas.restore();
            canvas.drawColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            drawAxes(canvas);
            paint.setColor(Color.RED);
            drawWave(canvas, 23);
            paint.setColor(Color.GREEN);
            drawWave(canvas, 28);
            paint.setColor(Color.BLUE);
            drawWave(canvas, 33);
        }
    }

    public void drawWave(Canvas theCanvas, int period)
    {
        float xPosOld = 0.0f;
        float yPosOld = 0.0f;
        int dStart = -15;
        int sDate = 0;
        int tDate = 0;

        sDate = 1 + dStart;

        for (i=0;i<=30;i++)
        {
            xPos = i * HalfAppletWidth;

            tDate = sDate + i;
            yPos = (float)(-100.0f * (Math.sin((tDate%period)*2*Math.PI/period)));

            if ( i == 0)
                paint.setStyle(Paint.Style.FILL);
            else
                theCanvas.drawLine(xPosOld, (yPosOld + HalfAppletHeight), xPos, (yPos + HalfAppletHeight), paint);
            xPosOld = xPos;
            yPosOld = yPos;
        }
    }

    public void drawAxes(Canvas theCanvas)
    {
        paint.setColor(Color.BLACK);
        theCanvas.drawLine(0,HalfAppletHeight,30*HalfAppletWidth,HalfAppletHeight, paint); // Horizontal X Axes
        theCanvas.drawLine(15* HalfAppletWidth,0,15* HalfAppletWidth,canvasHeight, paint); // Vertical Y Axes
    }


}
