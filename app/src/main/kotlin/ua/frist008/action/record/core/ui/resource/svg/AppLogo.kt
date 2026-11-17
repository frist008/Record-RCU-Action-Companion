package ua.frist008.action.record.core.ui.resource.svg

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.frist008.action.record.core.ui.resource.Icons

/** The launcher icon, both layers of it, cut to the safe zone the launchers always show */
val Icons.AppLogo: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "AppLogo",
        defaultWidth = 72.dp,
        defaultHeight = 72.dp,
        viewportWidth = 1080f,
        viewportHeight = 1080f,
    ).apply {
        group(translationX = -270f, translationY = -270f) {
            group(
                scaleX = 0.8f,
                scaleY = 0.8f,
                translationX = 162f,
                translationY = 162f,
            ) {
                path(
                    fill = Brush.linearGradient(
                        colorStops = *arrayOf(0f to Color(0xFF1ABC9C), 1f to Color(0xFF31685D)),
                        start = Offset(810f, 0f),
                        end = Offset(810f, 1620f),
                    ),
                ) {
                    moveTo(0f, 0f)
                    horizontalLineToRelative(1620f)
                    verticalLineToRelative(1620f)
                    horizontalLineToRelative(-1620f)
                    close()
                }
                path(
                    fill = Brush.linearGradient(
                        colorStops = *arrayOf(0f to Color(0xFF273D39), 1f to Color(0xD6127C68)),
                        start = Offset(1133.2f, 1613.2f),
                        end = Offset(1133.2f, 947f),
                    ),
                    fillAlpha = 0.63f,
                ) {
                    moveToRelative(1304.9f, 909f)
                    lineToRelative(-5.5f, -3.8f)
                    horizontalLineToRelative(0f)
                    reflectiveCurveToRelative(-362.2f, -254.6f, -362.2f, -254.6f)
                    lineToRelative(-20.1f, -14.1f)
                    curveToRelative(-57f, -35.6f, -133.9f, -50.1f, -182.8f, -49.4f)
                    lineToRelative(-86.3f, 3f)
                    lineToRelative(-1.7f, 461.5f)
                    lineToRelative(92.9f, 65.3f)
                    lineToRelative(218.9f, 153.9f)
                    lineToRelative(28.9f, 20.3f)
                    lineToRelative(468f, 329f)
                    horizontalLineToRelative(165f)
                    verticalLineToRelative(-490f)
                    lineToRelative(-315.1f, -221f)
                    close()
                    moveTo(753.7f, 675f)
                    lineToRelative(167.5f, 94.6f)
                    curveToRelative(1.7f, 1f, 1.6f, 3.4f, 0f, 4.4f)
                    lineToRelative(-165.5f, 97.8f)
                    curveToRelative(-1.8f, 1f, -4f, -0.3f, -3.9f, -2.3f)
                    lineToRelative(-2f, -192.4f)
                    curveToRelative(0.1f, -2f, 2.2f, -3.1f, 3.9f, -2.1f)
                    close()
                }
            }
            group(
                scaleX = 0.76521736f,
                scaleY = 0.76521736f,
                translationX = 190.17392f,
                translationY = 184.43478f,
            ) {
                path(
                    fill = SolidColor(Color(0xFFFFF1F0)),
                ) {
                    moveToRelative(991.7f, 750.4f)
                    curveToRelative(-1.5f, -42.3f, -23.3f, -75.1f, -54.5f, -99.9f)
                    curveToRelative(-6.3f, -5f, -13f, -9.7f, -20.1f, -14.1f)
                    curveToRelative(-57f, -35.6f, -133.9f, -50.1f, -182.8f, -49.4f)
                    lineToRelative(-86.3f, 3f)
                    lineToRelative(-1.7f, 461.5f)
                    lineToRelative(-0.3f, 67.7f)
                    lineToRelative(93.1f, -2.4f)
                    horizontalLineToRelative(0.2f)
                    reflectiveCurveToRelative(5.2f, -139.4f, 5.2f, -139.4f)
                    lineToRelative(190.4f, 139.8f)
                    lineToRelative(53.1f, -64.1f)
                    lineToRelative(-148.8f, -134.6f)
                    reflectiveCurveToRelative(156.9f, -38.8f, 152.3f, -168.1f)
                    close()
                    moveTo(751.7f, 869.4f)
                    lineToRelative(-2f, -192.4f)
                    curveToRelative(0.1f, -2f, 2.2f, -3.1f, 3.9f, -2.1f)
                    lineToRelative(167.5f, 94.6f)
                    curveToRelative(1.7f, 1f, 1.6f, 3.4f, 0f, 4.4f)
                    lineToRelative(-165.5f, 97.8f)
                    curveToRelative(-1.8f, 1f, -4f, -0.3f, -3.9f, -2.3f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFFFFF1F0)),
                ) {
                    moveToRelative(1126.5f, 629.7f)
                    lineToRelative(117.9f, -69.1f)
                    curveToRelative(15.2f, 24.4f, 32.2f, 56.3f, 46.1f, 95.3f)
                    curveToRelative(15.8f, 44.2f, 23f, 84f, 26.2f, 115.1f)
                    curveToRelative(-49.3f, 4.1f, -98.5f, 8.2f, -147.7f, 12.3f)
                    curveToRelative(-1.2f, -20.8f, -4.4f, -46.3f, -11.9f, -74.6f)
                    curveToRelative(-8.5f, -32.3f, -20.1f, -58.8f, -30.6f, -79f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFFFFF1F0)),
                    fillAlpha = 0.96f,
                ) {
                    moveToRelative(995.2f, 481.1f)
                    curveToRelative(21.7f, -35.4f, 43.4f, -70.9f, 65.2f, -106.3f)
                    curveToRelative(26.8f, 15.5f, 59.3f, 37.6f, 92.5f, 68.5f)
                    curveToRelative(30.3f, 28.1f, 53.1f, 56.1f, 69.9f, 79.8f)
                    curveToRelative(-38f, 24.9f, -76f, 49.8f, -113.9f, 74.7f)
                    curveToRelative(-11.5f, -18.4f, -27.4f, -40.3f, -48.9f, -62.7f)
                    curveToRelative(-23f, -23.9f, -45.8f, -41.5f, -64.7f, -54.1f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFFFFF1F0)),
                    fillAlpha = 0.9f,
                ) {
                    moveToRelative(1016.9f, 351.8f)
                    lineToRelative(-53.5f, 110.4f)
                    curveToRelative(-20.1f, -10.9f, -47.2f, -23.1f, -80.7f, -31.8f)
                    curveToRelative(-30.5f, -7.9f, -57.6f, -10.8f, -79.1f, -11.5f)
                    curveToRelative(0.6f, -37.5f, 1.3f, -75.1f, 1.9f, -112.6f)
                    curveToRelative(30.4f, -0.3f, 69.3f, 2.2f, 113.3f, 12.1f)
                    curveToRelative(39.4f, 8.9f, 72.4f, 21.5f, 98f, 33.3f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFFFFF1F0)),
                    fillAlpha = 0.85f,
                ) {
                    moveToRelative(769.3f, 417f)
                    curveToRelative(-23.6f, 0.7f, -53.3f, 3.6f, -86.7f, 12f)
                    curveToRelative(-15.2f, 3.8f, -29.2f, 8.3f, -41.9f, 13f)
                    curveToRelative(-12.9f, 4.8f, -24.4f, 9.8f, -34.6f, 14.7f)
                    curveToRelative(-10.9f, -20.7f, -21.8f, -41.3f, -32.7f, -62f)
                    curveToRelative(-4.7f, -8.9f, -9.3f, -17.7f, -14f, -26.5f)
                    curveToRelative(24.4f, -13.9f, 56.6f, -29.2f, 95.8f, -41.2f)
                    curveToRelative(42.2f, -12.9f, 79.8f, -18f, 109.2f, -19.9f)
                    lineToRelative(4.8f, 109.9f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFFFFF1F0)),
                    fillAlpha = 0.8f,
                ) {
                    moveToRelative(563.7f, 482f)
                    curveToRelative(-19.4f, 12.1f, -42.7f, 28.9f, -66.4f, 51.8f)
                    curveToRelative(-24.7f, 23.8f, -42.8f, 47.4f, -55.8f, 67f)
                    curveToRelative(-20.9f, -12.6f, -41.7f, -25.2f, -62.6f, -37.8f)
                    curveToRelative(-4.1f, -2.5f, -8.1f, -4.9f, -12.2f, -7.4f)
                    curveToRelative(14.4f, -25f, 35f, -55.6f, 63.8f, -86.9f)
                    curveToRelative(15.1f, -16.4f, 30.2f, -30.5f, 44.5f, -42.5f)
                    curveToRelative(11.9f, -10f, 23.4f, -18.7f, 33.8f, -26f)
                    lineToRelative(54.8f, 81.8f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFFFFF1F0)),
                    fillAlpha = 0.75f,
                ) {
                    moveToRelative(414.1f, 648.2f)
                    curveToRelative(-10.8f, 21.7f, -22.4f, 50f, -30.9f, 84.3f)
                    curveToRelative(-7.8f, 31.7f, -10.9f, 60.1f, -11.7f, 83.1f)
                    horizontalLineToRelative(-74.8f)
                    curveToRelative(-0.1f, -3.3f, -0.3f, -6.7f, -0.3f, -10.1f)
                    curveToRelative(-0.5f, -23.9f, 0.8f, -52.3f, 6.2f, -83.9f)
                    curveToRelative(5.3f, -30.9f, 13.2f, -58f, 21.7f, -80.8f)
                    curveToRelative(4.1f, -10.9f, 8.2f, -20.9f, 12.3f, -29.8f)
                    lineToRelative(6.5f, 3.1f)
                    lineToRelative(71.2f, 34.2f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFFFFF1F0)),
                    fillAlpha = 0.7f,
                ) {
                    moveToRelative(297.9f, 861f)
                    lineToRelative(72.4f, -3.1f)
                    curveToRelative(0.6f, 24.8f, 3.6f, 56.2f, 12.6f, 91.5f)
                    curveToRelative(8f, 31.6f, 18.7f, 58.1f, 28.8f, 78.9f)
                    curveToRelative(-18.5f, 9.7f, -36.9f, 19.4f, -55.4f, 29.1f)
                    curveToRelative(-12.8f, -22.2f, -26.9f, -50.9f, -38f, -85.7f)
                    curveToRelative(-13.8f, -42.9f, -18.7f, -81.3f, -20.3f, -110.6f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFFFFF1F0)),
                    fillAlpha = 0.65f,
                ) {
                    moveToRelative(393.8f, 1116.8f)
                    lineToRelative(49f, -33.4f)
                    curveToRelative(13.4f, 20.6f, 31.9f, 45.2f, 56.8f, 70.2f)
                    curveToRelative(24.6f, 24.6f, 48.8f, 42.9f, 69f, 56.2f)
                    curveToRelative(-8.7f, 14.1f, -17.4f, 28.3f, -26f, 42.4f)
                    curveToRelative(-22.9f, -13f, -50.3f, -31.2f, -78.6f, -56.3f)
                    curveToRelative(-31.3f, -27.9f, -54.1f, -55.9f, -70.3f, -79f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFFFFF1F0)),
                    fillAlpha = 0.6f,
                ) {
                    moveToRelative(616.2f, 1237.6f)
                    curveToRelative(21.8f, 11.7f, 50.4f, 24.6f, 85.4f, 34.4f)
                    curveToRelative(34f, 9.5f, 64.3f, 13.4f, 88.5f, 14.8f)
                    curveToRelative(-0.3f, 12.8f, -0.5f, 25.6f, -0.7f, 38.3f)
                    curveToRelative(-28f, -0.2f, -63.9f, -2.9f, -104.4f, -12.8f)
                    curveToRelative(-36.6f, -8.9f, -67f, -21.1f, -90.6f, -32.6f)
                    curveToRelative(7.3f, -14.1f, 14.5f, -28.1f, 21.8f, -42.2f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFFFFF1F0)),
                    fillAlpha = 0.5f,
                ) {
                    moveToRelative(855.7f, 1287.8f)
                    lineToRelative(2.6f, 34.8f)
                    curveToRelative(28.7f, -3.1f, 64.2f, -9.5f, 103.4f, -22.4f)
                    curveToRelative(33.8f, -11.2f, 62.2f, -24.6f, 85f, -37.2f)
                    curveToRelative(-4.1f, -7.3f, -8.3f, -14.5f, -12.4f, -21.7f)
                    curveToRelative(-22.9f, 11.3f, -52.5f, 23.5f, -87.9f, 32.7f)
                    curveToRelative(-34.5f, 8.9f, -65.5f, 12.6f, -90.6f, 13.9f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFFFFF1F0)),
                    fillAlpha = 0.45f,
                ) {
                    moveToRelative(1081.1f, 1215.1f)
                    lineToRelative(11.4f, 19.5f)
                    curveToRelative(23.4f, -15.2f, 52f, -36.9f, 80.6f, -67f)
                    curveToRelative(24.3f, -25.6f, 42.4f, -50.7f, 55.6f, -71.9f)
                    curveToRelative(-3.5f, -2.4f, -7f, -4.8f, -10.5f, -7.1f)
                    curveToRelative(-14.8f, 20.3f, -34.3f, 43.6f, -59.4f, 67.3f)
                    curveToRelative(-27.7f, 26f, -54.8f, 45.3f, -77.7f, 59.3f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFFFFF1F0)),
                    fillAlpha = 0.4f,
                ) {
                    moveToRelative(1264f, 1006.4f)
                    lineToRelative(9.2f, 3.7f)
                    curveToRelative(9.3f, -19.6f, 18.8f, -44f, 25.9f, -72.8f)
                    curveToRelative(10.9f, -44.6f, 12f, -83.8f, 10.4f, -112.7f)
                    curveToRelative(-0.9f, 27.5f, -4.6f, 62.4f, -15.1f, 101.4f)
                    curveToRelative(-8.6f, 31.9f, -19.7f, 58.8f, -30.3f, 80.3f)
                    close()
                }
            }
        }
    }.build()
}

@Preview
@Composable
private fun AppLogoPreview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.AppLogo, contentDescription = null)
    }
}