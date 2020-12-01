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

package net.mcreator.ui.minecraft.potions;

import net.mcreator.element.parts.EffectEntry;
import net.mcreator.element.parts.EntityEntry;
import net.mcreator.element.types.Biome;
import net.mcreator.element.types.PotionItem;
import net.mcreator.minecraft.ElementUtil;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.UIRES;
import net.mcreator.workspace.Workspace;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JPotionListEntry extends JPanel {

	private final JSpinner duration = new JSpinner(new SpinnerNumberModel(3600, 1, 72000, 1));
	private final JSpinner amplifier = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
	private final JCheckBox showParticles = new JCheckBox();
	private final JComboBox<String> effect = new JComboBox<>();

	private final Workspace workspace;

	public JPotionListEntry(MCreator mcreator, JPanel parent, List<JPotionListEntry> entryList) {
		super(new FlowLayout(FlowLayout.LEFT));

		this.workspace = mcreator.getWorkspace();

		final JComponent container = PanelUtils.expandHorizontally(this);

		parent.add(container);
		entryList.add(this);

		ElementUtil.loadAllPotionEffects(workspace).forEach(e -> effect.addItem(e.getName()));

		add(new JLabel("Effect: "));
		add(effect);

		add(HelpUtils.wrapWithHelpButton(IHelpContext.NONE.withEntry("potionitem/duration"), new JLabel("Duration: ")));
		add(duration);

		add(HelpUtils
				.wrapWithHelpButton(IHelpContext.NONE.withEntry("potionitem/amplifier"), new JLabel("Amplifier: ")));
		add(amplifier);

		add(HelpUtils.wrapWithHelpButton(IHelpContext.NONE.withEntry("potionitem/showParticles"),
				new JLabel("Show particles: ")));
		add(showParticles);

		JButton remove = new JButton(UIRES.get("16px.clear"));
		remove.setText("Remove this entry");
		remove.addActionListener(e -> {
			entryList.remove(this);
			parent.remove(container);
			parent.revalidate();
			parent.repaint();
		});
		add(remove);

		parent.revalidate();
		parent.repaint();
	}

	public PotionItem.CustomEffectEntry getEntry() {
		PotionItem.CustomEffectEntry entry = new PotionItem.CustomEffectEntry();
		entry.effect = new EffectEntry(workspace, (String) effect.getSelectedItem());
		entry.duration = (int) duration.getValue();
		entry.amplifier = (int) amplifier.getValue();
		entry.showParticles = showParticles.isSelected();
		return entry;
	}

	public void setEntry(PotionItem.CustomEffectEntry e) {
		effect.setSelectedItem(e.effect.getUnmappedValue());
		duration.setValue(e.duration);
		amplifier.setValue(e.amplifier);
		showParticles.setSelected(e.showParticles);
	}
}
