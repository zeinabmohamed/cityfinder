package com.zm.org.cityfinder.util;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.BindingAdapter;

public class ViewBindingAdapterUtil {
    @BindingAdapter("queryTextListener")
    public static void setOnQueryTextListener(SearchView searchView, SearchView.OnQueryTextListener listener) {
        searchView.setOnQueryTextListener(listener);
    }
}
