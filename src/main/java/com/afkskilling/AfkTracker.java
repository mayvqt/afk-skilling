package com.afkskilling;

/** Per-tick AFK state: dimmed while an activity runs, alerting once the player stands idle after it. */
final class AfkTracker
{
	enum Phase
	{
		NONE,
		AFK,
		ALERT,
	}

	private Phase phase = Phase.NONE;
	private int idleTicks;
	private boolean inputSinceActive;

	synchronized Phase phase()
	{
		return phase;
	}

	synchronized void tick(boolean active, boolean playerIdle, int alertDelayTicks)
	{
		if (active)
		{
			phase = Phase.AFK;
			idleTicks = 0;
			inputSinceActive = false;
			return;
		}
		if (phase != Phase.AFK)
		{
			return;
		}
		if (inputSinceActive)
		{
			// The user is back and took over (banking, dropping, walking off), so don't nag them.
			phase = Phase.NONE;
		}
		else if (!playerIdle)
		{
			idleTicks = 0;
		}
		else if (++idleTicks >= alertDelayTicks)
		{
			phase = Phase.ALERT;
		}
	}

	/** A key press or click: the user is at the keyboard. */
	synchronized void userInput()
	{
		inputSinceActive = true;
		if (phase == Phase.ALERT)
		{
			phase = Phase.NONE;
		}
	}

	synchronized void reset()
	{
		phase = Phase.NONE;
		idleTicks = 0;
		inputSinceActive = false;
	}
}
