package by.android.academy.minsk.fastfinger.android

import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat


class AndroidResourceManager(private val resources: Resources) {

    fun getString(@StringRes resId: Int): String = resources.getString(resId)

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String = resources.getString(resId, *formatArgs)

    fun getDimension(@DimenRes resId: Int): Float = resources.getDimension(resId)

    fun getDimensionPixelSize(@DimenRes resId: Int): Int = resources.getDimensionPixelSize(resId)

    fun getDrawable(@DrawableRes resId: Int): Drawable? = ResourcesCompat.getDrawable(resources, resId, null)

}