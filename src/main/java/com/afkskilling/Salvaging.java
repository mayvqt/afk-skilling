package com.afkskilling;

import java.util.Set;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.gameval.AnimationID;

/** Sailing salvaging with a hook. The hook's idle, inactive and reset animations don't count as salvaging. */
final class Salvaging implements AfkActivity
{
	private static final Set<Integer> SALVAGING_ANIMATIONS = Set.of(
		AnimationID.SAILING_HUMAN_SALVAGE_HOOK_KANDARIN_1X3_SALVAGING01,
		AnimationID.SAILING_HUMAN_SALVAGE_HOOK_KANDARIN_1X3_PULL01,
		AnimationID.SAILING_HUMAN_SALVAGE_HOOK_KANDARIN_1X3_INTERACT01,
		AnimationID.SAILING_HUMAN_SALVAGE_HOOK_KANDARIN_1X3_DROP01,
		AnimationID.SAILING_HUMAN_SALVAGE_HOOK_KANDARIN_2X5_DROP01,
		AnimationID.SAILING_HUMAN_SALVAGE_HOOK_KANDARIN_3X8_DROP01,
		AnimationID.HUMAN_SAILING_SALVAGE01_LARGE01_DROP01,
		AnimationID.HUMAN_SAILING_SALVAGE01_LARGE01_INTERACT01);

	@Override
	public String detect(Client client, Player player, AfkSkillingConfig config)
	{
		return config.salvaging() && SALVAGING_ANIMATIONS.contains(player.getAnimation()) ? "Sailing" : null;
	}
}
