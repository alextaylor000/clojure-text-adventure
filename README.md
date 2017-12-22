# text-adventures

A framework for creating text-based adventures. Mainly for me to hack on Clojure.

Scenes are represented by this data structure:

```clojure
  { :introduction { :next [:door-1 :door-2] :text "You see two doors. Which do you choose?" }
    :door-1 { :next [] :text "You're dead!" }
    :door-2 { :next [] :text "You're alive!" }
   }
```

Each key represents the name of a scene. `introduction` is a required scene.

Within each scene, the key `:next` defines a vector of choices to move forward from that scene. The `:text` field contains the text of the scene itself.

## Future
- [ ] Parse a YML document to load stories.
- [ ] Make the `next` data structure richer to allow sentences

## Usage

    $ lein run

or...

    $ java -jar text-adventures-0.1.0-standalone.jar [args]

