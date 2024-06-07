package ua.frist008.action.record.ui.entity.device

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.core.content.res.ResourcesCompat
import ua.frist008.action.record.R

@Immutable enum class DeviceType(
    @DrawableRes val imageRes: Int,
    @StringRes val description: Int,
) {

    // TODO Replace to real image
    ONLINE_PC(ResourcesCompat.ID_NULL, R.string.device_available),
    OFFLINE_PC(ResourcesCompat.ID_NULL, R.string.device_not_available),
    ;
}
