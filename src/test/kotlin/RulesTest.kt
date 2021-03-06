import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class RulesTest {

    @Test
    fun rulesTest() {
        val ruleBuilder = Rules.Builder("Noun")
        listOf<Registrable>(
                Token.Sound("p"),
                Token.Sound("t"),
                Token.Sound("a"),
                Token.SoundClass("C", listOf("p", "t")),
                Token.SoundClass("V", listOf("a")),
                Token.Syllable("S1", listOf("C", "V")),
                Token.Syllable("S2", listOf("C", "V", "C")),
                Pattern(Token.Syllable.INIT.id, "S1"),
                Pattern("S1", "S2"),
                Pattern("S2", Token.Syllable.LAST.id),
                Cluster(listOf("t", "a", "t"))
        ).forEach {
            ruleBuilder.register(it)
        }

        assertFails { ruleBuilder.register(Token.SoundClass("K", listOf("h"))) }
        assertFails { ruleBuilder.register(Token.Syllable("S3", listOf("K"))) }
        assertFails { ruleBuilder.register(Pattern("S3", "S1")) }
        assertFails { ruleBuilder.register(Cluster(listOf("h", "a", "h"))) }

        val rules = ruleBuilder.build()

        assertEquals(mapOf(
                "p" to Token.Sound("p"),
                "t" to Token.Sound("t"),
                "a" to Token.Sound("a")
        ), rules.sounds)

        assertEquals(mapOf(
                "C" to Token.SoundClass("C", listOf("p", "t")),
                "V" to Token.SoundClass("V", listOf("a")),
        ), rules.soundClasses)

        assertEquals(mapOf(
                "<init>" to mapOf(),
                "S1" to mapOf(Token.Syllable("S1", listOf("C", "V")) to 1.0),
                "S2" to mapOf(Token.Syllable("S2", listOf("C", "V", "C")) to 1.0),
                "<fin>" to mapOf(),
        ), rules.syllables)

        assertEquals(setOf(
                Pair("<init>", "S1"),
                Pair("S1", "S2"),
                Pair("S2", "<fin>"),
        ), rules.patterns)

        assertEquals(listOf(
                listOf("t", "a", "t")
        ), rules.illegalClusters)
    }

    @Test
    fun getId() {
        val rules = Rules.Builder("test")
        assertEquals("test", rules.build().id)
    }
}