// Generated code from Butter Knife. Do not modify!
package com.fgr.bmobdemo.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ShowActivity$$ViewBinder<T extends com.fgr.bmobdemo.ui.ShowActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230741, "field 'ivNewPost'");
    target.ivNewPost = finder.castView(view, 2131230741, "field 'ivNewPost'");
    view = finder.findRequiredView(source, 2131230737, "field 'pbLoading'");
    target.pbLoading = finder.castView(view, 2131230737, "field 'pbLoading'");
    view = finder.findRequiredView(source, 2131230740, "field 'ivRefresh' and method 'refreshPost'");
    target.ivRefresh = finder.castView(view, 2131230740, "field 'ivRefresh'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.refreshPost(p0);
        }
      });
    view = finder.findRequiredView(source, 2131230735, "field 'listView'");
    target.listView = finder.castView(view, 2131230735, "field 'listView'");
    view = finder.findRequiredView(source, 2131230739, "field 'ivAdd' and method 'jumpTo'");
    target.ivAdd = finder.castView(view, 2131230739, "field 'ivAdd'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.jumpTo(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.ivNewPost = null;
    target.pbLoading = null;
    target.ivRefresh = null;
    target.listView = null;
    target.ivAdd = null;
  }
}
