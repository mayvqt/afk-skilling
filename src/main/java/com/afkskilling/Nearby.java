package com.afkskilling;

import java.util.function.IntFunction;
import javax.annotation.Nullable;
import net.runelite.api.GameObject;
import net.runelite.api.Player;
import net.runelite.api.Tile;
import net.runelite.api.WallObject;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;

/**
 * Finds what the player is working on from the objects on the 3x3 tiles around them. Objects aren't
 * interaction targets, and a multi-tile object sits on every tile it covers, so the 3x3 is enough.
 */
final class Nearby
{
	private Nearby()
	{
	}

	/** The first non-null classification of a nearby wall or game object's id. */
	@Nullable
	static <T> T find(Player player, IntFunction<T> classify)
	{
		WorldView view = player.getWorldView();
		LocalPoint location = player.getLocalLocation();
		if (view == null || location == null)
		{
			return null;
		}
		Tile[][] tiles = view.getScene().getTiles()[view.getPlane()];
		int px = location.getSceneX();
		int py = location.getSceneY();
		for (int x = Math.max(px - 1, 0); x <= Math.min(px + 1, tiles.length - 1); x++)
		{
			for (int y = Math.max(py - 1, 0); y <= Math.min(py + 1, tiles[x].length - 1); y++)
			{
				T found = classify(tiles[x][y], classify);
				if (found != null)
				{
					return found;
				}
			}
		}
		return null;
	}

	@Nullable
	private static <T> T classify(Tile tile, IntFunction<T> classify)
	{
		if (tile == null)
		{
			return null;
		}
		WallObject wall = tile.getWallObject();
		if (wall != null)
		{
			T found = classify.apply(wall.getId());
			if (found != null)
			{
				return found;
			}
		}
		for (GameObject object : tile.getGameObjects())
		{
			if (object != null)
			{
				T found = classify.apply(object.getId());
				if (found != null)
				{
					return found;
				}
			}
		}
		return null;
	}
}
