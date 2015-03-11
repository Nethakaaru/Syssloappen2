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
 * A fragment used to input chores.
 * @author Sebastian Aspegren, Jonas Dahlström.
 */
public class AddChoresFragment extends Fragment {



    private Spinner spinnerPoints;
    private EditText etChoreName;
    private Controller controller;

    public AddChoresFragment() {
        // Required empty public constructor
    }

    /**
     * OnCreateView method so the fragment actually works.
     * @param inflater
     *                  Inflater so we can inflate the fragment.
     * @param container
     *                  ViewGroup  for the inflation.
     * @param savedInstanceState
     *                              Bundle for the inflation.
     * @return
     *          the inflated view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_chores, container, false);

        //Populate the spinner with values and set the adapter.
        String[] arraySpinner = new String[]{"10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
        spinnerPoints = (Spinner)view.findViewById(R.id.spinnerPoints);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(),
                R.layout.spinner_item, arraySpinner);
        spinnerPoints.setAdapter(adapter);

        //Find some more components.
        etChoreName = (EditText)view.findViewById(R.id.etChoreName);
        Button btnAddChore = (Button) view.findViewById(R.id.btnAddChore);

        //clickListener. If the text isn't empty save it to the database else message the user its empty.
        btnAddChore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controller == null) {
                    Log.d("TEST", "CONTROLLER ÄR NULL!");
                } else if (!etChoreName.getText().toString().equals("")) {
                    controller.btnAddChoreClicked(etChoreName.getText().toString(), spinnerPoints.getSelectedItem().toString());
                    etChoreName.setText("");
                } else {
                    Toast.makeText(getActivity(), R.string.nameInput, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    /**
     * Normal Controller setter
     * @param controller
     *                      reference to the controller.
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

}
