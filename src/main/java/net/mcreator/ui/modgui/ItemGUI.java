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

package net.mcreator.ui.modgui;

import net.mcreator.blockly.*;
import net.mcreator.blockly.data.*;
import net.mcreator.element.parts.Enchantment;
import net.mcreator.generator.blockly.OutputBlockCodeGenerator;
import net.mcreator.ui.blockly.*;
import net.mcreator.element.GeneratableElement;
import net.mcreator.element.ModElementType;
import net.mcreator.element.parts.TabEntry;
import net.mcreator.element.types.GUI;
import net.mcreator.element.types.Item;
import net.mcreator.generator.blockly.BlocklyBlockCodeGenerator;
import net.mcreator.generator.blockly.ProceduralBlockCodeGenerator;
import net.mcreator.generator.template.TemplateGeneratorException;
import net.mcreator.minecraft.DataListEntry;
import net.mcreator.minecraft.ElementUtil;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.MCreatorApplication;
import net.mcreator.ui.component.SearchableComboBox;
import net.mcreator.ui.component.util.ComboBoxUtil;
import net.mcreator.ui.component.util.ComponentUtils;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.dialogs.GeneralTextureSelector;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.laf.renderer.ModelComboBoxRenderer;
import net.mcreator.ui.minecraft.DataListComboBox;
import net.mcreator.ui.minecraft.MCItemHolder;
import net.mcreator.ui.minecraft.ProcedureSelector;
import net.mcreator.ui.minecraft.TextureHolder;
import net.mcreator.ui.validation.AggregatedValidationResult;
import net.mcreator.ui.validation.ValidationGroup;
import net.mcreator.ui.validation.component.VTextField;
import net.mcreator.ui.validation.validators.TextFieldValidator;
import net.mcreator.ui.validation.validators.TileHolderValidator;
import net.mcreator.util.ListUtils;
import net.mcreator.util.StringUtils;
import net.mcreator.workspace.elements.ModElement;
import net.mcreator.workspace.elements.VariableElementType;
import net.mcreator.workspace.resources.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.*;
import java.awt.*;
import java.io.StringReader;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.net.URI;
import java.net.URISyntaxException;

public class ItemGUI extends ModElementGUI<Item> {

	private TextureHolder texture;

	private final JSpinner stackSize = new JSpinner(new SpinnerNumberModel(64, 0, 64, 1));
	private final VTextField name = new VTextField(20);
	private final JComboBox<String> rarity = new JComboBox<>(new String[] { "COMMON", "UNCOMMON", "RARE", "EPIC" });

	private final MCItemHolder recipeRemainder = new MCItemHolder(mcreator, ElementUtil::loadBlocksAndItems);

	private final JSpinner enchantability = new JSpinner(new SpinnerNumberModel(0, -100, 128000, 1));
	private final JSpinner useDuration = new JSpinner(new SpinnerNumberModel(0, -100, 128000, 1));
	private final JSpinner toolType = new JSpinner(new SpinnerNumberModel(1.0, -100.0, 128000.0, 0.1));
	private final JSpinner damageCount = new JSpinner(new SpinnerNumberModel(0, 0, 128000, 1));

	private final JCheckBox destroyAnyBlock = L10N.checkbox("elementgui.common.enable");
	private final JCheckBox stayInGridWhenCrafting = L10N.checkbox("elementgui.common.enable");
	private final JCheckBox damageOnCrafting = L10N.checkbox("elementgui.common.enable");
	private final JCheckBox hasGlow = L10N.checkbox("elementgui.item.glowing_effect");
	private final JCheckBox hasTooltip = L10N.checkbox("elementgui.item.has_tooltip");
	private ProcedureSelector glowCondition;

	private final DataListComboBox creativeTab = new DataListComboBox(mcreator);

	private final Model normal = new Model.BuiltInModel("Normal");
	private final Model tool = new Model.BuiltInModel("Tool");
	private final SearchableComboBox<Model> renderType = new SearchableComboBox<>();

	private ProcedureSelector onRightClickedInAir;
	private ProcedureSelector onCrafted;
	private ProcedureSelector onRightClickedOnBlock;
	private ProcedureSelector onEntityHitWith;
	private ProcedureSelector onItemInInventoryTick;
	private ProcedureSelector onItemInUseTick;
	private ProcedureSelector onStoppedUsing;
	private ProcedureSelector onEntitySwing;
	private ProcedureSelector onDroppedByPlayer;

	private final JCheckBox hasDispenseBehavior = L10N.checkbox("elementgui.common.enable");
	private ProcedureSelector dispenseSuccessCondition;
	private ProcedureSelector dispenseResultItemstack;

	private BlocklyPanel blocklyPanel;
	private final CompileNotesPanel compileNotesPanel = new CompileNotesPanel();
	private boolean hasErrors = false;
	private Map<String, ToolboxBlock> externalBlocks;

	private final ValidationGroup page1group = new ValidationGroup();

	private final JSpinner damageVsEntity = new JSpinner(new SpinnerNumberModel(0, 0, 128000, 0.1));
	private final JCheckBox enableMeleeDamage = new JCheckBox();

	private final JComboBox<String> guiBoundTo = new JComboBox<>();
	private final JSpinner inventorySize = new JSpinner(new SpinnerNumberModel(9, 0, 256, 1));
	private final JSpinner inventoryStackSize = new JSpinner(new SpinnerNumberModel(64, 1, 1024, 1));

	public ItemGUI(MCreator mcreator, ModElement modElement, boolean editingMode) {
		super(mcreator, modElement, editingMode);
		this.initGUI();
		super.finalizeGUI();
	}

	private void regenerateTooltipProcedures() {
		BlocklyBlockCodeGenerator blocklyBlockCodeGenerator = new BlocklyBlockCodeGenerator(externalBlocks,
				mcreator.getWorkspace().getGenerator().getGeneratorStats().getTooltipProcedures());

		BlocklyToTooltip blocklyToJava;
		try {
			blocklyToJava = new BlocklyToTooltip(mcreator.getWorkspace(), blocklyPanel.getXML(), null,
					new ProceduralBlockCodeGenerator(blocklyBlockCodeGenerator),
			        new OutputBlockCodeGenerator(blocklyBlockCodeGenerator));
		} catch (TemplateGeneratorException e) {
			return;
		}

		List<BlocklyCompileNote> compileNotesArrayList = blocklyToJava.getCompileNotes();

		SwingUtilities.invokeLater(() -> {
			compileNotesPanel.updateCompileNotes(compileNotesArrayList);
			hasErrors = false;
			for (BlocklyCompileNote note : compileNotesArrayList) {
				if (note.getType() == BlocklyCompileNote.Type.ERROR) {
					hasErrors = true;
					break;
				}
			}
		});
	}

	@Override protected void initGUI() {
		onRightClickedInAir = new ProcedureSelector(this.withEntry("item/when_right_clicked"), mcreator,
				L10N.t("elementgui.common.event_right_clicked_air"),
				Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack"));
		onCrafted = new ProcedureSelector(this.withEntry("item/on_crafted"), mcreator,
				L10N.t("elementgui.common.event_on_crafted"),
				Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack"));
		onRightClickedOnBlock = new ProcedureSelector(this.withEntry("item/when_right_clicked_block"), mcreator,
				L10N.t("elementgui.common.event_right_clicked_block"),
				Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack/direction:direction"));
		onEntityHitWith = new ProcedureSelector(this.withEntry("item/when_entity_hit"), mcreator,
				L10N.t("elementgui.item.event_entity_hit"),
				Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/sourceentity:entity/itemstack:itemstack"));
		onItemInInventoryTick = new ProcedureSelector(this.withEntry("item/inventory_tick"), mcreator,
				L10N.t("elementgui.item.event_inventory_tick"),
				Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack/slot:number"));
		onItemInUseTick = new ProcedureSelector(this.withEntry("item/hand_tick"), mcreator,
				L10N.t("elementgui.item.event_hand_tick"),
				Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack/slot:number"));
		onStoppedUsing = new ProcedureSelector(this.withEntry("item/when_stopped_using"), mcreator,
				L10N.t("elementgui.item.event_stopped_using"),
				Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack/time:number"));
		onEntitySwing = new ProcedureSelector(this.withEntry("item/when_entity_swings"), mcreator,
				L10N.t("elementgui.item.event_entity_swings"),
				Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack"));
		onDroppedByPlayer = new ProcedureSelector(this.withEntry("item/on_dropped"), mcreator,
				L10N.t("elementgui.item.event_on_dropped"),
				Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack"));
		glowCondition = new ProcedureSelector(this.withEntry("item/condition_glow"), mcreator,
				L10N.t("elementgui.item.condition_glow"), ProcedureSelector.Side.CLIENT, true,
				VariableElementType.LOGIC,
				Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack"));
		dispenseSuccessCondition = new ProcedureSelector(this.withEntry("item/dispense_success_condition"), mcreator,
				L10N.t("elementgui.item.dispense_success_condition"), VariableElementType.LOGIC,
				Dependency.fromString("x:number/y:number/z:number/world:world/itemstack:itemstack/direction:direction"));
		dispenseResultItemstack = new ProcedureSelector(this.withEntry("item/dispense_result_itemstack"), mcreator,
				L10N.t("elementgui.item.dispense_result_itemstack"), VariableElementType.ITEMSTACK,
				Dependency.fromString("x:number/y:number/z:number/world:world/itemstack:itemstack/direction:direction/success:boolean"))
				.setDefaultName("(provided itemstack)");

		guiBoundTo.addActionListener(e -> {
			if (!isEditingMode()) {
				String selected = (String) guiBoundTo.getSelectedItem();
				if (selected != null) {
					ModElement element = mcreator.getWorkspace().getModElementByName(selected);
					if (element != null) {
						GeneratableElement generatableElement = element.getGeneratableElement();
						if (generatableElement instanceof GUI) {
							inventorySize.setValue(((GUI) generatableElement).getMaxSlotID() + 1);
						}
					}
				}
			}
		});

		JPanel visual = new JPanel(new BorderLayout(10, 10));
		JPanel properties = new JPanel(new BorderLayout(10, 10));
		JPanel inventory = new JPanel(new BorderLayout(10, 10));
		JPanel triggers = new JPanel(new BorderLayout(10, 10));

		texture = new TextureHolder(new GeneralTextureSelector(mcreator, GeneralTextureSelector.TextureType.ITEM));
		texture.setOpaque(false);

		JPanel visualPanel = new JPanel(new BorderLayout(0, 10));
		visualPanel.setOpaque(false);
		JPanel visualProperties = new JPanel(new BorderLayout(15, 15));
		visualProperties.setOpaque(false);
		visualProperties.add("West", PanelUtils.totalCenterInPanel(
				ComponentUtils.squareAndBorder(texture,
				L10N.t("elementgui.item.texture"))));
		visualPanel.add("North", visualProperties);

		JComponent checkBoxes = PanelUtils.gridElements(2, 1,
				HelpUtils.wrapWithHelpButton(this.withEntry("item/glowing_effect"), hasGlow),
				HelpUtils.wrapWithHelpButton(this.withEntry("item/special_information"), hasTooltip));

		hasGlow.setOpaque(false);
		hasGlow.setSelected(false);
		hasTooltip.setOpaque(false);
		hasTooltip.setSelected(false);

		hasGlow.addActionListener(e -> updateGlowElements());

		ComponentUtils.deriveFont(renderType, 16.0f);

		JPanel modelPanel = new JPanel();
		modelPanel.setLayout(new BoxLayout(modelPanel, BoxLayout.PAGE_AXIS));

		modelPanel.setOpaque(false);
		modelPanel.add(PanelUtils.join(HelpUtils.wrapWithHelpButton(
				this.withEntry("item/model"),
				L10N.label("elementgui.common.item_model")),
				PanelUtils.join(renderType)));

		renderType.setPreferredSize(new Dimension(350, 42));
		renderType.setRenderer(new ModelComboBoxRenderer());

		visualProperties.add("Center", modelPanel);
		visualProperties.add("East", PanelUtils.join(FlowLayout.LEFT, checkBoxes, glowCondition));

		modelPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder((Color) UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 2),
				L10N.t("elementgui.item.item_3d_model"), 0, 0, getFont().deriveFont(12.0f),
				(Color) UIManager.get("MCreatorLAF.BRIGHT_COLOR")));

		externalBlocks = BlocklyLoader.INSTANCE.getTooltipBlockLoader().getDefinedBlocks();

		blocklyPanel = new BlocklyPanel(mcreator);
		blocklyPanel.addTaskToRunAfterLoaded(() -> {
			BlocklyLoader.INSTANCE.getTooltipBlockLoader()
					.loadBlocksAndCategoriesInPanel(blocklyPanel, ExternalBlockLoader.ToolboxType.TOOLTIP);
			blocklyPanel.getJSBridge()
					.setJavaScriptEventListener(() -> new Thread(ItemGUI.this::regenerateTooltipProcedures).start());

			// If it's not editing mode (it's opened for the first time) set the xml data to default
			if (!isEditingMode())
				blocklyPanel.setXML("<xml><block type=\"tooltip_start\" deletable=\"false\" x=\"40\" y=\"40\"></block></xml>");
		});

		JPanel ttBlockly = new JPanel(new GridLayout());
		ttBlockly.setOpaque(false);
		ttBlockly.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder((Color) UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1),
				L10N.t("elementgui.item.tooltip_procedures"), TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
				getFont(), Color.white));
		ttBlockly.add(PanelUtils.topToToeElement(
			new TooltipEditorToolbar(mcreator, blocklyPanel), blocklyPanel, compileNotesPanel
		));

		visual.add("Center", PanelUtils.centerInPanel(PanelUtils.centerAndSouthElement(visualPanel, ttBlockly)));

		visual.setOpaque(false);

		JPanel generalProperties = new JPanel(new GridLayout(13, 2, 2, 2));

		ComponentUtils.deriveFont(name, 16);

		generalProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("common/gui_name"),
				L10N.label("elementgui.common.name_in_gui")));
		generalProperties.add(name);

		generalProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/rarity"),
				L10N.label("elementgui.common.rarity")));
		generalProperties.add(rarity);

		generalProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("common/creative_tab"),
				L10N.label("elementgui.common.creative_tab")));
		generalProperties.add(creativeTab);

		generalProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/stack_size"),
				L10N.label("elementgui.common.max_stack_size")));
		generalProperties.add(stackSize);

		generalProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/enchantability"),
				L10N.label("elementgui.common.enchantability")));
		generalProperties.add(enchantability);

		generalProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/destroy_speed"),
				L10N.label("elementgui.item.destroy_speed")));
		generalProperties.add(toolType);

		generalProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/damage_vs_entity"),
				L10N.label("elementgui.item.damage_vs_entity")));
		generalProperties.add(PanelUtils.westAndCenterElement(enableMeleeDamage, damageVsEntity));

		generalProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/number_of_uses"),
				L10N.label("elementgui.item.number_of_uses")));
		generalProperties.add(damageCount);

		generalProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/can_destroy_any_block"),
				L10N.label("elementgui.item.can_destroy_any_block")));
		generalProperties.add(destroyAnyBlock);

		generalProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/container_item"),
				L10N.label("elementgui.item.container_item")));
		generalProperties.add(stayInGridWhenCrafting);

		generalProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/container_item_damage"),
				L10N.label("elementgui.item.container_item_damage")));
		generalProperties.add(damageOnCrafting);

		generalProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/recipe_remainder"),
				L10N.label("elementgui.item.recipe_remainder")));
		generalProperties.add(PanelUtils.centerInPanel(recipeRemainder));

		generalProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/use_duration"),
				L10N.label("elementgui.item.use_duration")));
		generalProperties.add(useDuration);

		enchantability.setOpaque(false);
		useDuration.setOpaque(false);
		toolType.setOpaque(false);
		damageCount.setOpaque(false);
		destroyAnyBlock.setOpaque(false);
		stayInGridWhenCrafting.setOpaque(false);
		damageOnCrafting.setOpaque(false);

		generalProperties.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder((Color) UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1),
				L10N.t("elementgui.item.properties_general"), TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
				getFont(), (Color) UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
		generalProperties.setOpaque(false);

		JComponent canDispense =  PanelUtils.gridElements(2,1, PanelUtils.centerInPanel(
				HelpUtils.wrapWithHelpButton(this.withEntry("item/has_dispense_behavior"),
						L10N.label("elementgui.item.has_dispense_behavior"))),
				PanelUtils.centerInPanel(hasDispenseBehavior));
		JComponent dispenseProcedures = PanelUtils.gridElements(2, 1, 0, 8, dispenseSuccessCondition, dispenseResultItemstack);

		hasDispenseBehavior.setOpaque(false);
		hasDispenseBehavior.setSelected(false);
		hasDispenseBehavior.addActionListener(e -> updateDispenseElements());

		JComponent dispenseProperties = PanelUtils.northAndCenterElement(canDispense, PanelUtils.centerInPanel(dispenseProcedures));
		dispenseProperties.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder((Color) UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1),
				L10N.t("elementgui.item.dispense_properties"), TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
				getFont(), (Color) UIManager.get("MCreatorLAF.BRIGHT_COLOR")
				)
		);
		dispenseProperties.setOpaque(false);

		properties.setOpaque(false);
		properties.add("Center", PanelUtils.totalCenterInPanel(
				PanelUtils.westAndEastElement(generalProperties, PanelUtils.pullElementUp(dispenseProperties)))
		);

		JComponent events = PanelUtils.gridElements(3,3,
				onRightClickedInAir, onRightClickedOnBlock, onCrafted,
				onEntityHitWith, onItemInInventoryTick, onItemInUseTick,
				onStoppedUsing, onEntitySwing, onDroppedByPlayer);

		triggers.add("Center", PanelUtils.totalCenterInPanel(
				PanelUtils.maxMargin(events, 20, true, true, true, true))
		);
		triggers.setOpaque(false);

		JPanel invPanel = new JPanel(new GridLayout(3, 2, 35, 2));
		invPanel.setOpaque(false);

		invPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/bind_gui"),
				L10N.label("elementgui.item.bind_gui")));
		invPanel.add(guiBoundTo);

		invPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/inventory_size"),
				L10N.label("elementgui.item.inventory_size")));
		invPanel.add(inventorySize);

		invPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/inventory_stack_size"),
				L10N.label("elementgui.common.max_stack_size")));
		invPanel.add(inventoryStackSize);

		inventory.add(PanelUtils.totalCenterInPanel(invPanel));
		inventory.setOpaque(false);

		texture.setValidator(new TileHolderValidator(texture));

		page1group.addValidationElement(texture);

		name.setValidator(new TextFieldValidator(name, L10N.t("elementgui.item.error_item_needs_name")));
		name.enableRealtimeValidation();

		addPage(L10N.t("elementgui.common.page_visual"), visual);
		addPage(L10N.t("elementgui.common.page_properties"), properties);
		addPage(L10N.t("elementgui.common.page_inventory"), inventory);
		addPage(L10N.t("elementgui.common.page_triggers"), triggers);

		if (!isEditingMode()) {
			String readableNameFromModElement = StringUtils.machineToReadableName(modElement.getName());
			name.setText(readableNameFromModElement);
		}
	}

	private void updateGlowElements() {
		glowCondition.setEnabled(hasGlow.isSelected());
	}

	private void updateDispenseElements() {
		dispenseSuccessCondition.setEnabled(hasDispenseBehavior.isSelected());
		dispenseResultItemstack.setEnabled(hasDispenseBehavior.isSelected());
	}

	@Override public void onSave() {
		super.onSave();
		String xml = blocklyPanel.getXML();
		// Map of `translation_key, untranslated_text`
		Map<String, String> map = new HashMap<String, String>();

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xml)));
			doc.getDocumentElement().normalize();

			Element start_block = BlocklyBlockUtil.getStartBlock(doc, "tooltip_start");
			List<Element> blocks = BlocklyBlockUtil.getBlockProcedureStartingWithNext(start_block);

			BlocklyToTooltip.processTooltipProcedure(map, blocks);

		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			mcreator.getWorkspace().setLocalization(key, value);
		}

	}

	@Override public void reloadDataLists() {
		super.reloadDataLists();
		onRightClickedInAir.refreshListKeepSelected();
		onCrafted.refreshListKeepSelected();
		onRightClickedOnBlock.refreshListKeepSelected();
		onEntityHitWith.refreshListKeepSelected();
		onItemInInventoryTick.refreshListKeepSelected();
		onItemInUseTick.refreshListKeepSelected();
		onStoppedUsing.refreshListKeepSelected();
		onEntitySwing.refreshListKeepSelected();
		onDroppedByPlayer.refreshListKeepSelected();
		glowCondition.refreshListKeepSelected();
		dispenseSuccessCondition.refreshListKeepSelected();
		dispenseResultItemstack.refreshListKeepSelected();

		ComboBoxUtil.updateComboBoxContents(creativeTab, ElementUtil.loadAllTabs(mcreator.getWorkspace()),
				new DataListEntry.Dummy("MISC"));

		ComboBoxUtil.updateComboBoxContents(renderType, ListUtils.merge(Arrays.asList(normal, tool),
				Model.getModelsWithTextureMaps(mcreator.getWorkspace()).stream()
						.filter(el -> el.getType() == Model.Type.JSON || el.getType() == Model.Type.OBJ)
						.collect(Collectors.toList())));

		ComboBoxUtil.updateComboBoxContents(guiBoundTo, ListUtils.merge(Collections.singleton("<NONE>"),
				mcreator.getWorkspace().getModElements().stream().filter(var -> var.getType() == ModElementType.GUI)
						.map(ModElement::getName).collect(Collectors.toList())), "<NONE>");
	}

	@Override protected AggregatedValidationResult validatePage(int page) {
		if (page == 1) {
			return new AggregatedValidationResult(name);
		} else if (page == 0) {
			if (hasErrors) {
				return new AggregatedValidationResult.MULTIFAIL(compileNotesPanel.getCompileNotes().stream()
						.map(compileNote -> "Tooltip builder: " + compileNote.getMessage())
						.collect(Collectors.toList()));
			}
		    return new AggregatedValidationResult(page1group);
	    } else {
		    return new AggregatedValidationResult.PASS();
	}
	}

	@Override public void openInEditingMode(Item item) {
		name.setText(item.name);
		rarity.setSelectedItem(item.rarity);
		texture.setTextureFromTextureName(item.texture);

		onRightClickedInAir.setSelectedProcedure(item.onRightClickedInAir);
		onRightClickedOnBlock.setSelectedProcedure(item.onRightClickedOnBlock);
		onCrafted.setSelectedProcedure(item.onCrafted);
		onEntityHitWith.setSelectedProcedure(item.onEntityHitWith);
		onItemInInventoryTick.setSelectedProcedure(item.onItemInInventoryTick);
		onItemInUseTick.setSelectedProcedure(item.onItemInUseTick);
		onStoppedUsing.setSelectedProcedure(item.onStoppedUsing);
		onEntitySwing.setSelectedProcedure(item.onEntitySwing);
		onDroppedByPlayer.setSelectedProcedure(item.onDroppedByPlayer);
		creativeTab.setSelectedItem(item.creativeTab);
		stackSize.setValue(item.stackSize);
		enchantability.setValue(item.enchantability);
		toolType.setValue(item.toolType);
		useDuration.setValue(item.useDuration);
		damageCount.setValue(item.damageCount);
		recipeRemainder.setBlock(item.recipeRemainder);
		destroyAnyBlock.setSelected(item.destroyAnyBlock);
		stayInGridWhenCrafting.setSelected(item.stayInGridWhenCrafting);
		damageOnCrafting.setSelected(item.damageOnCrafting);
		hasGlow.setSelected(item.hasGlow);
		hasTooltip.setSelected(item.hasTooltip);
		glowCondition.setSelectedProcedure(item.glowCondition);
		damageVsEntity.setValue(item.damageVsEntity);
		enableMeleeDamage.setSelected(item.enableMeleeDamage);
		guiBoundTo.setSelectedItem(item.guiBoundTo);
		inventorySize.setValue(item.inventorySize);
		inventoryStackSize.setValue(item.inventoryStackSize);
		hasDispenseBehavior.setSelected(item.hasDispenseBehavior);
		dispenseSuccessCondition.setSelectedProcedure(item.dispenseSuccessCondition);
		dispenseResultItemstack.setSelectedProcedure(item.dispenseResultItemstack);

		updateGlowElements();
		updateDispenseElements();

		Model model = item.getItemModel();
		if (model != null)
			renderType.setSelectedItem(model);

		// Elements that are created before doesn't have the start block, xml data is null, so we set the data to default
		if (item.ttxml == null)
			item.ttxml = "<xml><block type=\"tooltip_start\" deletable=\"false\" x=\"40\" y=\"40\"></block></xml>";
		blocklyPanel.setXMLDataOnly(item.ttxml);
		blocklyPanel.addTaskToRunAfterLoaded(() -> {
			blocklyPanel.clearWorkspace();
			blocklyPanel.setXML(item.ttxml);
			regenerateTooltipProcedures();
		});
	}

	@Override public Item getElementFromGUI() {
		Item item = new Item(modElement);
		item.name = name.getText();
		item.rarity = (String) rarity.getSelectedItem();
		item.creativeTab = new TabEntry(mcreator.getWorkspace(), creativeTab.getSelectedItem());
		item.stackSize = (int) stackSize.getValue();
		item.enchantability = (int) enchantability.getValue();
		item.useDuration = (int) useDuration.getValue();
		item.toolType = (double) toolType.getValue();
		item.damageCount = (int) damageCount.getValue();
		item.recipeRemainder = recipeRemainder.getBlock();
		item.destroyAnyBlock = destroyAnyBlock.isSelected();
		item.stayInGridWhenCrafting = stayInGridWhenCrafting.isSelected();
		item.damageOnCrafting = damageOnCrafting.isSelected();
		item.hasGlow = hasGlow.isSelected();
		item.hasTooltip = hasTooltip.isSelected();
		item.glowCondition = glowCondition.getSelectedProcedure();
		item.ttxml = blocklyPanel.getXML();
		item.onRightClickedInAir = onRightClickedInAir.getSelectedProcedure();
		item.onRightClickedOnBlock = onRightClickedOnBlock.getSelectedProcedure();
		item.onCrafted = onCrafted.getSelectedProcedure();
		item.onEntityHitWith = onEntityHitWith.getSelectedProcedure();
		item.onItemInInventoryTick = onItemInInventoryTick.getSelectedProcedure();
		item.onItemInUseTick = onItemInUseTick.getSelectedProcedure();
		item.onStoppedUsing = onStoppedUsing.getSelectedProcedure();
		item.onEntitySwing = onEntitySwing.getSelectedProcedure();
		item.onDroppedByPlayer = onDroppedByPlayer.getSelectedProcedure();
		item.damageVsEntity = (double) damageVsEntity.getValue();
		item.enableMeleeDamage = enableMeleeDamage.isSelected();
		item.inventorySize = (int) inventorySize.getValue();
		item.inventoryStackSize = (int) inventoryStackSize.getValue();
		item.guiBoundTo = (String) guiBoundTo.getSelectedItem();
		item.hasDispenseBehavior = hasDispenseBehavior.isSelected();
		item.dispenseSuccessCondition = dispenseSuccessCondition.getSelectedProcedure();
		item.dispenseResultItemstack = dispenseResultItemstack.getSelectedProcedure();

		item.texture = texture.getID();
		Model.Type modelType = ((Model) Objects.requireNonNull(renderType.getSelectedItem())).getType();
		item.renderType = 0;
		if (modelType == Model.Type.JSON)
			item.renderType = 1;
		else if (modelType == Model.Type.OBJ)
			item.renderType = 2;
		item.customModelName = ((Model) Objects.requireNonNull(renderType.getSelectedItem())).getReadableName();

		return item;
	}

	@Override public @Nullable URI getContextURL() throws URISyntaxException {
		return new URI(MCreatorApplication.SERVER_DOMAIN + "/wiki/how-make-item");
	}

}
