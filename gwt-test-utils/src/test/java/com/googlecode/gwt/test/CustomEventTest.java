package com.googlecode.gwt.test;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CustomEventTest extends GwtTestTest {

    private int addCount;
    private ListModel listModel;
    private int removeCount;

    @Test
    public void add() {
        // Given
        listModel.addItemAddedHandler(event -> {
            addCount++;
            assertThat(event.getListItem().getText()).isEqualTo("addedItem");

        });

        listModel.addItemRemovedHandler(event -> fail("should not be called"));

        // When
        listModel.addItem(new ListItem("addedItem"));

        // Then
        assertThat(addCount).isEqualTo(1);
        assertThat(removeCount).isEqualTo(0);
    }

    @Before
    public void beforeCustomEventTest() {
        listModel = new ListModel();
        addCount = 0;
        removeCount = 0;
    }

    @Test
    public void remove() {
        // Given
        final ListItem itemToRemove = new ListItem("itemToRemove");
        listModel.addItem(itemToRemove);

        listModel.addItemRemovedHandler(event -> {
            removeCount++;
            assertThat(event.getListItem()).isEqualTo(itemToRemove);
        });

        listModel.addItemAddedHandler(event -> fail("should not be called"));

        // When
        listModel.removeItem(itemToRemove);

        // Then
        assertThat(removeCount).isEqualTo(1);
    }

    private static interface ItemAddedHandler extends EventHandler {

        void onItemAdded(ItemAddedEvent event);
    }

    private static interface ItemRemovedHandler extends EventHandler {

        void onItemRemoved(ItemRemovedEvent event);
    }

    private static class ItemAddedEvent extends GwtEvent<ItemAddedHandler> {
        private static final Type<ItemAddedHandler> TYPE = new Type<ItemAddedHandler>();
        private final ListItem listItem;

        public ItemAddedEvent(ListItem listItem) {
            this.listItem = listItem;
        }

        public static Type<ItemAddedHandler> getType() {
            return TYPE;
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

    private static class ItemRemovedEvent extends GwtEvent<ItemRemovedHandler> {
        private static final Type<ItemRemovedHandler> TYPE = new Type<ItemRemovedHandler>();
        private final ListItem listItem;

        public ItemRemovedEvent(ListItem listItem) {
            this.listItem = listItem;
        }

        public static Type<ItemRemovedHandler> getType() {
            return TYPE;
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
        private final List<ListItem> items = new ArrayList<>();

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

}
