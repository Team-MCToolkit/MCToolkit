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

package net.mcreator.ui.action.impl;

import net.mcreator.blockly.data.BlocklyLoader;
import net.mcreator.gradle.GradleDaemonUtils;
import net.mcreator.io.FileIO;
import net.mcreator.io.UserFolderManager;
import net.mcreator.minecraft.DataListLoader;
import net.mcreator.minecraft.api.ModAPIManager;
import net.mcreator.plugin.PluginLoader;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.action.ActionRegistry;
import net.mcreator.ui.action.BasicAction;
import net.mcreator.ui.dialogs.ProgressDialog;
import net.mcreator.ui.dialogs.tools.plugin.PackMakerToolIcons;
import net.mcreator.ui.dialogs.tools.plugin.PackMakerToolLoader;
import net.mcreator.ui.help.HelpLoader;
import net.mcreator.ui.init.BlockItemIcons;
import net.mcreator.ui.init.ImageMakerTexturesCache;
import net.mcreator.ui.init.L10N;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReloadPlugins extends BasicAction {
	public ReloadPlugins(ActionRegistry actionRegistry) {
		super(actionRegistry, L10N.t("action.reload_plugins"), e -> {
			Object[] options = { L10N.t("dialog.reload_plugins.option.reload"),
					UIManager.getString("OptionPane.cancelButtonText") };
			int reply = JOptionPane
					.showOptionDialog(actionRegistry.getMCreator(), L10N.t("dialog.reload_plugins.option.message"),
							L10N.t("dialog.reload_plugins.title"), JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE, null, options, options[0]);
			if (reply == 0 || reply == 1) {
				reloadPlugins(actionRegistry.getMCreator());
			}

		});
	}

	private static void reloadPlugins(MCreator mcreator){
		ProgressDialog progressDialog = new ProgressDialog(mcreator, L10N.t("dialog.reload_plugins.title"));
		new Thread(() -> {
			ProgressDialog.ProgressUnit p1 = new ProgressDialog.ProgressUnit(
					L10N.t("dialog.reload_plugins.progress.loading_plugins"));
			progressDialog.addProgress(p1);

			PluginLoader.initInstance();
			progressDialog.refreshDisplay();
			p1.ok();

			ProgressDialog.ProgressUnit p2 = new ProgressDialog.ProgressUnit(
					L10N.t("dialog.reload_plugins.progress.loading_l10n_and_helps"));
			progressDialog.addProgress(p2);
			// load translations after plugins are loaded
			L10N.initTranslations();
			// preload help entries cache
			HelpLoader.preloadCache();

			p2.ok();
			progressDialog.refreshDisplay();

			ProgressDialog.ProgressUnit p3 = new ProgressDialog.ProgressUnit(
					L10N.t("dialog.reload_plugins.progress.loading_datalists_and_icons"));
			progressDialog.addProgress(p3);
			// load datalists and icons for them after plugins are loaded
			BlockItemIcons.init();
			DataListLoader.preloadCache();
			p3.ok();

			ProgressDialog.ProgressUnit p4 = new ProgressDialog.ProgressUnit(
					L10N.t("dialog.reload_plugins.progress.loading_apis"));
			progressDialog.addProgress(p4);
			// load apis defined by plugins after plugins are loaded
			ModAPIManager.initAPIs();
			p4.ok();

			ProgressDialog.ProgressUnit p5 = new ProgressDialog.ProgressUnit(
					L10N.t("dialog.reload_plugins.progress.loading_blockly"));
			progressDialog.addProgress(p5);
			// load blockly blocks after plugins are loaded
			BlocklyLoader.init();
			p5.ok();

			ProgressDialog.ProgressUnit p6 = new ProgressDialog.ProgressUnit(
					L10N.t("dialog.reload_plugins.progress.loading_others"));
			progressDialog.addProgress(p6);
			// load templates for image maker
			ImageMakerTexturesCache.init();
			// load pack makers defined by plugins after plugins are loaded
			PackMakerToolIcons.init();
			PackMakerToolLoader.init();
			p6.ok();

			progressDialog.refreshDisplay();

			progressDialog.hideAll();
		}).start();
		progressDialog.setVisible(true);
	}
}
