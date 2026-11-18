package com.afkskilling;

import javax.annotation.Nullable;
import net.runelite.api.Client;
import net.runelite.api.Player;

/** An AFK activity the plugin dims for and alerts on when it stops. Called once per game tick. */
interface AfkActivity
{
	/**
	 * What to show on the dimmed screen for the activity the local player is doing this tick, or null when
	 * they aren't doing it or it's switched off in the config.
	 */
	@Nullable
	Caption detect(Client client, Player player, AfkSkillingConfig config);
}
