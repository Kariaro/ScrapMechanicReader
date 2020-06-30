# ScrapMechanicReader
This project contains a collection of readers that can parse files made by the game ScrapMechanic.


### Tiles
Use the TileReader to read data from terrain tile files.

<code>
Tile tile = TileReader.read("../TilePath.tile");
</code>


### Lua
Use the LuaDeserializer to parse serialized lua data.
