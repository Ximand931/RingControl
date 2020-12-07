package com.happs.ximand.ringcontrol.view.adapter;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private List<T> items;

    protected BaseRecyclerViewAdapter(List<T> items) {
        this.items = items;
    }

    protected List<T> getItems() {
        return items;
    }

    public void notifyListUpdated(List<T> updatedItems) {
        int oldSize = this.items.size();
        int updatedSize = updatedItems.size();
        if (updatedSize > oldSize) {
            notifyItemAdded(updatedItems);
        } else if (updatedSize < oldSize) {
            notifyItemRemoved(updatedItems);
        } else {
            notifyItemUpdated(updatedItems);
        }
    }

    private void notifyItemAdded(List<T> updatedItems) {
        this.items = updatedItems;
        notifyItemInserted(updatedItems.size() - 1);
    }

    private void notifyItemRemoved(List<T> updatedItems) {
        int difference = findDifference(updatedItems);
        this.items = updatedItems;
        notifyItemRemoved(difference);
    }

    private void notifyItemUpdated(List<T> updatedItems) {
        int dif = findDifference(updatedItems);
        this.items = updatedItems;
        notifyItemChanged(dif);
    }

    private int findDifference(List<T> updatedItems) {
        if (updatedItems.isEmpty() || items.isEmpty())
            return 0;
        for (int i = 0; i < updatedItems.size(); i++) {
            T itemFromUpdatedList = updatedItems.get(i);
            T itemFromOldList = items.get(i);
            if (!itemFromUpdatedList.equals(itemFromOldList)) {
                return i;
            }
        }
        return items.size();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
