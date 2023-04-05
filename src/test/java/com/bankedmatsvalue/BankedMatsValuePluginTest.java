package com.bankedmatsvalue;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class BankedMatsValuePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(BankedMatsValuePlugin.class);
		RuneLite.main(args);
	}
}