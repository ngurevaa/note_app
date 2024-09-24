package ru.kpfu.itis.gureva.mvi.util

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getString(@StringRes res: Int): String = context.resources.getString(res)

    fun getString(@StringRes res: Int, vararg args: Any?): String {
        return context.resources.getString(res, *args)
    }
}
