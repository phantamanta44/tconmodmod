# Tinkers' Modifier Modifier

Allows modpack creators to modify properties of Tinkers' Construct tool modifiers.

## Configuring Mutations

In your `.minecraft/config` folder, create a JSON document called `tconmodmod.json`. The document root should be a `ModifierEntry[]`, the specification for which is as follows:

```
// Represents a single modifier to be mutated
object ModifierEntry {
    String? modifier          // Identifier of the modifier to mutate
    String[]? modifiers       // Alternatively, a list of identifiers to mutate
    MutationEntry[] mutations // Set of mutations to apply to the modifier
}
```

The `MutationEntry` objects are specified as follows:

```
// Represents a single mutation to be applied to a modifier
object MutationEntry {
    String type  // An identifier for the mutation type
}

// Mutates the in-game colour for the modifier
object ColourMutationEntry : MutationEntry {
    String type = "colour"
    String value // A 6-digit hex string for the modifier's colour
}

// Mutates the amount of material necessary for one level of the modifier
object QuantityMutationEntry : MutationEntry {
    String type = "per_level"
    Integer value // The amount of material needed for one level of the modifier
}

// Mutates the ingredients used to apply the modifier
object MaterialMutationEntry : MutationEntry {
    String type = "material"
    Ingredient[] value // The set of possible ingredients that can apply the modifier
}

// Mutates an embossment such that it ignores the embossment limit
object UnlimitedEmbossMutationEntry : MutationEntry {
    String type = "unlimited_emboss"
}
```

Some mutations (e.g. `material`) require item stack matchers for matching ingredients. Those are specified as follows:

```
// Represents an item stack matcher used for matching recipe ingredients
object Ingredient {
    String type     // An identifier for the ingredient type
    Integer? amount // (Default = 1) The amount of the item needed
    Integer? value  // (Default = 1) The number of "units" of material applied per amount
}

// Matches items using the ore dictionary
object OreDictIngredient : Ingredient {
    String type = "ore"
    String ore // The ore dictionary name to match against
}

// Matches items by registry name
object ItemIngredient : Ingredient {
    String type = "item"
    String item   // The matched item's registry name
    Integer? meta // (Default = 0) The metadata value to match against
}
```

In addition to the mutation configuration as specified above, the main `tconmodmod.cfg` configuration file contains some useful options. It is recommended that modpack authors take a look at it before writing a mutation configuration.

## Example Configuration

This configuration does the following:
* The haste modifier becomes cyan in colour.
* The haste modifier requires 128 units of material per level.
* The haste modifier uses one gold ingot or equivalent for a unit of material.
* The paper and cactus embossments can be applied on top of other embossments.
    * Note that order matters! Applying paper first means that other embossments (aside from cactus) can no longer be applied!

```json
[
  {
    "modifier": "haste",
    "mutations": [
      {
        "type": "colour",
        "value": "00FFFF"
      },
      {
        "type": "per_level",
        "value": 128
      },
      {
        "type": "material",
        "value": [
          {
            "type": "ore",
            "ore": "ingotGold"
          },
          {
            "type": "ore",
            "ore": "blockGold",
            "value": 9
          },
          {
            "type": "ore",
            "ore": "nuggetGold",
            "amount": 9
          }
        ]
      },
      {
        "modifiers": [
          "extratraitpaperwritable1",
          "extratraitpaperwritable1writable2",
          "extratraitpaperwritable2",
          "extratraitcactusprickly"
        ],
        "mutations": [
          {
            "type": "unlimited_emboss"
          }
        ]
      }
    ]
  }
]

```
