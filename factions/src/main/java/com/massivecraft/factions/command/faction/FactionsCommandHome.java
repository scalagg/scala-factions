package com.massivecraft.factions.command.faction;

import com.massivecraft.factions.command.req.ReqFactionHomesEnabled;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.command.Visibility;

public class FactionsCommandHome extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public FactionsCommandHome()
	{
		this.addRequirements(ReqFactionHomesEnabled.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Visibility getVisibility()
	{
		return MConf.get().homesEnabled ? super.getVisibility() : Visibility.INVISIBLE;  
	}
	
}
