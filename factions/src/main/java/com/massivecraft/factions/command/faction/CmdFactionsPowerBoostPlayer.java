package com.massivecraft.factions.command.faction;

import com.massivecraft.factions.command.type.TypeMPlayer;

public class CmdFactionsPowerBoostPlayer extends CmdFactionsPowerBoostAbstract
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsPowerBoostPlayer()
	{
		super(TypeMPlayer.get(), "player");
	}
	
}
