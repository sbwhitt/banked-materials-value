package com.bankedmatsvalue;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(BankedMatsValuePlugin.CONFIG_GROUP)
public interface BankedMatsValueConfig extends Config
{
	@ConfigItem(
		position = 1,
		keyName = "productAmnt",
		name = "Products to display",
		description = "Choose the number of products you wish to see in the item tooltip."
	)
	default int productAmnt()
	{
		return 5;
	}
}
