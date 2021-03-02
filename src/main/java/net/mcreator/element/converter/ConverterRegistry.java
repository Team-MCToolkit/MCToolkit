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

package net.mcreator.element.converter;

import net.mcreator.element.converter.fv10.BiomeSpawnListConverter;
import net.mcreator.element.converter.fv11.GUICoordinateConverter;
import net.mcreator.element.converter.fv11.OverlayCoordinateConverter;
import net.mcreator.element.converter.fv12.BiomeDefaultFeaturesConverter;
import net.mcreator.element.converter.fv13.ProcedureSpawnGemPickupDelayFixer;
import net.mcreator.element.converter.fv14.BlockLuminanceFixer;
import net.mcreator.element.converter.fv14.DimensionLuminanceFixer;
import net.mcreator.element.converter.fv14.PlantLuminanceFixer;
import net.mcreator.element.converter.fv15.DimensionPortalSelectedFixer;
import net.mcreator.element.converter.fv16.EntityTexturesConverter;
import net.mcreator.element.converter.fv16.BlockBoundingBoxFixer;
import net.mcreator.element.converter.fv4.RecipeTypeConverter;
import net.mcreator.element.converter.fv5.AchievementFixer;
import net.mcreator.element.converter.fv6.GUIBindingInverter;
import net.mcreator.element.converter.fv7.ProcedureEntityDepFixer;
import net.mcreator.element.converter.fv8.OpenGUIProcedureDepFixer;
import net.mcreator.element.converter.fv9.ProcedureGlobalTriggerFixer;
import net.mcreator.element.registry.ModElementTypes;

import java.util.*;

public class ConverterRegistry {

	private static final Map<ModElementTypes<?>, List<IConverter>> converters = new HashMap<ModElementTypes<?>, List<IConverter>>() {{
		put(ModElementTypes.RECIPE, Collections.singletonList(new RecipeTypeConverter()));
		put(ModElementTypes.ADVANCEMENT, Collections.singletonList(new AchievementFixer()));
		put(ModElementTypes.GUI, Arrays.asList(new GUIBindingInverter(), new GUICoordinateConverter()));
		put(ModElementTypes.PROCEDURE, Arrays.asList(new ProcedureEntityDepFixer(), new OpenGUIProcedureDepFixer(),
				new ProcedureGlobalTriggerFixer(), new ProcedureSpawnGemPickupDelayFixer()));
		put(ModElementTypes.BIOME, Arrays.asList(new BiomeSpawnListConverter(), new BiomeDefaultFeaturesConverter()));
		put(ModElementTypes.OVERLAY, Collections.singletonList(new OverlayCoordinateConverter()));
		put(ModElementTypes.BLOCK, Arrays.asList(new BlockLuminanceFixer(), new BlockBoundingBoxFixer()));
		put(ModElementTypes.PLANT, Collections.singletonList(new PlantLuminanceFixer()));
		put(ModElementTypes.DIMENSION, Arrays.asList(new DimensionLuminanceFixer(), new DimensionPortalSelectedFixer()));
    	put(ModElementTypes.MOB, Collections.singletonList(new EntityTexturesConverter()));
	}};

	public static List<IConverter> getConvertersForModElementType(ModElementTypes<?> modElementType) {
		return converters.get(modElementType);
	}

}
