package org.me.myandroidstuff;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Graham on 15/12/2015.
 */
public class mySurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    private SurfaceHolder mySurface;

    myDrawThread drawingThread = null;


    public mySurfaceView(Context context)
    {
        super(context);
        mySurface = getHolder();
        mySurface.addCallback(this);
        drawingThread = new myDrawThread(getHolder(), this);
        setFocusable(true);

    }

    public myDrawThread getThread()
    {
        return drawingThread;
    }

    //@Override
    public void surfaceCreated(SurfaceHolder holder)
    {

        drawingThread.setRunning(true);
        drawingThread.start();
    }

    //@Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        drawingThread.setSurfaceSize(width,height);

    }

    //@Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
        drawingThread.setRunning(false);
        while(retry)
        {
            try {
                drawingThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
