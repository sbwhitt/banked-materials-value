package com.bankedmatsvalue;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(BankedMatsValuePlugin.CONFIG_GROUP)
public interface BankedMatsValueConfig extends Config
{
	@ConfigItem(
		keyName = "matsList",
		name = "Materials to Calculate",
		description = "Enter the raw materials you wish to be "
	)
	default String greeting()
	{
		return "Hello";
	}
}
