package com.massivecraft.factions.command.faction;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.command.req.ReqBankCommandsEnabled;
import com.massivecraft.factions.command.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsMoneyBalance extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsMoneyBalance()
	{
		// Parameters
		this.addParameter(TypeFaction.get(), "faction", "you");

		// Requirements
		this.addRequirements(ReqBankCommandsEnabled.get());
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		Faction faction = this.readArg(msenderFaction);
					
		if (faction != msenderFaction && ! Perm.MONEY_BALANCE_ANY.has(sender, true)) return;
		
		Econ.sendBalanceInfo(msender, faction);
	}
	
}
