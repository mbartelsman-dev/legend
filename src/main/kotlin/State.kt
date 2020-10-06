import util.calculateEntropy
import util.chooseWeighted

/**
 * Group of classes for handling states that can either be unobserved and consist of a superposition of values,
 * or are observe and have "collapsed" into a single, invariable value
 */
sealed class State<T> {
    /**
     * @return `true` if there are any remaining valid values, `false` otherwise
     */
    abstract fun restrict(syl: T): Boolean
}

/**
 * Represents an unobserved state that holds a superposition of values with weights
 */
class UnobservedState<T>() : State<T>() {
    private var outdatedEntropy: Boolean = false
    private val variants: MutableMap<T, Double> = mutableMapOf()
        get() { outdatedEntropy = true; return field }
    var entropy: Double = 1.0
        get() {
            if (outdatedEntropy) {
                field = variants.values.calculateEntropy()
                outdatedEntropy = false
            }
            return field
        }

    constructor(rules: Rules<T>) : this() {
        variants.putAll(rules.syllableSet)
    }

    fun observe(): ObservedState<T> {
        return ObservedState(variants.chooseWeighted())
    }

    override fun restrict(syl: T): Boolean {
        TODO("Not yet implemented")
    }
}

data class ObservedState<T>(val value: T) : State<T>() {
    override fun restrict(syl: T): Boolean {
        TODO("Not yet implemented")
    }
}