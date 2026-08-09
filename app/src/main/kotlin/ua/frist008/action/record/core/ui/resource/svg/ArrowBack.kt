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

val Icons.ArrowBack: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "ArrowBack",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = true,
    ).apply {
        path(fill = SolidColor(Color(0xFFF1F2F2))) {
            moveTo(19.647f, 11.135f)
            horizontalLineTo(5.795f)
            lineTo(10.338f, 6.184f)
            curveTo(10.616f, 5.882f, 10.596f, 5.412f, 10.294f, 5.135f)
            curveTo(9.992f, 4.857f, 9.522f, 4.877f, 9.245f, 5.179f)
            lineTo(3.558f, 11.375f)
            curveTo(3.558f, 11.375f, 3.541f, 11.398f, 3.534f, 11.41f)
            curveTo(3.521f, 11.425f, 3.509f, 11.437f, 3.499f, 11.454f)
            curveTo(3.494f, 11.464f, 3.487f, 11.472f, 3.482f, 11.482f)
            curveTo(3.472f, 11.499f, 3.462f, 11.516f, 3.452f, 11.534f)
            curveTo(3.444f, 11.548f, 3.435f, 11.563f, 3.427f, 11.581f)
            curveTo(3.422f, 11.59f, 3.42f, 11.6f, 3.415f, 11.61f)
            curveTo(3.407f, 11.63f, 3.402f, 11.65f, 3.395f, 11.67f)
            curveTo(3.39f, 11.687f, 3.385f, 11.702f, 3.38f, 11.719f)
            curveTo(3.38f, 11.729f, 3.375f, 11.739f, 3.375f, 11.751f)
            curveTo(3.373f, 11.771f, 3.37f, 11.791f, 3.368f, 11.811f)
            curveTo(3.368f, 11.828f, 3.363f, 11.848f, 3.363f, 11.865f)
            curveTo(3.363f, 11.87f, 3.363f, 11.875f, 3.363f, 11.88f)
            curveTo(3.363f, 11.885f, 3.363f, 11.89f, 3.363f, 11.895f)
            curveTo(3.363f, 11.912f, 3.365f, 11.932f, 3.368f, 11.949f)
            curveTo(3.368f, 11.969f, 3.37f, 11.989f, 3.375f, 12.009f)
            curveTo(3.375f, 12.019f, 3.378f, 12.031f, 3.38f, 12.041f)
            curveTo(3.383f, 12.058f, 3.39f, 12.073f, 3.395f, 12.09f)
            curveTo(3.4f, 12.11f, 3.407f, 12.13f, 3.415f, 12.15f)
            curveTo(3.42f, 12.16f, 3.422f, 12.17f, 3.427f, 12.179f)
            curveTo(3.435f, 12.194f, 3.444f, 12.209f, 3.452f, 12.226f)
            curveTo(3.462f, 12.244f, 3.469f, 12.261f, 3.482f, 12.278f)
            curveTo(3.487f, 12.288f, 3.491f, 12.296f, 3.499f, 12.306f)
            curveTo(3.509f, 12.321f, 3.524f, 12.335f, 3.536f, 12.35f)
            curveTo(3.546f, 12.36f, 3.551f, 12.372f, 3.561f, 12.385f)
            lineTo(9.247f, 18.581f)
            curveTo(9.393f, 18.739f, 9.594f, 18.821f, 9.794f, 18.821f)
            curveTo(9.972f, 18.821f, 10.153f, 18.757f, 10.296f, 18.625f)
            curveTo(10.598f, 18.348f, 10.618f, 17.878f, 10.341f, 17.576f)
            lineTo(5.798f, 12.625f)
            horizontalLineTo(19.65f)
            curveTo(20.061f, 12.625f, 20.392f, 12.293f, 20.392f, 11.882f)
            curveTo(20.392f, 11.472f, 20.061f, 11.14f, 19.65f, 11.14f)
            lineTo(19.647f, 11.135f)
            close()
        }
    }.build()
}

@Preview
@Composable
private fun ArrowBackPreview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.ArrowBack, contentDescription = null)
    }
}