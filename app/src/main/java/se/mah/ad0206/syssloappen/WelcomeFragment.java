package se.mah.ad0206.syssloappen;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A fragment with a summary of how the app works that we present the first time its opened.
 * @author Sebastian Aspegren, Jonas Dahlström.
 */
public class WelcomeFragment extends Fragment {

   private Controller controller;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    /**
     * Normal onCreateView method.
     * @param inflater
     *                  inflater for the view.
     * @param container
     *                  container for the inflating process.
     * @param savedInstanceState
     *                  The bundle.
     * @return
     *          the view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        Button btnWelcome = (Button)view.findViewById(R.id.btnWelcome);
        btnWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.btnWelcomeClicked();
            }
        });
        return view;
    }

    /**
     * Sets the controller to help keep the logic in the controller class.
     *
     * @param controller
     *                   A normal controller reference.
     */
    public void setController(Controller controller){
     this.controller=controller;
    }

}
