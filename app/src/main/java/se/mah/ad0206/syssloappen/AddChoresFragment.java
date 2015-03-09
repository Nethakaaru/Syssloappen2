package se.mah.ad0206.syssloappen;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddChoresFragment extends Fragment {



    private Spinner spinnerPoints;
    private String[] arraySpinner;
    private Button btnAddChore;
    private EditText etChoreName;
    private Controller controller;

    public AddChoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_chores, container, false);

        this.arraySpinner = new String[] {"10", "20", "30", "40", "50", "100"};
        spinnerPoints = (Spinner)view.findViewById(R.id.spinnerPoints);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_spinner_item, arraySpinner);
        spinnerPoints.setAdapter(adapter);

        etChoreName = (EditText)view.findViewById(R.id.etChoreName);

        btnAddChore = (Button)view.findViewById(R.id.btnAddChore);
        btnAddChore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controller == null){
                    Log.d("TEST", "CONTROLLER ÄR NULL!");
                } else if(!etChoreName.getText().toString().equals("")) {
                    controller.btnAddChoreClicked(etChoreName.getText().toString(), spinnerPoints.getSelectedItem().toString());
                    etChoreName.setText("");
                } else {
                    Toast.makeText(getActivity(), "Skriv in ett namn", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

}
