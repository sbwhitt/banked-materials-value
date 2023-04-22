package com.bankedmatsvalue;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
@PluginDescriptor(
		name = "Banked Materials Value"
)
public class BankedMatsValuePlugin extends Plugin
{
	public static final String CONFIG_GROUP = "bankedmatsvalue";
	@Inject
	private Client client;
	@Inject
	private RawMatsCache matsCache;
	@Inject
	private ProductsCache productCache;
	@Inject
	private BankedMatsValueOverlay overlay;
	@Inject
	private BankedMatsValueConfig config;
	@Inject
	private OverlayManager overlayManager;

	private Widget bank;
	private Boolean pluginToggled = false;

	@Override
	protected void startUp() throws Exception {
		log.info("Banked Materials Value started!");
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception {
		log.info("Banked Materials Value stopped!");
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event) {
		if (event.getType() != MenuAction.CC_OP.getId() || !event.getOption().equals("Show menu")
				|| (event.getActionParam1() >> 16) != WidgetID.BANK_GROUP_ID){
			return;
		}

		client.createMenuEntry(-1)
				.setOption("Toggle Banked Mats Value")
				.setTarget("")
				.setType(MenuAction.RUNELITE)
				.onClick(this::onClick)
				.setDeprioritized(true);
	}

	public void onClick(MenuEntry entry) {
		bank = client.getWidget(WidgetInfo.BANK_CONTAINER);

		if (bank == null) {
			overlayManager.remove(overlay);
			pluginToggled = false;
			return;
		}

		pluginToggled = !pluginToggled;
		if (pluginToggled) {
			buildNewBankData();
			overlayManager.add(overlay);
		} else {
			overlayManager.remove(overlay);
		}
	}

	@Provides
	BankedMatsValueConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(BankedMatsValueConfig.class);
	}

	@Subscribe
	private void onItemContainerChanged(ItemContainerChanged event) {
		if (event.getContainerId() != InventoryID.BANK.getId()) {
			pluginToggled = false;
			overlayManager.remove(overlay);
			return;
		}
		pluginToggled = true;
		buildNewBankData();
		overlayManager.add(overlay);
	}

	private void buildNewBankData() {
		overlay.bankedMats.clear();
		overlay.potentialProducts.clear();
		findRawMats();
		findPotentialProducts();
	}

	private void findRawMats() {
		ItemContainer bankContainer = client.getItemContainer(InventoryID.BANK);
		if (bankContainer != null) {
			Item[] items = bankContainer.getItems();
			for (int i = 0; i < items.length; i++) {
				RawMatsCache.RawMatData rawMat = matsCache.getRawMat(items[i].getId());
				if (rawMat != null) {
					rawMat.amount = items[i].getQuantity();
					overlay.bankedMats.put(items[i].getId(), rawMat);
				}
			}
		}
	}

	private void findPotentialProducts() {
		ItemContainer bankContainer = client.getItemContainer(InventoryID.BANK);
		if (bankContainer != null) {
			for (Map.Entry<Integer, ProductsCache.ProductData> entry : ProductsCache.cache.entrySet()) {
				ProductsCache.ProductData product = entry.getValue();
				if (isCreatable(product)) {
					for (int i = 0; i < product.ingredients.length; i++) {
						if (!overlay.potentialProducts.containsKey(product.ingredients[i])) {
							overlay.potentialProducts.put(product.ingredients[i], new ArrayList<>(product.id));
						}
						overlay.potentialProducts.get(product.ingredients[i]).add(product.id);
					}
				}
			}
		}
	}

	private Boolean isCreatable(ProductsCache.ProductData product) {
		for (int i = 0; i < product.ingredients.length; i++) {
			if (!overlay.bankedMats.containsKey(product.ingredients[i])) return false;
		}
		return true;
	}

	public static void logIt(String in) {
		log.info(in);
	}
}
