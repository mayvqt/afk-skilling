package com.afkskilling;

import com.google.inject.Provides;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
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

@Slf4j
@PluginDescriptor(
	name = "AFK Skilling",
	description = "Dims the screen while you AFK skill, then pulses a soft glow when you go idle",
	tags = {"afk", "idle", "skilling", "fishing", "mining", "stars", "woodcutting", "sailing", "notifier", "dim"}
)
public class AfkSkillingPlugin extends Plugin
{
	// Instances, not constants: Repeating keeps per-tick state, so each plugin start gets fresh ones.
	private final List<AfkActivity> activities = List.of(
		new Fishing(), new Mining(), new Woodcutting(), new Salvaging(), new GemstoneCrab(),
		Repeating.plankMake(), Repeating.campfire());

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
	private String lastDebug;

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

		Caption active = null;
		for (AfkActivity activity : activities)
		{
			active = activity.detect(client, player, config);
			if (active != null)
			{
				overlay.setCaption(active);
				break;
			}
		}
		tracker.tick(active != null, idle, config.alertDelay());

		// For adding activities: shows the animation and target ids to match on, once per change.
		Actor target = player.getInteracting();
		String debug = "anim=" + player.getAnimation() + " target="
			+ (target instanceof NPC ? target.getName() + "#" + ((NPC) target).getId() : target == null ? "none" : target.getName())
			+ " activity=" + active;
		if (!debug.equals(lastDebug))
		{
			lastDebug = debug;
			log.debug("AFK: {} phase={}", debug, tracker.phase());
		}
	}

	@Provides
	AfkSkillingConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AfkSkillingConfig.class);
	}
}
