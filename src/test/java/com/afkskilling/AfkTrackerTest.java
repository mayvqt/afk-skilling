package com.afkskilling;

import static com.afkskilling.AfkTracker.Phase.AFK;
import static com.afkskilling.AfkTracker.Phase.ALERT;
import static com.afkskilling.AfkTracker.Phase.NONE;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class AfkTrackerTest
{
	private final AfkTracker tracker = new AfkTracker();

	@Test
	public void alertsAfterStandingIdleForTheDelay()
	{
		tracker.tick(true, false, 2);
		assertEquals(AFK, tracker.phase());
		tracker.tick(false, true, 2);
		assertEquals(AFK, tracker.phase());
		tracker.tick(false, true, 2);
		assertEquals(ALERT, tracker.phase());
	}

	@Test
	public void trailingMovementDelaysTheAlertInsteadOfCancellingIt()
	{
		tracker.tick(true, false, 2);
		tracker.tick(false, true, 2);
		tracker.tick(false, false, 2);
		tracker.tick(false, true, 2);
		assertEquals(AFK, tracker.phase());
		tracker.tick(false, true, 2);
		assertEquals(ALERT, tracker.phase());
	}

	@Test
	public void inputDismissesTheAlert()
	{
		tracker.tick(true, false, 1);
		tracker.tick(false, true, 1);
		tracker.userInput();
		assertEquals(NONE, tracker.phase());
	}

	@Test
	public void inputAfterTheActivityStopsMeansNoAlert()
	{
		tracker.tick(true, false, 1);
		tracker.userInput();
		tracker.tick(false, true, 1);
		assertEquals(NONE, tracker.phase());
		tracker.tick(false, true, 1);
		assertEquals(NONE, tracker.phase());
	}

	@Test
	public void inputDuringTheActivityIsForgottenNextTick()
	{
		tracker.tick(true, false, 1);
		tracker.userInput();
		tracker.tick(true, false, 1);
		tracker.tick(false, true, 1);
		assertEquals(ALERT, tracker.phase());
	}

	@Test
	public void resumingTheActivityEndsTheAlert()
	{
		tracker.tick(true, false, 1);
		tracker.tick(false, true, 1);
		tracker.tick(true, false, 1);
		assertEquals(AFK, tracker.phase());
	}

	@Test
	public void idlingWithoutAnActivityNeverAlerts()
	{
		tracker.tick(false, true, 1);
		tracker.tick(false, true, 1);
		assertEquals(NONE, tracker.phase());
	}
}
