package com.afkskilling;

import net.runelite.api.Actor;
import net.runelite.api.NPC;
import net.runelite.api.Player;

/** Fishing at any spot: the player stays interacting with the spot NPC between catches. */
final class Fishing implements AfkActivity
{
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
		Actor target = player.getInteracting();
		if (!(target instanceof NPC))
		{
			return false;
		}
		String name = target.getName();
		// "Fishing spot" and "Rod Fishing spot" cover every regular spot, minnows, eels and karambwanji.
		return name != null && name.toLowerCase().contains("fishing spot");
	}
}
