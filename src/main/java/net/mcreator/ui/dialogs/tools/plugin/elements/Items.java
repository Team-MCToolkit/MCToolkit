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

package net.mcreator.ui.dialogs.tools.plugin.elements;

import javax.annotation.Nullable;

public class Items {
	//Variables used by all item mod elements
	private String elementType;
	private Name name;
	private String texture;

	public String getElementType() {
		return elementType;
	}

	public Name getName() {
		return name;
	}

	public String getTexture() {
		return texture;
	}

	public class Name{
		private String name;
		private boolean useTextField;
		@Nullable private String location;

		public String getName() {
			return name;
		}

		public boolean useTextField() {
			return useTextField;
		}

		@Nullable public String getLocation() {
			return location;
		}
	}
}
