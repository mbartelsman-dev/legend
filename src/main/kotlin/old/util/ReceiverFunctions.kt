package old.util

import java.util.*



@Deprecated("Bound for deletion if unused after restructuring")
fun <E> MutableList<E>.pad(count: Int, padding: E) {
    if (count <  0) {
        repeat(-count) { this.add(0, padding) }
    } else {
        repeat(count) { this.add(padding) }
    }
}

@Deprecated("Bound for deletion if unused after restructuring")
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

@Deprecated("Bound for deletion if unused after restructuring")
fun <E> MutableList<MutableList<E>>.alignOn(element: E, padding: E): MutableList<MutableList<E>>? {
    this.forEach { row -> row.dropWhile { it == padding } }
    val max = this.maxOfOrNull { it.indexOf(element) }!!
    this.forEach {
        if (!this.contains(element)) return null
        it.pad(max - it.indexOf(element), padding)
    }
    return null
}