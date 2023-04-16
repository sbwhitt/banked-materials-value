package com.bankedmatsvalue;

import java.awt.*;
import java.awt.Point;
import java.util.HashMap;
import net.runelite.api.*;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import net.runelite.api.widgets.Widget;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.ui.SkillColor;
import net.runelite.client.util.ColorUtil;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.ui.overlay.components.*;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.ui.overlay.components.ComponentOrientation;

@Singleton
public class BankedMatsValueOverlay extends OverlayPanel{
    private final Client client;
    private final BankedMatsValueConfig config;
    private final TooltipManager tooltipManager;
    @Inject
    ItemManager itemManager;
    public HashMap<Integer, RawMatsCache.RawMatData> bankedMats = new HashMap<>();
    public HashMap<Integer, ArrayList<Integer>> potentialProducts = new HashMap<>();

    @Inject
    private BankedMatsValueOverlay(Client client, TooltipManager tooltipManager, BankedMatsValueConfig config) {
        this.client = client;
        this.tooltipManager = tooltipManager;
        this.config = config;

        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(OverlayPriority.HIGHEST);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        final MenuEntry[] menuEntries = client.getMenuEntries();
        if (menuEntries.length == 0) return null;

        final ItemContainer bank = client.getItemContainer(InventoryID.BANK);
        final MenuEntry menuEntry = menuEntries[menuEntries.length - 1];
        final int widgetId = menuEntry.getParam1();
        if (client.isMenuOpen() || widgetId!= WidgetInfo.BANK_ITEM_CONTAINER.getId()) return null;

        final int index = menuEntry.getParam0();
        int itemId;
        if (bank.getItem(index) == null) return null;
        else itemId = bank.getItem(index).getId();

        if (!potentialProducts.containsKey(itemId)) return null;
        else {
            ArrayList<Integer> products = potentialProducts.get(itemId);
            for (int i = 0; i < products.size(); i++) {
                String colorStr = colorSkillString(ProductsCache.cache.get(products.get(i)).name, ProductsCache.cache.get(products.get(i)).skill);
                String priceStr = colorPriceString(itemManager.getItemPrice(products.get(i)) - itemManager.getItemPrice(itemId));
                tooltipManager.add(new Tooltip(colorStr + "  GE: " + priceStr + " gp"));
            }
        }
        return null;
    }

    private String colorSkillString(String str, String skillColor) {
        switch(skillColor) {
            case "Crafting":
                return ColorUtil.wrapWithColorTag(str, SkillColor.CRAFTING.getColor().brighter().brighter());
            case "Fletching":
                return ColorUtil.wrapWithColorTag(str, SkillColor.FLETCHING.getColor().brighter().brighter());
        }
        return "";
    }

    private String colorPriceString(Integer price) {
        if (price > 0) {
            return ColorUtil.wrapWithColorTag(price.toString(), Color.GREEN);
        }
        else if (price == 0) {
            return ColorUtil.wrapWithColorTag(price.toString(), Color.YELLOW);
        }
        else {
            return ColorUtil.wrapWithColorTag(price.toString(), Color.RED);
        }
    }
}
