package se.mah.ad0206.syssloappen;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteChoreFragment extends Fragment {

    private Controller controller;
    private ListView lvDeleteChores;
    private ChoreListAdapter adapter;

    public DeleteChoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delete_chore, container, false);
        lvDeleteChores = (ListView)view.findViewById(R.id.lvDeleteChores);
        lvDeleteChores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                controller.LVDeleteChoresClicked(position);
                adapter.notifyDataSetChanged();
            }
        });

        lvDeleteChores.setAdapter(adapter);

        return view;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setAdapter(ChoreListAdapter adapter) {
        this.adapter = adapter;
    }

}
