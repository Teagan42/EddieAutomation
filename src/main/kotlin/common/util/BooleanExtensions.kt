package common.util

inline fun Boolean.ifTrue(block: () -> Unit): Unit = if (this) block() else Unit

inline fun <R> Boolean.ifTrueMaybe(block: () -> R): R? = if (this) block() else null

inline fun Boolean.ifTrueAlso(block: () -> Unit): Boolean = this.ifTrueMaybe { this.also { block() } } ?: this

inline fun Boolean.ifFalse(block: () -> Unit): Unit = if (!this) block() else Unit

inline fun <R> Boolean.ifFalseMaybe(block: () -> R): R? = if (!this) block() else null

inline fun Boolean.ifFalseAlso(block: () -> Unit): Boolean = this.ifFalseMaybe { this.also { block() } } ?: this