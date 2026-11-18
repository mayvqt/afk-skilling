package com.afkskilling;

import java.awt.Color;

enum AlertPalette
{
	PASTELS("Soft pastels", Colors.LAVENDER, Colors.PEACH, Colors.MINT, Colors.SKY, Colors.ROSE),
	LAVENDER("Lavender", Colors.LAVENDER),
	PEACH("Peach", Colors.PEACH),
	MINT("Mint", Colors.MINT),
	SKY("Sky", Colors.SKY),
	ROSE("Rose", Colors.ROSE);

	private static final double SECONDS_PER_COLOUR = 4;

	private final String label;
	private final Color[] colours;

	AlertPalette(String label, Color... colours)
	{
		this.label = label;
		this.colours = colours;
	}

	/** The colour at a point in time, drifting smoothly through the palette. */
	Color at(double seconds)
	{
		if (colours.length == 1)
		{
			return colours[0];
		}
		double pos = (seconds / SECONDS_PER_COLOUR) % colours.length;
		int i = (int) pos;
		Color from = colours[i];
		Color to = colours[(i + 1) % colours.length];
		double t = pos - i;
		t = t * t * (3 - 2 * t);
		return new Color(
			lerp(from.getRed(), to.getRed(), t),
			lerp(from.getGreen(), to.getGreen(), t),
			lerp(from.getBlue(), to.getBlue(), t));
	}

	private static int lerp(int a, int b, double t)
	{
		return (int) Math.round(a + (b - a) * t);
	}

	@Override
	public String toString()
	{
		return label;
	}

	private static final class Colors
	{
		static final Color LAVENDER = new Color(190, 170, 255);
		static final Color PEACH = new Color(255, 194, 160);
		static final Color MINT = new Color(160, 235, 200);
		static final Color SKY = new Color(160, 205, 255);
		static final Color ROSE = new Color(255, 175, 205);
	}
}
