package com.bydlocoders.eyeinthesky;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by kinzo on 16.06.2016.
 */
@SuppressWarnings("deprecation")
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    Camera myCam = null;
    //Button myButton;


    // public void SetDisplay(Button but){
    //     myButton = but;
    //  }


    public CameraView(Context context) {
        super(context);
        openCamera();
    }

    public CameraView(Context context, AttributeSet as) {
        super(context, as);
        openCamera();

    }

    static Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        final double MAX_DOWNSIZE = 1.5;

        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            double downsize = (double) size.width / w;
            if (downsize > MAX_DOWNSIZE) {
                //if the preview is a lot larger than our display surface ignore it
                //reason - on some phones there is not enough heap available to show the larger preview sizes
                continue;
            }
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        //keep the max_downsize requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                double downsize = (double) size.width / w;
                if (downsize > MAX_DOWNSIZE) {
                    continue;
                }
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        //everything else failed, just take the closest match
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        return optimalSize;
    }

    private void openCamera() {
        if (!isInEditMode()) {
            getHolder().addCallback(this);
            getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            if (myCam == null)
                myCam = Camera.open();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w,
                               int h) {
        Camera.Parameters parameters = myCam.getParameters();
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size optimalPreviewSize = getOptimalPreviewSize(supportedPreviewSizes, w, h);
        if (optimalPreviewSize != null) {
            parameters.setPreviewSize(optimalPreviewSize.width, optimalPreviewSize.height);
            myCam.setParameters(parameters);
            myCam.startPreview();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            myCam.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //   drawThread = new DrawThread(getHolder());
        //   drawThread.setRunning(true);
        //    drawThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        myCam.release();
        try {
            if (myCam != null) {
                myCam.stopPreview();
                myCam.release();
            }
        } catch (Exception e) {
        }

    }

}
