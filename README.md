# Legend

"**Le**xicon **Gen**erator, **D**uh!." is a vocab generator for conlangs

# Implementation

- Language
    + print(): String

- Word
    + sound: String
    + structure: String

- Collapsible <T : Collapsible<T>>
    + collapsed: Boolean
    + observe(): T

- WordStructure: Collapsible
    - domain
    - collapseNext(): Boolean
    - expandDomain(): Unit
    - applyRestriction(): Boolean
    + toWordSounds(): WordSounds
    + toString(): String

- WordSounds: Collapsible
    - domain
    - collapseNext(): Boolean
    - applyRestriction(): Boolean
    + toString(): String

- QuantumState<T>: Collapsible
    + entropy: Double

# Word building algorithm

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