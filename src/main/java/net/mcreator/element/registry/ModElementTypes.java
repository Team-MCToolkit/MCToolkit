/*
 * MCToolkit (https://mctoolkit.net/)
 * Copyright (C) 2020-2021 MCToolkit and contributors
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

import net.mcreator.element.GeneratableElement;
import net.mcreator.element.types.*;
import net.mcreator.generator.GeneratorStats;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.modgui.*;
import net.mcreator.workspace.elements.ModElement;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ModElementTypes<GE extends GeneratableElement> implements Comparable<ModElementTypes<?>> {

	public static List<ModElementTypes<?>> elements = new ArrayList<>();

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

	private final ModElementGUIProvider<GE> modElementGUIProvider;
	private final Class<? extends GE> modElementStorageClass;

	private boolean hasProcedureTriggers;

	//Mod element variables
	public static final ModElementTypes<?> ADVANCEMENT = new ModElementTypes<>("achievement", "advancement", 'h', BaseType.ACHIEVEMENT, RecipeElementType.NONE, AchievementGUI::new, Achievement.class);
	public static final ModElementTypes<?> ARMOR = new ModElementTypes<>("armor", 'a', BaseType.ARMOR, RecipeElementType.ARMOR, ArmorGUI::new, Armor.class);
	public static final ModElementTypes<?> BIOME = new ModElementTypes<>("biome", 'o', BaseType.BIOME, RecipeElementType.NONE, BiomeGUI::new, Biome.class);
	public static final ModElementTypes<?> BLOCK = new ModElementTypes<>("block", 'b', BaseType.BLOCK, RecipeElementType.BLOCK, BlockGUI::new, Block.class);
	public static final ModElementTypes<?> CODE = new ModElementTypes<>("code", null, BaseType.OTHER, RecipeElementType.NONE, CustomElementGUI::new, CustomElement.class);
	public static final ModElementTypes<?> COMMAND = new ModElementTypes<>("command", 'c', BaseType.COMMAND, RecipeElementType.NONE, CommandGUI::new, Command.class);
	public static final ModElementTypes<?> DIMENSION = new ModElementTypes<>("dimension", 'd', BaseType.DIMENSION, RecipeElementType.ITEM, DimensionGUI::new, Dimension.class);
	public static final ModElementTypes<?> ENCHANTMENT = new ModElementTypes<>("enchantment", 'm', BaseType.ENCHANTMENT, RecipeElementType.NONE, EnchantmentGUI::new, Enchantment.class);
	public static final ModElementTypes<?> FLUID = new ModElementTypes<>("fluid", 'u', BaseType.BLOCK, RecipeElementType.BLOCK, FluidGUI::new, Fluid.class);
	public static final ModElementTypes<?> FOOD = new ModElementTypes<>("food", 'f', BaseType.ITEM, RecipeElementType.ITEM, FoodGUI::new, Food.class);
	public static final ModElementTypes<?> FUEL = new ModElementTypes<>("fuel", '/', BaseType.FUEL, RecipeElementType.NONE, FuelGUI::new, Fuel.class);
	public static final ModElementTypes<?> FUNCTION = new ModElementTypes<>("function", '\'', BaseType.DATAPACK, RecipeElementType.NONE, FunctionGUI::new, Function.class);
	public static final ModElementTypes<?> GAMERULE = new ModElementTypes<>("gamerule", ';', BaseType.GAMERULE, RecipeElementType.NONE, GameRuleGUI::new, GameRule.class);
	public static final ModElementTypes<?> GUI = new ModElementTypes<>("gui", 'g', BaseType.GUI, RecipeElementType.NONE, CustomGUIGUI::new, net.mcreator.element.types.GUI.class);
	public static final ModElementTypes<?> ITEM = new ModElementTypes<>("item", 'i', BaseType.ITEM, RecipeElementType.ITEM, ItemGUI::new, Item.class);
	public static final ModElementTypes<?> KEYBIND = new ModElementTypes<>("keybind", 'k', BaseType.KEYBIND, RecipeElementType.NONE, KeyBindGUI::new, KeyBinding.class);
	public static final ModElementTypes<?> LOOTTABLE = new ModElementTypes<>("loottable", 'l', BaseType.DATAPACK, RecipeElementType.NONE, LootTableGUI::new, LootTable.class);
	public static final ModElementTypes<?> MOB = new ModElementTypes<>("mob", "entity", 'e', BaseType.ENTITY, RecipeElementType.NONE, LivingEntityGUI::new, Mob.class);
	public static final ModElementTypes<?> MUSICDISC = new ModElementTypes<>("musicdisc",'x', BaseType.OTHER, RecipeElementType.ITEM, MusicDiscGUI::new, MusicDisc.class);
	public static final ModElementTypes<?> OVERLAY = new ModElementTypes<>("overlay", 'v', BaseType.OVERLAY, RecipeElementType.NONE, OverlayGUI::new, Overlay.class);
	public static final ModElementTypes<?> PAINTING = new ModElementTypes<>("painting", '.', BaseType.OTHER, RecipeElementType.NONE, PaintingGUI::new, Painting.class);
	public static final ModElementTypes<?> PARTICLE = new ModElementTypes<>("particle", 'y', BaseType.PARTICLE, RecipeElementType.NONE, ParticleGUI::new, Particle.class);
	public static final ModElementTypes<?> PLANT = new ModElementTypes<>("plant", 'n', BaseType.BLOCK, RecipeElementType.BLOCK, PlantGUI::new, Plant.class);
	public static final ModElementTypes<?> POTIONEFFECT = new ModElementTypes<>("potion", "potioneffect", "potioneffect", 'z', BaseType.POTIONEFFECT, RecipeElementType.NONE, PotionEffectGUI::new, PotionEffect.class);
	public static final ModElementTypes<?> POTIONITEM = new ModElementTypes<>("potionitem", ',', BaseType.POTIONITEM, RecipeElementType.NONE, PotionItemGUI::new, PotionItem.class);
	public static final ModElementTypes<?> PROCEDURE = new ModElementTypes<>("procedure", 'p', BaseType.PROCEDURE, RecipeElementType.NONE, ProcedureGUI::new, Procedure.class);
	public static final ModElementTypes<?> RANGEDITEM = new ModElementTypes<>("gun", "rangeditem", "rangeditem", 'q', BaseType.ITEM, RecipeElementType.ITEM, RangedItemGUI::new, RangedItem.class);
	public static final ModElementTypes<?> RECIPE = new ModElementTypes<>("recipe", 'r', BaseType.DATAPACK, RecipeElementType.NONE, RecipeGUI::new, Recipe.class);
	public static final ModElementTypes<?> STRUCTURE = new ModElementTypes<>("structure", 's', BaseType.STRUCTURE, RecipeElementType.NONE, StructureGenGUI::new, Structure.class);
	public static final ModElementTypes<?> TAB = new ModElementTypes<>("tab", 'w', BaseType.TAB, RecipeElementType.NONE, TabGUI::new, Tab.class);
	public static final ModElementTypes<?> TAG = new ModElementTypes<>("tag", 'j', BaseType.OTHER, RecipeElementType.NONE, TagGUI::new, Tag.class);
	public static final ModElementTypes<?> TOOL = new ModElementTypes<>("tool", 't', BaseType.ITEM, RecipeElementType.ITEM, ToolGUI::new, Tool.class);

	//Constructor used for the same registry name, localization ID and icon ID
	private ModElementTypes(String registryName, Character shortcut, BaseType baseType, RecipeElementType recipeElementType,
			ModElementGUIProvider<GE> modElementGUIProvider, Class<? extends GE> modElementStorageClass) {
		this(registryName, registryName, registryName, shortcut, baseType, recipeElementType, modElementGUIProvider, modElementStorageClass);
	}

	//Constructor used for the same registry name and localization ID.
	private ModElementTypes(String registryName, String iconID, Character shortcut, BaseType baseType, RecipeElementType recipeElementType,
			ModElementGUIProvider<GE> modElementGUIProvider, Class<? extends GE> modElementStorageClass) {
		this(registryName, registryName, iconID, shortcut, baseType, recipeElementType, modElementGUIProvider, modElementStorageClass);
	}

	//Used to have a different name and registry name
	private ModElementTypes(String registryName, String name, String iconID, Character shortcut, BaseType baseType, RecipeElementType recipeElementType,
			ModElementGUIProvider<GE> modElementGUIProvider, Class<? extends GE> modElementStorageClass) {
		this.baseType = baseType;
		this.recipeElementType = recipeElementType;
		this.registryName = registryName;
		this.name = name;
		this.iconID = iconID;
		this.shortcut = shortcut;

		this.readableName = L10N.t("modelement." + name.toLowerCase(Locale.ENGLISH));
		this.description = L10N.t("modelement." + name.toLowerCase(Locale.ENGLISH) + ".description");

		this.modElementGUIProvider = modElementGUIProvider;
		this.modElementStorageClass = modElementStorageClass;

		for (Field field : modElementStorageClass.getFields())
			if (field.getType().isAssignableFrom(net.mcreator.element.parts.Procedure.class)) {
				hasProcedureTriggers = true;
				break;
			}

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

	public ModElementGUI<GE> getModElement(MCreator mcreator, ModElement modElement, boolean editingMode) {
		return modElementGUIProvider.get(mcreator, modElement, editingMode);
	}

	public Class<? extends GeneratableElement> getModElementStorageClass() {
		return modElementStorageClass;
	}

	public GeneratorStats.CoverageStatus getStatus() {
		return status;
	}

	public boolean hasProcedureTriggers() {
		return hasProcedureTriggers;
	}

	public void setStatus(GeneratorStats.CoverageStatus status) {
		this.status = status;
	}

	@Override public int compareTo(@NotNull ModElementTypes o) {
		return o.getStatus().ordinal() - status.ordinal();
	}

	public static ModElementTypes<?> get(String modElementName){
		for(ModElementTypes<?> me : elements){
			if(me.registryName.equals(modElementName)){
				return me;
			}
		}
		return null;
	}

	private interface ModElementGUIProvider<GE extends GeneratableElement> {
		ModElementGUI<GE> get(MCreator mcreator, ModElement modElement, boolean editingMode);
	}
}
