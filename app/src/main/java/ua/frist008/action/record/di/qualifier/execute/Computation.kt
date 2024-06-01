package ua.frist008.action.record.di.qualifier.execute

import javax.inject.Qualifier

@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER,
)
@Qualifier
@MustBeDocumented
annotation class Computation
