package com.afkskilling;

import java.util.Set;
import java.util.function.Predicate;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.gameval.AnimationID;

/**
 * An activity whose animation plays in bursts with short gaps (a spell cast per item, adding a log to a
 * fire), so it stays active for a few ticks after the animation was last seen.
 */
final class Repeating implements AfkActivity
{
	private static final int GAP_TICKS = 4;

	private final String label;
	private final Set<Integer> animations;
	private final Predicate<AfkSkillingConfig> enabled;
	private int ticksSinceSeen = GAP_TICKS + 1;

	private Repeating(String label, Set<Integer> animations, Predicate<AfkSkillingConfig> enabled)
	{
		this.label = label;
		this.animations = animations;
		this.enabled = enabled;
	}

	static Repeating plankMake()
	{
		return new Repeating("Magic",Set.of(AnimationID.DREAM_PLAYER_MAKE_PLANK_SPELL),
			AfkSkillingConfig::plankMake);
	}

	static Repeating campfire()
	{
		return new Repeating("Firemaking",Set.of(AnimationID.FORESTRY_CAMPFIRE),
			AfkSkillingConfig::campfire);
	}

	@Override
	public String detect(Client client, Player player, AfkSkillingConfig config)
	{
		ticksSinceSeen = animations.contains(player.getAnimation()) ? 0 : ticksSinceSeen + 1;
		return ticksSinceSeen <= GAP_TICKS && enabled.test(config) ? label : null;
	}
}
