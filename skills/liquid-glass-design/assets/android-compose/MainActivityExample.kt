package com.example.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.app.R
import com.example.app.ui.components.ActionRow
import com.example.app.ui.components.ControlRow
import com.example.app.ui.components.InsetGroup
import com.example.app.ui.components.LargeTitlePage
import com.example.app.ui.components.ListRow
import com.example.app.ui.components.RowSeparator
import com.example.app.ui.components.SegmentedControl
import com.example.app.ui.components.ValueRow
import com.example.app.ui.glass.GlassTab
import com.example.app.ui.glass.GlassTabBar
import com.example.app.ui.glass.GlassToggle
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop

/**
 * Wiring example: the content layer is captured with layerBackdrop so the floating glass tab bar can refract it.
 * Icons: Material Symbols Rounded (filled) as vector drawables, e.g. ic_home_fill and ic_gear_fill.
 */
class MainActivityExample : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiquidGlassTheme(lightTint = Color(0xFF34C759), darkTint = Color(0xFF30D158)) {
                val colors = LocalSystemColors.current
                val reduceTransparency = rememberReduceTransparency()
                val backdrop = rememberLayerBackdrop()
                var tab by rememberSaveable { mutableIntStateOf(0) }
                // Keep each tab's scroll position across switches.
                val homeScroll = rememberScrollState()
                val settingsScroll = rememberScrollState()
                BackHandler(enabled = tab != 0) { tab = 0 }

                Box(Modifier.fillMaxSize()) {
                    // Content layer: everything here is captured for the glass to refract.
                    Box(
                        Modifier
                            .fillMaxSize()
                            .layerBackdrop(backdrop),
                    ) {
                        when (tab) {
                            0 -> LargeTitlePage("Summary", homeScroll) {
                                InsetGroup(header = "Details", footer = "Values update every second.") {
                                    ValueRow("Power", "7.6 W")
                                    RowSeparator()
                                    ValueRow("Temperature", "36.8 °C")
                                }
                            }
                            else -> LargeTitlePage("Settings", settingsScroll) {
                                var on by remember { mutableStateOf(true) }
                                var mode by remember { mutableStateOf("A") }
                                InsetGroup(header = "General") {
                                    ListRow {
                                        Text("Enabled", style = SystemType.body, color = colors.label, modifier = Modifier.weight(1f))
                                        GlassToggle(
                                            checked = { on },
                                            onCheckedChange = { on = it },
                                            onColor = colors.tint,
                                            offColor = colors.fill,
                                            reduceTransparency = reduceTransparency,
                                        )
                                    }
                                    RowSeparator()
                                    ControlRow("Mode") {
                                        SegmentedControl(listOf("A" to "Option A", "B" to "Option B"), mode, { mode = it })
                                    }
                                }
                                InsetGroup {
                                    ActionRow("Reset", colors.red) { }
                                }
                            }
                        }
                    }

                    // Navigation layer: the only glass on screen.
                    GlassTabBar(
                        selectedTabIndex = { tab },
                        onTabSelected = { tab = it },
                        backdrop = backdrop,
                        tabsCount = 2,
                        accentColor = colors.tint,
                        containerColor = colors.glassSurface,
                        selectionColor = colors.glassSelection,
                        reduceTransparency = reduceTransparency,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .navigationBarsPadding()
                            .padding(bottom = 12.dp)
                            .width(216.dp),
                    ) {
                        GlassTab(selected = tab == 0, onClick = { tab = 0 }) {
                            Icon(painterResource(R.drawable.ic_home_fill), contentDescription = null, tint = colors.label, modifier = Modifier.size(24.dp))
                            Text("Summary", style = SystemType.tabLabel, color = colors.label)
                        }
                        GlassTab(selected = tab == 1, onClick = { tab = 1 }) {
                            Icon(painterResource(R.drawable.ic_gear_fill), contentDescription = null, tint = colors.label, modifier = Modifier.size(24.dp))
                            Text("Settings", style = SystemType.tabLabel, color = colors.label)
                        }
                    }
                }
            }
        }
    }
}
