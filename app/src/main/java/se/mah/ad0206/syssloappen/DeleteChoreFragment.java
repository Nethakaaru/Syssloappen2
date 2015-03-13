package se.mah.ad0206.syssloappen;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


/**
 * A fragment that lets you delete active chores so they are no longer displayed on the main page.
 */
public class DeleteChoreFragment extends Fragment {

    private Controller controller;
    private ChoreListAdapter adapter;

    /**
     * Required empty public constructor
     */
    public DeleteChoreFragment() {}

    /**
     * The onCreateView method.
     * @param inflater
     *                  so we can inflate the layout.
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
        View view = inflater.inflate(R.layout.fragment_delete_chore, container, false);
        //find lind.
        ListView lvDeleteChores = (ListView) view.findViewById(R.id.lvDeleteChores);
        lvDeleteChores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //If the list is clicked on. send to controller where it was clicked and update the list.
                controller.LVDeleteChoresClicked(position);
                adapter.notifyDataSetChanged();
                //notify the user the chore was removed.
                Toast.makeText(getActivity(), "Syssla borttagen", Toast.LENGTH_SHORT).show();
            }
        });

        lvDeleteChores.setAdapter(adapter);

        return view;
    }

    /**
     * onResume method. update the arrays in the program.
     */
    @Override
    public void onResume() {
        super.onResume();
        controller.getChoresAndPoints();
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
     * A method to set the adapter for the list.
     * @param adapter
     *              the lists adapter.
     */
    public void setAdapter(ChoreListAdapter adapter) {
        this.adapter = adapter;
    }

}
