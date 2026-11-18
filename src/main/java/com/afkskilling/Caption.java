package com.afkskilling;

/** The text centred on the dimmed screen: the skill as the title, and a line under it that changes on alert. */
final class Caption
{
	final String skill;
	final String doing;
	final String stopped;

	Caption(String skill, String doing, String stopped)
	{
		this.skill = skill;
		this.doing = doing;
		this.stopped = stopped;
	}

	@Override
	public String toString()
	{
		return skill + " (" + doing + ")";
	}
}
