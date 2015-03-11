package se.mah.ad0206.syssloappen;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private Controller controller;

    private TextView TVPoints, TVLevel;
    private ListView LVChores;
    private ChoreListAdapter adapter;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        findComponents(view);
        initComponents();
        LVChores.setAdapter(adapter);
        return view;
    }

    private void initComponents() {
        LVChores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                controller.LVChoresClicked(position);
            }
        });
    }

    private void findComponents(View view) {
        TVPoints = (TextView)view.findViewById(R.id.TVPoints);
        TVLevel = (TextView)view.findViewById(R.id.TVLevel);
        LVChores = (ListView)view.findViewById(R.id.LVChores);
    }

    @Override
    public void onResume() {
        super.onResume();
        controller.getChoresAndPoints();
        controller.setPointsAndLevel();
    }

    /**
     * Sets the controller to help keep the logic in the controller class.
     *
     * @param controller
     *                   A normal controller reference.
     */
    public void setController(Controller controller){
        this.controller = controller;
    }

    public void setAdapter(ChoreListAdapter adapter) {
        this.adapter = adapter;
    }

    public void setTVPoints(String text) {
       TVPoints.setText(text);
    }

    public void setTVLevel(String text) {
        TVLevel.setText(text);
    }
}
