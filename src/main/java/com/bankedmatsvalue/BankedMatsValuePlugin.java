package com.bankedmatsvalue;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Example"
)
public class BankedMatsValuePlugin extends Plugin
{
	@Inject
	private Client client;
	private Widget bank;
	@Inject
	private BankedMatsValueOverlay overlay;
	@Inject
	private BankedMatsValueConfig config;
	@Inject
	private OverlayManager overlayManager;
	private Boolean pluginToggled = false;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Banked Materials Value started!");
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		log.info("Banked Materials Value stopped!");
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded entry) {}

	public void onClick(MenuEntry entry) {
		bank = client.getWidget(WidgetInfo.BANK_CONTAINER);

		if (bank == null) {
			overlayManager.remove(overlay);
			pluginToggled = false;
			return;
		}

		pluginToggled = !pluginToggled;
		if (pluginToggled) {
			overlayManager.add(overlay);
		}
		else {
			overlayManager.remove(overlay);
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}

	@Provides
	BankedMatsValueConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BankedMatsValueConfig.class);
	}

	public void hideOverlay() {
		pluginToggled = false;
		overlayManager.remove(overlay);
	}
}
