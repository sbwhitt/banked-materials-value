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
    private final BankedMatsValuePlugin plugin;
    private final BankedMatsValueConfig config;
    private final TooltipManager tooltipManager;
    private final OverlayManager overlayManager;
    private final PanelComponent mainPanel;
    private Widget bank;
    public HashMap<Integer, RawMatsCache.RawMatData> bankedMats = new HashMap<>();
    public HashMap<Integer, ProductsCache.ProductData> potentialProducts = new HashMap<>();

    @Inject
    private BankedMatsValueOverlay(Client client, TooltipManager tooltipManager, BankedMatsValueConfig config,
                                   BankedMatsValuePlugin plugin, OverlayManager overlayManager) {
        this.client = client;
        this.tooltipManager = tooltipManager;
        this.config = config;
        this.plugin = plugin;
        this.overlayManager = overlayManager;

        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(OverlayPriority.HIGHEST);
        setPosition(OverlayPosition.BOTTOM_LEFT);
        setResizable(false);
        setResettable(false);

        panelComponent.setBackgroundColor(new Color(51, 51, 51, 245));
        mainPanel = new PanelComponent();
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        bank = client.getWidget(WidgetInfo.BANK_CONTAINER);

        panelComponent.getChildren().clear();

        if (bank == null || bank.isHidden()) {
            plugin.hideOverlay();
            return null;
        }

        panelComponent.getChildren().add(TitleComponent.builder()
                .text("Banked Mats Value")
                .build());

        for (Map.Entry<Integer, RawMatsCache.RawMatData> entry : bankedMats.entrySet()) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left(entry.getValue().name + ":")
                    .right("" + entry.getValue().amount)
                    .build());
        }

        for (Map.Entry<Integer, ProductsCache.ProductData> entry : potentialProducts.entrySet()) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left(entry.getValue().name + ":")
                    .right("" + entry.getValue().skill)
                    .build());
        }

        return super.render(graphics);
    }
}
