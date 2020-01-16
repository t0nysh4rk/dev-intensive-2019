package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard(){
    // Check if no view has focus:
    val view = this.currentFocus
    view?.let { v ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}

fun Activity.isKeyboardOpen():Boolean{
        val r = Rect()

        val activityRoot = getActivityRoot()
        val visibleThreshold = dip(100)

        activityRoot.getWindowVisibleDisplayFrame(r)

        val heightDiff = activityRoot.rootView.height - r.height()

        return heightDiff > visibleThreshold
    }

fun dip(value: Int): Int {
    return (value * Resources.getSystem().displayMetrics.density).toInt()
}

fun Activity.getActivityRoot(): View {
        return (findViewById<ViewGroup>(android.R.id.content)).getChildAt(0)
    }




fun Activity.isKeyboardClosed(): Boolean{
    return !isKeyboardOpen()
}