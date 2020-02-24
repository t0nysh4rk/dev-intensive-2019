package ru.skillbranch.devintensive.ui.custom


import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.Align
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.utils.Utils


class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) :
    AppCompatImageView(context, attrs, defStyleAttr) {


    private val DEFAULT_TEXT_SIZE = 90
    private val DEFAULT_BORDER_COLOR = Color.WHITE
    private val DEFAULT_AVATAR_COLOR = Color.parseColor("#FC4C4C")
    private val DEFAULT_AVATAR_TEXT_COLOR = Color.WHITE
    private val DEFAULT_BORDER_WIDTH = 2
    private val DEFAULT_TEXT = "WW"


    @Dimension private var  mBorderWidth: Float = dpToPx(DEFAULT_BORDER_WIDTH)
    private var mBorderColor: Int = Color.WHITE
    private var mTextMode = true
    private var mAvatarColor: Int = DEFAULT_AVATAR_COLOR
    private var mAvatarTextColor: Int = DEFAULT_AVATAR_TEXT_COLOR

    private lateinit var mBitmapShader: Shader
    private lateinit var mShaderMatrix: Matrix

    private lateinit var mBitmapDrawBounds: RectF
    private lateinit var mStrokeBounds: RectF

    private var mBitmap: Bitmap? = null

    private lateinit var mBitmapPaint: Paint
    private lateinit var mStrokePaint: Paint

    private lateinit var mTextPaint: Paint
    private lateinit var mTextBounds: Rect

    private lateinit var mBackgroundPaint: Paint
    private lateinit var mBackgroundBounds: RectF
    private var mInitials: String? = null

    private var mText: String? = DEFAULT_TEXT
    private var mTextSize = DEFAULT_TEXT_SIZE
    private var mInitialized = false


    init {



        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, 0, 0)
            mBorderWidth = a.getDimension(R.styleable.CircleImageView_cv_borderWidth, dpToPx(DEFAULT_BORDER_WIDTH))
            mBorderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            mTextMode = a.getBoolean(R.styleable.CircleImageView_AvatarTextModeEnabled, mTextMode)
            mAvatarColor = a.getColor(R.styleable.CircleImageView_avatarColor, DEFAULT_AVATAR_COLOR)
            mAvatarTextColor = a.getColor(R.styleable.CircleImageView_AvatarTextColor, DEFAULT_AVATAR_TEXT_COLOR)
            mTextSize = a.getInt(R.styleable.CircleImageView_AvatarTextSize, DEFAULT_TEXT_SIZE)
            mText = a.getString(R.styleable.CircleImageView_AvatarText)
            a.recycle()
        }



        mShaderMatrix = Matrix()
        mBitmapPaint = Paint(ANTI_ALIAS_FLAG)
        mStrokePaint = Paint(ANTI_ALIAS_FLAG)
        mStrokeBounds = RectF()
        mBitmapDrawBounds = RectF()
        mStrokePaint.color = mBorderColor
        mStrokePaint.style = Paint.Style.STROKE
        mStrokePaint.strokeWidth = mBorderWidth


        mInitialized = true
        setupBitmap()



        mTextPaint = Paint(ANTI_ALIAS_FLAG)
        mTextPaint.textAlign = Align.CENTER
        mTextPaint.color = mAvatarTextColor

        mTextPaint.textSize = mTextSize.toFloat()

        mTextBounds = Rect()

        if (mText.equals(DEFAULT_TEXT)||mText.isNullOrBlank()){
            disableTextMode()
        } else {
            updateTextBounds()
        }
        mBackgroundPaint = Paint(ANTI_ALIAS_FLAG)
        mBackgroundPaint.color = mAvatarColor
        mBackgroundPaint.style = Paint.Style.FILL

        mBackgroundBounds = RectF()

        updateCircleDrawBounds(mBackgroundBounds)

}
     @Dimension fun getBorderWidth():Int{
        return mBorderWidth.toInt()
    }

   fun setBorderWidth(@Dimension dp: Int){
      mBorderWidth = dpToPx(dp)
       invalidate()
    }
   fun getBorderColor():Int{
      return mBorderColor
    }
   fun setBorderColor(hex:String){
       mBorderColor = Color.parseColor(hex)
       invalidate()

   }
   fun setBorderColor(@ColorRes colorId: Int){
       mBorderColor = colorId
       invalidate()
   }

    fun dpToPx(dp: Int): Float {
        return (dp.toFloat() * Resources.getSystem().displayMetrics.density)
    }
    /**
     * Choose one type Int or Float, mark another one as null
     * @return the float value of converted type
     */


    fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }
    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        setupBitmap()
    }

    override fun setImageDrawable(@Nullable drawable: Drawable?) {
        super.setImageDrawable(drawable)
        setupBitmap()
    }

    override fun setImageBitmap(@Nullable bm: Bitmap?) {
        super.setImageBitmap(bm)
        setupBitmap()
    }


    override fun setImageURI(@Nullable uri: Uri?) {
        super.setImageURI(uri)
        setupBitmap()
    }



    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

     fun enableTextMode(){
        mTextMode = true
    }
    fun disableTextMode(){
        mTextMode = false
    }


    private fun updateCircleDrawBounds(bounds: RectF) {
        val contentWidth = width - paddingLeft - paddingRight.toFloat()
        val contentHeight = height - paddingTop - paddingBottom.toFloat()
        var left = paddingLeft.toFloat()
        var top = paddingTop.toFloat()

        if (contentWidth > contentHeight) {
            left += (contentWidth - contentHeight) / 2f
        } else {
            top += (contentHeight - contentWidth) / 2f
        }

        val diameter = contentWidth.coerceAtMost(contentHeight)
        bounds[left, top, left + diameter] = top + diameter
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val halfStrokeWidth = mStrokePaint.strokeWidth / 2f
        updateCircleDrawBounds(mBitmapDrawBounds)
        mStrokeBounds.set(mBitmapDrawBounds)
        mStrokeBounds.inset(halfStrokeWidth, halfStrokeWidth)

        updateBitmapSize()
        updateCircleDrawBounds(mBackgroundBounds)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            outlineProvider = CircleImageViewOutlineProvider(mStrokeBounds)
        }

    }

    fun setText(text: String?){
        mText = text
        invalidate()
    }

    private fun convertToInitials(avatarText: String?) : String? {
        if (avatarText.isNullOrBlank()){
            return ""
        }

        val splittedString = Utils.parseFullName(avatarText, "_")
        if (splittedString.first.isNullOrBlank()){
            return splittedString.second?.take(1)?.toUpperCase()
        } else if (splittedString.second.isNullOrBlank()){
            return splittedString.first!!.take(1).toUpperCase()
        }
        return Utils.toInitials(splittedString.first, splittedString.second)
    }

    private fun setupBitmap() {
        if (!mInitialized) {
            return
        }
        mBitmap = getBitmapFromDrawable(drawable)
        if (mBitmap == null) {
              enableTextMode()
              return
        }
        disableTextMode()
        mBitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mBitmapPaint.shader = mBitmapShader
        updateBitmapSize()
    }



    private fun updateBitmapSize() {
        if (mBitmap == null) return

        val x: Float
        val y: Float

        val scale: Float

        if (mBitmap!!.width < mBitmap!!.height) {
            scale = mBitmapDrawBounds.width() / mBitmap!!.width.toFloat()

            x = mBitmapDrawBounds.left

            y = mBitmapDrawBounds.top - mBitmap!!.height * scale / 2f + mBitmapDrawBounds.width() / 2f
        } else {
            scale = mBitmapDrawBounds.height() / mBitmap!!.height.toFloat()
            x = mBitmapDrawBounds.left - mBitmap!!.width * scale / 2f + mBitmapDrawBounds.width() / 2f
            y = mBitmapDrawBounds.top
        }

        mShaderMatrix.setScale(scale, scale)
        mShaderMatrix.postTranslate(x, y)
        mBitmapShader.setLocalMatrix(mShaderMatrix)
    }



    override fun onDraw(canvas: Canvas) {

          if(!mTextMode){
              drawBitmap(canvas)
              drawStroke(canvas)
        } else {

              val textBottom = mBackgroundBounds.centerY() - mTextBounds.exactCenterY()
              canvas.drawOval(mBackgroundBounds, mBackgroundPaint)
              Log.d("M_CircleImageView", "mInitials = $mInitials, Avatarcolor = $mAvatarTextColor, txtcolor = ${mTextPaint.color}")
              canvas.drawText(mInitials!!, mBackgroundBounds.centerX(), textBottom, mTextPaint)


        }

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (!mText.isNullOrBlank()) {
            Log.d("M_onLayout1", "mtext = $mText \n mInitials = $mInitials")
            mInitials = convertToInitials(mText)!!
            Log.d("M_onLayout2", "mtext = $mText \n mInitials = $mInitials")
            enableTextMode()
        } else {
            disableTextMode()
            mInitials = DEFAULT_TEXT


        }

        updateTextBounds()

        if (mBitmap == null){
            enableTextMode()
        }

    }

    private fun updateTextBounds() {
        mTextPaint.getTextBounds(mInitials, 0, mInitials!!.length, mTextBounds)
    }


    private fun drawStroke(canvas: Canvas) {
        if (mStrokePaint.strokeWidth > 0f) {
            canvas.drawOval(mStrokeBounds, mStrokePaint)
        }
    }

    private fun drawBitmap(canvas: Canvas) {
        canvas.drawOval(mBitmapDrawBounds, mBitmapPaint)
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
     class CircleImageViewOutlineProvider() : ViewOutlineProvider() {


        private var mRect: Rect? = null

         constructor(rect: RectF) : this() {
            mRect = Rect(
                rect.left.toInt(),
                rect.top.toInt(),
                rect.right.toInt(),
                rect.bottom.toInt()
            )
        }


        override fun getOutline(p0: View?, outline: Outline?) {
            if (mRect != null) {
                outline?.setOval(mRect!!)
            }
        }
    }




    }