package se.mah.ad0206.syssloappen;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A fragment used to show the chore history.
 * @author Sebastian Aspegren, Jonas Dahlström.
 */
public class HistoryFragment extends Fragment {

    private ListView lvHistory;
    private TextView tvHistory;
    private ArrayAdapter<String> adapter;
    private Controller controller;

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
        Button btnClearHistory = (Button) view.findViewById(R.id.btnClearHistory);
        btnClearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.btnClearHistoryClicked();
                adapter.notifyDataSetChanged();
            }
        });
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
     * A normal set controller method.
     * @param controller
     *                  a reference to the controller.
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Sets the number of chore done by the user in a TextView.
     */
    public void setTvHistory(){
        tvHistory.setText(getResources().getString(R.string.tvHistory) + " " + lvHistory.getCount());
    }

}
