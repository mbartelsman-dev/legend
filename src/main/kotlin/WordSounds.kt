/**
 * Class that represents a word of unobserved phonemic values
 */
class WordSounds : CollapsibleCollection<Long>() {

    override val domain: List<QuantumState<Long>>
        get() = TODO("Not yet implemented")

    override var collapsed: Boolean = false
        get() = TODO("Not yet implemented")
        private set

    override fun collapseNext(): Boolean {
        TODO("Not yet implemented")
    }

    override fun applyRestrictions(element: Long, index: Int): Boolean {
        TODO("Not yet implemented")
    }

}