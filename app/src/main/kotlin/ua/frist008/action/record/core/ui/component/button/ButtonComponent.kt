package ua.frist008.action.record.core.ui.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.frist008.action.record.R
import ua.frist008.action.record.core.ui.theme.AppTheme

@Composable
fun ButtonComponent(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = AppTheme.typography.labelLarge,
    colors: ButtonColors = AppTheme.colors.buttonColors,
    shape: Shape = ButtonDefaults.shape,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = colors,
        modifier = modifier.clip(shape),
        content = {
            Text(
                textAlign = TextAlign.Center,
                text = text,
                style = style,
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun ButtonComponentPreview() {
    Column(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp),
    ) {
        ButtonComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            text = stringResource(R.string.app_name),
        ) {}
    }
}
