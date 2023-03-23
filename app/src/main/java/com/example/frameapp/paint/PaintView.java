package com.example.frameapp.paint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.frameapp.EditProjectScreen;

import java.util.ArrayList;

public class PaintView extends View implements UndoCommand {

    boolean canvasIsCreated = false;
    public static MotionEvent motionEvent;
    public static ArrayList<ToolInterface> mUndoStack = new ArrayList<ToolInterface>();
    public static ArrayList<ToolInterface> mOldActionStack = new ArrayList<ToolInterface>();
    public static Canvas mCanvas = null;

    public static ToolInterface mCurrentPainter = null;

    private Bitmap mBitmap = null;

    private Bitmap mOrgBitMap = null;

    private int mBitmapWidth = 0;

    private int mBitmapHeight = 0;

    private int mBackGroundColor = PaintConstants.DEFAULT.BACKGROUND_COLOR;

    private Paint mBitmapPaint = null;

    private paintPadUndoStack UndoStack = null;

    public static int mPenColor = PaintConstants.DEFAULT.PEN_COLOR;

    private static int mPenSize = PaintConstants.PEN_SIZE.SIZE_1;

    private static int mEraserSize = PaintConstants.ERASER_SIZE.SIZE_1;

    static int mPaintType = PaintConstants.PEN_TYPE.PLAIN_PEN;

    public static PaintViewCallBack mCallBack = null;

    private static int mCurrentShapeType = 0;

    private static ShapesInterface mCurrentShape = null;

    private static Paint.Style mStyle = Paint.Style.STROKE;

    public static boolean isTouchUp = false;

    private int mStackedSize = 50;
    ImageView imageView;
    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaintView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mCanvas = new Canvas();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        UndoStack = new paintPadUndoStack(this, mStackedSize);

        mPaintType = PaintConstants.PEN_TYPE.PLAIN_PEN;
        mCurrentShapeType = PaintConstants.SHAP.CURV;
        creatNewPen();
    }

    public Bitmap save() {
        return mBitmap;
    }

    public  void setCallBack(PaintViewCallBack callBack) {
        mCallBack = callBack;
    }

    @Override
    public  boolean onTouchEvent(MotionEvent event) {
        motionEvent = event;
//        StoryMakingFrg.Companion.getGetDrawPathID_IF().getDrawPath(motionEvent);

        float x = event.getX();
        float y = event.getY();

        Log.d("FloatXY",""+x+"    "+y);
        isTouchUp = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCanvas.setBitmap(mBitmap);
                creatNewPen();
                mCurrentPainter.touchDown(x, y);
                Log.d("touchDown",""+x+"   "+y);
                UndoStack.clearRedo();
                Log.d("undoSize",""+mUndoStack.size());
                EditProjectScreen.Companion.getGetUndoRedoSizeIF().getUndoRedoSize(mUndoStack.size(),mOldActionStack.size());
                mCallBack.onTouchDown();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentPainter.touchMove(x, y);
                if (mPaintType == PaintConstants.PEN_TYPE.ERASER) {
                    mCurrentPainter.draw(mCanvas);
                }

                Log.d("touchMove",""+x+"   "+y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (mCurrentPainter.hasDraw()) {
                    UndoStack.push(mCurrentPainter);
                    if (mCallBack != null) {

                        mCallBack.onHasDraw();
                    }
                }
                mCurrentPainter.touchUp(x, y);
                Log.d("touchUp",""+x+"   "+y);


                mCurrentPainter.draw(mCanvas);
                invalidate();
                isTouchUp = true;
                break;
        }
        return true;
    }

    final float[] getPointerCoords(ImageView view, MotionEvent e){
        final int index = e.getActionIndex();
        final float[] coords = new float[] { e.getX(index), e.getY(index) };
        Matrix matrix = new Matrix();
        view.getImageMatrix().invert(matrix);
        matrix.postTranslate(view.getScrollX(), view.getScrollY());
        matrix.mapPoints(coords);
        return coords;
    }

    private static void setShape() {
        if (mCurrentPainter instanceof Shapable) {
            switch (mCurrentShapeType) {
                case PaintConstants.SHAP.CURV:
                    mCurrentShape = new Curv((Shapable) mCurrentPainter);
                    break;
                default:
                    break;
            }
            ((Shapable) mCurrentPainter).setShap(mCurrentShape);
        }
    }

    @Override
    public void onDraw(Canvas cv) {
        cv.drawColor(mBackGroundColor);

        cv.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        Log.d("BITMAP",""+mBitmap+"  "+mBitmapPaint);

        if (!isTouchUp) {
            if (mPaintType != PaintConstants.PEN_TYPE.ERASER) {
                mCurrentPainter.draw(cv);
            }
        }

        Log.d("CV",""+cv);
    }

    public static void creatNewPen() {
        ToolInterface tool = null;
        switch (mPaintType) {
            case PaintConstants.PEN_TYPE.PLAIN_PEN:
                tool = new PlainPen(mPenSize, mPenColor, mStyle);
                break;
            case PaintConstants.PEN_TYPE.ERASER:
                tool = new Eraser(mEraserSize);
                break;
            default:
                break;
        }
        mCurrentPainter = tool;
        setShape();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!canvasIsCreated) {
            mBitmapWidth = w;
            mBitmapHeight = h;
            creatCanvasBitmap(w, h);
            canvasIsCreated = true;
        }
    }


    public void setForeBitMap(Bitmap bitmap) {
        if (bitmap != null) {
            recycleMBitmap();
            recycleOrgBitmap();
        }
        mBitmap = BitMapUtils.duplicateBitmap(bitmap, getWidth(), getHeight());
        mOrgBitMap = BitMapUtils.duplicateBitmap(mBitmap);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        invalidate();
    }


    private void recycleOrgBitmap() {
        if (mOrgBitMap != null && !mOrgBitMap.isRecycled()) {
            mOrgBitMap.recycle();
            mOrgBitMap = null;
        }
    }

    private void recycleMBitmap() {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }


    public Bitmap getSnapShoot() {

        setDrawingCacheEnabled(true);
        buildDrawingCache(true);
        Bitmap bitmap = getDrawingCache(true);
        Bitmap bmp = BitMapUtils.duplicateBitmap(bitmap);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }

        setDrawingCacheEnabled(false);
        return bmp;
    }

    public void clearAll(boolean clearBackground) {
        if (clearBackground) {
            recycleMBitmap();
            recycleOrgBitmap();
            creatCanvasBitmap(mBitmapWidth, mBitmapHeight);
        } else {
            if (mOrgBitMap != null) {
                mBitmap = BitMapUtils.duplicateBitmap(mOrgBitMap);
                mCanvas.setBitmap(mBitmap);
            } else {
                creatCanvasBitmap(mBitmapWidth, mBitmapHeight);
            }
        }
        UndoStack.clearAll();
        invalidate();
    }

    private void creatCanvasBitmap(int w, int h) {
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(mBitmap);
    }


    public void setCurrentPainterType(int type) {
        switch (type) {
            case PaintConstants.PEN_TYPE.BLUR:
            case PaintConstants.PEN_TYPE.PLAIN_PEN:
            case PaintConstants.PEN_TYPE.EMBOSS:
            case PaintConstants.PEN_TYPE.ERASER:
                mPaintType = type;
                break;
            default:
                mPaintType = PaintConstants.PEN_TYPE.PLAIN_PEN;
                break;
        }
    }

    public void setCurrentShapType(int type) {
        switch (type) {
            case PaintConstants.SHAP.CURV:
            case PaintConstants.SHAP.LINE:
            case PaintConstants.SHAP.RECT:
            case PaintConstants.SHAP.CIRCLE:
            case PaintConstants.SHAP.OVAL:
            case PaintConstants.SHAP.SQUARE:
            case PaintConstants.SHAP.STAR:
                mCurrentShapeType = type;
                break;
            default:
                mCurrentShapeType = PaintConstants.SHAP.CURV;
                break;
        }
    }


    public int getCurrentPainter() {
        return mPaintType;
    }


    public void setPenSize(int size) {
        mPenSize = size;
    }


    public void setEraserSize(int size) {
        mEraserSize = size;
    }


    public int getPenSize() {
        return mPenSize;
    }

    public void resetState() {
        setCurrentPainterType(PaintConstants.PEN_TYPE.PLAIN_PEN);
        setPenColor(PaintConstants.DEFAULT.PEN_COLOR);
        setBackGroundColor(PaintConstants.DEFAULT.BACKGROUND_COLOR);
        UndoStack.clearAll();
    }

    public void setBackGroundColor(int color) {
        mBackGroundColor = color;
        invalidate();
    }

    public int getBackGroundColor() {
        return mBackGroundColor;
    }

    public void setPenColor(int color) {
        mPenColor = color;
    }

    public int getPenColor() {
        return mPenColor;
    }


    protected void setTempForeBitmap(Bitmap tempForeBitmap) {
        if (null != tempForeBitmap) {
            recycleMBitmap();
            mBitmap = BitMapUtils.duplicateBitmap(tempForeBitmap);
            if (null != mBitmap && null != mCanvas) {
                mCanvas.setBitmap(mBitmap);
                invalidate();
            }
        }
    }

    public void setPenStyle(Paint.Style style) {
        mStyle = style;
    }

    public byte[] getBitmapArry() {
        return BitMapUtils.bitampToByteArray(mBitmap);
    }

    @Override
    public void undo() {
        if (null != UndoStack) {
            UndoStack.undo();
        }
    }

    @Override
    public void redo() {
        if (null != mUndoStack) {
            UndoStack.redo();
        }
    }

    @Override
    public boolean canUndo() {
        return UndoStack.canUndo();
    }

    @Override
    public boolean canRedo() {
        return UndoStack.canRedo();
    }

    @Override
    public String toString() {
        return "mPaint" + mCurrentPainter + mUndoStack;
    }


    public class paintPadUndoStack {
        private int m_stackSize = 0;

        public PaintView mPaintView = null;

        public ArrayList<ToolInterface> mRedoStack = new ArrayList<ToolInterface>();

        public paintPadUndoStack(PaintView paintView, int stackSize) {
            mPaintView = paintView;
            m_stackSize = stackSize;
        }


        public void push(ToolInterface penTool) {
            if (null != penTool) {

                if (mUndoStack.size() == m_stackSize && m_stackSize > 0) {
                    ToolInterface removedTool = mUndoStack.get(0);
                    mOldActionStack.add(removedTool);

                    mUndoStack.remove(0);
                    Log.d("mOldActionStack",""+mOldActionStack);
                }

                mUndoStack.add(penTool);
            }
        }

        public void clearAll() {
            mRedoStack.clear();
            mUndoStack.clear();
            mOldActionStack.clear();
        }

        public void undo() {
            if (canUndo() && null != mPaintView) {
                ToolInterface removedTool = mUndoStack.get(mUndoStack.size() - 1);
                mRedoStack.add(removedTool);
                mUndoStack.remove(mUndoStack.size() - 1);

                if (null != mOrgBitMap) {

                    mPaintView.setTempForeBitmap(mPaintView.mOrgBitMap);
                } else {
                    mPaintView.creatCanvasBitmap(mPaintView.mBitmapWidth, mPaintView.mBitmapHeight);
                }

                Canvas canvas = mPaintView.mCanvas;
                Log.d("canvas",""+canvas);

                // First draw the removed tools from undo stack.
                for (ToolInterface paintTool : mOldActionStack) {
                    paintTool.draw(canvas);
                }


               // StoryMakingFrg.Companion.getGetDrawPathID_IF().getDrawPath(mRedoStack);

                for (ToolInterface paintTool : mUndoStack) {
                    paintTool.draw(canvas);
                }

                mPaintView.invalidate();
            }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public void redo() {
            if (canRedo() && null != mPaintView) {
                ToolInterface removedTool = mRedoStack.get(mRedoStack.size() - 1);
                mUndoStack.add(removedTool);
                mRedoStack.remove(mRedoStack.size() - 1);

                if (null != mOrgBitMap) {
                    mPaintView.setTempForeBitmap(mPaintView.mOrgBitMap);
                } else {
                    mPaintView.creatCanvasBitmap(mPaintView.mBitmapWidth, mPaintView.mBitmapHeight);
                    Log.d("PAINTVIEW",""+mPaintView.mBitmapWidth+"  "+mPaintView.mBitmapHeight);
                }

                Canvas canvas = mPaintView.mCanvas;
                Log.d("canvas",""+canvas);
                for (ToolInterface sketchPadTool : mOldActionStack) {
                    sketchPadTool.draw(canvas);
                }

               // StoryMakingFrg.Companion.getGetDrawPathID_IF().getDrawPath(mUndoStack);
                for (ToolInterface sketchPadTool : mUndoStack) {
                    sketchPadTool.draw(canvas);
                }

                mPaintView.invalidate();
            }
        }

        public boolean canUndo() {
            return (mUndoStack.size() > 0);
        }

        public boolean canRedo() {
            return (mRedoStack.size() > 0);
        }

        public void clearRedo() {
            mRedoStack.clear();
        }

        @Override
        public String toString() {
            Log.d("canUndo",""+canUndo());
            return "canUndo" + canUndo();
        }
    }
}