/**
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
package net.redstonelamp;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

/**
 * Main Startup file for RedstoneLamp.
 *
 * @author RedstoneLamp Team
 */
public class RedstoneLamp {
    public static final String SOFTWARE = "RedstoneLamp";
    public static final String SOFTWARE_VERSION = "1.2.0";
    public static final String SOFTWARE_STATE = "DEV";
    public static final double API_VERSION = 1.5;

    public static void main(String[] args){
        RedstoneLamp main = new RedstoneLamp();
        main.getDefaultResources();
        //TODO
    }
    
    private void getDefaultResources() {
        if(!new File("server.properties").isFile()) {
            URL url = this.getClass().getResource("/conf/server.properties");
            File dest = new File("./server.properties");
            try {
                FileUtils.copyURLToFile(url, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!new File("redstonelamp.yml").isFile()) {
            URL url = this.getClass().getResource("/conf/redstonelamp.yml");
            File dest = new File("./redstonelamp.yml");
            try {
                FileUtils.copyURLToFile(url, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get the software's version string.
     * @return The version string (example: RedstoneLamp v1.0.0-development)
     */
    public static String getSoftwareVersionString(){
        return SOFTWARE + " " + SOFTWARE_VERSION + "-" + SOFTWARE_STATE;
    }

}
