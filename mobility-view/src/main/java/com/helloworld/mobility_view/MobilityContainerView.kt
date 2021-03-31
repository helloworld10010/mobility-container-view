package com.helloworld.mobility_view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

class MobilityContainerView(context: Context, attributeSet: AttributeSet) : FrameLayout(context,attributeSet) {

  var downX = 0f
  var downY = 0f
  var lastX = 0f
  var lastY = 0f
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.action) {
      MotionEvent.ACTION_DOWN -> {

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

  var interceptDownX = 0f
  var interceptDownY = 0f
  var accMoveX = 0f
  var accMoveY = 0f

  override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    when(ev.action){
      MotionEvent.ACTION_DOWN -> {
        accMoveX = 0f
        accMoveY = 0f
      }

      MotionEvent.ACTION_MOVE -> {

        accMoveX += ev.rawX
        accMoveY += ev.rawY
        val max = Math.max(accMoveX, accMoveY)
        return max >= 7500f
      }
      MotionEvent.ACTION_UP -> {
      }
    }
    return false
  }

  override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
    when(ev.action){
      MotionEvent.ACTION_DOWN->{
        lastX = ev.rawX
        lastY = ev.rawY
      }
      MotionEvent.ACTION_MOVE->{
        accMoveX += ev.x
        accMoveY += ev.y
      }
      MotionEvent.ACTION_UP->{
      }
    }
    return super.dispatchTouchEvent(ev)
  }

  override fun performClick(): Boolean {
    return super.performClick()
  }
}