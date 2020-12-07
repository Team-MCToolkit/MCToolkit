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

package net.mcreator.element.types;

import net.mcreator.element.GeneratableElement;
import net.mcreator.element.ModElementTypeRegistry;
import net.mcreator.element.parts.EffectEntry;
import net.mcreator.minecraft.MinecraftImageGenerator;
import net.mcreator.workspace.elements.ModElement;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PotionItem extends GeneratableElement {
	public String potionName;
	public String splashName;
	public String lingeringName;
	public String arrowName;
	public List<CustomEffectEntry> effects;

	public PotionItem(ModElement element) {
		super(element);
		effects = new ArrayList<>();
	}

	public static class CustomEffectEntry {
		public EffectEntry effect;
		public int duration;
		public int amplifier;

		public boolean showParticles;
		public ModElementTypeRegistry.Effect potion;

		public boolean doesShowParticles() {
			return this.showParticles;
		}

		public ModElementTypeRegistry.Effect getPotion() {
			return this.potion == null ? null : this.potion.delegate.get();
		}

		public int getAmplifier() {
			return this.amplifier;
		}
	}

	public static Color getPotionColorFromEffectList(Collection<CustomEffectEntry> effects) {
		int i = 3694022;
		if (effects.isEmpty()) {
			return new Color(3694022, true);
		} else {
			float f = 0.0F;
			float f1 = 0.0F;
			float f2 = 0.0F;
			int j = 0;

			for(CustomEffectEntry effectentry : effects) {
				if (effectentry.doesShowParticles()) {
					int k = effectentry.getPotion().getLiquidColor();
					int l = effectentry.getAmplifier() + 1;
					f += (float)(l * (k >> 16 & 255)) / 255.0F;
					f1 += (float)(l * (k >> 8 & 255)) / 255.0F;
					f2 += (float)(l * (k >> 0 & 255)) / 255.0F;
					j += l;
				}
			}

			if (j == 0) {
				return new Color(0, true);
			} else {
				f = f / (float)j * 255.0F;
				f1 = f1 / (float)j * 255.0F;
				f2 = f2 / (float)j * 255.0F;
				return new Color((int)f << 16 | (int)f1 << 8 | (int)f2, true);
			}
		}
	}

	@Override public BufferedImage generateModElementPicture() {
		return MinecraftImageGenerator.Preview.generatePotionIcon(
				PotionItem.getPotionColorFromEffectList(effects));
	}
}
