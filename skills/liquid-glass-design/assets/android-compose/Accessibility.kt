package com.example.app.ui

import android.animation.ValueAnimator
import android.app.UiModeManager
import android.os.Build
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Samsung's "Reduce transparency and blur" accessibility switch (no AOSP equivalent yet). Liquid Glass should
 * turn frostier and drop the lensing when people ask for less transparency.
 */
@Composable
fun rememberReduceTransparency(): Boolean {
    val context = LocalContext.current
    return remember {
        runCatching {
            Settings.System.getInt(context.contentResolver, "accessibility_reduce_transparency", 0) == 1
        }.getOrDefault(false)
    }
}

/** Android 14+ contrast setting; above ~0.5 use solid fills and visible borders instead of translucency. */
@Composable
fun rememberIncreasedContrast(): Boolean {
    val context = LocalContext.current
    return remember {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
            context.getSystemService(UiModeManager::class.java).contrast > 0.5f
    }
}

/** "Remove animations": skip squash/stretch, morphing and one-off fills; use short cross-fades. */
@Composable
fun rememberReduceMotion(): Boolean = remember { !ValueAnimator.areAnimatorsEnabled() }
