/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.react.views.popupmenu

import android.content.Context
import android.os.Build
import android.view.Menu
import android.widget.FrameLayout
import android.widget.PopupMenu
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.uimanager.UIManagerHelper

class ReactPopupMenuContainer(context: Context) : FrameLayout(context) {
  private var menuItems: ReadableArray? = null

  fun setMenuItems(items: ReadableArray?) {
    menuItems = items
  }

  fun showPopupMenu() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      val view = getChildAt(0)
      val popupMenu = PopupMenu(context, view)
      var menu: Menu? = null
      menu = popupMenu.menu
      val items = menuItems
      if (items != null) {
        for (i in 0 until items.size()) {
          menu.add(Menu.NONE, Menu.NONE, i, items.getString(i))
        }
      }
      popupMenu.setOnMenuItemClickListener { menuItem ->
        val reactContext = context as ReactContext
        val eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, id)
        if (eventDispatcher != null) {
          val surfaceId = UIManagerHelper.getSurfaceId(reactContext)
          eventDispatcher.dispatchEvent(PopupMenuSelectionEvent(surfaceId, id, menuItem.order))
        }
        true
      }
      popupMenu.show()
    }
  }
}
