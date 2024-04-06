package com.massivecraft.factions.views;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.money.Money;
import net.evilblock.cubed.scoreboard.ScoreboardAdapter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

public class FactionsScoreboardView extends ScoreboardAdapter {

    @Override
    public void getLines(@NotNull LinkedList<String> board, @NotNull Player player) {
        for (String line : Factions.get().getConfig().getStringList("scoreboard.lines")) {
            board.add(format(line, player));
        }
    }

    @NotNull
    @Override
    public String getTitle(@NotNull Player player) {
        return Factions.get().getConfig().getString("scoreboard.title");
    }

    public String format(String line, Player player) {
        String factionDisplay = "None";
        MPlayer factionPlayer = MPlayer.get(player.getUniqueId());

        if (factionPlayer != null && factionPlayer.hasFaction()) {
            factionDisplay = factionPlayer.getFactionName();
        }

        String balanceLine = "0.0";
        if (factionPlayer != null) {
            balanceLine = Money.format(Money.get(player.getUniqueId()));
        }

        return line.replace("{faction}", factionDisplay).replace("{balance}", balanceLine);
    }
}
