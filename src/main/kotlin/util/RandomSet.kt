import java.util.*

/**
 * A weighted set (a map from the key to their weights) from which a random element can be selected. It can only
 * be instantiated once, as element weights are otherwise meaningless
 * @property values Returns a [MutableCollection] of doubles containing the normalized weights for all elements
 */
class RandomSet<E>(
        private val map: MutableMap<E, Double> = mutableMapOf<E, Double>(),
        private val random: Random = Random(),
) {

    private var total = 0.0

    val size: Int
        get() = map.size
    val entries: MutableSet<Pair<E, Double>>
        get() = map.asIterable().map { Pair(it.key, it.value) }.toMutableSet()
    val elements: MutableSet<E>
        get() = map.keys
    val weights: MutableCollection<Double>
        get() = map.values.map { it / total }.toMutableList()

    /**
     * Returns a random weighted element
     */
    fun choose(): E {
        val rand = random.nextDouble() * total
        var cdf = 0.0
        return map
                .toList()
                .map { cdf += it.second; Pair(it.first, cdf) }
                .let { list -> list[list.binarySearch { it.second.compareTo(rand) }] }
                .first
    }

    fun fillWith(from: Map<out E, Double>) {
        if (map.isNotEmpty()) {
            throw Exception("Cannot populate the RandomSet, it has already been populated")
        } else {
            total += from.values.sum()
            return map.putAll(from)
        }
    }

    fun isEmpty(): Boolean = map.isEmpty()
    fun containsElement(key: E): Boolean = map.containsKey(key)
    operator fun get(key: E): Double? = map[key]?.div(total)


}