// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FriendFragment$$ViewBinder<T extends com.fgr.miaoxin.fragment.FriendFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165240, "field 'tvLetter'");
    target.tvLetter = finder.castView(view, 2131165240, "field 'tvLetter'");
    view = finder.findRequiredView(source, 2131165239, "field 'mlvLetters'");
    target.mlvLetters = finder.castView(view, 2131165239, "field 'mlvLetters'");
    view = finder.findRequiredView(source, 2131165238, "field 'listView'");
    target.listView = finder.castView(view, 2131165238, "field 'listView'");
  }

  @Override public void unbind(T target) {
    target.tvLetter = null;
    target.mlvLetters = null;
    target.listView = null;
  }
}
