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

import net.mcreator.element.ModElementType;
import net.mcreator.element.ModElementTypeRegistry;
import net.mcreator.element.parts.MItemBlock;
import net.mcreator.element.types.Food;
import net.mcreator.element.types.Item;
import net.mcreator.generator.GeneratorConfiguration;
import net.mcreator.generator.GeneratorStats;
import net.mcreator.minecraft.ElementUtil;
import net.mcreator.minecraft.MCItem;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.action.ActionRegistry;
import net.mcreator.ui.action.BasicAction;
import net.mcreator.ui.component.JColor;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.dialogs.MCreatorDialog;
import net.mcreator.ui.dialogs.tools.plugin.elements.Items;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.minecraft.MCItemHolder;
import net.mcreator.ui.validation.Validator;
import net.mcreator.ui.validation.component.VTextField;
import net.mcreator.ui.validation.validators.ModElementNameValidator;
import net.mcreator.util.StringUtils;
import net.mcreator.util.image.ImageUtils;
import net.mcreator.workspace.Workspace;
import net.mcreator.workspace.elements.ModElement;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;

public class CustomPackMakerTool {

	public static void open(MCreator mcreator, PackMakerTool pmt) {
		MCreatorDialog dialog = new MCreatorDialog(mcreator, L10N.t("dialog.tools." + pmt.getPackID() + "_title"),
				true);
		dialog.setLayout(new BorderLayout(10, 10));

		dialog.add("North", PanelUtils.centerInPanel(L10N.label("dialog.tools." + pmt.getPackID() + "_info")));

		int i = 1;
		if (pmt.getColor())
			i++;
		if (pmt.getPower() != null)
			i++;
		if (pmt.getBase())
			i = i + 2;
		JPanel props = new JPanel(new GridLayout(i, 2, 5, 5));
		JColor color = new JColor(mcreator, false);
		JSpinner power = new JSpinner(
				new SpinnerNumberModel(pmt.getPower().getValue(), pmt.getPower().getMin(), pmt.getPower().getMax(),
						pmt.getPower().getStepSize()));;
		MCItemHolder base = new MCItemHolder(mcreator, ElementUtil::loadBlocksAndItems);;

		VTextField name = new VTextField(pmt.getName().getLength());
		name.enableRealtimeValidation();
		props.add(L10N.label("dialog.tools." + pmt.getPackID() + "_info"));
		props.add(name);

		if (pmt.getColor() && !pmt.getBase()) {
			props.add(L10N.label("dialog.tools." + pmt.getColor() + "_color_accent"));
			props.add(color);
		}
		if (pmt.getPower() != null) {
			props.add(L10N.label("dialog.tools." + pmt.getPackID() + "_power_factor"));
			props.add(power);
		}
		if (pmt.getBase()) {
			props.add(L10N.label("dialog.tools." + pmt.getPackID() + "_color_accent"));
			props.add(color);

			props.add(L10N.label("dialog.tools." + pmt.getPackID() + "_base_item"));
			props.add(PanelUtils.centerInPanel(base));

			base.setBlockSelectedListener(e -> {
				try {
					color.setColor(ImageUtils
							.getAverageColor(ImageUtils.toBufferedImage(((ImageIcon) base.getIcon()).getImage()))
							.brighter().brighter());
					if (base.getBlock().getUnmappedValue().startsWith("CUSTOM:")) {
						name.setText(StringUtils
								.machineToReadableName(base.getBlock().getUnmappedValue().replace("CUSTOM:", ""))
								.split(" ")[0]);
					}
				} catch (Exception ignored) {
				}
			});
		}

		dialog.add("Center", PanelUtils.centerInPanel(props));
		JButton ok = L10N.button("dialog.tools." + pmt.getPackID() + "_create");
		JButton cancel = L10N.button(UIManager.getString("OptionPane.cancelButtonText"));
		cancel.addActionListener(e -> dialog.setVisible(false));
		dialog.add("South", PanelUtils.join(ok, cancel));

		name.setValidator(new ModElementNameValidator(mcreator.getWorkspace(), name));

		ok.addActionListener(e -> {
			if (name.getValidationStatus().getValidationResultType() != Validator.ValidationResultType.ERROR) {
				dialog.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				addPackToWorkspace(mcreator, mcreator.getWorkspace(), pmt, name.getText(), color.getColor(),
						(Double) power.getValue(), base.getBlock());
				mcreator.mv.updateMods();
				dialog.setCursor(Cursor.getDefaultCursor());
				dialog.setVisible(false);
			}
		});

		dialog.setSize(600, 280);
		dialog.setLocationRelativeTo(mcreator);
		dialog.setVisible(true);

	}

	private static void addPackToWorkspace(MCreator mcreator, Workspace workspace, PackMakerTool pmt, String name, @Nullable Color color,
			@Nullable double factor, @Nullable MItemBlock base) {
		for()


		for(Items item : pmt.getItems()){

			String itemName = "";
			//"default" uses the name of the text field only
			if (item.getName().useTextField()){
				if(item.getName().getLocation().equals("before"))
					itemName = name + " " + item.getName().getName();
				else if(item.getName().getLocation().equals("after"))
					itemName = item.getName().getName() + " " + name;
			} else
				itemName = item.getName().getName();

			//Create the mod element
			switch(item.getElementType()){
			case "item":
				Item itemElement = (Item) ModElementTypeRegistry.REGISTRY.get(ModElementType.ITEM)
						.getModElement(mcreator, new ModElement(workspace, itemName.replace(" ", ""), ModElementType.ITEM), false)
						.getElementFromGUI();
				itemElement.name = itemName;
				itemElement.texture = item.getTexture();
				mcreator.getWorkspace().getModElementManager().storeModElementPicture(itemElement);
				mcreator.getWorkspace().addModElement(itemElement.getModElement());
				mcreator.getWorkspace().getGenerator().generateElement(itemElement);
				mcreator.getWorkspace().getModElementManager().storeModElement(itemElement);
				break;
			case "food":
				Food foodElement = (Food) ModElementTypeRegistry.REGISTRY.get(ModElementType.FOOD)
						.getModElement(mcreator, new ModElement(workspace, itemName, ModElementType.FOOD), false)
						.getElementFromGUI();
				foodElement.name = itemName;
				foodElement.texture = item.getTexture();
				mcreator.getWorkspace().getModElementManager().storeModElementPicture(foodElement);
				mcreator.getWorkspace().addModElement(foodElement.getModElement());
				mcreator.getWorkspace().getGenerator().generateElement(foodElement);
				mcreator.getWorkspace().getModElementManager().storeModElement(foodElement);
				break;
			default:
				PackMakerToolLoader.LOG.error("Unexpected value: " + item.getElementType());
			}

		}
	}

	public static BasicAction getAction(ActionRegistry actionRegistry, PackMakerTool packMakerTool) {
		return new BasicAction(actionRegistry, L10N.t("action.pack_tools." + packMakerTool.getPackID()),
				e -> CustomPackMakerTool.open(actionRegistry.getMCreator(), packMakerTool)) {
			@Override public boolean isEnabled() {
				GeneratorConfiguration gc = actionRegistry.getMCreator().getWorkspace().getGenerator()
						.getGeneratorConfiguration();
				return elements(packMakerTool, gc);
			}
		};
	}

	private static boolean elements(PackMakerTool pmt, GeneratorConfiguration gc){
		boolean b = false;
		for(String element : pmt.getModElements()){
			if(gc.getGeneratorStats().getModElementTypeCoverageInfo().get(ModElementType.valueOf(element))
					!= GeneratorStats.CoverageStatus.NONE)
				b = true;
			else
				break;
		}
		return b;
	}
}
