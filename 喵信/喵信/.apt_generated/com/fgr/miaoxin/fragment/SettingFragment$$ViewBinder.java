// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SettingFragment$$ViewBinder<T extends com.fgr.miaoxin.fragment.SettingFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165264, "field 'ivVibrate' and method 'setVibrate'");
    target.ivVibrate = finder.castView(view, 2131165264, "field 'ivVibrate'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setVibrate(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165261, "field 'tvSound'");
    target.tvSound = finder.castView(view, 2131165261, "field 'tvSound'");
    view = finder.findRequiredView(source, 2131165259, "field 'tvNotification'");
    target.tvNotification = finder.castView(view, 2131165259, "field 'tvNotification'");
    view = finder.findRequiredView(source, 2131165262, "field 'ivSound' and method 'setSound'");
    target.ivSound = finder.castView(view, 2131165262, "field 'ivSound'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setSound(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165263, "field 'tvVibrate'");
    target.tvVibrate = finder.castView(view, 2131165263, "field 'tvVibrate'");
    view = finder.findRequiredView(source, 2131165260, "field 'ivNotification' and method 'setNotification'");
    target.ivNotification = finder.castView(view, 2131165260, "field 'ivNotification'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setNotification(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165257, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131165257, "field 'tvUsername'");
    view = finder.findRequiredView(source, 2131165258, "method 'setUserInfo'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setUserInfo(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165265, "method 'logout'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.logout(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.ivVibrate = null;
    target.tvSound = null;
    target.tvNotification = null;
    target.ivSound = null;
    target.tvVibrate = null;
    target.ivNotification = null;
    target.tvUsername = null;
  }
}
