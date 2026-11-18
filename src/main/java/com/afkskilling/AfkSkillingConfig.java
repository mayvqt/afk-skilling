package com.afkskilling;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup(AfkSkillingConfig.GROUP)
public interface AfkSkillingConfig extends Config
{
	String GROUP = "afkskilling";

	@ConfigSection(
		name = "Activities",
		description = "Which AFK activities dim the screen and alert when they stop",
		position = 10
	)
	String activities = "activities";

	@Range(max = 95)
	@Units(Units.PERCENT)
	@ConfigItem(
		keyName = "dimOpacity",
		name = "Dim amount",
		description = "How dark the screen gets while you AFK",
		position = 1
	)
	default int dimOpacity()
	{
		return 80;
	}

	@ConfigItem(
		keyName = "showActivity",
		name = "Show activity",
		description = "Show what you're doing in the middle of the dimmed screen",
		position = 4
	)
	default boolean showActivity()
	{
		return true;
	}

	@Range(min = 1, max = 20)
	@Units(Units.TICKS)
	@ConfigItem(
		keyName = "alertDelay",
		name = "Alert delay",
		description = "How long you stand idle after the activity stops before the alert starts",
		position = 2
	)
	default int alertDelay()
	{
		return 2;
	}

	@ConfigItem(
		keyName = "alertPalette",
		name = "Alert colours",
		description = "Colours of the idle glow",
		position = 3
	)
	default AlertPalette alertPalette()
	{
		return AlertPalette.PASTELS;
	}

	@ConfigItem(
		keyName = "fishing",
		name = "Fishing",
		description = "Any fishing spot",
		section = activities
	)
	default boolean fishing()
	{
		return true;
	}
}
