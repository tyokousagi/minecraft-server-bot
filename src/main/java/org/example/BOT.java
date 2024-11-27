package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationMap;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.awt.*;
import java.util.Arrays;
import java.util.Map;


public class BOT extends ListenerAdapter {
    private static JDA jda = null;
    public static void startBot() throws InterruptedException {
        System.setProperty("file.encoding", "UTF-8");
        String token = PropertyUtil.getToken();
        jda = JDABuilder.createDefault(token)
                .setRawEventsEnabled(true)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new CommandHandler(),new BOT())
                .setActivity(Activity.playing("v1.0"))
                .build();
        jda.awaitReady();

        registerCommands();
        jda.updateCommands().queue();
    }
    private static void registerCommands() {
        jda.upsertCommand("status", "バージョンを表示").queue(
                success -> System.out.println("コマンドが登録されました: status"),
                failure -> System.out.println("コマンドの登録に失敗しました: " + failure.getMessage())
        );
        jda.upsertCommand("help","このBOTでできることを表示").queue(
                success -> System.out.println("コマンドが登録されました: help"),
                failure -> System.out.println("コマンドの登録に失敗しました: " + failure.getMessage())
        );

        jda.upsertCommand("server", "サーバーの操作")
                .addOptions(new OptionData(OptionType.STRING, "action", "サーバーの操作を指定")
                        .addChoices(
                                new Command.Choice("start", "start"),
                                new Command.Choice("stop", "stop")
                        )
                        .setRequired(true))
                .queue(
                        success -> System.out.println("コマンドが登録されました: server"),
                        failure -> System.out.println("コマンドの登録に失敗しました: " + failure.getMessage())
                );
    }
    public static void sendMessageToChannel(String guildID,String channelID,String message) {
        TextChannel channel = jda.getGuildById(guildID).getTextChannelById(channelID);

        if(channel != null) {
            channel.sendMessage(message).queue();
        }else {
            System.out.println("指定されたチャンネルが見つかりませんでした");
        }
    }
    public static void sendEmbedMessageToChannel(String guildID,String channelID,String message) {
        TextChannel channel = jda.getGuildById(guildID).getTextChannelById(channelID);

        if(channel != null) {
            channel.sendMessageEmbeds(
                    new EmbedBuilder()
                            .setTitle(message)
                            .build()
            ).queue();
        }
    }
}
