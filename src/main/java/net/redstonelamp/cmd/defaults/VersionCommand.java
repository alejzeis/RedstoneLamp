/*
 * This file is part of RedstoneLamp.
 *
 * RedstoneLamp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.redstonelamp.cmd.defaults;

import net.redstonelamp.RedstoneLamp;
import net.redstonelamp.cmd.Command;
import net.redstonelamp.cmd.CommandExecutor;
import net.redstonelamp.cmd.CommandSender;
import net.redstonelamp.network.pc.PCNetworkConst;
import net.redstonelamp.network.pe.PENetworkConst;
import net.redstonelamp.response.ChatResponse;
import net.redstonelamp.utils.TextFormat;
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;

/**
 * /version command implementation. Shows the current version of RedstoneLamp,
 * and the PE, PC versions it supports.
 *
 * @author RedstoneLamp Team
 */
public class VersionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(label.equalsIgnoreCase("version")) {
            String pcVersion = PCNetworkConst.MC_VERSION;
            String peVersion = PENetworkConst.MCPE_VERSION;
            sender.sendMessage(new ChatResponse.ChatTranslation(TextFormat.GOLD + "redstonelamp.translation.command.version.line1", new String[] {
                    RedstoneLamp.SOFTWARE.equals("RedstoneLamp") ? TextFormat.RED + "RedstoneLamp" + TextFormat.RESET : RedstoneLamp.SOFTWARE,
                    RedstoneLamp.SOFTWARE_VERSION,
                    RedstoneLamp.SOFTWARE_STATE,
            }));
            sender.sendMessage(new ChatResponse.ChatTranslation(TextFormat.GOLD + "redstonelamp.translation.command.version.line2", new String[] {
                    peVersion,
                    pcVersion
            }));
            return true;
        }
        return false;
    }
}
