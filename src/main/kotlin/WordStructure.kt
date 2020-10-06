class WordStructure : CollapsibleCollection<QuantumState<Long>, WordStructure>() {

    override val domain: MutableList<QuantumState<Long>>
        get() = TODO("Not yet implemented")

    override var collapsed: Boolean = false
        get() = TODO("Not yet implemented")
        private set

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