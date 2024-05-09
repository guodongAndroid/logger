package com.guodong.android.logger.sample

import android.os.Looper

/**
 * Created by guodongAndroid on 2024/5/9 9:53.
 */

fun isMainThread() = Looper.getMainLooper() === Looper.myLooper()