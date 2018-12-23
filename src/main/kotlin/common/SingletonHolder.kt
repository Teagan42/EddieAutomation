package common

open class SingletonHolder<out TYPE, in ARGUMENT>(creator: (ARGUMENT) -> TYPE) {
    private var creator: ((ARGUMENT) -> TYPE)? = creator
    @Volatile private var instance: TYPE? = null

    fun getInstance(arg: ARGUMENT): TYPE {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}