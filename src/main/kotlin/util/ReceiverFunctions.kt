package util

import java.util.*
import kotlin.math.ln


/**
 * Calculates the entropy on an non-normalized list of weights or probabilities
 */
fun Iterable<Double>.calculateEntropy(): Double {
    val sumN = this.sum()
    val sumNLogN = this.sumByDouble { it * ln(it) }
    return ln(sumN) - (sumNLogN / sumN)
}

/**
 * Uses the values of the map as weights and chooses a random key
 * @return randomly chosen key
 */
fun <E> Map<E, Double>.chooseWeighted(random: Random = Random()): E {
    var cdf = 0.0
    val sum = this.asIterable().sumByDouble { it.value }
    val rnd = random.nextDouble() * sum

    return this
            .toList()
            .map { cdf += it.second; Pair(it.first, cdf) }
            .let { list -> list[list.binarySearch { it.second.compareTo(rnd) }] }
            .first
}

fun <E> MutableList<E>.pad(count: Int, padding: E) {
    if (count <  0) {
        repeat(-count) { this.add(0, padding) }
    } else {
        repeat(count) { this.add(padding) }
    }
}

fun <E> List<List<E>>.transposed(): List<List<E>> {
    val t = mutableListOf<MutableList<E>>()
    val n = this.maxByOrNull { it.size }?.size ?: 0
    for (i in 0 until n) {
        val col: MutableList<E> = mutableListOf<E>()
        for (row in this) {
            if (i < row.size) col.add(row[i])
        }
        t.add(col)
    }
    return t
}

fun <E> MutableList<MutableList<E>>.alignOn(element: E, padding: E): MutableList<MutableList<E>>? {
    this.forEach { row -> row.dropWhile { it == padding } }
    val max = this.maxOfOrNull { it.indexOf(element) }!!
    this.forEach {
        if (!this.contains(element)) return null
        this.pad(max - it.indexOf(element), padding)
    }
}