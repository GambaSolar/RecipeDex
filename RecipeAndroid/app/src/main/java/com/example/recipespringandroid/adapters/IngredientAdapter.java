package com.example.recipespringandroid.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipespringandroid.R;
import com.example.recipespringandroid.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredientList;
    private List<Integer> selectedIds = new ArrayList<>();

    public interface OnSelectionChangedListener {
        void onSelectionChanged(List<Integer> selectedIds);
    }

    private OnSelectionChangedListener listener;

    public IngredientAdapter(List<Ingredient> ingredientList,
                             OnSelectionChangedListener listener) {
        this.ingredientList = ingredientList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {

        Ingredient ingredient = ingredientList.get(position);

        holder.tvName.setText(ingredient.getName());

        holder.checkBox.setOnCheckedChangeListener(null);

        holder.checkBox.setChecked(selectedIds.contains(ingredient.getId()));

        holder.itemView.setOnClickListener(v -> {
            toggleSelection(ingredient);
            notifyItemChanged(position);
        });

        holder.checkBox.setOnClickListener(v -> {
            toggleSelection(ingredient);
            notifyItemChanged(position);
        });
    }

    private void toggleSelection(Ingredient ingredient) {

        if (selectedIds.contains(ingredient.getId())) {
            selectedIds.remove(Integer.valueOf(ingredient.getId()));
        } else {
            selectedIds.add(ingredient.getId());
        }

        if (listener != null) {
            listener.onSelectionChanged(selectedIds);
        }
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        CheckBox checkBox;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvIngredientName);
            checkBox = itemView.findViewById(R.id.checkboxIngredient);
        }
    }
}