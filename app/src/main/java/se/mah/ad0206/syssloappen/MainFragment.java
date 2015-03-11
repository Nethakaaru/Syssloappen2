package se.mah.ad0206.syssloappen;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


/**
 * The main fragment with the current chores, points and level being displayed.
 * @author Sebastian Aspegren, Jonas Dahlström.
 */
public class MainFragment extends Fragment {

    private Controller controller;

    private TextView TVPoints, TVLevel;
    private ListView LVChores;
    private ChoreListAdapter adapter;

    /**
     * Required empty public constructor
     */
    public MainFragment() {}

    /**
     * Normal onCreateView.
     * @param inflater
     *                  the inflater.
     * @param container
     *                  the ViewGroup.
     * @param savedInstanceState
     *                  the bundle.
     * @return
     *                  the view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        //Link the code components to the xml code.
        findComponents(view);
        //init listeners and such.
        initComponents();
        LVChores.setAdapter(adapter);
        return view;
    }

    /**
     * Method used to activate listeners. When a chore is clicked send the position of it to the controller.
     */
    private void initComponents() {
        LVChores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                controller.LVChoresClicked(position);
            }
        });
    }

    /**
     * Find all the components using their xml id
     * @param view
     *              the view.
     */
    private void findComponents(View view) {
        TVPoints = (TextView)view.findViewById(R.id.TVPoints);
        TVLevel = (TextView)view.findViewById(R.id.TVLevel);
        LVChores = (ListView)view.findViewById(R.id.LVChores);
    }

    /**
     * onResume update the points, chores and level.
     */
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

    /**
     * Sets the adapter for the list.
     * @param adapter
     *                  A custom list adapter containing two text views.
     */
    public void setAdapter(ChoreListAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * Set the current points the user has.
     * @param text
     *              users current points.
     */
    public void setTVPoints(String text) {
       TVPoints.setText(text);
    }

    /**
     * Set the current level the user has achieved.
     * @param text
     *              the users level.
     */
    public void setTVLevel(String text) {
        TVLevel.setText(getResources().getString(R.string.pointsNoColon) +" "+ text + "/500");
    }
}
