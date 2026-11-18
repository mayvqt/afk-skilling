package com.afkskilling;

import java.util.Set;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.gameval.AnimationID;
import net.runelite.api.gameval.ObjectID;

/**
 * Mining with any pickaxe, toggled per spot: shooting stars, Motherlode, amethyst, dense essence, or other rocks (off by
 * default, so power-mining isn't dimmed). A star dropping a tier stops the animation, which triggers the
 * alert. Boss and raid variants (Zalcano, Het) are left out.
 */
final class Mining implements AfkActivity
{
	private static final Set<Integer> MINING_ANIMATIONS = Set.of(
		AnimationID.HUMAN_MINING_BRONZE_PICKAXE,
		AnimationID.HUMAN_MINING_IRON_PICKAXE,
		AnimationID.HUMAN_MINING_STEEL_PICKAXE,
		AnimationID.HUMAN_MINING_BLACK_PICKAXE,
		AnimationID.HUMAN_MINING_MITHRIL_PICKAXE,
		AnimationID.HUMAN_MINING_ADAMANT_PICKAXE,
		AnimationID.HUMAN_MINING_RUNE_PICKAXE,
		AnimationID.HUMAN_MINING_GILDED_PICKAXE,
		AnimationID.HUMAN_MINING_DRAGON_PICKAXE,
		AnimationID.HUMAN_MINING_DRAGON_PICKAXE_PRETTY,
		AnimationID.HUMAN_MINING_INFERNAL_PICKAXE,
		AnimationID.HUMAN_MINING_3A_PICKAXE,
		AnimationID.HUMAN_MINING_CRYSTAL_PICKAXE,
		AnimationID.HUMAN_MINING_LEAGUE_TRAILBLAZER_PICKAXE,
		AnimationID.HUMAN_MINING_TRAILBLAZER_PICKAXE,
		AnimationID.HUMAN_MINING_TRAILBLAZER_PICKAXE_NO_INFERNAL,
		AnimationID.HUMAN_MINING_TRAILBLAZER_RELOADED_PICKAXE,
		AnimationID.HUMAN_MINING_TRAILBLAZER_RELOADED_PICKAXE_NO_INFERNAL,

		AnimationID.HUMAN_MINING_BRONZE_PICKAXE_NOREACHFORWARD,
		AnimationID.HUMAN_MINING_IRON_PICKAXE_NOREACHFORWARD,
		AnimationID.HUMAN_MINING_STEEL_PICKAXE_NOREACHFORWARD,
		AnimationID.HUMAN_MINING_BLACK_PICKAXE_NOREACHFORWARD,
		AnimationID.HUMAN_MINING_MITHRIL_PICKAXE_NOREACHFORWARD,
		AnimationID.HUMAN_MINING_ADAMANT_PICKAXE_NOREACHFORWARD,
		AnimationID.HUMAN_MINING_RUNE_PICKAXE_NOREACHFORWARD,
		AnimationID.HUMAN_MINING_GILDED_PICKAXE_NOREACHFORWARD,
		AnimationID.HUMAN_MINING_DRAGON_PICKAXE_NOREACHFORWARD,
		AnimationID.HUMAN_MINING_DRAGON_PICKAXE_PRETTY_NOREACHFORWARD,
		AnimationID.HUMAN_MINING_INFERNAL_PICKAXE_NOREACHFORWARD,
		AnimationID.HUMAN_MINING_3A_PICKAXE_NOREACHFORWARD,
		AnimationID.HUMAN_MINING_CRYSTAL_PICKAXE_NOREACHFORWARD,
		AnimationID.HUMAN_MINING_LEAGUE_TRAILBLAZER_PICKAXE_NOREACHFORWARD,
		AnimationID.HUMAN_MINING_TRAILBLAZER_PICKAXE_NOREACHFORWARD,
		AnimationID.HUMAN_MINING_TRAILBLAZER_PICKAXE_NO_INFERNAL_NOREACHFORWARD,
		AnimationID.HUMAN_MINING_TRAILBLAZER_RELOADED_PICKAXE_NOREACHFORWARD,
		AnimationID.HUMAN_MINING_TRAILBLAZER_RELOADED_PICKAXE_NO_INFERNAL_NOREACHFORWARD,

		AnimationID.HUMAN_MINING_BRONZE_PICKAXE_WALL,
		AnimationID.HUMAN_MINING_IRON_PICKAXE_WALL,
		AnimationID.HUMAN_MINING_STEEL_PICKAXE_WALL,
		AnimationID.HUMAN_MINING_BLACK_PICKAXE_WALL,
		AnimationID.HUMAN_MINING_MITHRIL_PICKAXE_WALL,
		AnimationID.HUMAN_MINING_ADAMANT_PICKAXE_WALL,
		AnimationID.HUMAN_MINING_RUNE_PICKAXE_WALL,
		AnimationID.HUMAN_MINING_GILDED_PICKAXE_WALL,
		AnimationID.HUMAN_MINING_DRAGON_PICKAXE_WALL,
		AnimationID.HUMAN_MINING_DRAGON_PICKAXE_PRETTY_WALL,
		AnimationID.HUMAN_MINING_INFERNAL_PICKAXE_WALL,
		AnimationID.HUMAN_MINING_3A_PICKAXE_WALL,
		AnimationID.HUMAN_MINING_CRYSTAL_PICKAXE_WALL,
		AnimationID.HUMAN_MINING_LEAGUE_TRAILBLAZER_PICKAXE_WALL,
		AnimationID.HUMAN_MINING_TRAILBLAZER_PICKAXE_WALL,
		AnimationID.HUMAN_MINING_TRAILBLAZER_PICKAXE_NO_INFERNAL_WALL,
		AnimationID.HUMAN_MINING_TRAILBLAZER_RELOADED_PICKAXE_WALL,
		AnimationID.HUMAN_MINING_TRAILBLAZER_RELOADED_PICKAXE_NO_INFERNAL_WALL);

	private static final Set<Integer> STARS = Set.of(
		ObjectID.STAR_SIZE_ONE_STAR,
		ObjectID.STAR_SIZE_TWO_STAR,
		ObjectID.STAR_SIZE_THREE_STAR,
		ObjectID.STAR_SIZE_FOUR_STAR,
		ObjectID.STAR_SIZE_FIVE_STAR,
		ObjectID.STAR_SIZE_SIX_STAR,
		ObjectID.STAR_SIZE_SEVEN_STAR,
		ObjectID.STAR_SIZE_EIGHT_STAR,
		ObjectID.STAR_SIZE_NINE_STAR);

	private static final Set<Integer> MOTHERLODE_VEINS = Set.of(
		ObjectID.MOTHERLODE_ORE_SINGLE,
		ObjectID.MOTHERLODE_ORE_LEFT,
		ObjectID.MOTHERLODE_ORE_MIDDLE,
		ObjectID.MOTHERLODE_ORE_RIGHT);

	private static final Set<Integer> AMETHYST = Set.of(
		ObjectID.AMETHYSTROCK1,
		ObjectID.AMETHYSTROCK2);

	private static final Set<Integer> DENSE_RUNESTONES = Set.of(
		ObjectID.ARCEUUS_RUNESTONE_BASE_MINE,
		ObjectID.ARCEUUS_RUNESTONE_MIDDLE_MINE,
		ObjectID.ARCEUUS_RUNESTONE_TOP_MINE);

	private enum Spot
	{
		STAR,
		MOTHERLODE,
		AMETHYST,
		DENSE_ESSENCE,
	}

	@Override
	public String detect(Client client, Player player, AfkSkillingConfig config)
	{
		if (!MINING_ANIMATIONS.contains(player.getAnimation()))
		{
			return null;
		}
		Spot spot = Nearby.find(player, Mining::spot);
		if (spot == null)
		{
			return config.miningOther() ? "Mining" : null;
		}
		switch (spot)
		{
			case STAR:
				return config.miningStars() ? "Mining" : null;
			case MOTHERLODE:
				return config.miningMotherlode() ? "Mining" : null;
			case AMETHYST:
				return config.miningAmethyst() ? "Mining" : null;
			default:
				return config.miningDenseEssence() ? "Mining" : null;
		}
	}

	private static Spot spot(int objectId)
	{
		if (STARS.contains(objectId))
		{
			return Spot.STAR;
		}
		if (MOTHERLODE_VEINS.contains(objectId))
		{
			return Spot.MOTHERLODE;
		}
		if (AMETHYST.contains(objectId))
		{
			return Spot.AMETHYST;
		}
		return DENSE_RUNESTONES.contains(objectId) ? Spot.DENSE_ESSENCE : null;
	}
}