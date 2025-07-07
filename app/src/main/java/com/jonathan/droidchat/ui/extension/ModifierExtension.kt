package com.jonathan.droidchat.ui.extension

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Dp

fun Modifier.bottomBorder(
    color: Color,
    strokeWidth: Dp
): Modifier {
    return this.drawBehind {
        val strokeWidthPx = strokeWidth.toPx()
        val width = size.width
        val height = size.height - strokeWidthPx / 2

        drawLine(
            color = color,
            start = Offset(x = 0f, y = height),
            end = Offset(x = width, y = height),
            strokeWidth = strokeWidthPx
        )
    }
}

fun Modifier.ifMatch(
    condition: Boolean,
    block: Modifier.() -> Modifier
): Modifier {
    return if (condition) this.then(block()) else this
}

private class BottomBorderNode(
    var color: Color,
    var strokeWidth: Dp,
) : DrawModifier, Modifier.Node() {
    override fun ContentDrawScope.draw() {
        val strokeWidthPx = strokeWidth.toPx()
        val width = size.width
        val height = size.height - strokeWidthPx / 2

        drawLine(
            color = color,
            start = Offset(x = 0f, y = height),
            end = Offset(x = width, y = height),
            strokeWidth = strokeWidthPx
        )
    }
}

private data class BottomBorderElement(
    val color: Color,
    val strokeWidth: Dp,
): ModifierNodeElement<BottomBorderNode>() {
    override fun create(): BottomBorderNode {
        return BottomBorderNode(color, strokeWidth)
    }

    override fun update(node: BottomBorderNode) {
        node.color = color
        node.strokeWidth = strokeWidth
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "bottomBorder"
        properties["color"] = color
        properties["strokeWidth"] = strokeWidth
    }


}

fun Modifier.bottomBorder2(color: Color, strokeWidth: Dp): Modifier {
    return this then BottomBorderElement(color, strokeWidth)
}