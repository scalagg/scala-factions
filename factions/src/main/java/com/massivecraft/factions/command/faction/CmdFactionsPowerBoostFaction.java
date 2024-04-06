package com.massivecraft.factions.command.faction;

import com.massivecraft.factions.command.type.TypeFaction;

public class CmdFactionsPowerBoostFaction extends CmdFactionsPowerBoostAbstract
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsPowerBoostFaction()
	{
		super(TypeFaction.get(), "faction");
	}
	
}
