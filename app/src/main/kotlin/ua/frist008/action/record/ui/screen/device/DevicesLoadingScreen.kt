package ua.frist008.action.record.ui.screen.device

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
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
import ua.frist008.action.record.ui.component.ColoredCircularProgressIndicator
import ua.frist008.action.record.ui.entity.device.DeviceLoadingState
import ua.frist008.action.record.ui.svg.DevicesNotAvailable
import ua.frist008.action.record.ui.theme.color.Palette
import ua.frist008.action.record.ui.theme.typography.Typography
import ua.frist008.action.record.ui.theme.typography.link
import ua.frist008.action.record.util.extension.ui.clickable

@Preview(
    showSystemUi = true,
    showBackground = true,
    backgroundColor = Palette.PURPLE_LIGHT_LONG,
)
@Composable
fun DevicesLoadingScreen(
    @PreviewParameter(DeviceProgressProvider::class) state: DeviceLoadingState,
    onSurfaceClick: (DeviceLoadingState) -> Unit = {},
) {
    // Example of use ConstraintLayout. Can be optimized to Column
    ConstraintLayout(
        modifier = Modifier
            .clickable(isRippleEnabled = false) { onSurfaceClick(state) }
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
    ) {
        val (descriptionAndImage, timer, link) = createRefs()

        createVerticalChain(descriptionAndImage, timer, link, chainStyle = ChainStyle.SpreadInside)

        DevicesLoadingHeader(
            descriptionAndImage = descriptionAndImage,
            isLoading = state.isLoading,
        ) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(timer.top)
        }
        DevicesLoadingTimer(timer = timer, state = state) {
            top.linkTo(descriptionAndImage.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(link.top)
        }
        DevicesLoadingFooter(link = link) {
            top.linkTo(timer.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.top)
        }
    }
}

@Composable
private fun ConstraintLayoutScope.DevicesLoadingHeader(
    descriptionAndImage: ConstrainedLayoutReference,
    isLoading: Boolean,
    constrainBlock: ConstrainScope.() -> Unit,
) {
    val textRes = if (isLoading) R.string.device_searching else R.string.device_error_not_found
    val text = stringResource(textRes)

    Column(modifier = Modifier.Companion.constrainAs(descriptionAndImage, constrainBlock)) {
        Text(
            text = text,
            style = Typography.headlineSmall,
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

// TODO Add swipe to refresh
@Composable
private fun ConstraintLayoutScope.DevicesLoadingTimer(
    timer: ConstrainedLayoutReference,
    state: DeviceLoadingState,
    constrainBlock: ConstrainScope.() -> Unit,
) {
    val timerModifier = Modifier.Companion
        .constrainAs(timer, constrainBlock)
        .padding(4.dp)

    if (state.isLoading) {
        ColoredCircularProgressIndicator(modifier = timerModifier, sizeDp = 56)
    } else {
        Text(
            text = state.timerValue,
            modifier = timerModifier,
            maxLines = 1,
            style = Typography.headlineSmall,
        )
    }
}

@Composable
private fun ConstraintLayoutScope.DevicesLoadingFooter(
    link: ConstrainedLayoutReference,
    constrainBlock: ConstrainScope.() -> Unit,
) {
    ClickableText(
        text = AnnotatedString(stringResource(R.string.device_error_more)),
        style = Typography.link,
        modifier = Modifier.Companion
            .constrainAs(link, constrainBlock)
            .padding(bottom = 16.dp),
    ) {
        // TODO open chrome tabs with link
    }
}

private class DeviceProgressProvider : PreviewParameterProvider<DeviceLoadingState> {

    override val values = sequenceOf(
        DeviceLoadingState("5", false),
        DeviceLoadingState("5", true),
        DeviceLoadingState("", false),
        DeviceLoadingState(),
    )
}
