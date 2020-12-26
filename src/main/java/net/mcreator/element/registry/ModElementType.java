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

import java.util.Locale;

public class ModElementType implements Comparable<ModElementType> {

	//Variables used for each mod element
	private final BaseType baseType;
	private final String registryName;
	private final String description;
	private final String readableName;
	private final String name;
	private final RecipeElementType recipeElementType;
	private GeneratorStats.CoverageStatus status = GeneratorStats.CoverageStatus.FULL;

	//Mod element variables
	public static final ModElementType ACHIEVEMENT = new ModElementType("achievement", BaseType.ACHIEVEMENT, RecipeElementType.BLOCK);
	public static final ModElementType ARMOR = new ModElementType("armor", BaseType.ARMOR, RecipeElementType.ARMOR);
	public static final ModElementType BIOME = new ModElementType("biome", BaseType.BIOME, RecipeElementType.NONE);
	public static final ModElementType BLOCK = new ModElementType("block", BaseType.BLOCK, RecipeElementType.BLOCK);
	public static final ModElementType CODE = new ModElementType("code", BaseType.OTHER, RecipeElementType.NONE);
	public static final ModElementType COMMAND = new ModElementType("command", BaseType.COMMAND, RecipeElementType.NONE);
	public static final ModElementType DIMENSION = new ModElementType("dimension", BaseType.DIMENSION, RecipeElementType.ITEM);
	public static final ModElementType ENCHANTMENT = new ModElementType("enchantment", BaseType.ENCHANTMENT, RecipeElementType.NONE);
	public static final ModElementType FLUID = new ModElementType("fluid", BaseType.BLOCK, RecipeElementType.BLOCK);
	public static final ModElementType FOOD = new ModElementType("food", BaseType.ITEM, RecipeElementType.ITEM);
	public static final ModElementType FUEL = new ModElementType("fuel", BaseType.FUEL, RecipeElementType.NONE);
	public static final ModElementType FUNCTION = new ModElementType("function", BaseType.DATAPACK, RecipeElementType.NONE);
	public static final ModElementType GUI = new ModElementType("gui", BaseType.GUI, RecipeElementType.NONE);
	public static final ModElementType ITEM = new ModElementType("item", BaseType.ITEM, RecipeElementType.ITEM);
	public static final ModElementType KEYBIND = new ModElementType("keybind", BaseType.KEYBIND, RecipeElementType.NONE);
	public static final ModElementType LOOTTABLE = new ModElementType("loottable", BaseType.DATAPACK, RecipeElementType.NONE);
	public static final ModElementType MOB = new ModElementType("mob", BaseType.ENTITY, RecipeElementType.NONE);
	public static final ModElementType MUSICDISC = new ModElementType("musicdisc", BaseType.OTHER, RecipeElementType.ITEM);
	public static final ModElementType OVERLAY = new ModElementType("overlay", BaseType.OVERLAY, RecipeElementType.NONE);
	public static final ModElementType PAINTING = new ModElementType("painting", BaseType.OTHER, RecipeElementType.NONE);
	public static final ModElementType PARTICLE = new ModElementType("particle", BaseType.PARTICLE, RecipeElementType.NONE);
	public static final ModElementType PLANT = new ModElementType("plant", BaseType.BLOCK, RecipeElementType.BLOCK);
	public static final ModElementType POTIONEFFECT = new ModElementType("potion", "potioneffect", BaseType.POTIONEFFECT, RecipeElementType.NONE);
	public static final ModElementType POTIONITEM = new ModElementType("potionitem", BaseType.POTIONITEM, RecipeElementType.NONE);
	public static final ModElementType PROCEDURE = new ModElementType("procedure", BaseType.PROCEDURE, RecipeElementType.NONE);
	public static final ModElementType RANGEDITEM = new ModElementType("gun", "rangeditem", BaseType.ITEM, RecipeElementType.ITEM);
	public static final ModElementType RECIPE = new ModElementType("recipe", BaseType.DATAPACK, RecipeElementType.NONE);
	public static final ModElementType STRUCTURE = new ModElementType("structure", BaseType.STRUCTURE, RecipeElementType.NONE);
	public static final ModElementType TAB = new ModElementType("tab", BaseType.TAB, RecipeElementType.NONE);
	public static final ModElementType TAG = new ModElementType("tag", BaseType.OTHER, RecipeElementType.NONE);
	public static final ModElementType TOOL = new ModElementType("tool", BaseType.ITEM, RecipeElementType.ITEM);

	//Constructor used for the same registry name and localization ID.
	ModElementType(String registryName, BaseType baseType, RecipeElementType recipeElementType) {
		this(registryName, registryName, baseType, recipeElementType)
	}

	//Used to have a different name and registry name
	ModElementType(String registryName, String name, BaseType baseType, RecipeElementType recipeElementType) {
		this.baseType = baseType;
		this.recipeElementType = recipeElementType;
		this.registryName = registryName;
		this.name = name;

		this.readableName = L10N.t("modelement." + name.toLowerCase(Locale.ENGLISH));
		this.description = L10N.t("modelement." + name.toLowerCase(Locale.ENGLISH) + ".description");
		ModElementTypeRegistry.elements.add(this);
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
		for(ModElementType me : ModElementTypeRegistry.elements){
			if(me.name.equals(modElementName)){
				return me;
			}
		}
		return null;
	}
}
