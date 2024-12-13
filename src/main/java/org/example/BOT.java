package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.awt.*;
import java.util.Objects;


public class BOT extends ListenerAdapter {

    private static  final String HOST = "192.168.1.8";
    private static final int PORT = 25565;
    private static JDA jda = null;
    public static void startBot() throws InterruptedException {
        System.setProperty("file.encoding", "UTF-8");
        String token = PropertyUtil.getToken();
        jda = JDABuilder.createDefault(token)
                .setRawEventsEnabled(true)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new CommandHandler(),new BOT())
                .setActivity(Activity.playing("v1.0"))
                .setAutoReconnect(true)
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
                .addOptions(
                        new OptionData(OptionType.STRING, "action", "サーバーの操作を指定")
                                .addChoices(
                                        new Command.Choice("start", "start"),
                                        new Command.Choice("stop", "stop"),
                                        new Command.Choice("backup", "backup"),
                                        new Command.Choice("execute", "execute")
                                )
                                .setRequired(true),
                        new OptionData(OptionType.STRING, "command", "実行するコマンド")
                                .setRequired(false) // "execute" 以外のときは不要
                )
                .queue(
                        success -> System.out.println("コマンドが登録されました: server"),
                        failure -> System.out.println("コマンドの登録に失敗しました: " + failure.getMessage())
                );
        jda.upsertCommand("whitelist","ホワイトリストの管理")
                .addOptions(
                        new OptionData(OptionType.STRING,"action","ホワイトリストの管理")
                                .addChoices(
                                        new Command.Choice("add","add"),
                                        new Command.Choice("delete","delete"),
                                        new Command.Choice("info","info")
                                )
                                .setRequired(true),
                        new OptionData(OptionType.STRING, "username","編集するユーザネーム")
                                .setRequired(true)
                )
                .queue(
                        success -> System.out.println("コマンドが登録されました: whitelist"),
                        failure -> System.out.println("コマンドが登録に失敗しました: " + failure.getMessage())
                );
        
    }
    public static void sendMessageToChannel(String guildID,String channelID,String message) {
        TextChannel channel = Objects.requireNonNull(jda.getGuildById(guildID)).getTextChannelById(channelID);

        if(channel != null) {
            channel.sendMessage(message).queue();
        }else {
            System.out.println("指定されたチャンネルが見つかりませんでした");
        }
    }
    public static void sendEmbedMessageToChannel(String guildID, String channelID, String message, Color color) {
        TextChannel channel = Objects.requireNonNull(jda.getGuildById(guildID)).getTextChannelById(channelID);

        if(channel != null) {
            channel.sendMessageEmbeds(
                    new EmbedBuilder()
                            .setTitle(message)
                            .setColor(color)
                            .build()
            ).queue();
        }
    }
    public static void serverActivity() {
        boolean isServerActive = MinecraftServerChecker.isServerRunning(HOST,PORT);
        if(isServerActive) {
            jda.getPresence().setActivity(Activity.customStatus("サーバー起動中"));
        }else {
            jda.getPresence().setActivity(Activity.customStatus("サーバー停止中"));
        }
    }
}
