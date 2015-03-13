package se.mah.ad0206.syssloappen;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SwapUserFragment extends Fragment {


    private Controller controller;
    private ListView lvSwapUser;
    private ArrayAdapter adapter;

    public SwapUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_swap_user, container, false);
        lvSwapUser=(ListView)view.findViewById(R.id.LVSwapUser);
        lvSwapUser.setAdapter(adapter);
        lvSwapUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                controller.swapUserClicked(position);
            }
        });
        return view;
    }

    public void onResume() {
        super.onResume();
        controller.loadUsers();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setAdapter(ArrayAdapter adapter){
        this.adapter=adapter;
    }
}
