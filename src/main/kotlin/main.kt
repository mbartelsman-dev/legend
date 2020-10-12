import util.chooseWeighted
import kotlin.math.pow
import kotlin.random.Random

fun main(args: Array<String>) {
    val rules = Rules.Builder("Noun").build()
    Word.Builder(rules).build()
}

class Word private constructor() {
    class Builder(val rules: Rules) {
        fun build(): Word {
            val syllables = buildSyllables()
            // Build list of Syllables
            // Substitute syllables for sound classes
            // Substitute classes for sound variants
            TODO("Finish implementation")
        }

        private fun buildSyllables() {
            val rand = Random.Default

            val syllables = mutableListOf<Token.Syllable>()
            syllables.add(Token.Syllable.INIT)

            do {
                // Increase the likelihood that the word will end based on syllable count
                val nextId = if (rand.nextFloat() < Math.E.pow(-0.22 * (syllables.size-1))) {
                    rules.patterns.filter { it.first == syllables.last().id }
                } else {
                    rules.patterns.filter { it.first == syllables.last().id && it.second == Token.Syllable.LAST.id }
                }.random().second

                val next = (rules.syllables[nextId] ?: error("Selected id was not found in syllables"))
                        .chooseWeighted()
                syllables.add(next)

            } while (syllables.last() != (Token.Syllable.LAST))
        }
    }

}
