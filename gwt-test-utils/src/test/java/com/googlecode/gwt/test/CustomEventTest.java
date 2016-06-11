package com.googlecode.gwt.test;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CustomEventTest extends GwtTestTest {

    private static class ItemAddedEvent extends GwtEvent<ItemAddedHandler> {
        private static final Type<ItemAddedHandler> TYPE = new Type<ItemAddedHandler>();

        public static Type<ItemAddedHandler> getType() {
            return TYPE;
        }

        private final ListItem listItem;

        public ItemAddedEvent(ListItem listItem) {
            this.listItem = listItem;
        }

        @Override
        public com.google.gwt.event.shared.GwtEvent.Type<ItemAddedHandler> getAssociatedType() {
            return TYPE;
        }

        /**
         * @returns The item added to the model
         */
        public ListItem getListItem() {
            return listItem;
        }

        @Override
        protected void dispatch(ItemAddedHandler handler) {
            handler.onItemAdded(this);
        }
    }

    private static interface ItemAddedHandler extends EventHandler {

        void onItemAdded(ItemAddedEvent event);
    }

    private static class ItemRemovedEvent extends GwtEvent<ItemRemovedHandler> {
        private static final Type<ItemRemovedHandler> TYPE = new Type<ItemRemovedHandler>();

        public static Type<ItemRemovedHandler> getType() {
            return TYPE;
        }

        private final ListItem listItem;

        public ItemRemovedEvent(ListItem listItem) {
            this.listItem = listItem;
        }

        @Override
        public com.google.gwt.event.shared.GwtEvent.Type<ItemRemovedHandler> getAssociatedType() {
            return TYPE;
        }

        public ListItem getListItem() {
            return listItem;
        }

        @Override
        protected void dispatch(ItemRemovedHandler handler) {
            handler.onItemRemoved(this);
        }
    }

    private static interface ItemRemovedHandler extends EventHandler {

        void onItemRemoved(ItemRemovedEvent event);
    }

    private static class ListItem {

        private final String text;

        public ListItem(String text) {
            this.text = text;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ListItem other = (ListItem) obj;
            if (text == null) {
                if (other.text != null) {
                    return false;
                }
            } else if (!text.equals(other.text)) {
                return false;
            }
            return true;
        }

        public String getText() {
            return text;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((text == null) ? 0 : text.hashCode());
            return result;
        }
    }

    private static class ListModel {
        private final HandlerManager handlerManager = new HandlerManager(this);
        private final List<ListItem> items = new ArrayList<ListItem>();

        public void addItem(ListItem item) {
            items.add(item);
            handlerManager.fireEvent(new ItemAddedEvent(item));
        }

        public void addItemAddedHandler(ItemAddedHandler handler) {
            handlerManager.addHandler(ItemAddedEvent.getType(), handler);
        }

        public void addItemRemovedHandler(ItemRemovedHandler handler) {
            handlerManager.addHandler(ItemRemovedEvent.getType(), handler);
        }

        public void removeItem(ListItem item) {
            items.remove(item);
            handlerManager.fireEvent(new ItemRemovedEvent(item));
        }
    }

    private int addCount;
    private ListModel listModel;
    private int removeCount;

    @Test
    public void add() {
        // Arrange
        listModel.addItemAddedHandler(new ItemAddedHandler() {

            public void onItemAdded(ItemAddedEvent event) {
                addCount++;
                assertEquals("addedItem", event.getListItem().getText());

            }
        });

        listModel.addItemRemovedHandler(new ItemRemovedHandler() {

            public void onItemRemoved(ItemRemovedEvent event) {
                fail();
            }
        });

        // Act
        listModel.addItem(new ListItem("addedItem"));

        // Assert
        assertEquals(1, addCount);
        assertEquals(0, removeCount);
    }

    @Before
    public void beforeCustomEventTest() {
        listModel = new ListModel();
        addCount = 0;
        removeCount = 0;
    }

    @Test
    public void remove() {
        // Arrange
        final ListItem itemToRemove = new ListItem("itemToRemove");
        listModel.addItem(itemToRemove);

        listModel.addItemRemovedHandler(new ItemRemovedHandler() {

            public void onItemRemoved(ItemRemovedEvent event) {
                removeCount++;
                assertEquals(itemToRemove, event.getListItem());
            }

        });

        listModel.addItemAddedHandler(new ItemAddedHandler() {

            public void onItemAdded(ItemAddedEvent event) {
                fail();

            }
        });

        // Act
        listModel.removeItem(itemToRemove);

        // Assert
        assertEquals(1, removeCount);
    }

}
