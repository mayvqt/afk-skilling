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

	@ConfigItem(
		keyName = "miningStars",
		name = "Shooting stars",
		description = "Mining a shooting star; alerts when it drops a tier",
		section = activities
	)
	default boolean miningStars()
	{
		return true;
	}

	@ConfigItem(
		keyName = "miningMotherlode",
		name = "Motherlode Mine",
		description = "Mining pay-dirt veins",
		section = activities
	)
	default boolean miningMotherlode()
	{
		return true;
	}

	@ConfigItem(
		keyName = "miningAmethyst",
		name = "Amethyst",
		description = "Mining amethyst crystals",
		section = activities
	)
	default boolean miningAmethyst()
	{
		return true;
	}

	@ConfigItem(
		keyName = "miningOther",
		name = "Other mining",
		description = "Any other rock. Off by default so power-mining isn't dimmed and alerted every few seconds",
		section = activities
	)
	default boolean miningOther()
	{
		return false;
	}
}
