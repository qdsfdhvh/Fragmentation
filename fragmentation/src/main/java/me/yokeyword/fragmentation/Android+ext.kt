package me.yokeyword.fragmentation

/**
 * Faster lazy delegation for Android.
 * Warning: Only use for objects accessed on main thread
 */
internal fun <T> lazyAndroid(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)