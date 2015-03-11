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
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private ListView lvHistory;
    private TextView tvHistory;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
         lvHistory = (ListView)view.findViewById(R.id.lvHistory);
        tvHistory = (TextView)view.findViewById(R.id.tvHistory);
        return view;
    }

    public void setAdapter(ArrayAdapter adapter){
        lvHistory.setAdapter(adapter);
    }
    public void settvHistory(){
        tvHistory.setText(getResources().getString(R.string.tvHistory) + " " + lvHistory.getCount());
    }
}
