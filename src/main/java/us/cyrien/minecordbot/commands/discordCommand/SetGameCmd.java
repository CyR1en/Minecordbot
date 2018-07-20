/*
 * Copyright 2017 John Grosh <john.a.grosh@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.entities.Game;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;

public class SetGameCmd extends MCBCommand {

    public SetGameCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "setgame";
        this.help = Locale.getCommandsMessage("setgame.description").finish();
        this.arguments = "[game]...";
        this.ownerCommand = true;
        this.category = Bot.OWNER;
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent event) {
        try {
            event.getJDA().getPresence().setGame(event.getArgs().isEmpty() ? null : Game.playing(event.getArgs()));
            String notPlaying = Locale.getCommandsMessage("setgame.notplaying").f(event.getJDA().getSelfUser().getName());
            String playing = Locale.getCommandsMessage("setgame.playing").f(event.getJDA().getSelfUser().getName());
            respond(event, event.getClient().getSuccess() + (event.getArgs().isEmpty() ? notPlaying : playing + "`" + event.getArgs() + "`"));
        } catch (Exception e) {
            event.reply(event.getClient().getError() + " The game could not be set!");
        }
    }
}
