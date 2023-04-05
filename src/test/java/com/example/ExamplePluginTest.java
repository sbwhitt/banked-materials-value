package com.example;

import com.bankedmatsvalue.BankedMatsValuePlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ExamplePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(BankedMatsValuePlugin.class);
		RuneLite.main(args);
	}
}