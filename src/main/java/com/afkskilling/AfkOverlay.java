package com.afkskilling;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

/** Full-canvas dim while AFK and a breathing edge glow when idle, both faded in and out. */
final class AfkOverlay extends Overlay
{
	private static final double DIM_FADE_SECONDS = 0.6;
	private static final double ALERT_FADE_SECONDS = 0.3;
	private static final double PULSE_SECONDS = 2.2;
	private static final double BREATHE_SECONDS = 6;
	private static final float[] GLOW_STOPS = {0f, 0.55f, 1f};
	private static final Color TEXT = new Color(240, 235, 225);
	private static final Font TITLE_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 44);
	private static final Font SUBTITLE_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 16);

	private final Client client;
	private final AfkSkillingConfig config;
	private final AfkTracker tracker;

	private double dim;
	private double alert;
	private double text;
	private long lastNanos;
	private volatile Caption caption;

	@Inject
	AfkOverlay(Client client, AfkSkillingConfig config, AfkTracker tracker)
	{
		this.client = client;
		this.config = config;
		this.tracker = tracker;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ALWAYS_ON_TOP);
		setPriority(PRIORITY_HIGHEST);
	}

	@Override
	public Dimension render(Graphics2D g)
	{
		long now = System.nanoTime();
		double dt = lastNanos == 0 ? 0 : Math.min((now - lastNanos) / 1e9, 0.25);
		lastNanos = now;

		AfkTracker.Phase phase = tracker.phase();
		dim = approach(dim, phase == AfkTracker.Phase.AFK ? config.dimOpacity() / 100.0 : 0, dt, DIM_FADE_SECONDS);
		alert = approach(alert, phase == AfkTracker.Phase.ALERT ? 1 : 0, dt, ALERT_FADE_SECONDS);
		text = approach(text, phase != AfkTracker.Phase.NONE && config.showActivity() ? 1 : 0, dt, DIM_FADE_SECONDS);
		if (dim < 0.002 && alert < 0.002 && text < 0.002)
		{
			return null;
		}

		int w = client.getCanvasWidth();
		int h = client.getCanvasHeight();
		if (dim >= 0.002)
		{
			g.setColor(new Color(0f, 0f, 0f, (float) dim));
			g.fillRect(0, 0, w, h);
		}
		double seconds = now / 1e9;
		Caption caption = this.caption;
		if (text >= 0.002 && caption != null)
		{
			drawCaption(g, caption, w, h, seconds);
		}
		if (alert >= 0.002)
		{
			double pulse = 0.5 - 0.5 * Math.cos(2 * Math.PI * seconds / PULSE_SECONDS);
			double strength = alert * (0.35 + 0.65 * pulse);
			Color c = config.alertPalette().at(seconds);
			float radius = (float) Math.hypot(w, h) / 2;
			g.setPaint(new RadialGradientPaint(w / 2f, h / 2f, Math.max(radius, 1), GLOW_STOPS, new Color[]{
				withAlpha(c, strength * 0.12),
				withAlpha(c, strength * 0.30),
				withAlpha(c, strength * 0.85),
			}));
			g.fillRect(0, 0, w, h);
		}
		return null;
	}

	void setCaption(Caption caption)
	{
		this.caption = caption;
	}

	/** The skill, breathing slowly, with a quiet line under it that swaps to the stopped line while alerting. */
	private void drawCaption(Graphics2D g, Caption caption, int w, int h, double seconds)
	{
		String title = caption.skill;
		String subtitle = alert > 0.5 ? caption.stopped : caption.doing;
		double breathe = 0.8 + 0.2 * Math.sin(2 * Math.PI * seconds / BREATHE_SECONDS);

		Object aa = g.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		int cy = h / 2;
		drawCentred(g, TITLE_FONT, title, w / 2, cy, text * breathe * 0.9);
		drawCentred(g, SUBTITLE_FONT, subtitle, w / 2, cy + 34, text * breathe * 0.5);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, aa);
	}

	private static void drawCentred(Graphics2D g, Font font, String s, int cx, int y, double alpha)
	{
		g.setFont(font);
		int x = cx - g.getFontMetrics().stringWidth(s) / 2;
		g.setColor(withAlpha(Color.BLACK, alpha * 0.5));
		g.drawString(s, x + 2, y + 2);
		g.setColor(withAlpha(TEXT, alpha));
		g.drawString(s, x, y);
	}

	/** Exponential ease toward the target, frame-rate independent. */
	private static double approach(double value, double target, double dt, double seconds)
	{
		return target + (value - target) * Math.exp(-dt * 3 / seconds);
	}

	private static Color withAlpha(Color c, double alpha)
	{
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) Math.round(Math.min(alpha, 1) * 255));
	}
}
