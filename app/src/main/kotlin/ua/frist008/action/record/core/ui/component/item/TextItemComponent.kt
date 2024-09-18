package ua.frist008.action.record.core.ui.component.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.frist008.action.record.core.ui.theme.AppTheme
import ua.frist008.action.record.core.ui.theme.RootTheme

@Composable
fun TextColumnItemComponent(text: String?, subtext: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        if (!text.isNullOrEmpty()) {
            Text(
                text = text,
                style = AppTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                maxLines = 1,
            )
        }
        if (!text.isNullOrEmpty() && subtext.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (subtext.isNotEmpty()) {
            Text(
                text = subtext,
                style = AppTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                maxLines = 1,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TextColumnItemComponentPreview() {
    val itemModifier = Modifier
        .padding(vertical = 8.dp)
        .fillMaxWidth()
        .shadow(1.dp)
        .padding(vertical = 24.dp)
    RootTheme {
        Column {
            TextColumnItemComponent(
                "Name PC",
                "192.168.0.1:25566",
                itemModifier,
            )
            TextColumnItemComponent(
                "Name Very Very Very Very Very Very Very Very Very Long",
                "192.168.0.1:25566",
                itemModifier,
            )
            TextColumnItemComponent(
                "Name PC",
                "192.168.192.192:2556655",
                itemModifier,
            )
            TextColumnItemComponent(
                "Name PC",
                "",
                itemModifier,
            )
            TextColumnItemComponent(
                "",
                "192.168.0.1:255666666666666666666666666666666666666666666666",
                itemModifier,
            )
        }
    }
}
