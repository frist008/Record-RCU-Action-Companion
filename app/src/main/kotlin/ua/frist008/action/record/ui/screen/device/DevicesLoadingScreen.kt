package ua.frist008.action.record.ui.screen.device

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import ua.frist008.action.record.ui.entity.device.DeviceProgressState
import ua.frist008.action.record.ui.svg.DevicesNotAvailable
import ua.frist008.action.record.ui.theme.color.Palette
import ua.frist008.action.record.ui.theme.typography.Typography
import ua.frist008.action.record.ui.theme.typography.link

@Preview(
    showSystemUi = true,
    showBackground = true,
    backgroundColor = Palette.PURPLE_LIGHT_LONG,
)
@Composable
fun DevicesLoadingScreen(
    @PreviewParameter(DeviceProgressProvider::class) state: DeviceProgressState,
    innerPadding: PaddingValues = PaddingValues(),
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
    ) {
        val (image, description, timer, link) = createRefs()

        createVerticalChain(image, description, timer, link, chainStyle = ChainStyle.SpreadInside)

        DevicesLoadingHeader(image = image) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(description.top)
        }
        DevicesLoadingMessage(description = description) {
            top.linkTo(image.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(timer.top)
        }
        DevicesLoadingTimer(timer = timer, state = state) {
            top.linkTo(description.top)
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
    image: ConstrainedLayoutReference,
    constrainBlock: ConstrainScope.() -> Unit,
) {
    Image(
        imageVector = Icons.DevicesNotAvailable,
        contentDescription = stringResource(R.string.device_error_not_found),
        modifier = Modifier.Companion
            .constrainAs(image, constrainBlock)
            .padding(horizontal = 20.dp)
            .padding(top = 30.dp),
    )
}

@Composable
private fun ConstraintLayoutScope.DevicesLoadingMessage(
    description: ConstrainedLayoutReference,
    constrainBlock: ConstrainScope.() -> Unit,
) {
    Text(
        text = stringResource(R.string.device_error_not_found),
        style = Typography.headlineSmall,
        modifier = Modifier.Companion.constrainAs(description, constrainBlock),
    )
}

@Composable
private fun ConstraintLayoutScope.DevicesLoadingTimer(
    timer: ConstrainedLayoutReference,
    state: DeviceProgressState,
    constrainBlock: ConstrainScope.() -> Unit,
) {
    val timerModifier = Modifier.Companion
        .constrainAs(timer, constrainBlock)
        .padding(4.dp)

    if (state.isLoading) {
        CircularProgressIndicator(
            modifier = timerModifier.width(64.dp),
            color = Palette.WHITE_LIGHT,
            trackColor = Palette.WHITE_DARK,
        )
    } else {
        ClickableText(
            text = AnnotatedString(state.timerValue),
            style = Typography.headlineSmall,
            maxLines = 1,
            modifier = timerModifier,
        ) {
            // TODO force reconnection (may not needs)
        }
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
            .padding(bottom = 30.dp),
    ) {
        // TODO open chrome tabs with link
    }
}

private class DeviceProgressProvider : PreviewParameterProvider<DeviceProgressState> {

    override val values = sequenceOf(
        DeviceProgressState("5", false),
        DeviceProgressState("5", true),
        DeviceProgressState("", false),
    )
}
