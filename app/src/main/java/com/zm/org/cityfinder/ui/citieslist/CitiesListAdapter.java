package com.zm.org.cityfinder.ui.citieslist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.zm.org.cityfinder.R;
import com.zm.org.cityfinder.databinding.RowCityViewBinding;
import com.zm.org.cityfinder.model.dto.CityData;

public class CitiesListAdapter extends ListAdapter<CityData, CitiesListAdapter.CityViewHolder> {

    private OnItemClickListener listener;

    protected CitiesListAdapter(OnItemClickListener listener) {
        super( DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        RowCityViewBinding binding= DataBindingUtil.inflate(inflater, R.layout.row_city_view,parent,false);
        return new CityViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {

        holder.bind(getItem(position),listener);
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {

        private RowCityViewBinding rowCityViewBinding;

        public CityViewHolder(@NonNull RowCityViewBinding rowCityViewBinding) {
            super(rowCityViewBinding.getRoot());
            this.rowCityViewBinding = rowCityViewBinding;
        }

        public void bind(CityData cityData, OnItemClickListener listener) {
            rowCityViewBinding.setCityData(cityData);
            rowCityViewBinding.setOnItemClickListener(listener);
        }
    }
    public static final DiffUtil.ItemCallback<CityData> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<CityData>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull CityData oldCity, @NonNull CityData newCity) {
                    // City properties may have changed if reloaded from the DB, but ID is fixed
                    return oldCity.id == newCity.id;
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull CityData oldCity, @NonNull CityData newCity) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldCity.id == newCity.id;
                }
            };

    public interface OnItemClickListener {
        void onItemClick(CityData item);
    }
}


