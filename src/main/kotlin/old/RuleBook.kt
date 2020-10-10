package old

/**
 * Class describing the rules for word construction
 * @property set The set of all possible allowed values that a single element in a word can have
 */
class RuleBook<V>(
        val set: Map<V, Double>,
        private val rules: Map<V, Rules<V>>
) {

    /**
     * Find the rules that apply to a particular value
     * @param value to look up rules for
     * @return a [Rules] object that applies to the given value
     */
    operator fun get(value: V): Rules<V>? = rules[value]
}

data class Rules<V>(val index: Int, val list: List<Map<V, Double>>) {
    val scope: Pair<Int, Int> = Pair(index, list.size - index)
}