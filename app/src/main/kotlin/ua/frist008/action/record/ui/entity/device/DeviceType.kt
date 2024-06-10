package ua.frist008.action.record.ui.entity.device

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import ua.frist008.action.record.R
import ua.frist008.action.record.ui.svg.ComputerOff
import ua.frist008.action.record.ui.svg.ComputerOn

@Immutable enum class DeviceType(
    val image: ImageVector,
    @StringRes val description: Int,
) {

    ONLINE_PC(Icons.ComputerOn, R.string.device_available),
    OFFLINE_PC(Icons.ComputerOff, R.string.device_not_available),
    ;
}
