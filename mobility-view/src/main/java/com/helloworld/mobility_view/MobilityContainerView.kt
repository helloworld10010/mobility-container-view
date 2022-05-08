package com.helloworld.mobility_view

import android.content.Context
import android.graphics.Rect
import android.os.SystemClock
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.widget.FrameLayout
import android.widget.Scroller
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow

class MobilityContainerView(context: Context, attributeSet: AttributeSet)
  : FrameLayout(context,attributeSet) {

  private val mVelocityTracker:VelocityTracker by lazy {
    VelocityTracker.obtain()
  }

  private var fling = true

  private val screenRect by lazy {
    val rect = Rect()
    getWindowVisibleDisplayFrame(rect)
    rect
  }

  private val screenWidth:Int by lazy {
    screenRect.width()
  }

  private val screenHeight:Int by lazy {
    screenRect.height()
  }


  /**
   * 多长时间停止，时间越长越’滑‘，单位ms
   */
  private var viscosity = 0
  private var accelerationX = 0f
  private var accelerationY = 0f

  private val mTouchSlop = 50f

  var lastX = 0f
  var lastY = 0f

  init {
    val typeArray = context.obtainStyledAttributes(attributeSet,R.styleable.MobilityContainerView)
    fling = typeArray.getBoolean(R.styleable.MobilityContainerView_fling,true)
    viscosity = typeArray.getInt(R.styleable.MobilityContainerView_viscosity,200)
    typeArray.recycle()
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    Timber.d("onTouchEvent:${event.action}")
    when (event.action) {
      MotionEvent.ACTION_DOWN -> {
        handler.removeCallbacksAndMessages(null)
        lastX = event.rawX
        lastY = event.rawY
      }
      MotionEvent.ACTION_MOVE -> {
        mVelocityTracker.addMovement(event)
        val moveX = event.rawX - lastX
        val moveY = event.rawY - lastY
        this.x += moveX
        this.y += moveY
        lastX = event.rawX
        lastY = event.rawY
      }
      MotionEvent.ACTION_UP -> {
        if(fling){
          mVelocityTracker.computeCurrentVelocity(1000)
          val xv = mVelocityTracker.xVelocity
          val yv = mVelocityTracker.yVelocity
          fling(xv,yv)
        }
        performClick()
      }
    }
    return true
  }

  private var accMoveX = 0f
  private var accMoveY = 0f

  override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    Timber.d("onInterceptTouchEvent:${ev.action}")
    when(ev.action){
      MotionEvent.ACTION_DOWN -> {
        lastX = ev.rawX
        lastY = ev.rawY
        accMoveX = 0f
        accMoveY = 0f
      }

      MotionEvent.ACTION_MOVE -> {
        accMoveX += abs(ev.rawX - lastX)
        accMoveY += abs(ev.rawY - lastY)
        lastX = ev.rawX
        lastY = ev.rawY
        val max = max(accMoveX, accMoveY)
        return max >= mTouchSlop
      }
      MotionEvent.ACTION_UP -> {
      }
    }
    return false
  }

  /**
   * s=v0t+(at^2)/2
   */
  private fun fling(xv: Float, yv: Float) {
    Timber.d("v0x:$xv,v0y:$yv")

    accelerationX = (0-xv) / (viscosity/1000f)
    accelerationY = (0-yv) / (viscosity/1000f)
    Timber.d("acceleration:${accelerationX}:$accelerationY")
    var t = viscosity / 1000.0
    val runnable = object:Runnable{
      override fun run() {
        if(t > 0){
          t -= 0.01
          Timber.d("spend:${viscosity-t}")
          Timber.d("x:$x")
          val v0x = (xv+accelerationX*(viscosity/1000.0-t))
          val v0y = (yv+accelerationY*(viscosity/1000.0-t))
          Timber.d("v0x:$v0x-v0y:$v0y")
          val willMoveX = ((v0x*0.01)+(accelerationX*0.01.pow(2))/2).toFloat()
          val willMoveY = ((v0y*0.01)+(accelerationY*0.01.pow(2))/2).toFloat()
          if(checkXInBounds(willMoveX)){
            this@MobilityContainerView.x +=willMoveX
          }
          if(checkYInBounds(willMoveY)){
            this@MobilityContainerView.y += willMoveY
          }
          postDelayed(this,10)
        }

      }
    }
    postDelayed(runnable,10)
  }

  private fun checkXInBounds(willMoveX:Float):Boolean {
    return (this@MobilityContainerView.x+willMoveX) in (0-width/2f)..(screenWidth-width/2f)
  }
  private fun checkYInBounds(willMoveY:Float):Boolean {
    return (this@MobilityContainerView.y+willMoveY) in (0-height/2f)..(screenHeight-height/2f)
  }

  override fun performClick(): Boolean {
    return super.performClick()
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    mVelocityTracker.recycle()
  }
}