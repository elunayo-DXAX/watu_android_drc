package com.watutz.app.view;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;


/**
 * Created by wajdichamakhi on 26/01/15.
 */
public class ResizeAnimation extends Animation {


  private int endHeight; // distance between start and end height
  private int endWidth; // distance between start and end width


  private View view; 

  /**
   * constructor, do not forget to use the setParams(int, int) method before
   * starting the animation
   *
   * @param v
   */
  public ResizeAnimation(View v) {
      this.view = v;
  }

  @Override
  protected void applyTransformation(float interpolatedTime, Transformation t) {

      view.getLayoutParams().height = (int) (view.getHeight() + (endHeight - view.getHeight()) * interpolatedTime);
      view.getLayoutParams().width = (int) (view.getWidth() + (endWidth -   view.getWidth()) * interpolatedTime);

      view.requestLayout();
  }

  /**
   * set the starting and ending height for the resize animation
   * starting height is usually the views current height, the end height is the height
   * we want to reach after the animation is completed
   *
   * @param endWidths width in pixels
   * @param endHeight height in pixels
   */
  public void setParams(int endWidths, int endHeight) {
      this.endWidth = endWidths;
      this.endHeight = endHeight;
  }

  /**
   * set the duration for the hideshowanimation
   */
  @Override
  public void setDuration(long durationMillis) {
      super.setDuration(durationMillis);
  }

  @Override
  public boolean willChangeBounds() {
      return true;
  }

   /**
   * Verify if this animation needs to be fired or not.
   */
  public boolean isValidAnimation() {
      if (this.endWidth == view.getWidth() && this.endHeight ==  view.getHeight())
          return false;
      else
          return true;
   }



public static AnimationSet getExpend_CollapseAnimationSet(final View v, int height, int width) {

    AnimationSet animationSet = new AnimationSet(true);

        ResizeAnimation resizeAnimation = new ResizeAnimation(v);
        resizeAnimation.setParams(
                width,
                height);
        if (resizeAnimation.isValidAnimation()) {
            resizeAnimation.setDuration(2000);
            animationSet.addAnimation(resizeAnimation);
        }

    return animationSet;
 }
}