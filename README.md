# ScrapMechanicReader
This project contains a collection of readers that can parse files made by the game ScrapMechanic.


#### Ideas

The plan right now is to read as much as I can about reversing and looking into the game for clues about how stuff will work.
I'm working on a lua deserializer right now.


### Tiles
Use the TileReader to read data from terrain tile files.

<code>
Tile tile = TileReader.read("../TilePath.tile");
</code>


### Lua
Use the LuaDeserializer to parse serialized lua data.


