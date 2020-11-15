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
import net.mcreator.element.parts.MItemBlock;
import net.mcreator.generator.GeneratorConfiguration;
import net.mcreator.generator.GeneratorStats;
import net.mcreator.minecraft.ElementUtil;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.action.ActionRegistry;
import net.mcreator.ui.action.BasicAction;
import net.mcreator.ui.component.JColor;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.dialogs.MCreatorDialog;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.minecraft.MCItemHolder;
import net.mcreator.ui.validation.component.VTextField;
import net.mcreator.util.StringUtils;
import net.mcreator.util.image.ImageUtils;
import net.mcreator.workspace.Workspace;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;

public class CustomPackMakerTool {

	public static void open(MCreator mcreator, PackMakerTool pmt) {
		MCreatorDialog dialog = new MCreatorDialog(mcreator, L10N.t("dialog.tools." + pmt.getPackID() + "_title"),
				true);
		dialog.setLayout(new BorderLayout(10, 10));

		dialog.add("North", PanelUtils.centerInPanel(L10N.label("dialog.tools." + pmt.getPackInfo() + "_info")));

		byte i = 1;
		if (pmt.getColor())
			i++;
		if (pmt.getPower() != null)
			i++;
		if (pmt.getBase())
			i++;
		JPanel props = new JPanel(new GridLayout(i, 2, 5, 5));
		JColor color;
		JSpinner power = null;
		MCItemHolder base;

		VTextField name = new VTextField(pmt.getName().getLength());
		name.enableRealtimeValidation();
		props.add(L10N.label("dialog.tools." + pmt.getPackID() + "_info"));
		props.add(name);

		if (pmt.getColor() && !pmt.getBase()) {
			color = new JColor(mcreator, false);
			props.add(L10N.label("dialog.tools." + pmt.getColor() + "wood_pack_color_accent"));
			props.add(color);
		}
		if (pmt.getPower() != null) {
			power = new JSpinner(
					new SpinnerNumberModel(pmt.getPower().getValue(), pmt.getPower().getMin(), pmt.getPower().getMax(),
							pmt.getPower().getStepSize()));
			props.add(L10N.label("dialog.tools." + pmt.getPackID() + "_power_factor"));
			props.add(power);
		}
		if (pmt.getBase()) {
			JColor color1 = new JColor(mcreator, false);
			props.add(L10N.label("dialog.tools." + pmt.getColor() + "wood_pack_color_accent"));
			props.add(color1);

			base = new MCItemHolder(mcreator, ElementUtil::loadBlocksAndItems);
			props.add(L10N.label("dialog.tools." + pmt.getPackID() + "_base_item"));
			props.add(PanelUtils.centerInPanel(base));

			base.setBlockSelectedListener(e -> {
				try {
					if (base.getBlock() != null) {
						color1.setColor(ImageUtils
								.getAverageColor(ImageUtils.toBufferedImage(((ImageIcon) base.getIcon()).getImage()))
								.brighter().brighter());
						if (base.getBlock().getUnmappedValue().startsWith("CUSTOM:")) {
							name.setText(StringUtils
									.machineToReadableName(base.getBlock().getUnmappedValue().replace("CUSTOM:", ""))
									.split(" ")[0]);
						}
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

		/*
		ok.addActionListener(e -> {
			if (name.getValidationStatus().getValidationResultType() != Validator.ValidationResultType.ERROR) {
				dialog.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				addPackToWorkspace(mcreator, mcreator.getWorkspace(), name.getText(), color.getColor(),
						(Double) power.getValue());
				mcreator.mv.updateMods();
				dialog.setCursor(Cursor.getDefaultCursor());
				dialog.setVisible(false);
			}
		});
		 */

		dialog.setSize(600, 250);
		dialog.setLocationRelativeTo(mcreator);
		dialog.setVisible(true);

	}

	private static void addPackToWorkspace(MCreator mcreator, Workspace workspace, String name, @Nullable Color color,
			@Nullable double factor, @Nullable MItemBlock base) {

	}

	public static BasicAction getAction(ActionRegistry actionRegistry, PackMakerTool packMakerTool) {
		return new BasicAction(actionRegistry, L10N.t("action.pack_tools." + packMakerTool.getPackID()),
				e -> CustomPackMakerTool.open(actionRegistry.getMCreator(), packMakerTool)) {
			@Override public boolean isEnabled() {
				GeneratorConfiguration gc = actionRegistry.getMCreator().getWorkspace().getGenerator()
						.getGeneratorConfiguration();
				return gc.getGeneratorStats().getModElementTypeCoverageInfo().get(ModElementType.RECIPE)
						!= GeneratorStats.CoverageStatus.NONE
						&& gc.getGeneratorStats().getModElementTypeCoverageInfo().get(ModElementType.ARMOR)
						!= GeneratorStats.CoverageStatus.NONE;
			}
		};
	}
}
