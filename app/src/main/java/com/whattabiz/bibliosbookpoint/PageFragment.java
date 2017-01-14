package com.whattabiz.bibliosbookpoint;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    // --Commented out by Inspection (08-10-2016 17:27):private static String CATEGORIES_ITEMS_URL = "http://whattabiz.com/Biblios/androidcategory.php";
    private int mPage;
    private String ID;
    private String KEY;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        // page will give the n'th position of the Categories List
        // refer to the Category of nth position's id


        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the id of the categories page
        mPage = getArguments().getInt(ARG_PAGE);

        // get the key from this index
        // hope no IndexOutOfBoundsExceptiom
        KEY = NavigationHomeActivity.BookCategories.get(mPage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_fragment, container, false);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_circle);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.category_list_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new CategoriesListItemAdapter(getContext(), KEY));

        if (!Store.bookCategories.isEmpty()) {
            progressBar.setVisibility(View.GONE);
        }

        return view;
    }


}
