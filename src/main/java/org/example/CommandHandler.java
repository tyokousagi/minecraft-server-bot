package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.io.IOException;

public class CommandHandler extends ListenerAdapter {
    private static final String channelID = PropertyUtil.getChannelId();
    private static final String guildID = PropertyUtil.getGuildId();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equalsIgnoreCase("status")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("status");
            embed.addField("BOT",PropertyUtil.getBotVersion(),false);
            embed.addField("Minecraft",PropertyUtil.getMinecraftVersion(),false);
            embed.setColor(Color.GREEN);
            event.replyEmbeds(embed.build()).queue();
        }else if(event.getName().equalsIgnoreCase("help")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("仕様書");
            embed.addField("status","BOTとMinecraftのバージョンを表示します",false);
            embed.addField("server start","サーバーを起動します",false);
            embed.addField("server stop","サーバーを停止します",false);
            embed.setColor(Color.CYAN);
            event.replyEmbeds(embed.build()).queue();
        }else if(event.getName().equalsIgnoreCase("server")) {
            String action = event.getOption("action").getAsString();
            if(action.equalsIgnoreCase("start")) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("サーバー起動");
                embed.setColor(Color.YELLOW);
                event.replyEmbeds(embed.build()).queue();
            }else if(action.equalsIgnoreCase("stop")) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("サーバー停止");
                embed.setColor(Color.DARK_GRAY);
                event.replyEmbeds(embed.build()).queue();
                MinecraftRcon.sendCommand("stop");
            }
        }
    }
}
