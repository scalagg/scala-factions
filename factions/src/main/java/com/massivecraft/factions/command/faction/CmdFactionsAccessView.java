package com.massivecraft.factions.command.faction;

public class CmdFactionsAccessView extends CmdFactionsAccessAbstract
{
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void innerPerform()
	{
		this.sendAccessInfo();
	}
	
}
