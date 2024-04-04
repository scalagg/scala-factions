package com.massivecraft.factions.command.faction;

import com.massivecraft.factions.command.req.ReqHasFaction;

public class CmdFactionsLeave extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsLeave()
	{
		// Requirements
		this.addRequirements(ReqHasFaction.get());
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		msender.leave();
	}
	
}
