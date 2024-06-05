package ua.frist008.action.record.ui.entity.base

import androidx.compose.runtime.Stable

@Stable class UIState<Entity : UIEntity, Error : Exception> private constructor(
    val entity: Entity? = null,
    val cause: Error? = null,
    val isLoading: Boolean = entity == null && cause == null,
) {

    constructor(entity: Entity) : this(entity = entity, cause = null)
    constructor(cause: Error) : this(entity = null, cause = cause)
    constructor() : this(entity = null, cause = null)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UIState<*, *>) return false

        if (entity != other.entity) return false
        if (cause != other.cause) return false
        if (isLoading != other.isLoading) return false

        return true
    }

    override fun hashCode(): Int {
        var result = entity?.hashCode() ?: 0
        result = 31 * result + (cause?.hashCode() ?: 0)
        result = 31 * result + isLoading.hashCode()
        return result
    }
}
