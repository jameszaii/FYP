package com.james.fyp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private ArrayList<CardItem> mCardList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private CardAdapter2 mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public SavedDBHandler savedDBHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // declaration
        final View historyView = inflater.inflate(R.layout.fragment_history, container, false); // declare history view
        Button btnDelete = historyView.findViewById(R.id.btnDelete); //delete button
        final HistoryDBHandler historyDBHandler = new HistoryDBHandler(getActivity(),null,null,1); // db handler
        savedDBHandler = new SavedDBHandler(getActivity(), null, null, 1);

        //loading view
        historyDBHandler.loadHandler(mCardList);

        //dialog pop up
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); //confirm delete dialog
        builder.setTitle(getString(R.string.confirm_history));
        builder.setMessage("This action cannot be undone!");
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { // action yes
                dialog.dismiss();
                historyDBHandler.deleteAllHandler();
                getFragmentManager().beginTransaction()
                        .replace(((ViewGroup) getView().getParent()).getId(), new HistoryFragment())
                        .addToBackStack(null)
                        .commit(); // refresh the fragment
                Toast.makeText(getContext(), getString(R.string.del_history), Toast.LENGTH_LONG).show();

            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { //action no
                dialog.dismiss();
            }
        });

        // delete button action
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        buildRecyclerView(historyView);

        return historyView;


    }

    public void RemoveAllItem() {

        int size = mCardList.size();

        for (int i = 0; i < size; i++) {

            mCardList.remove(0);

        }

    }


    public void buildRecyclerView(View v) {
        mRecyclerView = v.findViewById(R.id.recyclerView3);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new CardAdapter2(mCardList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener2(new CardAdapter2.OnItemClickListener2() {
            @Override
            public void onItemClick(int position) {

                getFragmentManager().beginTransaction()
                        .replace(((ViewGroup) getView().getParent()).getId(), new WordFragment(mCardList.get(position).getText1(), mCardList.get(position).getText2(), ""))
                        .addToBackStack(null)
                        .commit();

                RemoveAllItem(); // to prevent item duplicate once back button pressed

            }

            @Override
            public void onSaveClick(int position) {
                Toast.makeText(getContext(), "Vocabulary saved", Toast.LENGTH_LONG).show();
                savedDBHandler.addHandler(mCardList.get(position).getText1(), mCardList.get(position).getText2(), "");
            }
        });
    }





}
