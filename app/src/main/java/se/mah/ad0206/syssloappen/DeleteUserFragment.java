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
 * A simple {@link Fragment} subclass.
 */
public class DeleteUserFragment extends Fragment {
    private ArrayAdapter adapter;
    private Controller controller;



    public DeleteUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delete_user, container, false);
        ListView lvDeleteUsers = (ListView) view.findViewById(R.id.LVDeleteUser);
        lvDeleteUsers.setAdapter(adapter);
        lvDeleteUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                controller.deleteUserClicked(position);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        controller.loadUsers();
    }

    public void setAdapter(ArrayAdapter adapter){
        this.adapter=adapter;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

}
