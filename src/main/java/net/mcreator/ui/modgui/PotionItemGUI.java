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

package net.mcreator.ui.modgui;

import net.mcreator.element.types.PotionItem;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.validation.AggregatedValidationResult;
import net.mcreator.workspace.elements.ModElement;
import org.jetbrains.annotations.NotNull;

public class PotionItemGUI extends ModElementGUI<PotionItem> {

	public PotionItemGUI(MCreator mcreator, @NotNull ModElement modElement, boolean editingMode) {
		super(mcreator, modElement, editingMode);
		this.initGUI();
		super.finalizeGUI();
	}

	@Override protected void initGUI() {

	}

	@Override protected AggregatedValidationResult validatePage(int page) {
		return null;
	}

	/**
	 * This method is called to open a mod element in the GUI
	 *
	 * @param generatableElement
	 */
	@Override protected void openInEditingMode(PotionItem generatableElement) {

	}

	@Override public PotionItem getElementFromGUI() {
		return null;
	}
}
