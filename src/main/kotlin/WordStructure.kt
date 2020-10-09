import kotlin.math.max

/**
 * Class that represents a word of unobserved syllable structure values
 */
class WordStructure(
        private val rules: RuleBook<Long>
) : CollapsibleCollection<Long>() {

    override val domain: MutableList<QuantumState<Long>> = mutableListOf(QuantumState(rules.set))

    /**
     * Expands the size of the domain to accommodate all possible applicable restrictions
     * @return the new value for [index] after the expansion
     */
    private fun expandDomain(left: Int, index: Int, right: Int): Int {
        repeat(left - index) { domain.add(0, QuantumState(rules.set)) }
        repeat(index + right - domain.size) { domain.add(QuantumState(rules.set)) }
        return index + max(left - index, 0)
    }

    override fun collapseNext(): Boolean {
        val indexedState = domain
                .withIndex()
                .filterNot { it.value.collapsed }
                .minByOrNull { it.value.entropy } ?:
            throw Exception("Can't collapseNext(), there are no more collapsible values")

        val (left, right) = rules[indexedState.value.collapseAndGet()]?.scope ?:
            throw Exception("No rule for object $indexedState!!.value.collapseAndGet()")

        val newIndex = expandDomain(left, indexedState.index, right)
        return applyRestrictions(indexedState.value.collapseAndGet(), newIndex)
    }

    override fun applyRestrictions(element: Long, index: Int): Boolean {
        val (left, right) = rules[element]!!.scope

        var conflicting = false
        for (d in index - left..index + right) {
            val r = d - (index - left)
            conflicting = conflicting || !domain[d].restrictWith( rules[element]!!.list[r] )
        }
        return conflicting
    }
}
