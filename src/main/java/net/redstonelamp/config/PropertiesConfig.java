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
package net.redstonelamp.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Server configuration class that uses Java Properties files.
 *
 * @author RedstoneLamp Team
 */
public class PropertiesConfig {
    private Properties properties;

    /**
     * Create a new <code>ServerConfig</code> instance and load the config from the specified <code>configLocation</code>
     *
     * @param configLocation
     * @throws IOException If the configuration can not be loaded.
     */
    public PropertiesConfig(File configLocation) throws IOException{
        properties = new Properties();
        properties.load(new FileInputStream(configLocation));
    }

    /**
     * Gets a <code>String</code> from the configuration belonging to the <code>property</code>
     *
     * @param property The Property to get the value from
     * @return The property's value, as a <code>String</code>
     */
    public String getString(String property){
        return properties.getProperty(property);
    }

    /**
     * Gets a <code>Boolean</code> from the configuration belonging to the <code>property</code>
     *
     * @param property The Property to get the value from
     * @return The property's value, as a <code>Boolean</code>
     */
    public boolean getBoolean(String property){
        return Boolean.parseBoolean(properties.getProperty(property));
    }

    /**
     * Gets an <code>Integer</code> from the configuration belonging to the <code>property</code>
     *
     * @param property The Property to get the value from
     * @return The property's value, as an <code>Integer</code>
     */
    public int getInt(String property){
        return Integer.parseInt(properties.getProperty(property));
    }
}
