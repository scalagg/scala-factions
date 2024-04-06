package com.massivecraft.factions.command.type;

public class TypeFactionNameStrict extends TypeFactionNameAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeFactionNameStrict i = new TypeFactionNameStrict();
	public static TypeFactionNameStrict get() {return i; }
	public TypeFactionNameStrict()
	{
		super(true);
	}
	
}
