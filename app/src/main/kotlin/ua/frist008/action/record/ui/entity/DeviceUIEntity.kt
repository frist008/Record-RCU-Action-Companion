package ua.frist008.action.record.ui.entity

import androidx.compose.runtime.Stable
import ua.frist008.action.record.ui.entity.base.UIEntity

@Stable data class DeviceUIEntity(
    val type: String,
    val name: String,
    val address: String,
    val status: Boolean,
) : UIEntity
