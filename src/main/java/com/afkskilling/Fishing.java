package com.afkskilling;

import java.util.Set;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Player;
import net.runelite.api.gameval.AnimationID;
import net.runelite.api.gameval.NpcID;

/**
 * Fishing, toggled per spot: anglerfish, karambwans, or any other spot (off by default, so power-fishing
 * isn't dimmed). The Sailing boat spots are separate NPCs from the shore ones.
 */
final class Fishing implements AfkActivity
{
	private static final Set<Integer> ANGLER_SPOTS = Set.of(
		NpcID.PISCARILIUSFISH,
		NpcID.FISHING_BOAT_PISCARILIUSFISH);

	private static final Set<Integer> KARAMBWAN_SPOTS = Set.of(
		NpcID._0_45_48_KARAMBWAN,
		NpcID.FISHING_BOAT_KARAMBWANFISH);

	private static final Set<String> FISHING_OPTIONS = Set.of(
		"net", "small net", "big net", "bait", "lure", "cage", "harpoon", "fish", "catch");

	private static final Set<Integer> FISHING_ANIMATIONS = Set.of(
		AnimationID.HUMAN_FISHING_CASTING,
		AnimationID.HUMAN_FISH_ONSPOT,
		AnimationID.HUMAN_FISHING_CASTING_PEARL,
		AnimationID.HUMAN_FISHING_CASTING_PEARL_FLY,
		AnimationID.HUMAN_FISHING_CASTING_PEARL_BRUT,
		AnimationID.HUMAN_FISHING_CASTING_PEARL_OILY,
		AnimationID.HUMAN_FISH_ONSPOT_PEARL,
		AnimationID.HUMAN_FISH_ONSPOT_PEARL_FLY,
		AnimationID.HUMAN_FISH_ONSPOT_PEARL_BRUT,
		AnimationID.HUMAN_FISH_ONSPOT_PEARL_OILY,
		AnimationID.HUMAN_FISHING_CASTING_BRUT,
		AnimationID.HUMAN_FISHING_ONSPOT_BRUT,
		AnimationID.HUMAN_FISHING_CASTING_NPC);

	@Override
	public String detect(Client client, Player player, AfkSkillingConfig config)
	{
		Actor target = player.getInteracting();
		if (target instanceof NPC)
		{
			int id = ((NPC) target).getId();
			if (ANGLER_SPOTS.contains(id))
			{
				return config.fishingAnglers() ? "Anglerfish" : null;
			}
			if (KARAMBWAN_SPOTS.contains(id))
			{
				return config.fishingKarambwans() ? "Karambwans" : null;
			}
		}
		boolean fishing = FISHING_ANIMATIONS.contains(player.getAnimation()) || isFishingSpot(target);
		return fishing && config.fishingOther() ? "Fishing" : null;
	}

	private static boolean isFishingSpot(Actor target)
	{
		if (!(target instanceof NPC))
		{
			return false;
		}
		String name = target.getName();
		if (name != null && name.toLowerCase().contains("fishing spot"))
		{
			return true;
		}
		NPCComposition composition = ((NPC) target).getTransformedComposition();
		if (composition == null)
		{
			return false;
		}
		for (String action : composition.getActions())
		{
			if (action != null && FISHING_OPTIONS.contains(action.toLowerCase()))
			{
				return true;
			}
		}
		return false;
	}
}
