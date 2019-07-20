# Tinkers' Modifier Modifier

Allows modpack creators to modify properties of Tinkers' Construct tool modifiers.

## How the heck do I use this thing?

In your `.minecraft/config` folder, create a JSON document called `tconmodmod.json`. The document root should be a `ModifierEntry[]`. The specifications are as follows:

```
// Represents a single modifier to be mutated
object ModifierEntry {
    String modifier           // Identifier of the modifier to mutate
    MutationEntry[] mutations // Set of mutations to apply to the modifier
}

// Represents a single mutation to be applied to a modifier
object MutationEntry {
    String type  // An identifier for the mutation type
    Object value // The parameters to the mutation
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

## Example configuration

This configuration causes the "Haste" modifier to become cyan, require 128 units of material per level, and require one gold ingot or equivalent for one unit of material.

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
      }
    ]
  }
]

```
