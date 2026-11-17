package ua.frist008.action.record.features.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.frist008.action.record.R
import ua.frist008.action.record.core.ui.resource.Icons
import ua.frist008.action.record.core.ui.resource.svg.ChevronRight
import ua.frist008.action.record.core.ui.resource.svg.OpenInNew
import ua.frist008.action.record.core.ui.resource.svg.StepperMinus
import ua.frist008.action.record.core.ui.resource.svg.StepperPlus
import ua.frist008.action.record.core.ui.theme.AppTheme
import ua.frist008.action.record.core.ui.theme.color.PreviewPalette
import ua.frist008.action.record.features.settings.entity.SettingsSuccessState

@Composable
fun SettingsSuccessScreen(
    state: SettingsSuccessState,
    actions: SettingsActions,
    innerPadding: PaddingValues = PaddingValues(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
            .padding(top = 11.dp, bottom = 16.dp),
    ) {
        StorageSection(state, actions)
        SoundsSection(state, actions)
        DimmingSection(state, actions)
        AppSection(state, actions)

        if (state.isAdsPrivacyVisible) {
            PrivacySection(actions)
        }
    }
}

@Composable
private fun ColumnScope.StorageSection(state: SettingsSuccessState, actions: SettingsActions) {
    StepperRow(
        title = stringResource(R.string.settings_min_free_space),
        summary = stringResource(R.string.settings_min_free_space_summary),
        value = stringResource(R.string.settings_gb_pattern, state.minFreeSpaceGb),
        onChange = actions::onMinFreeSpaceChange,
    )
}

@Composable
private fun ColumnScope.SoundsSection(state: SettingsSuccessState, actions: SettingsActions) {
    val buttonSoundSummary = stringResource(R.string.settings_button_sound_summary)
    val isDurationSound = state.isDurationSound

    SectionHeader(stringResource(R.string.settings_section_sounds))
    SwitchRow(
        title = stringResource(R.string.settings_low_space),
        summary = stringResource(R.string.settings_low_space_summary),
        isChecked = state.isLowSpaceSound,
        onCheckedChange = actions::onLowSpaceSoundChange,
    )
    SwitchRow(
        title = stringResource(R.string.settings_record_button),
        summary = buttonSoundSummary,
        isChecked = state.isRecordButtonSound,
        onCheckedChange = actions::onRecordButtonSoundChange,
    )
    SwitchRow(
        title = stringResource(R.string.settings_pause_button),
        summary = buttonSoundSummary,
        isChecked = state.isPauseButtonSound,
        onCheckedChange = actions::onPauseButtonSoundChange,
    )
    SwitchRow(
        title = stringResource(R.string.settings_stop_button),
        summary = buttonSoundSummary,
        isChecked = state.isStopButtonSound,
        onCheckedChange = actions::onStopButtonSoundChange,
    )
    SwitchRow(
        title = stringResource(R.string.settings_duration),
        summary = stringResource(R.string.settings_duration_summary),
        isChecked = isDurationSound,
        onCheckedChange = actions::onDurationSoundChange,
    )
    StepperRow(
        title = stringResource(R.string.settings_duration_after),
        summary = stringResource(R.string.settings_duration_after_summary),
        value = stringResource(R.string.settings_minutes_pattern, state.durationSoundAfterMinutes),
        onChange = actions::onDurationSoundAfterChange,
        isEnabled = isDurationSound,
    )
}

@Composable
private fun ColumnScope.DimmingSection(state: SettingsSuccessState, actions: SettingsActions) {
    SectionHeader(stringResource(R.string.settings_section_dimming))
    StepperRow(
        title = stringResource(R.string.settings_dim_after),
        summary = stringResource(R.string.settings_dim_after_summary),
        value = stringResource(R.string.settings_minutes_pattern, state.dimAfterMinutes),
        onChange = actions::onDimAfterChange,
    )
    StepperRow(
        title = stringResource(R.string.settings_dim_to),
        summary = stringResource(R.string.settings_dim_to_summary),
        value = stringResource(R.string.settings_percent_pattern, state.dimToPercent),
        onChange = actions::onDimToChange,
    )
    SwitchRow(
        title = stringResource(R.string.settings_first_touch),
        summary = stringResource(R.string.settings_first_touch_summary),
        isChecked = state.isFirstTouchOnlyWakes,
        onCheckedChange = actions::onFirstTouchChange,
    )
}

@Composable
private fun ColumnScope.AppSection(state: SettingsSuccessState, actions: SettingsActions) {
    SectionHeader(stringResource(R.string.settings_section_app))

    if (state.isLanguageVisible) {
        LinkRow(
            title = stringResource(R.string.settings_language),
            summary = state.languageName ?: stringResource(R.string.settings_language_system),
            icon = Icons.OpenInNew,
            onClick = actions::onLanguageClick,
        )
    }

    LinkRow(
        title = stringResource(R.string.settings_about),
        summary = stringResource(R.string.settings_version_pattern, state.versionName),
        icon = Icons.ChevronRight,
        onClick = actions::onAboutClick,
    )
}

@Composable
private fun ColumnScope.PrivacySection(actions: SettingsActions) {
    SectionHeader(stringResource(R.string.settings_section_privacy))
    LinkRow(
        title = stringResource(R.string.settings_ads),
        summary = stringResource(R.string.settings_ads_summary),
        icon = Icons.OpenInNew,
        onClick = actions::onAdsPrivacyClick,
    )
}

@Composable
private fun ColumnScope.SectionHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 30.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(9.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = title, style = AppTheme.typography.bodyLarge)
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = AppTheme.colors.onBackground,
        )
    }
}

@Composable
private fun ColumnScope.SwitchRow(
    title: String,
    summary: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(value = isChecked, role = Role.Switch, onValueChange = onCheckedChange)
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .heightIn(min = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RowTexts(title = title, summary = summary)
        Switch(
            checked = isChecked,
            onCheckedChange = null,
            colors = AppTheme.colors.switchColors,
        )
    }
}

@Composable
private fun ColumnScope.StepperRow(
    title: String,
    summary: String,
    value: String,
    onChange: (isIncrease: Boolean) -> Unit,
    isEnabled: Boolean = true,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (isEnabled) 1f else 0.38f)
            .padding(start = 16.dp, top = 16.dp, end = 4.dp)
            .heightIn(min = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RowTexts(title = title, summary = summary)
        IconButton(onClick = { onChange(false) }, enabled = isEnabled) {
            Image(
                imageVector = Icons.StepperMinus,
                contentDescription = stringResource(R.string.settings_decrease_pattern, title),
            )
        }
        Text(
            text = value,
            style = AppTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.widthIn(min = 52.dp),
        )
        IconButton(onClick = { onChange(true) }, enabled = isEnabled) {
            Image(
                imageVector = Icons.StepperPlus,
                contentDescription = stringResource(R.string.settings_increase_pattern, title),
            )
        }
    }
}

@Composable
private fun ColumnScope.LinkRow(
    title: String,
    summary: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .heightIn(min = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RowTexts(title = title, summary = summary)
        Icon(imageVector = icon, contentDescription = null)
    }
}

@Composable
private fun RowScope.RowTexts(title: String, summary: String) {
    Column(modifier = Modifier.weight(1f)) {
        Text(text = title, style = AppTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = summary,
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.hint,
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = PreviewPalette.PURPLE_LIGHT_LONG,
)
@Composable
private fun SettingsSuccessScreenPreview() {
    SettingsSuccessScreen(
        state = SettingsSuccessState.preview(),
        actions = object : SettingsActions {},
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = PreviewPalette.PURPLE_LIGHT_LONG,
)
@Composable
private fun SettingsSuccessScreenOffPreview() {
    SettingsSuccessScreen(
        state = SettingsSuccessState.preview().copy(
            isDurationSound = false,
            isLanguageVisible = false,
            isAdsPrivacyVisible = false,
        ),
        actions = object : SettingsActions {},
    )
}
