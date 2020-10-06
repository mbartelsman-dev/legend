import util.transposed
import java.lang.Integer.max

/**
  * Process to make a word:
  * Make a new, empty word of size 1
      * The single element of the word is a "total superposition,
      * a superposition of all possible syllables allowed by the rules.
  * Collapse the words least entropic element
      * The word is now composed of 1 random syllable
  * Propagate restrictions
  * With the 1 syllable, we go through the rules looking for all
  * matching patterns, generating new superpositions based on that.
  * Repeat the process
  * Collapse
  * Propagate
  * As the word grows longer, the weight of word boundaries grows
  * A word boundary is the limit of a word, no syllables may fall
  * to the left of a word's start, and no syllables may fall to the
  * right of a word's end.
  * The loop ends when all spaces between the boundaries are collapsed.
  *
  * Word: represents a word in an undefined state
  *     domain (List<?>)
  *     collapse: collapses all states in the word
  *
  */

class Word(private val rules: Rules<Long>) {
    private val domain: MutableList<State<Long>> = mutableListOf()
    val size get() = domain.size

    /**
     * Collapses the [State] in the [domain] with the lowest entropy among all [UnobservedState].
     * @return the index of the collapsed value, or `-1` if there are no [UnobservedState]s in the domain
     */
    private fun collapseNext(): Int {
        val o = domain.filterIsInstance<UnobservedState<Long>>().minByOrNull { it.entropy }
        val i = domain.indexOf(o as State<Long>)
        if (i != -1) domain[i] = o.observe()
        return i
    }

    /**
     * Collapses the entire domain while propagating restrictions
     * @return the collapsed [Word] object
     */
    fun collapse(): Word {
        while (domain.filterIsInstance<UnobservedState<Long>>().isNotEmpty()) {
            val i = collapseNext()
            propagateRestrictions(i)
        }
        return this
    }

    private fun propagateRestrictions(index: Int) {
        if (index < 0  ||  index >= domain.size  ||  domain[index] !is ObservedState) { return }
        val obj: Long = (domain[index] as ObservedState).value
        rules.syllableRules
                .filter { it.contains(obj) }
                .forEach {
                    // TODO: Doesn't take into account that a rule can have multiple instances of the same Long
                    val leftPad = it.indexOf(obj) - index
                    val rightPad = (it.size - it.indexOf(obj)) - (domain.size - index)

                    expandDomain(leftPad, rightPad)
                    applyRule(it, index + max(leftPad, 0) - it.indexOf(obj))

                }
    }

    private fun applyRule(rule: List<Long>, index: Int) {
        TODO("Not yet implemented")
    }

    private fun expandDomain(left: Int, right: Int) {
        repeat(left)  { domain.add(0, UnobservedState(rules)) }
        repeat(right) { domain.add(UnobservedState(rules)) }
    }
}
//     0 1 2 3 4 5 : index
//           |
//       0 1 2 3 : r
interface Rules<T> {
    val syllableSet: Map<T, Double>
    val syllableRules: Set<List<T>>
}




fun main(args: Array<String>) {
    val t = listOf<List<Int>>(
        listOf(1,2,3,4),
        listOf(8,2,3,4,5),
        listOf(1,2,3),
        listOf(1,2,3,4),
    )
    println(t.transposed().map { it.toSet() })
}