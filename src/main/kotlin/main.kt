import util.transposed

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

fun main(args: Array<String>) {
    val t = listOf<List<Int>>(
        listOf(1,2,3,4),
        listOf(8,2,3,4,5),
        listOf(1,2,3),
        listOf(1,2,3,4),
    )
    println(t.transposed().map { it.toSet() })
}