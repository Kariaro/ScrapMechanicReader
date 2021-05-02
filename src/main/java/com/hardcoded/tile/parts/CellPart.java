package com.hardcoded.tile.parts;

public class CellPart {
	protected MipPart mip;
	protected ClutterPart clutter;
	protected AssetListPart assetList;
	protected BlueprintListPart blueprintList;
	protected NodePart node;
	protected ScriptPart script;
	protected PrefabPart prefab;
	protected DecalPart decal;
	protected HarvestableListPart harvestableList;
	
	public CellPart() {
		mip = new MipPart();
		clutter = new ClutterPart();
		assetList = new AssetListPart();
		blueprintList = new BlueprintListPart();
		node = new NodePart();
		script = new ScriptPart();
		prefab = new PrefabPart();
		decal = new DecalPart();
		harvestableList = new HarvestableListPart();
	}
}
