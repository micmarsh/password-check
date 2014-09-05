# password-check

password checker library for clojure

## Usage

#### checker functions

 * not-blank?
 * contains-uppercase?
 * contains-lowercase?
 * contains-alphabet?
 * contains-symbol?
 * not-same-characters?
 * not-sequential-password?
 * not-contains-sequence?
 * not-contains-repeats?
 * not-contains-multi-byte-character?
 * length-range

## TODO

Combinators for `check`/`checker`
* remove old ones entirely
* start with one that returns first failed, then one that returns all failed

All kinds of documentation
* docstrings for each of the funcitons listed above, a note on length-range
* a section in here on ways to use `check`/`checker`

## License

Copyright (C) 2014 Michael Marsh and 2011 Masashi Iizuka

Distributed under the Eclipse Public License, the same as Clojure.
