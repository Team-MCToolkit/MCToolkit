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

/*
 * MCreator (https://mcreator.net/)
 * Copyright (C) 2020 Pylo and contributors
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
import net.mcreator.element.registry.ModElementType;
import net.mcreator.element.types.*;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.modgui.*;
import net.mcreator.workspace.elements.ModElement;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ModElementTypeRegistry {

	public static List<ModElementType> elements = new ArrayList<>();

	public static final Map<ModElementType, ModTypeRegistration<?>> REGISTRY = new LinkedHashMap<ModElementType, ModTypeRegistration<?>>() {{
		put(ModElementType.BLOCK, new ModTypeRegistration<>('b', "block", BlockGUI::new, Block.class));
		put(ModElementType.ITEM, new ModTypeRegistration<>('i', "item", ItemGUI::new, Item.class));
		put(ModElementType.TOOL, new ModTypeRegistration<>('t', "tool", ToolGUI::new, Tool.class));
		put(ModElementType.FOOD, new ModTypeRegistration<>('f', "food", FoodGUI::new, Food.class));
		put(ModElementType.FLUID, new ModTypeRegistration<>('l', "fluid", FluidGUI::new, Fluid.class));
		put(ModElementType.ARMOR, new ModTypeRegistration<>('a', "armor", ArmorGUI::new, Armor.class));
		put(ModElementType.RANGEDITEM, new ModTypeRegistration<>('n', "rangeditem", RangedItemGUI::new, RangedItem.class));
		put(ModElementType.RECIPE, new ModTypeRegistration<>('r', "recipe", RecipeGUI::new, Recipe.class));
		put(ModElementType.FUEL, new ModTypeRegistration<>(null, "fuel", FuelGUI::new, Fuel.class));
		put(ModElementType.TAB, new ModTypeRegistration<>('w', "tab", TabGUI::new, Tab.class));
		put(ModElementType.MOB, new ModTypeRegistration<>('e', "entity", LivingEntityGUI::new, Mob.class));
		put(ModElementType.PLANT, new ModTypeRegistration<>('y', "plant", PlantGUI::new, Plant.class));
		put(ModElementType.STRUCTURE, new ModTypeRegistration<>('s', "structure", StructureGenGUI::new, Structure.class));
		put(ModElementType.BIOME, new ModTypeRegistration<>('o', "biome", BiomeGUI::new, Biome.class));
		put(ModElementType.DIMENSION, new ModTypeRegistration<>('d', "dimension", DimensionGUI::new, Dimension.class));
		put(ModElementType.ACHIEVEMENT, new ModTypeRegistration<>('h', "advancement", AchievementGUI::new, Achievement.class));
		put(ModElementType.COMMAND, new ModTypeRegistration<>('c', "command", CommandGUI::new, Command.class));
		put(ModElementType.KEYBIND, new ModTypeRegistration<>('k', "keybinding", KeyBindGUI::new, KeyBinding.class));
		put(ModElementType.GUI, new ModTypeRegistration<>('g', "gui", CustomGUIGUI::new, GUI.class));
		put(ModElementType.OVERLAY, new ModTypeRegistration<>('v', "overlay", OverlayGUI::new, Overlay.class));
		put(ModElementType.PROCEDURE, new ModTypeRegistration<>('p', "procedure", ProcedureGUI::new, Procedure.class));
		put(ModElementType.POTIONEFFECT, new ModTypeRegistration<>('z', "potioneffect", PotionEffectGUI::new, PotionEffect.class));
		put(ModElementType.ENCHANTMENT, new ModTypeRegistration<>('m', "enchantment", EnchantmentGUI::new, Enchantment.class));
		put(ModElementType.CODE, new ModTypeRegistration<>(null, "code", CustomElementGUI::new, CustomElement.class));
		put(ModElementType.TAG, new ModTypeRegistration<>('j', "tag", TagGUI::new, Tag.class));
		put(ModElementType.LOOTTABLE, new ModTypeRegistration<>('q', "loottable", LootTableGUI::new, LootTable.class));
		put(ModElementType.FUNCTION, new ModTypeRegistration<>('u', "function", FunctionGUI::new, Function.class));
		put(ModElementType.MUSICDISC, new ModTypeRegistration<>(null, "musicdisc", MusicDiscGUI::new, MusicDisc.class));
		put(ModElementType.PAINTING, new ModTypeRegistration<>(null, "painting", PaintingGUI::new, Painting.class));
		put(ModElementType.PARTICLE, new ModTypeRegistration<>(null, "particle", ParticleGUI::new, Particle.class));
		put(ModElementType.POTIONITEM, new ModTypeRegistration<>(null, "potionitem", PotionItemGUI::new, PotionItem.class));
	}};

	public static class ModTypeRegistration<GE extends GeneratableElement> {

		private final String icon_id;
		private final Character shortcut;

		private final ModElementGUIProvider<GE> modElementGUIProvider;
		private final Class<? extends GE> modElementStorageClass;

		private boolean hasProcedureTriggers;

		ModTypeRegistration(Character shortcut, String icon_id, ModElementGUIProvider<GE> modElementGUIProvider,
				Class<? extends GE> modElementStorageClass) {
			this.icon_id = icon_id;
			this.modElementGUIProvider = modElementGUIProvider;
			this.shortcut = shortcut;
			this.modElementStorageClass = modElementStorageClass;

			for (Field field : modElementStorageClass.getFields())
				if (field.getType().isAssignableFrom(net.mcreator.element.parts.Procedure.class)) {
					hasProcedureTriggers = true;
					break;
				}
		}

		public Character getShortcut() {
			return shortcut;
		}

		public String getIconID() {
			return icon_id;
		}

		public ModElementGUI<GE> getModElement(MCreator mcreator, ModElement modElement, boolean editingMode) {
			return modElementGUIProvider.get(mcreator, modElement, editingMode);
		}

		public Class<? extends GeneratableElement> getModElementStorageClass() {
			return modElementStorageClass;
		}

		public boolean hasProcedureTriggers() {
			return hasProcedureTriggers;
		}
	}

	private interface ModElementGUIProvider<GE extends GeneratableElement> {
		ModElementGUI<GE> get(MCreator mcreator, ModElement modElement, boolean editingMode);
	}

}
