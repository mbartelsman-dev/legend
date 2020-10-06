# LeGenD

"**Le**xical **Gen**erator, **D**." is a vocab generator for conlangs

# Implementation

- Language: class containing both rules and words, as well as construction parameters
    - rules: rules used for word generation
    - maxSize: max size for the dictionary
    + dictionary: a list of all words that have been generated
    + generateWords(): populates the dictionary up to the max

- WordBuilder(rules)
    + build() -> Word: returns a built word, which also contains the intermediate representations
    - ???() -> WordStructure: generates a new, empty word structure

- WordStructure(rules): A class which represents a word, in terms of syllable structures

# Word building algorithm

1. Start with an empty state list
2. Add a single "total set" state, holding all possible syllable structures
3. Collapse said state
4. Propagate the rules
    1. Transpose all rules with the collapsed value as a pivot, if a rule contains the value more than once, then duplicate it
        1. Align lists on x all possible values of x
        2. Transpose them
        3. Turn them into sets
    2. Intersect the state sets with each respective set that resulted from the previous operation.
        If there's no set, add a new one with the result.
        If the result is the empty set, reject the word
5. Select the state with the lowest entropy
6. Repeat 3, 4, and 5 until finished

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