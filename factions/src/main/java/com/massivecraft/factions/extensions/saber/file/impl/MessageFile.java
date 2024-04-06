package com.massivecraft.factions.extensions.saber.file.impl;

import com.massivecraft.factions.extensions.saber.SaberCore;
import com.massivecraft.factions.extensions.saber.file.CustomFile;
import com.massivecraft.factions.extensions.saber.utils.Message;

public class MessageFile extends CustomFile {

    public MessageFile() {
        super(SaberCore.instance, "", "messages");
        for (Message message : Message.values()) {
            getConfig().addDefault(message.getConfig(), message.getMessage());
        }
        getConfig().options().copyDefaults(true);
        saveConfig();
    }


    public void init() {
        for (Message message : Message.values()) {
            message.setMessage(getConfig().getString(message.getConfig()));
        }
    }
}
