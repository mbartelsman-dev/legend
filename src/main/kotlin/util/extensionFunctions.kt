package util

import kotlin.random.Random


/**
 * Uses the values of the map as weights and chooses a random key
 * @return randomly chosen key
 */
fun <E> Map<E, Double>.chooseWeighted(random: Random = Random.Default): E {
    var cdf = 0.0
    val sum = this.asIterable().sumByDouble { it.value }
    val rnd = random.nextDouble() * sum

    return this
            .toList()
            .map { cdf += it.second; Pair(it.first, cdf) }
            .let { list -> list[list.binarySearch { it.second.compareTo(rnd) }] }
            .first
}