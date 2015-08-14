package redstonelamp.inventory;

import java.util.Stack;

import redstonelamp.item.Item;

public interface Inventory {
	int MAX_STACK = 64;

	int getSize();

	int getMaxStackSize();

	void setMaxStackSize(int size);

	Stack<Item> getStackAt(int index);

	Item popItemFromStack(int index);

	Item getItemFromStack(int index);

	void addItemToStack(Item item, int index);

	void setStackAt(Stack<Item> stack, int index);

	byte[] store();

	void load(byte[] source);
}
