/**
 * Abstract class for [Collapsible] objects made up of a sequence of [Collapsible] values
 * @property domain list of [Collapsible] values
 */
abstract class CollapsibleCollection<E : Collapsible<E>, T : CollapsibleCollection<E, T>> : Collapsible<T> {

    abstract val domain: List<E>

    /**
     * Collapses the next element in the [domain], what constitutes as "next" is up to the implementation
     */
    abstract fun collapseNext(): Boolean;

    /**
     * Propagates restrictions in the [domain]
     */
    abstract fun applyRestriction(): Boolean

    override fun observe(): T {
        while (domain.any { !it.collapsed }) collapseNext()
        return this as T
    }
}