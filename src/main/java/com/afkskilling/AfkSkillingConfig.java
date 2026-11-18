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
		name = "Fishing",
		description = "Fishing spots that dim the screen and alert when they stop",
		position = 10
	)
	String fishing = "fishing";

	@ConfigSection(
		name = "Mining",
		description = "Mining spots that dim the screen and alert when they stop",
		position = 11
	)
	String mining = "mining";

	@ConfigSection(
		name = "Woodcutting",
		description = "Trees that dim the screen and alert when they stop",
		position = 12
	)
	String woodcutting = "woodcutting";

	@ConfigSection(
		name = "Other activities",
		description = "Other AFK activities that dim the screen and alert when they stop",
		position = 13
	)
	String other = "other";

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
		keyName = "fishingAnglers",
		name = "Anglerfish",
		description = "Anglerfish spots, on shore and from a boat",
		section = fishing,
		position = 1
	)
	default boolean fishingAnglers()
	{
		return true;
	}

	@ConfigItem(
		keyName = "fishingKarambwans",
		name = "Karambwans",
		description = "Karambwan spots, on shore and from a boat",
		section = fishing,
		position = 2
	)
	default boolean fishingKarambwans()
	{
		return true;
	}

	@ConfigItem(
		keyName = "fishingOther",
		name = "Other fishing",
		description = "Any other spot. Off by default so power-fishing isn't dimmed and alerted every few seconds",
		section = fishing,
		position = 3
	)
	default boolean fishingOther()
	{
		return false;
	}

	@ConfigItem(
		keyName = "miningStars",
		name = "Shooting stars",
		description = "Mining a shooting star; alerts when it drops a tier",
		section = mining,
		position = 1
	)
	default boolean miningStars()
	{
		return true;
	}

	@ConfigItem(
		keyName = "miningMotherlode",
		name = "Motherlode Mine",
		description = "Mining pay-dirt veins",
		section = mining,
		position = 2
	)
	default boolean miningMotherlode()
	{
		return true;
	}

	@ConfigItem(
		keyName = "miningAmethyst",
		name = "Amethyst",
		description = "Mining amethyst crystals",
		section = mining,
		position = 3
	)
	default boolean miningAmethyst()
	{
		return true;
	}

	@ConfigItem(
		keyName = "miningOther",
		name = "Other mining",
		description = "Any other rock. Off by default so power-mining isn't dimmed and alerted every few seconds",
		section = mining,
		position = 4
	)
	default boolean miningOther()
	{
		return false;
	}

	@ConfigItem(
		keyName = "woodcuttingMagic",
		name = "Magic trees",
		description = "Cutting magic trees",
		section = woodcutting,
		position = 1
	)
	default boolean woodcuttingMagic()
	{
		return true;
	}

	@ConfigItem(
		keyName = "woodcuttingYew",
		name = "Yews",
		description = "Cutting yew trees",
		section = woodcutting,
		position = 2
	)
	default boolean woodcuttingYew()
	{
		return true;
	}

	@ConfigItem(
		keyName = "woodcuttingRedwood",
		name = "Redwoods",
		description = "Cutting redwood trees",
		section = woodcutting,
		position = 3
	)
	default boolean woodcuttingRedwood()
	{
		return true;
	}

	@ConfigItem(
		keyName = "salvaging",
		name = "Salvaging",
		description = "Sailing: salvaging with a hook",
		section = other,
		position = 1
	)
	default boolean salvaging()
	{
		return true;
	}

	@ConfigItem(
		keyName = "gemstoneCrab",
		name = "Gemstone crab",
		description = "Attacking the Gemstone Crab; alerts when it burrows",
		section = other,
		position = 2
	)
	default boolean gemstoneCrab()
	{
		return true;
	}

	@ConfigItem(
		keyName = "plankMake",
		name = "Plank Make",
		description = "Casting Plank Make on an inventory of logs",
		section = other,
		position = 3
	)
	default boolean plankMake()
	{
		return true;
	}

	@ConfigItem(
		keyName = "campfire",
		name = "Campfires",
		description = "Tending a Forestry campfire",
		section = other,
		position = 4
	)
	default boolean campfire()
	{
		return true;
	}
}
