package net.redstonelamp.network.packet;

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

import net.redstonelamp.request.Request;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class RLPParser implements Closeable{
    private BufferedReader reader;
    private String sourcePath;
    private String name, description;
    private Packet currentDeclaringPacket = null;
    private int currentLine = 0;
    public RLPParser(File file) throws IOException{
        this(new FileInputStream(file), file.getCanonicalPath());
    }
    public RLPParser(InputStream is, String sourcePath){
        this(new InputStreamReader(is), sourcePath);
    }
    public RLPParser(Reader in, String sourcePath){
        this.sourcePath = sourcePath;
        reader = new BufferedReader(in);
    }
    public void parse() throws IOException, RLPFormatException{
        String line;
        String prefix = "";
        while((line = readLine()) != null){
            line = line.trim();
            if(line.charAt(0) == '#'){
                continue;
            }
            if(line.charAt(line.length() - 1) == '\\'){
                prefix += line + "\n"; // or System.lineSeparator()?
                continue;
            }
            line = prefix + line;
            prefix = "";
            List<String> args = Arrays.asList(line.split(" "));
            handleCommand(args);
        }
    }
    private void handleCommand(List<String> args) throws RLPFormatException{
        String cmd = args.remove(0);
        if(cmd.equalsIgnoreCase("ProtocolName")){
            name = implode(" ", args);
        }else if(cmd.equalsIgnoreCase("ProtocolDescription")){
            description = implode(" ", args);
        }else if(cmd.equalsIgnoreCase("DeclarePacket")){
            currentDeclaringPacket = new Packet();
            currentDeclaringPacket.pid = Byte.decode(args.get(0));
        }else if(cmd.equalsIgnoreCase("PacketName")){
            if(currentDeclaringPacket == null){
                throw error("Attempt to declare packet name while not declaring a packet");
            }
            currentDeclaringPacket.name = args.get(0);
        }
        // TODO not finished
    }
    @Override
    public void close() throws IOException{
        reader.close();
    }
    private String implode(String glue, List<String> list){
        StringBuilder builder = new StringBuilder();
        for(String word : list){
            builder.append(word).append(glue);
        }
        return builder.substring(0, builder.length() - glue.length());
    }
    private String readLine() throws IOException{
        currentLine++;
        return reader.readLine();
    }
    private RLPFormatException error(String msg){
        return new RLPFormatException(msg + " on line " + currentLine + " at " + sourcePath);
    }
    public int getCurrentLine(){
        return currentLine;
    }
    private class Packet{
        public byte pid;
        public String name;
        public Class<? extends Request> requestClass;
        public List<Field> fields;
    }

    private class Field{
        public String name;
        public FieldType type;
    }

    private enum FieldType{
        BYTE,
        SHORT,
        TRIAD,
        L_TRIAD,
        ADDRESS,
        INT,
        LONG,
        STRING,
        METADATA;
        public Object read(){
            // TODO more work
            return null;
        }
    }
}
