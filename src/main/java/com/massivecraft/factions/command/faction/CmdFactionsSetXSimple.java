package com.massivecraft.factions.command.faction;

import com.massivecraft.factions.command.type.TypeFaction;

public abstract class CmdFactionsSetXSimple extends CmdFactionsSetX
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsSetXSimple(boolean claim)
	{
		// Super
		super(claim);
		
		// Parameters
		if (claim)
		{
			this.addParameter(TypeFaction.get(), "faction", "you");
			this.setFactionArgIndex(0);
		}
	}
	
}
