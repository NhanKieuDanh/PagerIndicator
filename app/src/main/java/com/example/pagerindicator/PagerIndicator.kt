package com.example.pagerindicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager

class PagerIndicator : View, PagerIndicatorListener {

    ///region CONSTRUCTORS

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    ///endregion

    ///region OVERRIDDEN FUNCTIONS

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // allocations per draw cycle.
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        val centerY = (contentHeight / 2).toFloat()
        val startX =
            (contentWidth.toFloat() - mNumberOfPage.toFloat() * mCircleRadius * 2f - mMarginItem * (mNumberOfPage - 1)) / 2

        for (i in 0 until mNumberOfPage) {
            val centerX = startX + mCircleRadius * (2 * i + 1) + mMarginItem * i
            drawCircle(canvas, centerX, centerY, i)
        }
    }

    override fun setViewPager(viewPager: ViewPager) {
        this.mViewPager = viewPager
        this.mViewPager?.addOnPageChangeListener(this)
        try {
            this.mNumberOfPage = viewPager.adapter!!.count
            this.mCurrentPage = viewPager.currentItem
            invalidate()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun setViewPager(viewPager: ViewPager, initPosition: Int) {
        this.mViewPager = viewPager
        this.mViewPager?.addOnPageChangeListener(this)
        try {
            this.mNumberOfPage = viewPager.adapter!!.count
            this.mViewPager?.setCurrentItem(initPosition, true)
            this.mCurrentPage = initPosition
            invalidate()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun setCurrentItem(position: Int) {
        this.mViewPager?.let {
            setCurrentPage(position)
            it.setCurrentItem(position, true)
        }
    }

    override fun notifyDataSetChange() {
        this.mViewPager?.let { it.adapter?.notifyDataSetChanged() }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        mListener?.onPageScrolled(position, positionOffset, positionOffsetPixels)
    }

    override fun onPageSelected(position: Int) {
        setCurrentPage(position)
        mListener?.onPageSelected(position)
    }

    override fun onPageScrollStateChanged(state: Int) {
        mListener?.onPageScrollStateChanged(state)
    }

    ///endregion

    ///region VARIABLES

    private var mNumberOfPage = 0
    private var mCurrentPage = 0

    private var mSelectedColor = Color.RED
    private var mBorderColor = Color.BLACK
    private var mNormalColor = Color.GREEN

    private var mBorderWidth = 4f
    private var mCircleRadius = 20f
    private var mMarginItem = 10f

    private var mPaint: Paint? = null

    private var mViewPager: ViewPager? = null
    private var mListener: ViewPager.OnPageChangeListener? = null

    ///endregion

    ///region PRIVATE FUNCTIONS

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(attrs, R.styleable.PagerIndicator, defStyle, 0)

        mNumberOfPage = a.getInt(R.styleable.PagerIndicator_number_of_page, 0)
        mCurrentPage = a.getInt(R.styleable.PagerIndicator_current_page, 0)

        mSelectedColor = a.getColor(R.styleable.PagerIndicator_selected_color, mSelectedColor)
        mBorderColor = a.getColor(R.styleable.PagerIndicator_border_color, mBorderColor)
        mNormalColor = a.getColor(R.styleable.PagerIndicator_normal_color, mNormalColor)

        mBorderWidth = a.getDimension(R.styleable.PagerIndicator_border_width, mBorderWidth)
        mCircleRadius = a.getDimension(R.styleable.PagerIndicator_circle_radius, mCircleRadius)
        mMarginItem = a.getDimension(R.styleable.PagerIndicator_margin_item, mMarginItem)

        a.recycle()

        // Set up a default Paint object
        mPaint = Paint()
        mPaint!!.flags = Paint.ANTI_ALIAS_FLAG
    }

    private fun drawCircle(canvas: Canvas, cx: Float, cy: Float, position: Int) {
        //        mPaint.setColor(mBorderColor);
        //        mPaint.setStrokeWidth(mBorderWidth);
        //        mPaint.setStyle(Paint.Style.STROKE);
        //        canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
        mPaint!!.color = if (position == mCurrentPage) mSelectedColor else mNormalColor
        mPaint!!.style = Paint.Style.FILL_AND_STROKE
        canvas.drawCircle(
            cx, cy,
            if (position == mCurrentPage) mCircleRadius + mBorderWidth else mCircleRadius - mBorderWidth, mPaint!!
        )
    }

    ///endregion

    ///region PUBLIC FUNCTIONS

    fun getNumberOfPage(): Int {
        return mNumberOfPage
    }

    fun setNumberOfPage(numberOfPage: Int) {
        this.mNumberOfPage = numberOfPage
        invalidate()
    }

    fun getCurrentPage(): Int {
        return mCurrentPage
    }

    fun setCurrentPage(currentPage: Int) {
        this.mCurrentPage = currentPage
        invalidate()
    }

    fun getSelectedColor(): Int {
        return mSelectedColor
    }

    fun setSelectedColor(selectedColor: Int) {
        this.mSelectedColor = selectedColor
        invalidate()
    }

    fun getBorderColor(): Int {
        return mBorderColor
    }

    fun setBorderColor(borderColor: Int) {
        this.mBorderColor = borderColor
        invalidate()
    }

    fun getNormalColor(): Int {
        return mNormalColor
    }

    fun setNormalColor(normalColor: Int) {
        this.mNormalColor = normalColor
        invalidate()
    }

    fun getBorderWidth(): Float {
        return mBorderWidth
    }

    fun setBorderWidth(borderWidth: Float) {
        this.mBorderWidth = borderWidth
        invalidate()
    }

    fun getCircleRadius(): Float {
        return mCircleRadius
    }

    fun setCircleRadius(circleRadius: Float) {
        this.mCircleRadius = circleRadius
        invalidate()
    }

    fun setListener(mListener: ViewPager.OnPageChangeListener) {
        this.mListener = mListener
    }

    ///endregion

}

interface PagerIndicatorListener : ViewPager.OnPageChangeListener {

    fun setViewPager(viewPager: ViewPager)

    fun setViewPager(viewPager: ViewPager, initPosition: Int)

    fun setCurrentItem(position: Int)

    fun notifyDataSetChange()

}