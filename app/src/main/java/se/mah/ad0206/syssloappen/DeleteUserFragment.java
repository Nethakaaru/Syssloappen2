package se.mah.ad0206.syssloappen;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A fragment used to delete users.
 * @author Sebastian Aspegren, Jonas Dahlström.
 */
public class DeleteUserFragment extends Fragment {

    private ArrayAdapter adapter;
    private Controller controller;

    public DeleteUserFragment() {
        // Required empty public constructor
    }

    /**
     * OnCreateView method so the fragment actually works.
     * @param inflater
     *      Inflater so we can inflate the fragment.
     * @param container
     *      ViewGroup for the inflation.
     * @param savedInstanceState
     *      Bundle for the inflation.
     * @return
     *      the inflated view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delete_user, container, false);
        ListView lvDeleteUsers = (ListView) view.findViewById(R.id.LVDeleteUser);

        lvDeleteUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                controller.deleteUserClicked(position);
                adapter.notifyDataSetChanged();
            }
        });

        lvDeleteUsers.setAdapter(adapter);

        return view;
    }

    /**
     * onResume method that load all users.
     */
    @Override
    public void onResume() {
        super.onResume();
        controller.loadUsers();
    }

    /**
     * A method to set the adapter for the list.
     * @param adapter
     *              the lists adapter.
     */
    public void setAdapter(ArrayAdapter adapter){
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

}
