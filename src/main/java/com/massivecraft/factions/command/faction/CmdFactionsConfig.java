package com.massivecraft.factions.command.faction;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.command.editor.CommandEditSingleton;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

public class CmdFactionsConfig extends CommandEditSingleton<MConf>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsConfig()
	{
		super(MConf.get());
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.CONFIG));
	}
	
}
