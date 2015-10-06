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
package net.redstonelamp.inventory;

import net.redstonelamp.Player;
import net.redstonelamp.item.Item;
import org.spout.nbt.*;
import org.spout.nbt.stream.NBTInputStream;
import org.spout.nbt.stream.NBTOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A Player Inventory that can be stored and loaded using NBT Compound Tags.
 *
 * @author RedstoneLamp Team
 */
//TODO: Save item in hand's slot/index
//TODO: Migrate Stacks to single Items to save memory (requires modification of entire inventory system classes)
public class NBTPlayerInventory implements PlayerInventory{
    private Map<Integer, Stack<Item>> inventory = new ConcurrentHashMap<>();
    private Item inHand = new Item(0, (short) 0, 0); //TODO: Item.get()
    private int inHandSlot = 0;
    private int inventorySlot = 0;
    private Player player;
    private int maxStackSize = MAX_STACK;

    public static PlayerInventory createFromBytes(byte[] bytes){
        NBTPlayerInventory inv = new NBTPlayerInventory();
        inv.loadFromBytes(bytes);
        return inv;
    }

    @Override
    public Item getItemInHand(){
        return inHand;
    }

    @Override
    public void setItemInHand(Item item){
        inHand = item;
    }

    @Override
    public Player getPlayer(){
        return player;
    }

    @Override
    public int getSelectedSlot() {
        return inventorySlot;
    }

    @Override
    public void setSelectedSlot(int slot) {
        inventorySlot = slot;
    }

    @Override
    public int getItemInHandSlot() {
        return inHandSlot;
    }

    @Override
    public void setItemInHandSlot(int index) {
        inHandSlot = index;
    }

    @Override
    public int getSize(){
        int i = 0;
        for(Stack<Item> stack : inventory.values()){
            i = i + stack.size();
        }
        return i;
    }

    @Override
    public void setMaxStackSize(int size){
        maxStackSize = size;
    }

    @Override
    public int getMaxStackSize(){
        return maxStackSize;
    }

    @Override
    public Stack<Item> getStackAt(int index){
        return inventory.get(index);
    }

    @Override
    public Item popItemFromStack(int index){
        Stack<Item> stack = inventory.get(index);
        if(stack != null){
            return stack.pop();
        }
        return null;
    }

    @Override
    public Item getItemFromStack(int index){
        Stack<Item> stack = inventory.get(index);
        if(stack != null){
            return stack.get(0);
        }
        return null;
    }

    @Override
    public void addItemToStack(Item item, int index){
        Stack<Item> stack = inventory.get(index);
        if(stack != null){
            stack.push(item);
        }else{
            stack = new Stack<>();
            stack.push(item);
            setStackAt(stack, index);
        }
    }

    @Override
    public void setStackAt(Stack<Item> stack, int index){
        inventory.put(index, stack);
    }

    @Override
    public byte[] saveToBytes(){
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try{
            NBTOutputStream out = new NBTOutputStream(bout, true, false);
            List<Tag> rootTags = getTags();
            out.writeTag(new CompoundTag("inventory", rootTags));
            out.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return bout.toByteArray();
    }

    @Override
    public void loadFromBytes(byte[] bytes){
        try{
            NBTInputStream in = new NBTInputStream(new ByteArrayInputStream(bytes), true, false);
            Tag t = in.readTag();
            if(!(t instanceof CompoundTag)){
                throw new IOException("First Tag is not Compound!");
            }
            CompoundTag tag = (CompoundTag) t;
            for(Tag insideTag : tag.getValue()){
                if(!(insideTag instanceof CompoundTag)){
                    throw new IOException("Tag is not Compound!");
                }
                CompoundTag ct = (CompoundTag) insideTag;
                ByteTag index = (ByteTag) ct.getValue().get(0);
                IntTag itemsNum = (IntTag) ct.getValue().get(1);
                IntTag itemid = (IntTag) ct.getValue().get(2);
                ShortTag itemMeta = (ShortTag) ct.getValue().get(3);
                Item[] items = new Item[itemsNum.getValue()];
                Arrays.fill(items, Item.get(itemid.getValue(), itemMeta.getValue(), 1));
                Stack<Item> stack = new Stack<>();
                stack.addAll(Arrays.asList(items));
                setStackAt(stack, index.getValue());
            }
            in.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private List<Tag> getTags(){
        List<Tag> tags = new ArrayList<>();
        for(int i = 0; i < inventory.keySet().size(); i++){ //For Each stack
            Stack<Item> stack = inventory.get(i);
            List<Tag> stackTags = new ArrayList<>();
            stackTags.add(new ByteTag("inventoryIndex", (byte) i));
            stackTags.add(new IntTag("items", stack.size()));
            Item item = stack.get(0); //Assume all items in the stack are the same (they should be)
            stackTags.add(new IntTag("itemId", item.getId()));
            stackTags.add(new ShortTag("itemMeta", item.getMeta()));
            tags.add(new CompoundTag("stack-" + i, stackTags));
        }
        return tags;
    }

    protected void setPlayer(Player player){
        this.player = player;
    }
}