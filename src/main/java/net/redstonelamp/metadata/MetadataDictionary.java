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
package net.redstonelamp.metadata;


import net.redstonelamp.nio.BinaryBuffer;

import java.nio.ByteOrder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Represents a Dictionary of MetadataElements.
 * This code is based off of the code here: https://github.com/NiclasOlofsson/MiNET/tree/master/src/MiNET/MiNET/Utils
 *
 * @author RedstoneLamp team
 */
@SuppressWarnings("SuspiciousMethodCalls")
public class MetadataDictionary extends Dictionary<Byte, MetadataElement>{
    private static Map<Byte, Class<? extends MetadataElement>> elements;
    private ConcurrentMap<Byte, MetadataElement> entries = new ConcurrentHashMap<>();

    public static void init(){
        elements = new HashMap<>();
        elements.put((byte) 0, MetadataByte.class);
        elements.put((byte) 1, MetadataShort.class);
        elements.put((byte) 2, MetadataInt.class);
        elements.put((byte) 4, MetadataString.class);
        elements.put((byte) 8, MetadataLong.class);
    }

    @Override
    public int size(){
        return entries.size();
    }

    @Override
    public boolean isEmpty(){
        return entries.isEmpty();
    }

    @Override
    public Enumeration<Byte> keys(){
        List<Byte> list = new ArrayList<>(entries.size());
        list.addAll(entries.keySet());
        return new MetadataEnumeration<>(list);
    }

    @Override
    public Enumeration<MetadataElement> elements(){
        List<MetadataElement> list = new ArrayList<>(entries.size());
        list.addAll(entries.values());
        return new MetadataEnumeration<>(list);
    }

    @Override
    public MetadataElement get(Object key){
        return entries.get(key);
    }

    @Override
    public MetadataElement put(Byte key, MetadataElement value){
        return entries.put(key, value);
    }

    @Override
    public MetadataElement remove(Object key){
        return entries.remove(key);
    }

    public void fromBytes(byte[] bytes){
        BinaryBuffer bb = BinaryBuffer.wrapBytes(bytes, ByteOrder.LITTLE_ENDIAN);
        while(true){
            byte b = bb.getByte();
            if(b == -127){
                break;
            }

            byte type = (byte) ((b & 0xE0) >> 5);
            byte index = (byte) (b & 0x1F);

            MetadataElement element = index == 17 ? new MetadataLong(0) : getElementByType(type);
            element.fromBytes(bb);
            element.setIndex(index);

            entries.put(index, element);
        }
    }

    private MetadataElement getElementByType(byte type){
        if(elements.containsKey(type)){
            try{
                return elements.get(type).newInstance();
            }catch(InstantiationException | IllegalAccessException e){
                e.printStackTrace();
            }
        }
        throw new IllegalArgumentException();
    }

    public byte[] toBytes(){
        BinaryBuffer bb = BinaryBuffer.newInstance(getLength() + 1, ByteOrder.LITTLE_ENDIAN);
        for(Byte key : entries.keySet()){
            MetadataElement element = entries.get(key);
            element.toBytes(bb, key);
        }
        bb.putByte((byte) 0x7f);
        return bb.toArray();
    }

    public int getLength(){
        final int[] len = {entries.keySet().size()};
        entries.values().forEach(e -> len[0] = len[0] + e.getLength());
        return len[0];
    }

    public static class MetadataEnumeration<T> implements Enumeration<T>{
        private List<T> list;
        private int position = 0;

        public MetadataEnumeration(List<T> elements){
            list = elements;
        }

        public MetadataEnumeration(T[] elements){
            list = new ArrayList<>();
            Collections.addAll(list, elements);
        }

        @Override
        public boolean hasMoreElements(){
            return position < list.size();
        }

        @Override
        public T nextElement(){
            return list.get(position++);
        }
    }
}
