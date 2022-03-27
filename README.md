# ScrapMechanicReader
[![Release](https://jitpack.io/v/kariaro/ScrapMechanicReader.svg)](https://jitpack.io/#kariaro/ScrapMechanicReader)

This project contains a collection of readers that can parse files made by the game ScrapMechanic.
The game uses `lz4` to compress most of the data.

### Info
The focus of this project is to discover how the tile files are generated and provide an api to
create tiles from code.

The game does not currently allow users to export nodes and blueprints inside tile files and this
project is going to try fix that.

### Usage
Some example code of how to use this api.

```java
Tile tile = TileReader.readTile("<path to tile>");
tile.setVersion(9); // Change the tile version
tile.resize(2, 2);  // Change the tile size

if(!TileWriter.writeTile(tile, "<path to write>")) {
    throw new TileException("Failed to write tile");
}
```

### Example
There are examples for how to use this api.
See [Example.java](/src/main/java/me/hardcoded/smreader/example/Example.java).


# Usage
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.kariaro:ScrapMechanicReader:version'
}
```