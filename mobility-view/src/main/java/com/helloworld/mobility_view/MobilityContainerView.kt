package com.helloworld.mobility_view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.max

class MobilityContainerView(context: Context, attributeSet: AttributeSet) : FrameLayout(context,attributeSet) {

  private val mTouchSlop = 50f
  var lastX = 0f
  var lastY = 0f
  override fun onTouchEvent(event: MotionEvent): Boolean {
    Timber.d("onTouchEvent:${event.action}")
    when (event.action) {
      MotionEvent.ACTION_DOWN -> {
        lastX = event.rawX
        lastY = event.rawY
      }
      MotionEvent.ACTION_MOVE -> {
        val moveX = event.rawX - lastX
        val moveY = event.rawY - lastY
        this.x += moveX
        this.y += moveY
        lastX = event.rawX
        lastY = event.rawY
      }
      MotionEvent.ACTION_UP -> {
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

  override fun performClick(): Boolean {
    return super.performClick()
  }
}