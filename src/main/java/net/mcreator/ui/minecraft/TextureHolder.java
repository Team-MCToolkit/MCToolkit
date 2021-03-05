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

package net.mcreator.ui.minecraft;

import net.mcreator.ui.dialogs.GeneralTextureSelector;
import net.mcreator.ui.init.UIRES;
import net.mcreator.ui.validation.component.VButton;
import net.mcreator.util.image.ImageUtils;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class TextureHolder extends VButton {

	private final GeneralTextureSelector td;
	private final int size;
	private String id = "";
	private ActionListener actionListener;
	private boolean removeButtonHover;

	public TextureHolder(GeneralTextureSelector td) {
		this(td, 70);
	}

	public TextureHolder(GeneralTextureSelector td, int size) {
		super("");
		this.td = td;

		this.size = size;

		setMargin(new Insets(0, 0, 0, 0));
		setPreferredSize(new Dimension(this.size, this.size));
		td.getConfirmButton().addActionListener(event -> {
			if (td.list.getSelectedValue() != null) {
				File file = td.list.getSelectedValue();
				id = file.getPath();
				id = GeneralTextureSelector.fixName(id, td.getMCreator().getFolderManager());
				if (id.startsWith("minecraft:")) {
					setIcon(new ImageIcon(ImageUtils.resize(UIRES.get("tag").getImage(), size)));
				} else {
					setIcon(new ImageIcon(ImageUtils.resize(new ImageIcon(td.list.getSelectedValue().toString()).getImage(), this.size)));
				}
				td.setVisible(false);
				if (actionListener != null)
					actionListener.actionPerformed(new ActionEvent(this, 0, ""));
				getValidationStatus();
				if (id.startsWith("minecraft:")) {
					setToolTipText(id);
				} else {
					setToolTipText(id.substring(1));
				}
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				if (isEnabled()) {
					if (e.getX() > 1 && e.getX() < 11 && e.getY() < getHeight() - 1 && e.getY() > getHeight() - 11
							&& !id.equals("")) {
						id = "";
						setIcon(null);
						getValidationStatus();
						setToolTipText("");
					} else {
						td.setVisible(true);
					}
					repaint();
				}
			}

			@Override public void mouseExited(MouseEvent e) {
				removeButtonHover = false;
				repaint();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override public void mouseMoved(MouseEvent e) {
				super.mouseMoved(e);
				removeButtonHover =
						e.getX() > 1 && e.getX() < 11 && e.getY() < getHeight() - 1 && e.getY() > getHeight() - 11;
				repaint();
			}
		});
	}

	@Override public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (!id.equals("")) {
			ImageIcon removeIcon;
			if (!removeButtonHover || !isEnabled()) {
				removeIcon = ImageUtils.changeSaturation(UIRES.get("18px.remove"), 0.4f);
			} else {
				removeIcon = UIRES.get("18px.remove");
			}
			g.drawImage(removeIcon.getImage(), 0, getHeight() - 11, 11, 11, null);
		}
	}

	public String getID() {
		return id;
	}

	public void setTextureFromTextureName(String texture) {
		if (texture != null && !texture.equals("")) {
			id = texture;
			setToolTipText(texture);
			if (td.getTextureType() == GeneralTextureSelector.TextureType.BLOCK)
				setIcon(new ImageIcon(ImageUtils
						.resize(td.getMCreator().getFolderManager().getBlockImageIcon(texture).getImage(), this.size)));
			else
				setIcon(new ImageIcon(ImageUtils
						.resize(td.getMCreator().getFolderManager().getItemImageIcon(texture).getImage(), this.size)));
			if(texture.contains("minecraft:"))
				setIcon(new ImageIcon(ImageUtils.resize(UIRES.get("tag").getImage(), size)));
		}
	}

	public boolean has() {
		return id != null && !id.equals("");
	}

	public void setActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}

}
