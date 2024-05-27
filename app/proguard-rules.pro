## Common
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes *Annotation*
-keep class kotlin.Metadata { *; }
-dontwarn javax.annotation.**
-dontwarn kotlin.reflect.jvm.internal.**
-dontwarn java.lang.ClassValue
-dontnote kotlin.internal.PlatformImplementationsKt

## Remove logs
-assumenosideeffects class android.util.Log* {
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** d(...);
}
-assumenosideeffects class timber.log.Timber* {
    public static *** tag(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** d(...);
}

# Kotlin serializer
-dontnote kotlinx.serialization.SerializationKt

# Crashlytics
-keepattributes SourceFile,LineNumberTable
