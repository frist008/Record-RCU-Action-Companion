package ua.frist008.action.record.core.ui.resource.svg

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.frist008.action.record.core.ui.resource.Icons

val Icons.StepperPlus: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "StepperPlus",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color(0xE6FFFFFF))) {
            moveTo(12f, 0f)
            arcTo(12f, 12f, 0f, true, true, 12f, 24f)
            arcTo(12f, 12f, 0f, true, true, 12f, 0f)
            close()
        }
        path(fill = SolidColor(Color(0xFF0C1E3A))) {
            moveTo(5f, 11.25f)
            horizontalLineTo(19f)
            verticalLineTo(12.75f)
            horizontalLineTo(5f)
            close()
        }
        path(fill = SolidColor(Color(0xFF0C1E3A))) {
            moveTo(11.25f, 5f)
            horizontalLineTo(12.75f)
            verticalLineTo(19f)
            horizontalLineTo(11.25f)
            close()
        }
    }.build()
}

@Preview
@Composable
private fun StepperPlusPreview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.StepperPlus, contentDescription = null)
    }
}
