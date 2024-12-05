package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

public class CommandHandler extends ListenerAdapter {
    private static final String channelID = PropertyUtil.getChannelId();
    private static final String guildID = PropertyUtil.getGuildId();
    private static final String HOST = PropertyUtil.getRconHost();
    private static final int PORT = Integer.parseInt(PropertyUtil.getRconPort());

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equalsIgnoreCase("status")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("status");
            embed.addField("BOT",PropertyUtil.getBotVersion(),false);
            embed.addField("Minecraft",PropertyUtil.getMinecraftVersion(),false);
            embed.addField("サーバーの状態",MinecraftServerChecker.isServerRunningString(HOST,PORT),false);
            embed.setColor(Color.GREEN);
            event.replyEmbeds(embed.build()).queue();
        }else if(event.getName().equalsIgnoreCase("help")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("仕様書");
            embed.addField("status","BOTとMinecraftのバージョンとサーバーの状態を表示します",false);
            embed.addField("server start","サーバーを起動します",false);
            embed.addField("server stop","サーバーを停止します",false);
            embed.addField("server backup","サーバーのバックアップを開始します",false);
            embed.setColor(Color.CYAN);
            event.replyEmbeds(embed.build()).queue();
        }else if(event.getName().equalsIgnoreCase("server")) {
            String action = Objects.requireNonNull(event.getOption("action")).getAsString();
            String command = event.getOption("command") != null ? Objects.requireNonNull(event.getOption("command")).getAsString() : null;
            if(action.equalsIgnoreCase("start")) {
                EmbedBuilder embed = new EmbedBuilder();
                if(MinecraftServerChecker.isServerRunning(HOST,PORT)) {
                    embed.setTitle("サーバーはすでに起動しています");
                    embed.setColor(Color.RED);
                    event.replyEmbeds(embed.build()).queue();
                }else {
                    embed.setTitle("サーバー起動");
                    embed.setColor(Color.YELLOW);
                    event.replyEmbeds(embed.build()).queue();
                    BatFileRunner.serverStart();
                }

                BOT.serverActivity();

            }else if(action.equalsIgnoreCase("stop")) {
                EmbedBuilder embed = new EmbedBuilder();
                if(!MinecraftServerChecker.isServerRunning(HOST,PORT)) {
                    embed.setTitle("サーバーはすでに停止しています");
                    embed.setColor(Color.RED);
                    event.replyEmbeds(embed.build()).queue();
                }else {
                    embed.setTitle("サーバー停止");
                    embed.setColor(Color.DARK_GRAY);
                    event.replyEmbeds(embed.build()).queue();
                    MinecraftRcon.build();
                    MinecraftRcon.sendCommand("stop");
                }

                BOT.serverActivity();
            }else if(action.equalsIgnoreCase("backup")) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("バックアップ開始");
                embed.setColor(Color.CYAN);
                event.replyEmbeds(embed.build()).queue();
                BatFileRunner.backupStart();

            }else if(action.equalsIgnoreCase("execute")) {
                EmbedBuilder embed = new EmbedBuilder();
                if(command != null && !command.isEmpty()) {
                    MinecraftRcon.sendCommand(command);
                    embed.setTitle(command + "が実行されました");
                    embed.setColor(Color.GREEN);
                    event.replyEmbeds(embed.build()).queue();
                }else {
                    embed.setTitle("コマンド文字列を入力してください");
                    embed.setColor(Color.RED);
                    event.replyEmbeds(embed.build()).queue();
                }
            }
        }else if(event.getName().equalsIgnoreCase("whitelist")) {
            String action = Objects.requireNonNull(event.getOption("action")).getAsString();
            if(action.equalsIgnoreCase("add")) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("ホワイトリストに追加する名前を入力してください");
                embed.setColor(Color.GREEN);
                event.replyEmbeds(embed.build())
                        .setActionRow(Button.primary("input","文字を入力"))
                        .queue();
            }
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equalsIgnoreCase("input")) {
            // モーダルを作成
            Modal modal = Modal.create("input", "文字列を入力")
                    .addComponents(
                            ActionRow.of(
                                    TextInput.create("input_text", "入力欄", TextInputStyle.SHORT)
                                            .setPlaceholder("ここに文字を入力してください")
                                            .setRequired(true) // 必須入力
                                            .setMinLength(1) // 最小文字数
                                            .setMaxLength(100) // 最大文字数
                                            .build()
                            )
                    )
                    .build();

            // モーダルをユーザーに送信
            event.replyModal(modal).queue();
        }
    }
    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("input")) {
            String input = Objects.requireNonNull(event.getValue("input_text")).getAsString();
            // 入力された文字列を処理
            event.reply("あなたが入力した文字列: `" + input + "`").queue();
        }
    }


}
