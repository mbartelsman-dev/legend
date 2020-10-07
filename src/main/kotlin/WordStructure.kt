/**
 * Class that represents a word of unobserved syllable structure values
 */
class WordStructure : CollapsibleCollection<Long>() {

    override val domain: MutableList<QuantumState<Long>>
        get() = TODO("Not yet implemented")

    override var collapsed: Boolean = false
        get() = TODO("Not yet implemented")
        private set

    /**
     * Expands the size of the domain to accommodate all possible applicable restrictions
     */
    fun expandDomain() {
        TODO("Not yet implemented")
    }

    override fun collapseNext(): Boolean {
        TODO("Not yet implemented")
    }

    override fun applyRestriction(): Boolean {
        TODO("Not yet implemented")
    }
}