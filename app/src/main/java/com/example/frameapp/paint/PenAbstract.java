package com.example.frameapp.paint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public abstract class PenAbstract implements ToolInterface, Shapable {

	private static final float TOUCH_TOLERANCE = 4.0f;
	private float mCurrentX = 0.0f;
	private float mCurrentY = 0.0f;
	private Path mPath = null;
	protected Paint mPenPaint = null;
	private boolean mHasDraw = false;
	protected FirstCurrentPosition mFirstCurrentPosition = null;
	protected ShapesInterface currentShape = null;
	protected int penSize;
	protected Paint.Style style;

	public PenAbstract() {
		this(0, 0);
	}

	protected PenAbstract(int penSize, int penColor) {
		this(penSize, penColor, Paint.Style.STROKE);
	}

	protected PenAbstract(int penSize, int penColor, Paint.Style style) {
		super();
		initPaint(penSize, penColor, style);
		mFirstCurrentPosition = new FirstCurrentPosition();
		currentShape = new Curv(this);
		mPath = new Path();
	}


	public void setPenSize(int width) {
		mPenPaint.setStrokeWidth(width);
	}

	public void setPenColor(int color) {
		mPenPaint.setColor(color);
	}

	private void saveDownPoint(float x, float y) {
		mFirstCurrentPosition.firstX = x;
		mFirstCurrentPosition.firstY = y;
	}

	@Override
	public void draw(Canvas canvas) {
		if (canvas != null) {
			mFirstCurrentPosition.currentX = mCurrentX;
			mFirstCurrentPosition.currentY = mCurrentY;
			currentShape.draw(canvas, mPenPaint);
		}
	}

	@Override
	public void touchDown(float x, float y) {
		saveDownPoint(x, y);
		mPath.reset();
		mPath.moveTo(x, y);
		savePoint(x, y);
	}

	@Override
	public void touchMove(float x, float y) {
		if (isMoved(x, y)) {
			drawBeziercurve(x, y);
			savePoint(x, y);

			mHasDraw = true;
		}
	}

	@Override
	public void touchUp(float x, float y) {
		mPath.lineTo(x, y);
	}

	@Override
	public boolean hasDraw() {
		return mHasDraw;
	}


	private void savePoint(float x, float y) {
		mCurrentX = x;
		mCurrentY = y;
	}

	private boolean isMoved(float x, float y) {
		float dx = Math.abs(x - mCurrentX);
		float dy = Math.abs(y - mCurrentY);
		boolean isMoved = dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE;
		return isMoved;
	}

	private void drawBeziercurve(float x, float y) {
		mPath.quadTo(mCurrentX, mCurrentY, (x + mCurrentX) / 2,
				(y + mCurrentY) / 2);
	}

	@Override
	public Path getPath() {
		return mPath;
	}

	@Override
	public FirstCurrentPosition getFirstLastPoint() {
		return mFirstCurrentPosition;
	}

	@Override
	public void setShap(ShapesInterface shape) {
		currentShape = shape;
	}

	protected void initPaint(int penSize, int penColor, Paint.Style style) {
		mPenPaint = new Paint();
		mPenPaint.setStrokeWidth(penSize);
		mPenPaint.setColor(penColor);
		this.penSize = penSize;
		this.style = style;
		mPenPaint.setDither(true);
		mPenPaint.setAntiAlias(true);
		mPenPaint.setStyle(style);
		mPenPaint.setStrokeJoin(Paint.Join.ROUND);
		mPenPaint.setStrokeCap(Paint.Cap.ROUND);
	}
}