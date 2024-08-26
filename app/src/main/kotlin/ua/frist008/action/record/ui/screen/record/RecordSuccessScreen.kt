@file:Suppress("UnusedReceiverParameter")

package ua.frist008.action.record.ui.screen.record

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ua.frist008.action.record.R
import ua.frist008.action.record.ui.component.DefaultScaffold
import ua.frist008.action.record.ui.entity.record.EngineState
import ua.frist008.action.record.ui.entity.record.ErrorType
import ua.frist008.action.record.ui.entity.record.LiveState
import ua.frist008.action.record.ui.entity.record.RecordButtonsState
import ua.frist008.action.record.ui.entity.record.RecordSuccessState
import ua.frist008.action.record.ui.entity.record.StorageState
import ua.frist008.action.record.ui.svg.Pause
import ua.frist008.action.record.ui.svg.Play
import ua.frist008.action.record.ui.svg.Stop
import ua.frist008.action.record.ui.theme.AppTheme
import ua.frist008.action.record.ui.theme.color.PreviewPalette

@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = PreviewPalette.PURPLE_LIGHT_LONG,
)
@Composable
fun RecordSuccessScreen(
    @PreviewParameter(RecordProvider::class) state: RecordSuccessState,
    onStartClick: () -> Unit = {},
    onResumeClick: () -> Unit = {},
    onPauseClick: () -> Unit = {},
    onStopClick: () -> Unit = {},
) {
    DefaultScaffold(
        titleRes = if (state.live.isLive) {
            R.string.record_title_stream
        } else {
            R.string.record_title_record
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HeaderInfo(engine = state.engine, live = state.live, storage = state.storage)
            FpsContainer(
                fpsState = state.fpsState,
                isPaused = state.buttonsData.isPaused,
                isRecording = state.buttonsData.isRecording,
            )
            TimeComponent(
                timeState = state.timeState,
                isPaused = state.buttonsData.isPaused,
                isRecording = state.buttonsData.isRecording,
            )
            FooterActions(
                buttonsState = state.buttonsData,
                onStartClick = onStartClick,
                onResumeClick = onResumeClick,
                onPauseClick = onPauseClick,
                onStopClick = onStopClick,
            )
            Ads()
        }
    }
}

@Composable
private fun ColumnScope.HeaderInfo(engine: EngineState, live: LiveState, storage: StorageState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        EngineComponent(engine)
        LiveComponent(live)
        StorageComponent(storage)
    }
}

@Composable
private fun RowScope.EngineComponent(engine: EngineState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.record_header_engine),
            style = AppTheme.typography.bodyLarge,
            modifier = Modifier.padding(4.dp),
        )
        Text(
            text = engine.name,
            style = AppTheme.typography.titleLarge,
            modifier = Modifier.padding(4.dp),
            color = engine.errorType.getColor(AppTheme.colors),
        )
    }
}

@Composable
private fun RowScope.LiveComponent(live: LiveState) {
    if (live.isLive) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.record_header_live),
                style = AppTheme.typography.bodyLarge,
                modifier = Modifier.padding(4.dp),
            )
            Text(
                text = stringResource(
                    if (live.isOnline) {
                        R.string.record_header_live_on
                    } else {
                        R.string.record_header_live_off
                    },
                ),
                style = AppTheme.typography.titleLarge,
                color = if (live.isOnline) {
                    AppTheme.colors.record
                } else {
                    AppTheme.colors.stop
                },
                modifier = Modifier.padding(4.dp),
            )
        }
    }
}

@Composable
private fun RowScope.StorageComponent(storage: StorageState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        StorageTitleText(PaddingValues(4.dp))
        StorageValueText(
            paddingValues = PaddingValues(4.dp),
            sizeState = storage.freeSpaceState,
            pattern = storage.pattern,
            errorType = storage.errorType,
        )
    }
}

@Composable
fun StorageTitleText(paddingValues: PaddingValues) {
    Text(
        text = stringResource(R.string.record_header_storage),
        style = AppTheme.typography.bodyLarge,
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
fun StorageValueText(
    paddingValues: PaddingValues,
    sizeState: State<Float>,
    @StringRes pattern: Int,
    errorType: ErrorType,
) {
    val size by remember { sizeState }

    Text(
        text = stringResource(pattern, size),
        style = AppTheme.typography.titleLarge,
        color = errorType.getColor(AppTheme.colors),
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
private fun ColumnScope.FpsContainer(
    fpsState: State<String>,
    isPaused: Boolean,
    isRecording: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val fps by remember { fpsState }

            Text(
                text = fps,
                style = AppTheme.typography.displayLarge,
                color = when {
                    isPaused -> AppTheme.colors.pause
                    isRecording -> AppTheme.colors.record
                    else -> AppTheme.colors.fps
                },
                modifier = Modifier.padding(start = 32.dp),
            )
            FpsSuffix(isPaused, isRecording)
        }
    }
}

@Composable
private fun RowScope.FpsSuffix(isPaused: Boolean, isRecording: Boolean) {
    Text(
        text = stringResource(R.string.record_fps),
        style = AppTheme.typography.displaySmall,
        color = when {
            isPaused -> AppTheme.colors.pause
            isRecording -> AppTheme.colors.record
            else -> AppTheme.colors.fps
        },
        modifier = Modifier
            .padding(12.dp)
            .align(Alignment.Bottom),
    )
}

@Composable
private fun ColumnScope.TimeComponent(
    timeState: MutableState<String>,
    isPaused: Boolean,
    isRecording: Boolean,
) {
    val time by remember { timeState }

    Text(
        text = time,
        style = AppTheme.typography.displaySmall,
        color = when {
            isPaused -> AppTheme.colors.pause
            isRecording -> AppTheme.colors.record
            else -> AppTheme.colors.onBackground
        },
        modifier = Modifier.padding(22.dp),
    )
}

@Composable
private fun ColumnScope.FooterActions(
    buttonsState: RecordButtonsState,
    onStartClick: () -> Unit,
    onResumeClick: () -> Unit = {},
    onPauseClick: () -> Unit = {},
    onStopClick: () -> Unit,
) {
    Row {
        if (buttonsState.isRecordingVisible) {
            IconButton(
                onClick = when {
                    !buttonsState.isStopEnabled -> onStartClick
                    buttonsState.isRecording -> onPauseClick
                    else -> onResumeClick
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .size(72.dp)
                    .clip(RoundedCornerShape(64.dp))
                    .background(
                        if (buttonsState.isRecording) {
                            AppTheme.colors.pause
                        } else {
                            AppTheme.colors.record
                        },
                    ),
            ) {
                if (buttonsState.isRecording) {
                    Icon(
                        imageVector = Icons.Pause,
                        contentDescription = stringResource(R.string.record_action_pause),
                        tint = AppTheme.colors.onBackground,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Play,
                        contentDescription = stringResource(R.string.record_action_record),
                        tint = AppTheme.colors.onBackground,
                        modifier = Modifier.padding(start = 7.dp),
                    )
                }
            }
        }

        IconButton(
            onClick = onStopClick,
            enabled = buttonsState.isStopEnabled,
            modifier = Modifier
                .padding(16.dp)
                .size(72.dp)
                .clip(RoundedCornerShape(64.dp))
                .background(
                    if (buttonsState.isStopEnabled) {
                        AppTheme.colors.stop
                    } else {
                        AppTheme.colors.stopDisabled
                    },
                ),
        ) {
            Icon(
                imageVector = Icons.Stop,
                contentDescription = stringResource(R.string.record_action_stop),
                tint = AppTheme.colors.onBackground,
            )
        }
    }
}

@Composable
private fun ColumnScope.Ads() {
}

private class RecordProvider : PreviewParameterProvider<RecordSuccessState> {

    override val values = sequenceOf(
        RecordSuccessState.previewStop(),
        RecordSuccessState.previewRecording(),
        RecordSuccessState.previewPaused(),
        RecordSuccessState.previewLive(),
    )
}
