package ua.frist008.action.record.features.device

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import ua.frist008.action.record.R
import ua.frist008.action.record.core.ui.component.button.ButtonComponent
import ua.frist008.action.record.core.ui.component.progress.ColoredCircularProgressIndicator
import ua.frist008.action.record.core.ui.resource.svg.DevicesNotAvailable
import ua.frist008.action.record.core.ui.theme.AppTheme
import ua.frist008.action.record.core.ui.theme.color.PreviewPalette
import ua.frist008.action.record.features.device.entity.DevicesProgressState
import kotlin.time.Duration.Companion.seconds

@Preview(
    showSystemUi = true,
    showBackground = true,
    backgroundColor = PreviewPalette.PURPLE_LIGHT_LONG,
)
@Composable
fun DevicesProgressScreen(
    @PreviewParameter(DeviceProgressProvider::class) state: DevicesProgressState,
    onLinkCLick: () -> Unit = {},
) {
    // Example of use ConstraintLayout. Can be optimized to Column
    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .safeContentPadding(),
    ) {
        val (descriptionAndImage, timer, link) = createRefs()

        createVerticalChain(descriptionAndImage, timer, link, chainStyle = ChainStyle.SpreadInside)

        DevicesProgressHeader(
            descriptionAndImage = descriptionAndImage,
            isLoading = state.isLoading,
        ) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(timer.top)
        }
        DevicesProgressTimer(timer = timer, state = state) {
            top.linkTo(descriptionAndImage.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(link.top)
        }
        DevicesProgressFooter(link = link, onCLick = onLinkCLick) {
            top.linkTo(timer.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.top)
        }
    }
}

@Composable
private fun ConstraintLayoutScope.DevicesProgressHeader(
    descriptionAndImage: ConstrainedLayoutReference,
    isLoading: Boolean,
    constrainBlock: ConstrainScope.() -> Unit,
) {
    val textRes = if (isLoading) R.string.device_searching else R.string.device_error_not_found
    val text = stringResource(textRes)

    Column(modifier = Modifier.Companion.constrainAs(descriptionAndImage, constrainBlock)) {
        Text(
            text = text,
            style = AppTheme.typography.headlineSmall,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 60.dp),
        )
        Image(
            imageVector = Icons.DevicesNotAvailable,
            contentDescription = text,
            modifier = Modifier.Companion
                .padding(horizontal = 24.dp)
                .padding(top = 56.dp),
        )
    }
}

@Composable
private fun ConstraintLayoutScope.DevicesProgressTimer(
    timer: ConstrainedLayoutReference,
    state: DevicesProgressState,
    constrainBlock: ConstrainScope.() -> Unit,
) {
    val height = 56.dp
    val timerModifier = Modifier.Companion
        .constrainAs(timer, constrainBlock)
        .padding(4.dp)

    if (state.isLoading) {
        ColoredCircularProgressIndicator(modifier = timerModifier, size = height)
    } else {
        Text(
            text = state.timerValue,
            modifier = timerModifier.height(height),
            style = AppTheme.typography.headlineSmall,
        )
    }
}

@Composable
private fun ConstraintLayoutScope.DevicesProgressFooter(
    link: ConstrainedLayoutReference,
    onCLick: () -> Unit,
    constrainBlock: ConstrainScope.() -> Unit,
) {
    ButtonComponent(
        text = stringResource(R.string.device_error_more),
        style = AppTheme.typography.link,
        colors = AppTheme.colors.transparentButtonColors,
        onClick = onCLick,
        modifier = Modifier
            .constrainAs(link, constrainBlock)
            .padding(bottom = 20.dp),
    )
}

private class DeviceProgressProvider : PreviewParameterProvider<DevicesProgressState> {

    override val values = sequenceOf(
        DevicesProgressState(5.seconds),
        DevicesProgressState(),
    )
}
