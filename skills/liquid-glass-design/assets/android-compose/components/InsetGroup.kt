package com.example.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app.ui.LocalSystemColors
import com.example.app.ui.SystemType
import com.kyant.shapes.RoundedRectangle

/** iOS 26 inset-grouped sections: larger, continuous corners that match the rounder controls. */
val GroupShape = RoundedRectangle(26.dp)

@Composable
fun InsetGroup(
    modifier: Modifier = Modifier,
    header: String? = null,
    footer: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = LocalSystemColors.current
    Column(
        modifier
            .fillMaxWidth()
            .padding(top = 28.dp),
    ) {
        if (header != null) {
            // Title-style capitalization: iOS 26 headers no longer shout in capitals.
            Text(header, style = SystemType.headline, color = colors.label, modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp))
        }
        Column(
            Modifier
                .fillMaxWidth()
                .clip(GroupShape)
                .background(colors.cell),
            content = content,
        )
        if (footer != null) {
            Text(footer, style = SystemType.footnote, color = colors.secondaryLabel, modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp))
        }
    }
}

/** A list row with the system's gray press highlight instead of a ripple. */
@Composable
fun ListRow(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = LocalSystemColors.current
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    Row(
        modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier
                        .background(if (pressed) colors.fill else Color.Transparent)
                        .clickable(interactionSource = interaction, indication = null, onClick = onClick)
                } else {
                    Modifier
                },
            )
            .heightIn(min = 52.dp)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

/** Hairline separator, inset to the text like iOS (use inset 58 after rows with icon tiles). */
@Composable
fun RowSeparator(inset: Int = 16) {
    val colors = LocalSystemColors.current
    val hairline = with(LocalDensity.current) { 1f.toDp() }
    Box(
        Modifier
            .padding(start = inset.dp)
            .fillMaxWidth()
            .height(hairline)
            .background(colors.separator),
    )
}

@Composable
fun ValueRow(title: String, value: String, detail: String? = null, valueColor: Color? = null) {
    val colors = LocalSystemColors.current
    ListRow {
        Text(title, style = SystemType.body, color = colors.label, modifier = Modifier.weight(1f))
        if (detail != null) {
            Text(detail, style = SystemType.body.merge(SystemType.figures), color = colors.tertiaryLabel)
            Spacer(Modifier.width(10.dp))
        }
        Text(value, style = SystemType.body.merge(SystemType.figures), color = valueColor ?: colors.secondaryLabel)
    }
}

/** Title … value … chevron; pass a right-chevron icon (e.g. Material Symbols Rounded "chevron_right"). */
@Composable
fun NavigationRow(title: String, chevron: Painter, value: String? = null, onClick: () -> Unit) {
    val colors = LocalSystemColors.current
    ListRow(onClick = onClick) {
        Text(title, style = SystemType.body, color = colors.label, modifier = Modifier.weight(1f))
        if (value != null) {
            Text(value, style = SystemType.body, color = colors.secondaryLabel)
            Spacer(Modifier.width(6.dp))
        }
        Icon(chevron, contentDescription = null, tint = colors.tertiaryLabel, modifier = Modifier.size(20.dp))
    }
}

/** A single tinted (or red, for destructive) action, left aligned like iOS. */
@Composable
fun ActionRow(title: String, color: Color, onClick: () -> Unit) {
    ListRow(onClick = onClick) {
        Text(title, style = SystemType.body, color = color)
    }
}

/** A row whose control (e.g. a segmented control) sits under its title. */
@Composable
fun ControlRow(title: String, content: @Composable ColumnScope.() -> Unit) {
    val colors = LocalSystemColors.current
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(title, style = SystemType.body, color = colors.label)
        content()
    }
}

/** Settings-style row: colored rounded-square glyph, the problem, its consequence, and a trailing fix. */
@Composable
fun NoticeRow(
    icon: Painter,
    iconTint: Color,
    title: String,
    subtitle: String,
    action: String,
    onAction: () -> Unit,
) {
    val colors = LocalSystemColors.current
    ListRow(onClick = onAction) {
        Box(
            Modifier
                .size(30.dp)
                .clip(RoundedRectangle(8.dp))
                .background(iconTint),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(19.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = SystemType.body, color = colors.label)
            Text(subtitle, style = SystemType.footnote, color = colors.secondaryLabel)
        }
        Spacer(Modifier.width(8.dp))
        Text(action, style = SystemType.body.copy(fontWeight = FontWeight.Medium), color = colors.tint)
    }
}
