package se.mah.ad0206.syssloappen;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddUserFragment extends Fragment {
    private Button btnAddUser;
    private EditText etAddUser;
    private Controller controller;

    public AddUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_user, container, false);
        findComponents(view);
        initListener(view);
        return view;
    }

    private void findComponents(View view) {
        etAddUser=(EditText)view.findViewById(R.id.ETAddUser);
        btnAddUser=(Button)view.findViewById(R.id.btnAddUser);
    }

    private void initListener(View view) {
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etAddUser.getText().toString().equals("")) {
                    controller.btnAddUserClicked(etAddUser.getText().toString());
                    etAddUser.setText("");
                   controller.toastShort("Användare tillagd");
                }else{
                    controller.toastShort("Skriv in ett namn");
                }
            }
        });
    }
    public void setController(Controller controller) {
        this.controller = controller;
    }

}
