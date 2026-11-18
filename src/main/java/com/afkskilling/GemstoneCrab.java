package com.afkskilling;

import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.gameval.NpcID;

/** Attacking the Gemstone Crab; the alert fires when it burrows and the player stops attacking. */
final class GemstoneCrab implements AfkActivity
{
	private static final Caption CAPTION = new Caption("Combat", "bonking the crab", "follow the crab");

	@Override
	public Caption detect(Client client, Player player, AfkSkillingConfig config)
	{
		Actor target = player.getInteracting();
		boolean attacking = target instanceof NPC && ((NPC) target).getId() == NpcID.GEMSTONE_CRAB;
		return attacking && config.gemstoneCrab() ? CAPTION : null;
	}
}
