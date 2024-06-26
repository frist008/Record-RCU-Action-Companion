package ua.frist008.action.record.ui.screen.record

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ua.frist008.action.record.ui.component.ColoredCircularProgressIndicator
import ua.frist008.action.record.ui.theme.color.Palette

@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = Palette.PURPLE_LIGHT_LONG,
)
@Composable
fun RecordProgressScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        ColoredCircularProgressIndicator(sizeDp = 56)
    }
}
