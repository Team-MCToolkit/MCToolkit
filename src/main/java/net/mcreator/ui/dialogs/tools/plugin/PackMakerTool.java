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

package net.mcreator.ui.dialogs.tools.plugin;

import net.mcreator.element.GeneratableElement;
import net.mcreator.element.types.Item;
import net.mcreator.ui.dialogs.tools.plugin.elements.Items;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PackMakerTool {

	public String packID;
	private NameField name;
	private boolean color;
	@Nullable private PowerSpinner power;
	private boolean itemBase;

	@Nullable private List<Texture> textures;
	@Nullable private List<Items> items;
	private List<String> mod_elements;

	public String getPackID() {
		return packID;
	}

	public NameField getName() {
		return name;
	}

	@Nullable public boolean getColor() {
		return color;
	}

	@Nullable public PowerSpinner getPower() {
		return power;
	}

	@Nullable public boolean getBase() {
		return itemBase;
	}

	@Nullable public List<Texture> getTextures() {
		return textures;
	}

	@Nullable public List<Items> getItems() {
		return items;
	}

	public List<String> getModElements() {
		return mod_elements;
	}

	public class NameField{
		private short length;
		public short getLength() {
			return length;
		}
	}

	public class PowerSpinner{
		private double value;
		private double min;
		private double max;
		private double stepSize;

		public double getValue() {
			return value;
		}

		public double getMin() {
			return min;
		}

		public double getMax() {
			return max;
		}

		public double getStepSize() {
			return stepSize;
		}
	}

	public class Texture{
		private List<String> textures;
		private String type;
		@Nullable private String armorType;
		private String name;

		public List<String> getTextures() {
			return textures;
		}

		public String getName() {
			return name;
		}

		public String getType() {
			return type;
		}

		@Nullable public String getArmorType() {
			return armorType;
		}
	}
}
