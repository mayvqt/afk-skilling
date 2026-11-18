package com.afkskilling;

import java.util.Set;
import net.runelite.api.Actor;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Player;
import net.runelite.api.gameval.AnimationID;

/** Fishing: interacting with any NPC that has a fishing option, or playing a rod fishing animation. */
final class Fishing implements AfkActivity
{
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
	public String label()
	{
		return "Fishing";
	}

	@Override
	public boolean isEnabled(AfkSkillingConfig config)
	{
		return config.fishing();
	}

	@Override
	public boolean isActive(Player player)
	{
		return FISHING_ANIMATIONS.contains(player.getAnimation()) || isFishingSpot(player.getInteracting());
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
