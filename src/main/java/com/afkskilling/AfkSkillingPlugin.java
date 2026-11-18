package com.afkskilling;

import com.google.inject.Provides;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.input.MouseAdapter;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "AFK Skilling",
	description = "Dims the screen while you AFK skill, then pulses a soft glow when you go idle",
	tags = {"afk", "idle", "skilling", "fishing", "mining", "stars", "notifier", "dim"}
)
public class AfkSkillingPlugin extends Plugin
{
	static final List<AfkActivity> ACTIVITIES = List.of(new Fishing(), new Mining());

	// gameval has no constant for "no animation"; the legacy AnimationID.IDLE is deprecated.
	private static final int NO_ANIMATION = -1;

	@Inject
	private Client client;

	@Inject
	private AfkSkillingConfig config;

	@Inject
	private AfkTracker tracker;

	@Inject
	private AfkOverlay overlay;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private KeyManager keyManager;

	@Inject
	private MouseManager mouseManager;

	private WorldPoint lastLocation;

	private final KeyListener keyListener = new KeyListener()
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			tracker.userInput();
		}

		@Override
		public void keyTyped(KeyEvent e)
		{
		}

		@Override
		public void keyReleased(KeyEvent e)
		{
		}
	};

	private final MouseAdapter mouseListener = new MouseAdapter()
	{
		@Override
		public MouseEvent mousePressed(MouseEvent e)
		{
			tracker.userInput();
			return e;
		}
	};

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);
		keyManager.registerKeyListener(keyListener);
		mouseManager.registerMouseListener(mouseListener);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		keyManager.unregisterKeyListener(keyListener);
		mouseManager.unregisterMouseListener(mouseListener);
		tracker.reset();
		lastLocation = null;
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() != GameState.LOGGED_IN)
		{
			tracker.reset();
			lastLocation = null;
		}
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		Player player = client.getLocalPlayer();
		if (player == null)
		{
			return;
		}
		WorldPoint location = player.getWorldLocation();
		boolean idle = player.getAnimation() == NO_ANIMATION
			&& player.getInteracting() == null
			&& location.equals(lastLocation);
		lastLocation = location;

		String active = null;
		for (AfkActivity activity : ACTIVITIES)
		{
			active = activity.detect(player, config);
			if (active != null)
			{
				overlay.setActivity(active);
				break;
			}
		}
		tracker.tick(active != null, idle, config.alertDelay());
	}

	@Provides
	AfkSkillingConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AfkSkillingConfig.class);
	}
}
