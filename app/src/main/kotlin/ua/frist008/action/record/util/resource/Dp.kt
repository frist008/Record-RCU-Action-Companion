package ua.frist008.action.record.util.resource

import androidx.annotation.Dimension
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.annotation.AnnotationTarget.PROPERTY_SETTER
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

@Retention(AnnotationRetention.BINARY)
@Target(
    FIELD,
    PROPERTY,
    FUNCTION,
    VALUE_PARAMETER,
    PROPERTY_GETTER,
    PROPERTY_SETTER,
)
@Dimension(unit = Dimension.DP)
annotation class Dp
