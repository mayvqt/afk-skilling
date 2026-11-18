package com.afkskilling;

import net.runelite.api.Player;

/** An AFK activity the plugin dims for and alerts on when it stops. */
interface AfkActivity
{
	/** Shown in the middle of the dimmed screen. */
	String label();

	boolean isEnabled(AfkSkillingConfig config);

	/** Whether the local player is doing this activity on the current tick. */
	boolean isActive(Player player);
}
