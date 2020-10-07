/**
 * A class that holds a single [Collapsible] element
 * @property entropy A non-negative [Double] representing the entropy of the system. This is `0.0` if observed.
 * @property values A collection of the superposed states
 * @property observedValue stores the chosen observed value. It has the invariant that it is `null` when [collapsed] is `false`
 * and contains a immutable value otherwise
 */
class QuantumState<T> : Collapsible<T> {
    val entropy: Double = 1.0
    private val values: Map<T, Double> = emptyMap()
    private var observedValue: T? = null

    override var collapsed: Boolean = false
        get() = TODO("Not yet implemented")
        private set

    override fun observe(): T {
        if (!collapsed) {
            TODO("Not yet implemented")
        }
        return observedValue!!
    }
}