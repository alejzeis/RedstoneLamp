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
package net.redstonelamp.plugin.java;

import lombok.Getter;
import lombok.Setter;

public class JavaPluginProperties{
    @Getter
    @Setter
    private String main = null;
    @Getter
    @Setter
    private String version = null;
    @Getter
    @Setter
    private String name = null;
    @Getter
    @Setter
    private String url = null;
    @Getter
    @Setter
    private String[] authors = null;
    @Getter
    @Setter
    private String[] depend = null;
    @Getter
    @Setter
    private String[] softdepend = null;
}
