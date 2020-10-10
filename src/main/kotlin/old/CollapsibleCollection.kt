package old

/**
 * Abstract class for [Collapsible] objects made up of a collection of [Collapsible] values
 * @property domain list of [Collapsible] values representing the state of this class
 */
abstract class CollapsibleCollection<V> : Collapsible<Collection<V>> {
    abstract val domain: List<Collapsible<V>>

    private val value: Collection<V> by lazy {
        _isCollapsed = true
        observe()
    }

    private var _isCollapsed: Boolean = false
    override val collapsed: Boolean
        get() = _isCollapsed

    override fun collapseAndGet(): Collection<V> = value

    /**
     * Collapses the next element in the [domain], what constitutes as "next" is up to the implementation
     * @return `false` if collapsing the element results in a contradiction, `true`otherwise // TODO: Maybe implement backtracking here?
     */
    abstract fun collapseNext(): Boolean;

    /**
     * Propagates restrictions in the [domain]
     * @param element object to look up the restrictions for
     * @param index index of the element within the domain
     * @return `false` if the restrictions result in a contradiction, `true`otherwise
     */
    abstract fun applyRestrictions(element: V, index: Int): Boolean

    /**
     * Observes the object, collapsing the entire collection in order
     * @return this
     */
     private fun observe(): List<V> {
        while (domain.any { !it.collapsed }) collapseNext()
        return domain.map { it.collapseAndGet() }
    }
}