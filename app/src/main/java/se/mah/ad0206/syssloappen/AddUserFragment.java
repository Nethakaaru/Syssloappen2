package se.mah.ad0206.syssloappen;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A fragment used to add users.
 * @author Sebastian Aspegren, Jonas Dahlström.
 */
public class AddUserFragment extends Fragment {
    private Button btnAddUser;
    private EditText etAddUser;
    private Controller controller;

    public AddUserFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);
        findComponents(view);
        initListener();

        return view;
    }

    /**
     * Find components.
     * @param view
     *      The view
     */
    private void findComponents(View view) {
        etAddUser = (EditText)view.findViewById(R.id.ETAddUser);
        btnAddUser = (Button)view.findViewById(R.id.btnAddUser);
    }

    /**
     * Set listener on button. Send the username to the controller.
     */
    private void initListener() {
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etAddUser.getText().toString().equals("")) {
                    controller.btnAddUserClicked(etAddUser.getText().toString());
                    etAddUser.setText("");
                    controller.toastShort(getResources().getString(R.string.userAdded));
                }else{
                    controller.toastShort(getResources().getString(R.string.nameRequired));
                }
            }
        });
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
