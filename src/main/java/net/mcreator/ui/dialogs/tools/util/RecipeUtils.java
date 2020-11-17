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

package net.mcreator.ui.dialogs.tools.util;

import net.mcreator.element.ModElementType;
import net.mcreator.element.ModElementTypeRegistry;
import net.mcreator.element.parts.MItemBlock;
import net.mcreator.element.types.Block;
import net.mcreator.element.types.Recipe;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.dialogs.tools.plugin.elements.Recipes;
import net.mcreator.util.StringUtils;
import net.mcreator.workspace.Workspace;
import net.mcreator.workspace.elements.ModElement;

public class RecipeUtils {
	public static void stairs(MCreator mcreator, Workspace workspace, Block block, String name){
		Recipe stairsRecipe = (Recipe) ModElementTypeRegistry.REGISTRY.get(ModElementType.RECIPE)
				.getModElement(mcreator, new ModElement(workspace, name + "StairsRecipe", ModElementType.RECIPE), false)
				.getElementFromGUI();
		stairsRecipe.recipeSlots[0] = new MItemBlock(workspace, "CUSTOM:" + block.getModElement().getName());
		stairsRecipe.recipeSlots[3] = new MItemBlock(workspace, "CUSTOM:" + block.getModElement().getName());
		stairsRecipe.recipeSlots[4] = new MItemBlock(workspace, "CUSTOM:" + block.getModElement().getName());
		stairsRecipe.recipeSlots[6] = new MItemBlock(workspace, "CUSTOM:" + block.getModElement().getName());
		stairsRecipe.recipeSlots[7] = new MItemBlock(workspace, "CUSTOM:" + block.getModElement().getName());
		stairsRecipe.recipeSlots[8] = new MItemBlock(workspace, "CUSTOM:" + block.getModElement().getName());
		stairsRecipe.recipeReturnStack = new MItemBlock(workspace, "CUSTOM:" + name + "Stairs");
		stairsRecipe.recipeRetstackSize = 4;
		mcreator.getWorkspace().getModElementManager().storeModElementPicture(stairsRecipe);
		mcreator.getWorkspace().addModElement(stairsRecipe.getModElement());
		mcreator.getWorkspace().getGenerator().generateElement(stairsRecipe);
		mcreator.getWorkspace().getModElementManager().storeModElement(stairsRecipe);
	}

	public static void stairs(MCreator mcreator, Workspace workspace, String block, String name, String resultBlock){
		Recipe stairsRecipe = (Recipe) ModElementTypeRegistry.REGISTRY.get(ModElementType.RECIPE)
				.getModElement(mcreator, new ModElement(workspace, name, ModElementType.RECIPE), false)
				.getElementFromGUI();
		stairsRecipe.recipeSlots[0] = new MItemBlock(workspace, "CUSTOM:" + block);
		stairsRecipe.recipeSlots[3] = new MItemBlock(workspace, "CUSTOM:" + block);
		stairsRecipe.recipeSlots[4] = new MItemBlock(workspace, "CUSTOM:" + block);
		stairsRecipe.recipeSlots[6] = new MItemBlock(workspace, "CUSTOM:" + block);
		stairsRecipe.recipeSlots[7] = new MItemBlock(workspace, "CUSTOM:" + block);
		stairsRecipe.recipeSlots[8] = new MItemBlock(workspace, "CUSTOM:" + block);
		stairsRecipe.recipeReturnStack = new MItemBlock(workspace, "CUSTOM:" + resultBlock);
		stairsRecipe.recipeRetstackSize = 4;
		mcreator.getWorkspace().getModElementManager().storeModElementPicture(stairsRecipe);
		mcreator.getWorkspace().addModElement(stairsRecipe.getModElement());
		mcreator.getWorkspace().getGenerator().generateElement(stairsRecipe);
		mcreator.getWorkspace().getModElementManager().storeModElement(stairsRecipe);
	}

	public static void slab(MCreator mcreator, Workspace workspace, Block block, String name){
		Recipe slabRecipe = (Recipe) ModElementTypeRegistry.REGISTRY.get(ModElementType.RECIPE)
				.getModElement(mcreator, new ModElement(workspace, name + "SlabRecipe", ModElementType.RECIPE), false)
				.getElementFromGUI();
		slabRecipe.recipeSlots[6] = new MItemBlock(workspace, "CUSTOM:" + block.getModElement().getName());
		slabRecipe.recipeSlots[7] = new MItemBlock(workspace, "CUSTOM:" + block.getModElement().getName());
		slabRecipe.recipeSlots[8] = new MItemBlock(workspace, "CUSTOM:" + block.getModElement().getName());
		slabRecipe.recipeReturnStack = new MItemBlock(workspace, "CUSTOM:" + name + "Slab");
		slabRecipe.recipeRetstackSize = 6;
		mcreator.getWorkspace().getModElementManager().storeModElementPicture(slabRecipe);
		mcreator.getWorkspace().addModElement(slabRecipe.getModElement());
		mcreator.getWorkspace().getGenerator().generateElement(slabRecipe);
		mcreator.getWorkspace().getModElementManager().storeModElement(slabRecipe);
	}

	public static void slab(MCreator mcreator, Workspace workspace, String block, String name, String resultBlock){
		Recipe slabRecipe = (Recipe) ModElementTypeRegistry.REGISTRY.get(ModElementType.RECIPE)
				.getModElement(mcreator, new ModElement(workspace, name, ModElementType.RECIPE), false)
				.getElementFromGUI();
		slabRecipe.recipeSlots[6] = new MItemBlock(workspace, "CUSTOM:" + block);
		slabRecipe.recipeSlots[7] = new MItemBlock(workspace, "CUSTOM:" + block);
		slabRecipe.recipeSlots[8] = new MItemBlock(workspace, "CUSTOM:" + block);
		slabRecipe.recipeReturnStack = new MItemBlock(workspace, "CUSTOM:" + resultBlock);
		slabRecipe.recipeRetstackSize = 6;
		mcreator.getWorkspace().getModElementManager().storeModElementPicture(slabRecipe);
		mcreator.getWorkspace().addModElement(slabRecipe.getModElement());
		mcreator.getWorkspace().getGenerator().generateElement(slabRecipe);
		mcreator.getWorkspace().getModElementManager().storeModElement(slabRecipe);
	}

	public static void fence(MCreator mcreator, Workspace workspace, Block block, String name){
		Recipe fenceRecipe = (Recipe) ModElementTypeRegistry.REGISTRY.get(ModElementType.RECIPE)
				.getModElement(mcreator, new ModElement(workspace, name + "FenceRecipe", ModElementType.RECIPE), false)
				.getElementFromGUI();
		fenceRecipe.recipeSlots[3] = new MItemBlock(workspace, "CUSTOM:" + block.getModElement().getName());
		fenceRecipe.recipeSlots[4] = new MItemBlock(workspace, "Items.STICK");
		fenceRecipe.recipeSlots[5] = new MItemBlock(workspace, "CUSTOM:" + block.getModElement().getName());
		fenceRecipe.recipeSlots[6] = new MItemBlock(workspace, "CUSTOM:" + block.getModElement().getName());
		fenceRecipe.recipeSlots[7] = new MItemBlock(workspace, "Items.STICK");
		fenceRecipe.recipeSlots[8] = new MItemBlock(workspace, "CUSTOM:" + block.getModElement().getName());
		fenceRecipe.recipeReturnStack = new MItemBlock(workspace, "CUSTOM:" + name + "Fence");
		fenceRecipe.recipeRetstackSize = 3;
		mcreator.getWorkspace().getModElementManager().storeModElementPicture(fenceRecipe);
		mcreator.getWorkspace().addModElement(fenceRecipe.getModElement());
		mcreator.getWorkspace().getGenerator().generateElement(fenceRecipe);
		mcreator.getWorkspace().getModElementManager().storeModElement(fenceRecipe);
	}

	public static void fence(MCreator mcreator, Workspace workspace, String block, String name, String resultBlock){
		Recipe fenceRecipe = (Recipe) ModElementTypeRegistry.REGISTRY.get(ModElementType.RECIPE)
				.getModElement(mcreator, new ModElement(workspace, name, ModElementType.RECIPE), false)
				.getElementFromGUI();
		fenceRecipe.recipeSlots[3] = new MItemBlock(workspace, "CUSTOM:" + block);
		fenceRecipe.recipeSlots[4] = new MItemBlock(workspace, "Items.STICK");
		fenceRecipe.recipeSlots[5] = new MItemBlock(workspace, "CUSTOM:" + block);
		fenceRecipe.recipeSlots[6] = new MItemBlock(workspace, "CUSTOM:" + block);
		fenceRecipe.recipeSlots[7] = new MItemBlock(workspace, "Items.STICK");
		fenceRecipe.recipeSlots[8] = new MItemBlock(workspace, "CUSTOM:" + block);
		fenceRecipe.recipeReturnStack = new MItemBlock(workspace, "CUSTOM:" + resultBlock);
		fenceRecipe.recipeRetstackSize = 3;
		mcreator.getWorkspace().getModElementManager().storeModElementPicture(fenceRecipe);
		mcreator.getWorkspace().addModElement(fenceRecipe.getModElement());
		mcreator.getWorkspace().getGenerator().generateElement(fenceRecipe);
		mcreator.getWorkspace().getModElementManager().storeModElement(fenceRecipe);
	}

	public static void fenceGate(MCreator mcreator, Workspace workspace, Block block, String name){
		Recipe fenceGateRecipe = (Recipe) ModElementTypeRegistry.REGISTRY.get(ModElementType.RECIPE)
				.getModElement(mcreator, new ModElement(workspace, name + "FenceGateRecipe", ModElementType.RECIPE),
						false).getElementFromGUI();
		fenceGateRecipe.recipeSlots[3] = new MItemBlock(workspace, "Items.STICK");
		fenceGateRecipe.recipeSlots[4] = new MItemBlock(workspace, "CUSTOM:" + block.getModElement().getName());
		fenceGateRecipe.recipeSlots[5] = new MItemBlock(workspace, "Items.STICK");
		fenceGateRecipe.recipeSlots[6] = new MItemBlock(workspace, "Items.STICK");
		fenceGateRecipe.recipeSlots[7] = new MItemBlock(workspace, "CUSTOM:" + block.getModElement().getName());
		fenceGateRecipe.recipeSlots[8] = new MItemBlock(workspace, "Items.STICK");
		fenceGateRecipe.recipeReturnStack = new MItemBlock(workspace, "CUSTOM:" + name + "FenceGate");
		fenceGateRecipe.recipeRetstackSize = 1;
		mcreator.getWorkspace().getModElementManager().storeModElementPicture(fenceGateRecipe);
		mcreator.getWorkspace().addModElement(fenceGateRecipe.getModElement());
		mcreator.getWorkspace().getGenerator().generateElement(fenceGateRecipe);
		mcreator.getWorkspace().getModElementManager().storeModElement(fenceGateRecipe);
	}

	public static void fenceGate(MCreator mcreator, Workspace workspace, String block, String name, String resultBlock){
		Recipe fenceGateRecipe = (Recipe) ModElementTypeRegistry.REGISTRY.get(ModElementType.RECIPE)
				.getModElement(mcreator, new ModElement(workspace, name, ModElementType.RECIPE),
						false).getElementFromGUI();
		fenceGateRecipe.recipeSlots[3] = new MItemBlock(workspace, "Items.STICK");
		fenceGateRecipe.recipeSlots[4] = new MItemBlock(workspace, "CUSTOM:" + block);
		fenceGateRecipe.recipeSlots[5] = new MItemBlock(workspace, "Items.STICK");
		fenceGateRecipe.recipeSlots[6] = new MItemBlock(workspace, "Items.STICK");
		fenceGateRecipe.recipeSlots[7] = new MItemBlock(workspace, "CUSTOM:" + block);
		fenceGateRecipe.recipeSlots[8] = new MItemBlock(workspace, "Items.STICK");
		fenceGateRecipe.recipeReturnStack = new MItemBlock(workspace, "CUSTOM:" + resultBlock);
		fenceGateRecipe.recipeRetstackSize = 1;
		mcreator.getWorkspace().getModElementManager().storeModElementPicture(fenceGateRecipe);
		mcreator.getWorkspace().addModElement(fenceGateRecipe.getModElement());
		mcreator.getWorkspace().getGenerator().generateElement(fenceGateRecipe);
		mcreator.getWorkspace().getModElementManager().storeModElement(fenceGateRecipe);
	}

	public static void stick(MCreator mcreator, Workspace workspace, Block block, String name){
		Recipe stickRecipe = (Recipe) ModElementTypeRegistry.REGISTRY.get(ModElementType.RECIPE)
				.getModElement(mcreator, new ModElement(workspace, name + "StickRecipe", ModElementType.RECIPE), false)
				.getElementFromGUI();
		stickRecipe.recipeSlots[0] = new MItemBlock(workspace, "CUSTOM:" + block.getModElement().getName());
		stickRecipe.recipeSlots[3] = new MItemBlock(workspace, "CUSTOM:" + block.getModElement().getName());
		stickRecipe.recipeReturnStack = new MItemBlock(workspace, "Items.STICK");
		stickRecipe.recipeRetstackSize = 4;
		mcreator.getWorkspace().getModElementManager().storeModElementPicture(stickRecipe);
		mcreator.getWorkspace().addModElement(stickRecipe.getModElement());
		mcreator.getWorkspace().getGenerator().generateElement(stickRecipe);
		mcreator.getWorkspace().getModElementManager().storeModElement(stickRecipe);
	}

	public static void stick(MCreator mcreator, Workspace workspace, String block, String name){
		Recipe stickRecipe = (Recipe) ModElementTypeRegistry.REGISTRY.get(ModElementType.RECIPE)
				.getModElement(mcreator, new ModElement(workspace, name, ModElementType.RECIPE), false)
				.getElementFromGUI();
		stickRecipe.recipeSlots[0] = new MItemBlock(workspace, "CUSTOM:" + block);
		stickRecipe.recipeSlots[3] = new MItemBlock(workspace, "CUSTOM:" + block);
		stickRecipe.recipeReturnStack = new MItemBlock(workspace, "Items.STICK");
		stickRecipe.recipeRetstackSize = 4;
		mcreator.getWorkspace().getModElementManager().storeModElementPicture(stickRecipe);
		mcreator.getWorkspace().addModElement(stickRecipe.getModElement());
		mcreator.getWorkspace().getGenerator().generateElement(stickRecipe);
		mcreator.getWorkspace().getModElementManager().storeModElement(stickRecipe);
	}

	public static void fourBlocks(MCreator mcreator, Workspace workspace, String block, String recipeName, String resultBlock){
		Recipe fourBlocksRecipe = (Recipe) ModElementTypeRegistry.REGISTRY.get(ModElementType.RECIPE)
				.getModElement(mcreator, new ModElement(workspace, recipeName, ModElementType.RECIPE), false)
				.getElementFromGUI();
		fourBlocksRecipe.recipeSlots[0] = new MItemBlock(workspace, "CUSTOM:" + block);
		fourBlocksRecipe.recipeSlots[1] = new MItemBlock(workspace, "CUSTOM:" + block);
		fourBlocksRecipe.recipeSlots[3] = new MItemBlock(workspace, "CUSTOM:" + block);
		fourBlocksRecipe.recipeSlots[4] = new MItemBlock(workspace, "CUSTOM:" + block);
		fourBlocksRecipe.recipeReturnStack = new MItemBlock(workspace, resultBlock);
		fourBlocksRecipe.recipeRetstackSize = 4;
		mcreator.getWorkspace().getModElementManager().storeModElementPicture(fourBlocksRecipe);
		mcreator.getWorkspace().addModElement(fourBlocksRecipe.getModElement());
		mcreator.getWorkspace().getGenerator().generateElement(fourBlocksRecipe);
		mcreator.getWorkspace().getModElementManager().storeModElement(fourBlocksRecipe);
	}

	public static void fullBlock(MCreator mcreator, Workspace workspace, String block, String textFieldName, String recipeName, String resultBlock, int stackSize){
		Recipe fullBlockRecipe = (Recipe) ModElementTypeRegistry.REGISTRY.get(ModElementType.RECIPE)
				.getModElement(mcreator, new ModElement(workspace,textFieldName + recipeName, ModElementType.RECIPE), false)
				.getElementFromGUI();
		fullBlockRecipe.recipeSlots[0] = new MItemBlock(workspace, "CUSTOM:" + block);
		fullBlockRecipe.recipeSlots[1] = new MItemBlock(workspace, "CUSTOM:" + block);
		fullBlockRecipe.recipeSlots[2] = new MItemBlock(workspace, "CUSTOM:" + block);
		fullBlockRecipe.recipeSlots[3] = new MItemBlock(workspace, "CUSTOM:" + block);
		fullBlockRecipe.recipeSlots[4] = new MItemBlock(workspace, "CUSTOM:" + block);
		fullBlockRecipe.recipeSlots[5] = new MItemBlock(workspace, "CUSTOM:" + block);
		fullBlockRecipe.recipeSlots[6] = new MItemBlock(workspace, "CUSTOM:" + block);
		fullBlockRecipe.recipeSlots[7] = new MItemBlock(workspace, "CUSTOM:" + block);
		fullBlockRecipe.recipeSlots[8] = new MItemBlock(workspace, "CUSTOM:" + block);
		fullBlockRecipe.recipeReturnStack = new MItemBlock(workspace, resultBlock);
		fullBlockRecipe.recipeRetstackSize = stackSize;
		mcreator.getWorkspace().getModElementManager().storeModElementPicture(fullBlockRecipe);
		mcreator.getWorkspace().addModElement(fullBlockRecipe.getModElement());
		mcreator.getWorkspace().getGenerator().generateElement(fullBlockRecipe);
		mcreator.getWorkspace().getModElementManager().storeModElement(fullBlockRecipe);
	}

	public static void smelting(MCreator mcreator, Workspace workspace, Recipes recipe, String block, String textFieldName, double factor){
		Recipe furnaceRecipe = (Recipe) ModElementTypeRegistry.REGISTRY.get(ModElementType.RECIPE)
				.getModElement(mcreator, new ModElement(workspace, textFieldName + recipe.recipeName, ModElementType.RECIPE), false)
				.getElementFromGUI();
		furnaceRecipe.recipeType = "Smelting";
		furnaceRecipe.smeltingInputStack = new MItemBlock(workspace, "CUSTOM:" + block);
		furnaceRecipe.smeltingReturnStack = new MItemBlock(workspace, recipe.returnItem);
		furnaceRecipe.xpReward = recipe.xpReward * factor;
		furnaceRecipe.cookingTime = recipe.cookingTime;
		mcreator.getWorkspace().getModElementManager().storeModElementPicture(furnaceRecipe);
		mcreator.getWorkspace().addModElement(furnaceRecipe.getModElement());
		mcreator.getWorkspace().getGenerator().generateElement(furnaceRecipe);
		mcreator.getWorkspace().getModElementManager().storeModElement(furnaceRecipe);
	}

	public static void smoking(MCreator mcreator, Workspace workspace, Recipes recipe, String block, String textFieldName, double factor){
		Recipe furnaceRecipe = (Recipe) ModElementTypeRegistry.REGISTRY.get(ModElementType.RECIPE)
				.getModElement(mcreator, new ModElement(workspace, textFieldName + recipe.recipeName, ModElementType.RECIPE), false)
				.getElementFromGUI();
		furnaceRecipe.recipeType = "Smoking";
		furnaceRecipe.smokingInputStack = new MItemBlock(workspace, "CUSTOM:" + block);
		furnaceRecipe.smokingReturnStack = new MItemBlock(workspace, recipe.returnItem);
		furnaceRecipe.xpReward = recipe.xpReward * factor;
		furnaceRecipe.cookingTime = recipe.cookingTime;
		mcreator.getWorkspace().getModElementManager().storeModElementPicture(furnaceRecipe);
		mcreator.getWorkspace().addModElement(furnaceRecipe.getModElement());
		mcreator.getWorkspace().getGenerator().generateElement(furnaceRecipe);
		mcreator.getWorkspace().getModElementManager().storeModElement(furnaceRecipe);
	}

	public static void blasting(MCreator mcreator, Workspace workspace, Recipes recipe, String block, String textFieldName, double factor){
		Recipe furnaceRecipe = (Recipe) ModElementTypeRegistry.REGISTRY.get(ModElementType.RECIPE)
				.getModElement(mcreator, new ModElement(workspace, textFieldName + recipe.recipeName, ModElementType.RECIPE), false)
				.getElementFromGUI();
		furnaceRecipe.recipeType = "Blasting";
		furnaceRecipe.blastingInputStack = new MItemBlock(workspace, "CUSTOM:" + block);
		furnaceRecipe.blastingReturnStack = new MItemBlock(workspace, recipe.returnItem);
		furnaceRecipe.xpReward = recipe.xpReward * factor;
		furnaceRecipe.cookingTime = recipe.cookingTime;
		mcreator.getWorkspace().getModElementManager().storeModElementPicture(furnaceRecipe);
		mcreator.getWorkspace().addModElement(furnaceRecipe.getModElement());
		mcreator.getWorkspace().getGenerator().generateElement(furnaceRecipe);
		mcreator.getWorkspace().getModElementManager().storeModElement(furnaceRecipe);
	}

	public static void campfireCooking(MCreator mcreator, Workspace workspace, Recipes recipe, String block, String textFieldName, double factor){
		Recipe furnaceRecipe = (Recipe) ModElementTypeRegistry.REGISTRY.get(ModElementType.RECIPE)
				.getModElement(mcreator, new ModElement(workspace, textFieldName + recipe.recipeName, ModElementType.RECIPE), false)
				.getElementFromGUI();
		furnaceRecipe.recipeType = "Campfire cooking";
		furnaceRecipe.campfireCookingInputStack = new MItemBlock(workspace, "CUSTOM:" + block);
		furnaceRecipe.campfireCookingReturnStack = new MItemBlock(workspace, recipe.returnItem);
		furnaceRecipe.xpReward = recipe.xpReward * factor;
		furnaceRecipe.cookingTime = recipe.cookingTime;
		mcreator.getWorkspace().getModElementManager().storeModElementPicture(furnaceRecipe);
		mcreator.getWorkspace().addModElement(furnaceRecipe.getModElement());
		mcreator.getWorkspace().getGenerator().generateElement(furnaceRecipe);
		mcreator.getWorkspace().getModElementManager().storeModElement(furnaceRecipe);
	}

	public static void stoneCutting(MCreator mcreator, Workspace workspace, Recipes recipe, String block, String textFieldName, double factor){
		Recipe stoneCuttingRecipe = (Recipe) ModElementTypeRegistry.REGISTRY.get(ModElementType.RECIPE)
				.getModElement(mcreator, new ModElement(workspace, textFieldName + recipe.recipeName, ModElementType.RECIPE), false)
				.getElementFromGUI();
		stoneCuttingRecipe.recipeType = "Stone cutting";
		stoneCuttingRecipe.stoneCuttingInputStack = new MItemBlock(workspace, "CUSTOM:" + block);
		stoneCuttingRecipe.stoneCuttingReturnStack = new MItemBlock(workspace, recipe.returnItem);
		stoneCuttingRecipe.recipeRetstackSize = recipe.stackSize;
		mcreator.getWorkspace().getModElementManager().storeModElementPicture(stoneCuttingRecipe);
		mcreator.getWorkspace().addModElement(stoneCuttingRecipe.getModElement());
		mcreator.getWorkspace().getGenerator().generateElement(stoneCuttingRecipe);
		mcreator.getWorkspace().getModElementManager().storeModElement(stoneCuttingRecipe);
	}
}
