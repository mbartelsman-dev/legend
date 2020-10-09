/**
 * Interface representing an object that holds a superposition of possible values until observed.
 * The value of Collapsible objects should be immutable after observation
 * @property collapsed `true` if the object has been previously observed, otherwise `false`
 * @property value collpases the object and returns a single, definite value
 */
interface Collapsible<V> {
    val collapsed: Boolean

    /**
     * On first call, it collapses the object and returns a single, definite value.
     * Following executions will return the value without any added effects
     * @return the collapsed value
     */
    fun collapseAndGet(): V
}