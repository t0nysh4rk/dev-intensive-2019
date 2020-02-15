package ru.skillbranch.devintensive.ui.custom


import android.annotation.TargetApi
import android.content.Context
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
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView
import kotlinx.android.synthetic.main.activity_profile.view.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.ui.profile.ProfileActivity
import ru.skillbranch.devintensive.utils.Utils


class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) :
    AppCompatImageView(context, attrs, defStyleAttr) {

    companion object {
        const val DEFAULT_BORDER_WIDTH = 2.0f
        const val DEFAULT_TEXT_MODE_VALUE = false



    }

    private var cv_borderWidth = DEFAULT_BORDER_WIDTH
    private var cv_borderColor: Int = Color.parseColor("#ECECEC")
    private var mTextMode = DEFAULT_TEXT_MODE_VALUE
    private var mAvatarColor: Int = Color.parseColor("#FC4C4C")
    private var mAvatarTextColor: Int = Color.parseColor("#ECECEC")

    private lateinit var mBitmapShader: Shader
    private lateinit var mShaderMatrix: Matrix

    private lateinit var mBitmapDrawBounds: RectF
    private lateinit var mStrokeBounds: RectF

    private lateinit var mBitmap: Bitmap

    private lateinit var mBitmapPaint: Paint
    private lateinit var mStrokePaint: Paint

    private lateinit var mTextPaint: Paint
    private lateinit var mTextBounds: Rect

    private lateinit var mBackgroundPaint: Paint
    private lateinit var mBackgroundBounds: RectF


    private var mText: String? = null

    private var mInitialized = false


    init {
         var textSize = 90
         val defBorderColor = Color.parseColor("#ECECEC")
         val defAvatarColor = Color.parseColor("#FC4C4C")
         val defAvatarTextColor = Color.parseColor("#ECECEC")

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, 0, 0)
            cv_borderWidth = a.getDimension(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH)
            cv_borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, defBorderColor)
            mTextMode = a.getBoolean(R.styleable.CircleImageView_AvatarTextModeEnabled, DEFAULT_TEXT_MODE_VALUE)
            mAvatarColor = a.getColor(R.styleable.CircleImageView_avatarColor, defAvatarColor)
            mAvatarTextColor = a.getColor(R.styleable.CircleImageView_avatarColor, defAvatarTextColor)
            textSize =a.getInt(R.styleable.CircleImageView_AvatarTextSize, textSize)
            mText = a.getString(R.styleable.CircleImageView_AvatarText)
            a.recycle()
        }



        mShaderMatrix = Matrix()
        mBitmapPaint = Paint(ANTI_ALIAS_FLAG)
        mStrokePaint = Paint(ANTI_ALIAS_FLAG)
        mStrokeBounds = RectF()
        mBitmapDrawBounds = RectF()
        //mStrokePaint.color = strokeColor
        mStrokePaint.color = cv_borderColor
        mStrokePaint.style = Paint.Style.STROKE
        mStrokePaint.strokeWidth = cv_borderWidth


            mInitialized = true
            setupBitmap()



        mTextPaint = Paint(ANTI_ALIAS_FLAG)
        mTextPaint.textAlign = Align.CENTER
     //   mTextPaint.color = textColor
        mTextPaint.color = mAvatarTextColor

        mTextPaint.textSize = textSize.toFloat()

        mTextBounds = Rect()


        updateTextBounds()
        mBackgroundPaint = Paint(ANTI_ALIAS_FLAG)
        mBackgroundPaint.color = mAvatarColor
        mBackgroundPaint.style = Paint.Style.FILL

        mBackgroundBounds = RectF()

        updateCircleDrawBounds(mBackgroundBounds)

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
    }

    private fun convertToInitials(avatarText: String?) : String? {
        if (avatarText.isNullOrBlank()){
            return null
        }

        val splittedString = Utils.parseFullName(avatarText, "_")
        return Utils.toInitials(splittedString.first, splittedString.second)
    }

    private fun setupBitmap() {
        if (!mInitialized) {
            return
        }
        mBitmap = getBitmapFromDrawable(drawable)!!
        if (mBitmap == null) {
            return
        }

        mBitmapShader = BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mBitmapPaint.shader = mBitmapShader
        updateBitmapSize()
    }


    private fun updateBitmapSize() {
        if (mBitmap == null) return

        val x: Float
        val y: Float

        val scale: Float

        if (mBitmap.width < mBitmap.height) {
            scale = mBitmapDrawBounds.width() / mBitmap.width.toFloat()

            x = mBitmapDrawBounds.left

            y = mBitmapDrawBounds.top - mBitmap.height * scale / 2f + mBitmapDrawBounds.width() / 2f
        } else {
            scale = mBitmapDrawBounds.height() / mBitmap.height.toFloat()
            x = mBitmapDrawBounds.left - mBitmap.width * scale / 2f + mBitmapDrawBounds.width() / 2f
            y = mBitmapDrawBounds.top
        }

        mShaderMatrix.setScale(scale, scale)
        mShaderMatrix.postTranslate(x, y)
        mBitmapShader.setLocalMatrix(mShaderMatrix)
    }

    override fun onDraw(canvas: Canvas) {
        val initials = convertToInitials(mText)
        if(!mTextMode || initials.isNullOrBlank()){
            drawBitmap(canvas)
            drawStroke(canvas)
        } else {
            updateTextBounds()
            val textBottom = mBackgroundBounds.centerY() - mTextBounds.exactCenterY()
            canvas.drawOval(mBackgroundBounds, mBackgroundPaint)
            Log.d("M_Civ_OnDraw", "mText = $mText")
            canvas.drawText(initials, mBackgroundBounds.centerX(), textBottom, mTextPaint)


        }

    }


    private fun updateTextBounds() {
        mTextPaint.getTextBounds(mText, 0, mText!!.length, mTextBounds)
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