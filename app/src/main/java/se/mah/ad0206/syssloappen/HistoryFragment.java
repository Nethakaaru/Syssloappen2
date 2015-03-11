package se.mah.ad0206.syssloappen;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A fragment to view what chores the user has previously completed.
 */
public class HistoryFragment extends Fragment {

    private ListView lvHistory;
    private TextView tvHistory;
    private ArrayAdapter<String> adapter;

    /**
     * Required empty public constructor
     */
    public HistoryFragment() {}

    /**
     * Normal onCreateView
     * @param inflater
     *              inflater for the view.
     * @param container
     *              the ViewGroup.
     * @param savedInstanceState
     *              the Bundle.
     * @return
     *              the view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        //find components.
         lvHistory = (ListView)view.findViewById(R.id.lvHistory);
        tvHistory = (TextView)view.findViewById(R.id.tvHistory);
        //set adapter
        lvHistory.setAdapter(adapter);
        //set the text
        setTvHistory();
        return view;
    }

    /**
     * A method to set te adapter for the list.
     * @param adapter
     *              the adapter. a simple list containing the chore, points and date the chore was completed.
     */
    public void setAdapter(ArrayAdapter<String> adapter){
        this.adapter = adapter;
    }

    /**
     * Sets the number of chore done by the user in a TextView.
     */
    public void setTvHistory(){
        tvHistory.setText(getResources().getString(R.string.tvHistory) + " " + lvHistory.getCount());
    }
}
