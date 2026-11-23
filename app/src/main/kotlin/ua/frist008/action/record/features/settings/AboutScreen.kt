package ua.frist008.action.record.features.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ua.frist008.action.record.BuildConfig
import ua.frist008.action.record.R
import ua.frist008.action.record.core.ui.component.root.DefaultScaffold
import ua.frist008.action.record.core.ui.resource.Icons
import ua.frist008.action.record.core.ui.resource.svg.AppLogo
import ua.frist008.action.record.core.ui.theme.AppTheme
import ua.frist008.action.record.core.ui.theme.color.PreviewPalette

@Composable
fun AboutScreen(viewModel: AboutViewModel = hiltViewModel()) {
    val privacyPolicyUrl = stringResource(R.string.settings_about_privacy_policy_url)

    DefaultScaffold(title = stringResource(R.string.settings_about)) { innerPadding ->
        AboutSuccessScreen(
            versionName = BuildConfig.VERSION_NAME,
            innerPadding = innerPadding,
            onPrivacyPolicyClick = { viewModel.onPrivacyPolicyClick(privacyPolicyUrl) },
            onLicensesClick = viewModel::onLicensesClick,
        )
    }
}

@Composable
private fun AboutSuccessScreen(
    versionName: String,
    innerPadding: PaddingValues = PaddingValues(),
    onPrivacyPolicyClick: () -> Unit = {},
    onLicensesClick: () -> Unit = {},
) {
    val appName = stringResource(R.string.settings_about_app_name)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Image(
            imageVector = Icons.AppLogo,
            contentDescription = appName,
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(28.dp)),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = appName,
            style = AppTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.settings_version_pattern, versionName),
            style = AppTheme.typography.bodyLarge,
            color = AppTheme.colors.hint,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(40.dp))
        LinkText(
            text = stringResource(R.string.settings_about_privacy_policy),
            onClick = onPrivacyPolicyClick,
        )
        LinkText(
            text = stringResource(R.string.settings_about_licenses),
            onClick = onLicensesClick,
        )
    }
}

/** A link of the screen «About»: underlined and centred, touched 8 dp above and below the text */
@Composable
private fun LinkText(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = AppTheme.typography.link,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = PreviewPalette.PURPLE_LIGHT_LONG,
)
@Composable
private fun AboutSuccessScreenPreview() {
    AboutSuccessScreen(versionName = "1.0.4")
}
