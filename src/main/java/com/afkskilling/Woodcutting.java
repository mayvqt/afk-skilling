package com.afkskilling;

import java.util.Set;
import net.runelite.api.Client;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Player;
import net.runelite.api.gameval.AnimationID;

/**
 * Woodcutting magic trees, yews and redwoods. Trees are matched by name, since each has dozens of object
 * ids (farming growth stages, Forestry, Woodcutting Guild); other trees are too quick to be AFK.
 */
final class Woodcutting implements AfkActivity
{
	private static final Set<Integer> WOODCUTTING_ANIMATIONS = Set.of(
		AnimationID.HUMAN_WOODCUTTING_BRONZE_AXE,
		AnimationID.HUMAN_WOODCUTTING_IRON_AXE,
		AnimationID.HUMAN_WOODCUTTING_STEEL_AXE,
		AnimationID.HUMAN_WOODCUTTING_BLACK_AXE,
		AnimationID.HUMAN_WOODCUTTING_MITHRIL_AXE,
		AnimationID.HUMAN_WOODCUTTING_ADAMANT_AXE,
		AnimationID.HUMAN_WOODCUTTING_RUNE_AXE,
		AnimationID.HUMAN_WOODCUTTING_GILDED_AXE,
		AnimationID.HUMAN_WOODCUTTING_DRAGON_AXE,
		AnimationID.HUMAN_WOODCUTTING_INFERNAL_AXE,
		AnimationID.HUMAN_WOODCUTTING_3A_AXE,
		AnimationID.HUMAN_WOODCUTTING_CRYSTAL_AXE,
		AnimationID.HUMAN_WOODCUTTING_BLESSED_AXE,
		AnimationID.HUMAN_WOODCUTTING_BLESSED_AXE_MISS,
		AnimationID.HUMAN_WOODCUTTING_LEAGUE_AXE,
		AnimationID.HUMAN_WOODCUTTING_TRAILBLAZER_AXE,
		AnimationID.HUMAN_WOODCUTTING_TRAILBLAZER_AXE_NO_INFERNAL,
		AnimationID.HUMAN_WOODCUTTING_TRAILBLAZER_RELOADED_AXE,
		AnimationID.HUMAN_WOODCUTTING_TRAILBLAZER_RELOADED_AXE_NO_INFERNAL);

	@Override
	public String detect(Client client, Player player, AfkSkillingConfig config)
	{
		if (!WOODCUTTING_ANIMATIONS.contains(player.getAnimation()))
		{
			return null;
		}
		Tree tree = Nearby.find(player, id -> tree(client, id));
		if (tree == null)
		{
			return null;
		}
		switch (tree)
		{
			case MAGIC:
				return config.woodcuttingMagic() ? "Woodcutting" : null;
			case YEW:
				return config.woodcuttingYew() ? "Woodcutting" : null;
			default:
				return config.woodcuttingRedwood() ? "Woodcutting" : null;
		}
	}

	private enum Tree
	{
		MAGIC,
		YEW,
		REDWOOD,
	}

	private static Tree tree(Client client, int objectId)
	{
		ObjectComposition composition = client.getObjectDefinition(objectId);
		String name = composition == null ? null : composition.getName();
		if (name == null)
		{
			return null;
		}
		name = name.toLowerCase();
		if (name.startsWith("magic tree"))
		{
			return Tree.MAGIC;
		}
		if (name.startsWith("yew"))
		{
			return Tree.YEW;
		}
		return name.startsWith("redwood") ? Tree.REDWOOD : null;
	}
}
