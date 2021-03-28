package at.cnoize.util

fun <T> Iterable<T>.toSingleOrNull(): T? = this.toSet().singleOrNull()
