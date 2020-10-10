# Legend

"**Le**xicon **Gen**erator, **D**uh!." is a vocab generator for conlangs

# Implementation

- Language
    + print(): String

- old.Word
    + sound: String
    + structure: String

- old.Collapsible <T : old.Collapsible<T>>
    + collapsed: Boolean
    + observe(): T

- old.WordStructure: old.Collapsible
    - domain
    - collapseNext(): Boolean
    - expandDomain(): Unit
    - applyRestriction(): Boolean
    + toWordSounds(): old.WordSounds
    + toString(): String

- old.WordSounds: old.Collapsible
    - domain
    - collapseNext(): Boolean
    - applyRestriction(): Boolean
    + toString(): String

- old.QuantumState<T>: old.Collapsible
    + entropy: Double

# old.Word building algorithm

index, element

collapseElement(index/element)
elementScope(element) -> Pair<Int, Int>
growDomain(index, left, right) -> Int (new index)
left..right.foreach{ 
    restrictionsAt(element, it) -> Map<Long, Double>
    applyRestrictions(it - left, restriction) -> Boolean (valid word)
}

getStatesFor(x, -1).

1. Start with an empty state list
2. Add a single "total set" state, holding all possible syllable structures
3. Collapse said state
4. Propagate the rules
5. Select the state with the lowest entropy
6. Repeat 3, 4, and 5 until finished
7. Convert the domain of structures into a domain of sounds
8. Select the state with the lowest entropy
9. Collapse said state
10. Propagate the restrictions to clusters
11. Repeat 8, 9, and 10 until finished
12. Convert domain to string
13. Apply any rewrite rules

```
< set of syllable structures > :: collapse
< x > :: propagate
< set(y in (y,...,x)) > ... < set(y in (y,x)) > < x > < set(y in (x,y)) > ... < set(y in (x,...,y)) > :: find lowest entropy
< set_1 > ... < set_z > ... < set_n > :: collapse
< set_1 > ...   < z >   ... < set_n > :: 
```

get all rules which contains an x
transpose them on x
  a b x c d
    e x f g h
i j k x l

(i) (a j) (x) (c f l) (d g) (h)
union these sets with the current word states

```kotlin
class old.Rules {

    /**
     * Extends the possible sequences of syllable structures in the rules
     *
     * @param first The name of a [SyllableToken]
     * @param second The name of a [SyllableToken] that can now follow the first
     * @throws InvalidTokenException if either token is not registered
     */
    fun extendSyllableRules(first: String, second: String)


    /**
     * Restricts the possible sequences of syllable structures in the rules
     *
     * @param first The name of a [SyllableToken]
     * @param second The name of a [SyllableToken] that is not allowed to follow the first
     * @throws InvalidTokenException if either token is not registered
     */
    fun restrictSyllableRules(first: String, second: String)


    /**
     * Registers a new SyllableToken with the rules. Adding the same token id multiple times will result
     * in multiple variants being registered under that name.
     *
     * @param id The name of the SyllableToken
     * @param tokens An ordered sequence of the names of [SoundClassToken]s that compose this [SyllableToken]
     * @param weight A [Double] defining the likelihood that this variant will be chosen
     * @throws InvalidTokenException if any [SoundClassToken] is not registered
     */
    fun addSyllableToken(id: String, vararg tokens: String, weight: Double = 1.0)


    /**
     * Registers a new [SoundClassToken] with the rules. Adding the same token id multiple times will result
     * in the new SoundTokens being appended at the end
     *
     * @param id The name of the SyllableToken
     * @param sounds An sequence, in descending order of likelihood, of all the [SoundToken]s that this class stands for
     * @throws InvalidTokenException if any [SoundToken] is not registered
     */
    fun addSoundClassToken(id: String, vararg sounds: String)


    /**
     * Restricts the possible sequences of sounds in the rules. By default there are no restrictions
     *
     * @param sounds A sequence of [SoundToken] names representing an illegal sequence of character
     * @throws InvalidTokenException if either [SoundToken] is not registered
     */
    fun restrictSoundRules(vararg sounds: String)


    /**
     * Registers a new [SoundToken] with the rules. Adding the same token id multiple times will have no effect
     *
     * @param id The name of the SyllableToken
     */
    fun addSoundToken(id: String)
}
// Restriction: 
```

```
Language    = { old.Word } Syllables
old.Word        = WordBlock [ WordExcept ] [ WordWhere ]
WordBlock   = "word" Identifier "{" SyllRules "}"
WordExcept  = "except" "{" SyllRules "}"
WordWhere   = "where" "{" SyllCats "}"
SyllRules   = SyllRule 
            | SyllRule { EOF SyllRule }
SyllRule    = Sylls ">>" Sylls
Sylls       = Identifier
            | "[" { Identifier } "]"
            | "$"

word BasicWord {

    $   >> S1
    SF  >> SF
    SF3 >> SB
    SB  >> SB
    SB3 >> SF

} except { 

    S1 >> S3

} where {

    SF: [SF1 SF2 SF3]
    SB: [SB1 SB2 SB3]
    S1: [SF1 SB1]
    S3: [SF3 SB3]
}

syllables {

    SF1: O F, O F K, F K
    SF2: C F (K)
    SF3: C /r/ F [K /s/]
    SB1: (O) B (K)
    SB2: C B (K)
    SB3: C /r/ B [K /s/]
    :: /n/ is shorthand for n ... } where { ... n: n

} except { 

    /t/ >> /r/
    /d/ >> /r/

} where {

    C: p t k b d g
    F: a e
    B: o u
    K: l r n
    O: s p t
}

```