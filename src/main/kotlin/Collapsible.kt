/**
 * Interface representing an object that holds a superposition of possible values until observed.
 * The value of Collapsible objects should be immutable after observation
 * @property collapsed `true` if the object has been previously observed, otherwise `false`
 */
interface Collapsible<T : Collapsible<T>> {
    val collapsed: Boolean

    /**
     * On first call, it collapses the state of the object and returns it.
     * On subsequent calls it does nothing but return the object itself
     */
    fun observe(): T
}