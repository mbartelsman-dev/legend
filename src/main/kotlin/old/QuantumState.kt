package old

import old.util.chooseWeighted
import kotlin.math.ln

/**
 * A class that holds a single [Collapsible] element
 * @property entropy A non-negative [Double] representing the entropy of the system. This is `0.0` if observed.
 * @property values A collection of the superposed states
 * and contains a immutable value otherwise
 */
open class QuantumState<T>(
        private var values: Map<T, Double> = emptyMap()
) : Collapsible<T> {

    private var _isCollapsed: Boolean = false
    override val collapsed: Boolean
        get() = _isCollapsed

    val entropy: Double
        get() = if (collapsed) 0.0 else calculateEntropy()

    private val value: T by lazy {
        _isCollapsed = true
        values.chooseWeighted()
    }

    override fun collapseAndGet(): T = value

    /**
     * Calculates the entropy of the available superpositions
     * @return non-negative entropy value
     */
    private fun calculateEntropy(): Double {
        check(values.values.all { it >= 0.0 }) { "Negative weight found. All weights for a old.QuantumState must be non-negative." }
        val weights = values.values
        val sumN = weights.sum()
        val sumNLogN = weights.sumByDouble { it * ln(it) }
        val result = ln(sumN) - (sumNLogN / sumN)
        assert(result >= 0.0) { "Entropy must be non-negative, found $result" }
        return result
    }

    fun restrictWith(rule: Map<T, Double>): Boolean {
        values = values.keys.intersect(rule.keys)
                .map { Pair(it, values.getValue(it) + rule.getValue(it)) }
                .toMap()
        return values.isNotEmpty()
    }
}