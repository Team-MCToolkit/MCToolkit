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

package net.mcreator.ui.dialogs.tools;

import net.mcreator.element.ModElementType;
import net.mcreator.element.parts.MItemBlock;
import net.mcreator.element.parts.Material;
import net.mcreator.element.parts.StepSound;
import net.mcreator.element.types.Block;
import net.mcreator.generator.GeneratorConfiguration;
import net.mcreator.generator.GeneratorStats;
import net.mcreator.minecraft.ElementUtil;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.action.ActionRegistry;
import net.mcreator.ui.action.BasicAction;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.dialogs.GeneralTextureSelector;
import net.mcreator.ui.dialogs.MCreatorDialog;
import net.mcreator.ui.dialogs.tools.util.RecipeUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.init.UIRES;
import net.mcreator.ui.minecraft.MCItemHolder;
import net.mcreator.ui.minecraft.TextureHolder;
import net.mcreator.ui.validation.Validator;
import net.mcreator.ui.validation.component.VTextField;
import net.mcreator.ui.validation.validators.ModElementNameValidator;
import net.mcreator.util.StringUtils;
import net.mcreator.workspace.Workspace;
import net.mcreator.workspace.elements.ModElement;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class BuildingPackMakerTool {

	private static void open(MCreator mcreator){
		MCreatorDialog dialog = new MCreatorDialog(mcreator, L10N.t("dialog.tools.building_pack.title"), true);
		dialog.setLayout(new BorderLayout(10, 10));
		dialog.setIconImage(UIRES.get("16px.woodpack").getImage());
		dialog.add("North", PanelUtils.centerInPanel(L10N.label("dialog.tools.building_pack.info")));

		JPanel props = new JPanel(new GridLayout(0, 1, 2, 2));

		JScrollPane scrollPane = new JScrollPane(props);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);

		dialog.add("Center", scrollPane);

		VTextField name = new VTextField(25);
		TextureHolder texture = new TextureHolder(new GeneralTextureSelector(mcreator, GeneralTextureSelector.TextureType.BLOCK));
		JSpinner factor = new JSpinner(new SpinnerNumberModel(1.0, 0.01, 1000000000, 1.0));

		name.enableRealtimeValidation();

		props.add(PanelUtils.gridElements(1, 2, L10N.label("dialog.tools.building_pack.name"), name));
		props.add(PanelUtils.gridElements(1, 2, L10N.label("dialog.tools.building_pack.texture"), texture));
		props.add(PanelUtils.gridElements(1, 2, L10N.label("dialog.tools.building_pack.factor"), factor));

		name.setValidator(new ModElementNameValidator(mcreator.getWorkspace(), name));

		List<Consumer<Boolean>> callables = new ArrayList<>();
		JCheckBox normalBlock = L10N.checkbox("elementgui.common.enable");
		callables.add(addBlock(mcreator, L10N.label("dialog.tools.building_pack.normal"), normalBlock,
				props, true, "Normal", name.getText(), texture.getText(), (Double) factor.getValue(), false));
		callables.add(addBlock(mcreator, L10N.label("dialog.tools.building_pack.stairs"), L10N.checkbox("elementgui.common.enable"),
				props, true, "Stairs", name.getText(), texture.getText(), (Double) factor.getValue(), normalBlock.isSelected()));
		callables.add(addBlock(mcreator, L10N.label("dialog.tools.building_pack.slab"), L10N.checkbox("elementgui.common.enable"),
				props, true, "Slab", name.getText(), texture.getText(), (Double) factor.getValue(), normalBlock.isSelected()));
		callables.add(addBlock(mcreator, L10N.label("dialog.tools.building_pack.wall"), L10N.checkbox("elementgui.common.enable"),
				props, true, "Wall", name.getText(), texture.getText(), (Double) factor.getValue(), normalBlock.isSelected()));
		callables.add(addBlock(mcreator, L10N.label("dialog.tools.building_pack.fence"), L10N.checkbox("elementgui.common.enable"),
				props, true, "Fence", name.getText(), texture.getText(), (Double) factor.getValue(), normalBlock.isSelected()));
		callables.add(addBlock(mcreator, L10N.label("dialog.tools.building_pack.fencegate"), L10N.checkbox("elementgui.common.enable"),
				props, true, "Fence Gate", name.getText(), texture.getText(), (Double) factor.getValue(), normalBlock.isSelected()));
		callables.add(addBlock(mcreator, L10N.label("dialog.tools.building_pack.trapdoor"), L10N.checkbox("elementgui.common.enable"),
				props, true, "Trap Door", name.getText(), texture.getText(), (Double) factor.getValue(), normalBlock.isSelected()));
		callables.add(addBlock(mcreator, L10N.label("dialog.tools.building_pack.door"), L10N.checkbox("elementgui.common.enable"),
				props, true, "Door", name.getText(), texture.getText(), (Double) factor.getValue(), normalBlock.isSelected()));
		callables.add(addBlock(mcreator, L10N.label("dialog.tools.building_pack.button"), L10N.checkbox("elementgui.common.enable"),
				props, true, "Stone Button", name.getText(), texture.getText(), (Double) factor.getValue(), normalBlock.isSelected()));
		callables.add(addBlock(mcreator, L10N.label("dialog.tools.building_pack.pressureplate"), L10N.checkbox("elementgui.common.enable"),
				props, true, "Pressure Plate", name.getText(), texture.getText(), (Double) factor.getValue(), normalBlock.isSelected()));

		dialog.add("Center", scrollPane);
		JButton ok = L10N.button("dialog.tools.building_pack_create");
		JButton cancel = L10N.button(UIManager.getString("OptionPane.cancelButtonText"));
		cancel.addActionListener(e -> dialog.setVisible(false));
		dialog.add("South", PanelUtils.join(ok, cancel));

		ok.addActionListener(e -> {
			dialog.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			callables.forEach(c -> c.accept(false));
			mcreator.mv.updateMods();
			dialog.setCursor(Cursor.getDefaultCursor());
			dialog.setVisible(false);
		});

		dialog.setSize(740, 420);
		dialog.setLocationRelativeTo(mcreator);
		dialog.setVisible(true);
	}

	private static Consumer<Boolean> addBlock(MCreator mcreator, JLabel text, JCheckBox box, JPanel panel, boolean checked,
			String type, String name, String texture, double factor, boolean generateRecipe) {
		box.setSelected(checked);
		panel.add(PanelUtils.centerAndEastElement(text, box));

		return altcondition -> {
			if (box.isSelected() || altcondition)
				addBlockToWorkspace(mcreator, mcreator.getWorkspace(), type, name, texture, factor, generateRecipe);
		};
	}

	private static void addBlockToWorkspace(MCreator mcreator, Workspace workspace, String type, String name, String texture,
			double factor, boolean generateRecipe) {
		String fullname = type.equals("Normal") ? name : name + type.replace(" ", "");
		Block block = new Block(new ModElement(workspace, fullname, ModElementType.BLOCK));
		block.name = type.equals("Normal") ? name : name + " " + type;;
		if(!type.equals("Normal"))
			block.blockBase = type.replace(" ", "");
		block.material = new Material(workspace, "ROCK");
		block.texture = texture;
		block.renderType = 11; // single texture
		block.customModelName = "Single texture";
		block.soundOnStep = new StepSound(workspace, "STONE");
		block.hardness = 1.5 * factor;
		block.resistance = 6.0 * factor;
		block.destroyTool = "pickaxe";
		if(type.equals("Pressure Plate"))
			block.sensitivity = "EVERYTHING";
		mcreator.getWorkspace().getModElementManager().storeModElementPicture(block);
		mcreator.getWorkspace().addModElement(block.getModElement());
		mcreator.getWorkspace().getGenerator().generateElement(block);
		mcreator.getWorkspace().getModElementManager().storeModElement(block);

		if(generateRecipe) {
			String blockName = name;
			switch (type) {
			case "Stairs":
				RecipeUtils.stairs(mcreator, workspace, blockName, fullname + "Recipe", block.getModElement().getName());
				RecipeUtils.stoneCutting(mcreator, workspace, blockName, fullname + "Recipe", block.getModElement().getName(), 1);
				break;
			case "Slab":
				RecipeUtils.slab(mcreator, workspace, blockName, fullname + "Recipe", block.getModElement().getName());
				RecipeUtils.stoneCutting(mcreator, workspace, blockName, fullname + "Recipe", block.getModElement().getName(), 2);
				break;
			case "Wall":
				RecipeUtils.wall(mcreator, workspace, blockName, fullname + "Recipe", block.getModElement().getName());
				RecipeUtils.stoneCutting(mcreator, workspace, blockName, fullname + "Recipe", block.getModElement().getName(), 1);
				break;
			case "Fence":
				RecipeUtils.fence(mcreator, workspace, blockName, fullname + "Recipe", block.getModElement().getName());
				break;
			case "Fence Gate":
				RecipeUtils.fenceGate(mcreator, workspace, blockName, fullname + "Recipe", block.getModElement().getName());
				break;
			case "Trap Door":
				RecipeUtils.wall(mcreator, workspace, blockName, fullname + "Recipe", block.getModElement().getName());
				break;
			case "Door":
				RecipeUtils.door(mcreator, workspace, blockName, fullname + "Recipe", block.getModElement().getName());
				break;
			case "Stone Button":
				RecipeUtils.button(mcreator, workspace, blockName,  fullname + "Recipe", block.getModElement().getName());
				RecipeUtils.stoneCutting(mcreator, workspace, blockName, fullname + "Recipe", block.getModElement().getName(), 1);
				break;
			case "Pressure Plate":
				RecipeUtils.pressurePlate(mcreator, workspace, blockName, fullname + "Recipe", block.getModElement().getName());
				break;
			}
		}
	}

	public static BasicAction getAction(ActionRegistry actionRegistry) {
		return new BasicAction(actionRegistry, L10N.t("action.pack_tools.stone"),
				e -> open(actionRegistry.getMCreator())) {
			@Override public boolean isEnabled() {
				GeneratorConfiguration gc = actionRegistry.getMCreator().getWorkspace().getGenerator()
						.getGeneratorConfiguration();
				return gc.getGeneratorStats().getModElementTypeCoverageInfo().get(ModElementType.RECIPE)
						!= GeneratorStats.CoverageStatus.NONE
						&& gc.getGeneratorStats().getModElementTypeCoverageInfo().get(ModElementType.BLOCK)
						!= GeneratorStats.CoverageStatus.NONE;
			}
		}.setIcon(UIRES.get("16px.woodpack"));
	}

}
