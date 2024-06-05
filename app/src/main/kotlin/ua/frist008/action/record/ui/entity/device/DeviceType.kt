package ua.frist008.action.record.ui.entity.device

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable enum class DeviceType(val image: ImageVector?) {

    ONLINE_PC(null),
    OFFLINE_PC(null),
    ;
}
