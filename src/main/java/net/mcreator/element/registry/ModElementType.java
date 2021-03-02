/*
 * MCToolkit (https://mctoolkit.net/)
 * Copyright (C) 2020 MCToolkit and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.mcreator.element.registry;

import net.mcreator.generator.GeneratorStats;
import net.mcreator.ui.init.L10N;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ModElementType implements Comparable<ModElementType> {

	public static List<ModElementType> elements = new ArrayList<>();

	//Variables used for each mod element
	private final BaseType baseType;
	private final String registryName;
	private final String description;
	private final String readableName;
	private final String name;
	private final String iconID;
	private final Character shortcut;
	private final RecipeElementType recipeElementType;
	private GeneratorStats.CoverageStatus status = GeneratorStats.CoverageStatus.FULL;

	//Mod element variables
	public static final ModElementType ADVANCEMENT = new ModElementType("achievement", "advancement", 'h', BaseType.ACHIEVEMENT, RecipeElementType.NONE);
	public static final ModElementType ARMOR = new ModElementType("armor", 'a', BaseType.ARMOR, RecipeElementType.ARMOR);
	public static final ModElementType BIOME = new ModElementType("biome", 'o', BaseType.BIOME, RecipeElementType.NONE);
	public static final ModElementType BLOCK = new ModElementType("block", 'b', BaseType.BLOCK, RecipeElementType.BLOCK);
	public static final ModElementType CODE = new ModElementType("code", null, BaseType.OTHER, RecipeElementType.NONE);
	public static final ModElementType COMMAND = new ModElementType("command", 'c', BaseType.COMMAND, RecipeElementType.NONE);
	public static final ModElementType DIMENSION = new ModElementType("dimension", 'd', BaseType.DIMENSION, RecipeElementType.ITEM);
	public static final ModElementType ENCHANTMENT = new ModElementType("enchantment", 'm', BaseType.ENCHANTMENT, RecipeElementType.NONE);
	public static final ModElementType FLUID = new ModElementType("fluid", 'u', BaseType.BLOCK, RecipeElementType.BLOCK);
	public static final ModElementType FOOD = new ModElementType("food", 'f', BaseType.ITEM, RecipeElementType.ITEM);
	public static final ModElementType FUEL = new ModElementType("fuel", '/', BaseType.FUEL, RecipeElementType.NONE);
	public static final ModElementType FUNCTION = new ModElementType("function", '\'', BaseType.DATAPACK, RecipeElementType.NONE);
	public static final ModElementType GAMERULE = new ModElementType("gamerule", ';', BaseType.GAMERULE, RecipeElementType.NONE);
	public static final ModElementType GUI = new ModElementType("gui", 'g', BaseType.GUI, RecipeElementType.NONE);
	public static final ModElementType ITEM = new ModElementType("item", 'i', BaseType.ITEM, RecipeElementType.ITEM);
	public static final ModElementType KEYBIND = new ModElementType("keybind", 'k', BaseType.KEYBIND, RecipeElementType.NONE);
	public static final ModElementType LOOTTABLE = new ModElementType("loottable", 'l', BaseType.DATAPACK, RecipeElementType.NONE);
	public static final ModElementType MOB = new ModElementType("mob", "entity", 'e', BaseType.ENTITY, RecipeElementType.NONE);
	public static final ModElementType MUSICDISC = new ModElementType("musicdisc",'x', BaseType.OTHER, RecipeElementType.ITEM);
	public static final ModElementType OVERLAY = new ModElementType("overlay", 'v', BaseType.OVERLAY, RecipeElementType.NONE);
	public static final ModElementType PAINTING = new ModElementType("painting", '.', BaseType.OTHER, RecipeElementType.NONE);
	public static final ModElementType PARTICLE = new ModElementType("particle", 'y', BaseType.PARTICLE, RecipeElementType.NONE);
	public static final ModElementType PLANT = new ModElementType("plant", 'n', BaseType.BLOCK, RecipeElementType.BLOCK);
	public static final ModElementType POTIONEFFECT = new ModElementType("potion", "potioneffect", 'z', BaseType.POTIONEFFECT, RecipeElementType.NONE);
	public static final ModElementType POTIONITEM = new ModElementType("potionitem", ',', BaseType.POTIONITEM, RecipeElementType.NONE);
	public static final ModElementType PROCEDURE = new ModElementType("procedure", 'p', BaseType.PROCEDURE, RecipeElementType.NONE);
	public static final ModElementType RANGEDITEM = new ModElementType("gun", "rangeditem", "rangeditem", 'q', BaseType.ITEM, RecipeElementType.ITEM);
	public static final ModElementType RECIPE = new ModElementType("recipe", 'r', BaseType.DATAPACK, RecipeElementType.NONE);
	public static final ModElementType STRUCTURE = new ModElementType("structure", 's', BaseType.STRUCTURE, RecipeElementType.NONE);
	public static final ModElementType TAB = new ModElementType("tab", 'w', BaseType.TAB, RecipeElementType.NONE);
	public static final ModElementType TAG = new ModElementType("tag", 'j', BaseType.OTHER, RecipeElementType.NONE);
	public static final ModElementType TOOL = new ModElementType("tool", 't', BaseType.ITEM, RecipeElementType.ITEM);

	//Constructor used for the same registry name, localization ID and icon ID
	private ModElementType(String registryName, Character shortcut, BaseType baseType, RecipeElementType recipeElementType) {
		this(registryName, registryName, registryName, shortcut, baseType, recipeElementType);
	}

	//Constructor used for the same registry name and localization ID.
	private ModElementType(String registryName, String iconID, Character shortcut, BaseType baseType, RecipeElementType recipeElementType) {
		this(registryName, registryName, iconID, shortcut, baseType, recipeElementType);
	}

	//Used to have a different name and registry name
	private ModElementType(String registryName, String name, String iconID, Character shortcut, BaseType baseType, RecipeElementType recipeElementType) {
		this.baseType = baseType;
		this.recipeElementType = recipeElementType;
		this.registryName = registryName;
		this.name = name;
		this.iconID = iconID;
		this.shortcut = shortcut;

		this.readableName = L10N.t("modelement." + name.toLowerCase(Locale.ENGLISH));
		this.description = L10N.t("modelement." + name.toLowerCase(Locale.ENGLISH) + ".description");
		elements.add(this);
	}

	public String getRegistryName(){
		return registryName;
	}

	public String name() {
		return name;
	}

	public String getReadableName() {
		return readableName;
	}

	public String getDescription() {
		return description;
	}

	public String getIconID() {
		return iconID;
	}

	public Character getShortcut() {
		return shortcut;
	}

	public RecipeElementType getRecipeElementType() {
		return recipeElementType;
	}

	public BaseType getBaseType() {
		return baseType;
	}

	public GeneratorStats.CoverageStatus getStatus() {
		return status;
	}

	public void setStatus(GeneratorStats.CoverageStatus status) {
		this.status = status;
	}

	@Override public int compareTo(@NotNull ModElementType o) {
		return o.getStatus().ordinal() - status.ordinal();
	}

	public static ModElementType get(String modElementName){
		for(ModElementType me : elements){
			if(me.registryName.equals(modElementName)){
				return me;
			}
		}
		return null;
	}
}
