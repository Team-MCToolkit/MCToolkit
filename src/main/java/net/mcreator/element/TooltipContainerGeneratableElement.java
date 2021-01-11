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

package net.mcreator.element;

import net.mcreator.blockly.BlocklyToTooltip;
import net.mcreator.blockly.data.BlocklyLoader;
import net.mcreator.generator.Generator;
import net.mcreator.generator.blockly.BlocklyBlockCodeGenerator;
import net.mcreator.generator.blockly.OutputBlockCodeGenerator;
import net.mcreator.generator.blockly.ProceduralBlockCodeGenerator;
import net.mcreator.generator.template.TemplateGeneratorException;
import net.mcreator.workspace.elements.ModElement;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class TooltipContainerGeneratableElement extends GeneratableElement {

	public TooltipContainerGeneratableElement(ModElement element) {
		super(element);
	}

	/**
	 * Gets the xml data of the procedure
	 * @return Xml data of the procedure
	 */
	public abstract String getXml();

	@Override public void provideAdditionalData(Map<String, Object> additionalData, Generator generator) throws TemplateGeneratorException {
		super.provideAdditionalData(additionalData, generator);
		BlocklyBlockCodeGenerator blocklyBlockCodeGenerator = new BlocklyBlockCodeGenerator(
				BlocklyLoader.INSTANCE.getTooltipBlockLoader().getDefinedBlocks(),
				generator.getTooltipGenerator(),
				additionalData
		).setTemplateExtension(generator.getGeneratorConfiguration()
				.getGeneratorFlavor().getBaseLanguage().name()
				.toLowerCase(Locale.ENGLISH));

		BlocklyToTooltip blocklyToJava = new BlocklyToTooltip(
				generator.getWorkspace(),
				this.getXml(),
				generator.getTooltipGenerator(),
				new ProceduralBlockCodeGenerator(blocklyBlockCodeGenerator),
				new OutputBlockCodeGenerator(blocklyBlockCodeGenerator)
		);

		String tooltipCode = blocklyToJava.getGeneratedCode();
		if (tooltipCode == null)
			tooltipCode = "";

		additionalData.put("tooltipCode", tooltipCode);
	}
}
