package ua.frist008.action.record.ui.screen.record

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ua.frist008.action.record.R
import ua.frist008.action.record.ui.entity.record.StorageState
import ua.frist008.action.record.ui.svg.RecordNoConnection
import ua.frist008.action.record.ui.theme.AppTheme
import ua.frist008.action.record.ui.theme.color.PreviewPalette

@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = PreviewPalette.PURPLE_LIGHT_LONG,
)
@Composable
fun RecordInactiveGameScreen(
    @PreviewParameter(StorageProvider::class) storage: StorageState,
    isLive: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val subtitle = stringResource(R.string.record_inactive_subtitle)

        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = stringResource(
                if (isLive) {
                    R.string.record_inactive_title_stream
                } else {
                    R.string.record_inactive_title_record
                },
            ),
            style = AppTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 36.dp),
        )
        Text(
            text = subtitle,
            style = AppTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 36.dp),
        )
        Text(
            text = stringResource(R.string.record_inactive_message),
            style = AppTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.hint,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 36.dp),
        )

        Image(
            imageVector = Icons.RecordNoConnection,
            contentDescription = subtitle,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(36.dp),
        )

        StorageTitleText(PaddingValues(8.dp))
        StorageValueText(
            paddingValues = PaddingValues(8.dp),
            sizeState = storage.freeSpaceState,
            pattern = storage.pattern,
            errorType = storage.errorType,
        )
        Spacer(modifier = Modifier.height(28.dp))
    }
}

private class StorageProvider : PreviewParameterProvider<StorageState> {

    override val values = sequenceOf(
        StorageState.previewDefault(),
        StorageState.previewError(),
        StorageState.previewWarning(),
    )
}
