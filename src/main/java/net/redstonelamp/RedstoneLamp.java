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
        //TODO
    }

    /**
     * Get the software's version string.
     * @return The version string (example: RedstoneLamp v1.0.0-development)
     */
    public static String getSoftwareVersionString(){
        return SOFTWARE + " " + SOFTWARE_VERSION + "-" + SOFTWARE_STATE;
    }

}
