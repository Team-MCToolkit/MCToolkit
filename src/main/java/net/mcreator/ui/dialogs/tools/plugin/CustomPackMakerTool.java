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
import net.mcreator.element.parts.Material;
import net.mcreator.element.parts.StepSound;
import net.mcreator.element.types.Block;
import net.mcreator.element.types.Food;
import net.mcreator.element.types.Item;
import net.mcreator.generator.GeneratorConfiguration;
import net.mcreator.generator.GeneratorStats;
import net.mcreator.io.FileIO;
import net.mcreator.io.ResourcePointer;
import net.mcreator.minecraft.ElementUtil;
import net.mcreator.minecraft.RegistryNameFixer;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.action.ActionRegistry;
import net.mcreator.ui.action.BasicAction;
import net.mcreator.ui.component.JColor;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.dialogs.MCreatorDialog;
import net.mcreator.ui.dialogs.tools.plugin.elements.Blocks;
import net.mcreator.ui.dialogs.tools.plugin.elements.Items;
import net.mcreator.ui.init.ImageMakerTexturesCache;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.minecraft.MCItemHolder;
import net.mcreator.ui.validation.Validator;
import net.mcreator.ui.validation.component.VTextField;
import net.mcreator.ui.validation.validators.ModElementNameValidator;
import net.mcreator.ui.views.ArmorImageMakerView;
import net.mcreator.util.ListUtils;
import net.mcreator.util.StringUtils;
import net.mcreator.util.image.ImageUtils;
import net.mcreator.workspace.Workspace;
import net.mcreator.workspace.elements.ModElement;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Locale;

public class CustomPackMakerTool {

	public static void open(MCreator mcreator, PackMakerTool pmt) {
		MCreatorDialog dialog = new MCreatorDialog(mcreator, L10N.t("dialog.tools." + pmt.getPackID() + "_title"),
				true);
		dialog.setLayout(new BorderLayout(10, 10));

		dialog.add("North", PanelUtils.centerInPanel(L10N.label("dialog.tools." + pmt.getPackID() + "_info")));

		int i = 1;
		if (pmt.getUI().getColor())
			i++;
		if (pmt.getUI().getPower() != null)
			i++;
		if (pmt.getUI().getBase())
			i = i + 2;
		JPanel props = new JPanel(new GridLayout(i, 2, 5, 5));
		JColor color = new JColor(mcreator, false);
		JSpinner power = new JSpinner(
				new SpinnerNumberModel(pmt.getUI().getPower().getValue(), pmt.getUI().getPower().getMin(), pmt.getUI().getPower().getMax(),
						pmt.getUI().getPower().getStepSize()));
		MCItemHolder base = new MCItemHolder(mcreator, ElementUtil::loadBlocksAndItems);

		VTextField name = new VTextField(pmt.getUI().getName().getLength());
		name.enableRealtimeValidation();
		props.add(L10N.label("dialog.tools." + pmt.getPackID() + "_info"));
		props.add(name);

		if (pmt.getUI().getColor() && !pmt.getUI().getBase()) {
			props.add(L10N.label("dialog.tools." + pmt.getUI().getColor() + "_color_accent"));
			props.add(color);
		}
		if (pmt.getUI().getPower() != null) {
			props.add(L10N.label("dialog.tools." + pmt.getPackID() + "_power_factor"));
			props.add(power);
		}
		if (pmt.getUI().getBase()) {
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
			double factor, @Nullable MItemBlock base) {

		if (pmt.getTextures() != null) {
			for(PackMakerTool.Texture texture : pmt.getTextures()){
				if(!texture.getType().equals("armor")) {
					ImageIcon image = ImageUtils.colorize(ImageMakerTexturesCache.CACHE.get(new ResourcePointer(
									"templates/textures/texturemaker/" + ListUtils.getRandomItem(Arrays.asList(texture.getTextures().toArray())) + ".png")),
							color, true);
					String textureName = (texture.getName()).toLowerCase(Locale.ENGLISH);
					switch (texture.getType()) {
					case "item":
						FileIO.writeImageToPNGFile(ImageUtils.toBufferedImage(image.getImage()),
								mcreator.getWorkspace().getFolderManager()
										.getItemTextureFile(RegistryNameFixer.fix(textureName)));
						break;
					case "block":
						FileIO.writeImageToPNGFile(ImageUtils.toBufferedImage(image.getImage()),
								mcreator.getWorkspace().getFolderManager()
										.getBlockTextureFile(RegistryNameFixer.fix(textureName)));
						break;
					case "entity":
						FileIO.writeImageToPNGFile(ImageUtils.toBufferedImage(image.getImage()),
								mcreator.getWorkspace().getFolderManager()
										.getEntityTextureFile(RegistryNameFixer.fix(textureName)));
						break;
					case "painting":
						FileIO.writeImageToPNGFile(ImageUtils.toBufferedImage(image.getImage()),
								mcreator.getWorkspace().getFolderManager()
										.getPaintingTextureFile(RegistryNameFixer.fix(textureName)));
						break;
					case "other":
						FileIO.writeImageToPNGFile(ImageUtils.toBufferedImage(image.getImage()),
								mcreator.getWorkspace().getFolderManager()
										.getOtherTextureFile(RegistryNameFixer.fix(textureName)));
						break;
					}
				} else {
					ArmorImageMakerView
							.generateArmorImages(workspace, name.toLowerCase(Locale.ENGLISH), "", texture.getArmorType(), color, true);
				}
			}
		}

		if (pmt.getItems() != null) {
			for(Items item : pmt.getItems()){

				String itemName = "";
				//"default" uses the name of the text field only
				if (item.getName().useTextField()){
					if(item.getName().getLocation().equals("after"))
						itemName = name + " " + item.getName().getName();
					else if(item.getName().getLocation().equals("before"))
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

		if (pmt.getBlocks() != null) {
			for(Blocks block : pmt.getBlocks()){
				String blockName = "";
				//"default" uses the name of the text field only
				if (block.name.useTextField()){
					if(block.name.getLocation().equals("after"))
						blockName = name + " " + block.name.getName();
					else if(block.name.getLocation().equals("before"))
						blockName = block.name.getName() + " " + name;
				} else
					blockName = block.name.getName();

				//Create the mod element
				switch(block.elementType){
				case "block":
					Block blockElement = (Block) ModElementTypeRegistry.REGISTRY.get(ModElementType.BLOCK)
							.getModElement(mcreator, new ModElement(workspace, blockName.replace(" ", ""), ModElementType.BLOCK), false)
							.getElementFromGUI();
					blockElement.name = blockName;
					blockElement.texture = block.texture;
					if(block.textureBack != null)
						blockElement.textureBack = block.textureBack;
					if(block.textureFront != null)
						blockElement.textureFront = block.textureFront;
					if(block.textureLeft != null)
						blockElement.textureLeft = block.textureLeft;
					if(block.textureRight != null)
						blockElement.textureRight = block.textureRight;
					if(block.textureTop != null)
						blockElement.textureTop = block.textureTop;
					blockElement.customModelName = block.customModelName;
					if(block.blockBase != null)
						blockElement.blockBase = block.blockBase;
					blockElement.material = new Material(workspace, block.material);
					blockElement.soundOnStep = new StepSound(workspace, block.soundOnStep);
					blockElement.hardness = block.hardness * factor;
					blockElement.resistance = block.resistance * factor;
					blockElement.destroyTool = block.destroyTool;
					blockElement.breakHarvestLevel = block.breakHarvestLevel;
					blockElement.renderType = block.renderType;
					blockElement.flammability = (int) (block.flammability * factor);
					if(block.spawnWorldTypes != null){
						blockElement.spawnWorldTypes = block.spawnWorldTypes;
						blockElement.minGenerateHeight = block.minGenerateHeight;
						blockElement.maxGenerateHeight = block.maxGenerateHeight;
						blockElement.frequencyPerChunks = block.frequencyPerChunks;
						blockElement.frequencyOnChunk = block.frequencyOnChunk;
					}
					if(block.dropAmount >= 1) {
						blockElement.dropAmount = block.dropAmount;
					}
					mcreator.getWorkspace().getModElementManager().storeModElementPicture(blockElement);
					mcreator.getWorkspace().addModElement(blockElement.getModElement());
					mcreator.getWorkspace().getGenerator().generateElement(blockElement);
					mcreator.getWorkspace().getModElementManager().storeModElement(blockElement);
					break;
				default:
					PackMakerToolLoader.LOG.error("Unexpected value: " + block.elementType);
				}
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
