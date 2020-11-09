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

package net.mcreator.ui.dialogs.workspace;

import net.mcreator.generator.Generator;
import net.mcreator.generator.GeneratorConfiguration;
import net.mcreator.generator.GeneratorFlavor;
import net.mcreator.ui.component.JEmptyBox;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.init.UIRES;

import javax.swing.*;
import java.awt.*;

public class ResourcepackWorkspacePanel extends AbstractWorkspacePanel {
	public ResourcepackWorkspacePanel(Window parent) {
		super(parent);
		add(new JEmptyBox(20, 20));

		add(PanelUtils
				.westAndEastElement(new JLabel("<html>Resource pack display name:<br><small>This name is shown in-game"),
						PanelUtils.join(workspaceDialogPanel.modName)));

		add(new JEmptyBox(5, 5));

		add(PanelUtils
				.westAndEastElement(new JLabel("<html>Resource pack description:<br><small>This description is shown in-game"),
						PanelUtils.join(workspaceDialogPanel.description)));

		add(new JEmptyBox(5, 5));

		add(PanelUtils.westAndEastElement(
				new JLabel("<html>Minecraft version (generator):<br><small>Target Java Edition version"),
				PanelUtils.join(workspaceDialogPanel.generatorSelector)));

		add(new JEmptyBox(30, 30));

		add(PanelUtils.westAndEastElement(new JLabel("Workspace folder:      "),
				PanelUtils.centerAndEastElement(workspaceFolder, selectWorkspaceFolder, 5, 5)));

		add(new JEmptyBox(30, 242));

		add(PanelUtils.join(FlowLayout.LEFT, new JLabel(UIRES.get("18px.info")), new JEmptyBox(0, 0), new JLabel(
				"<html><font color='#aaaaaa' size=2>Minecraft Java Edition Resource packs provides a way to customize Minecraft's assets without"
						+ "<br> any code modification.<br>")));

		validationGroup.addValidationElement(workspaceDialogPanel.modName);
		validationGroup.addValidationElement(workspaceFolder);

		workspaceDialogPanel.setFlavorFilter(GeneratorFlavor.RESOURCEPACK);

		workspaceDialogPanel.generator.removeAllItems();
		Generator.GENERATOR_CACHE.values().stream().filter(gc -> gc.getGeneratorFlavor() == GeneratorFlavor.RESOURCEPACK)
				.forEach(workspaceDialogPanel.generator::addItem);

		GeneratorConfiguration generatorConfiguration = GeneratorConfiguration
				.getRecommendedGeneratorForFlavor(Generator.GENERATOR_CACHE.values(), GeneratorFlavor.RESOURCEPACK);
		workspaceDialogPanel.generator.setSelectedItem(generatorConfiguration);
	}
}
