package com.afkskilling;

import javax.annotation.Nullable;
import net.runelite.api.Client;
import net.runelite.api.Player;

/** An AFK activity the plugin dims for and alerts on when it stops. Called once per game tick. */
interface AfkActivity
{
	/**
	 * The skill the local player is training this tick ("Fishing", not the spot), shown in the middle of
	 * the dimmed screen, or null when
	 * they aren't doing this activity or it's switched off in the config.
	 */
	@Nullable
	String detect(Client client, Player player, AfkSkillingConfig config);
}
