package se.mah.ad0206.syssloappen;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Sebastian Aspegren on 2015-03-04.
 *
 */
public class Controller {
    private MainActivity mainActivity;
    private MainFragment mainFragment;
    private AddChoresFragment addChoresFragment;
    private DBController dbController;
 //   private String[] chores;
   // private String[] points;
    private ArrayList<String> chores = new ArrayList<>();
    private ArrayList<String> points = new ArrayList<>();
    private View lastView = null;
    private SharedPreferences preferences;

    /**
     * This is the constructor.
     *
     * @param mainActivity
     *                      A normal reference to the mainActivity.
     */
    public Controller(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        preferences = mainActivity.getSharedPreferences("myCache", Context.MODE_PRIVATE);
        mainFragment = new MainFragment();
        addChoresFragment = new AddChoresFragment();
        addChoresFragment.setController(this);
        dbController = new DBController(mainActivity);
        //If it is the users first time we show them the instructions.
       if(isFirstTime()){
           WelcomeFragment welcomeFragment = new WelcomeFragment();
           welcomeFragment.setController(this);
           swapFragment(welcomeFragment, false);
       }else {
           swapFragment(mainFragment, false);
           mainFragment.setController(this);
       }
        getChoresAndPoints();
       // Toast.makeText(mainActivity,points.get(0),Toast.LENGTH_SHORT).show();
       mainFragment.setAdapter(new ChoreListAdapter(mainActivity,chores,points));


    }

    /**
     * A method used to swap between fragments we want to show the user.
     *
     * @param fragment
     *                  This is the fragment we want to view to the user.
     * @param backstack
     *                  Boolean in case we want to be able to go back to the previous fragment.
     */
    public void swapFragment(Fragment fragment, boolean backstack) {
        FragmentManager fragmentManager = mainActivity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer,fragment);
        if(backstack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    /**
     * A method used to check if this is the users first time using the application and if they need to see the instructions.
     * @return
     *          true if it is their first time, else false.
     */
    private boolean isFirstTime(){
        //Note the exclamation mark.
        return !preferences.contains("firstTime");
    }

    /**
     * If the user clicks on this button they'll never have to see the current screen again.
     * We also swap to the main screen.
     */
    public void btnWelcomeClicked() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("firstTime", "firstTime");
        swapFragment(mainFragment, false);
        mainFragment.setController(this);
        editor.apply();
    }

    /**
     *
     */
    public void getChoresAndPoints(){
        chores.clear();
        points.clear();
        dbController.open();
        Cursor c = dbController.getChores();
        if( c.moveToFirst() ){
            do{
                chores.add(c.getString(0));
                points.add(c.getString(1));

            }while(c.moveToNext());
        }
        c.close();
        dbController.close();
    }

    public void getPointsAndLevel() {
        //TODO
    }

    public void insertIntoDB(String chore, String points){
        dbController.open();
        dbController.save(chore, points);
        dbController.close();
    }

    public void btnAddChoreClicked(String chore, String points) {
        insertIntoDB(chore, points);
        Toast.makeText(mainActivity, "Syssla tillagd", Toast.LENGTH_SHORT).show();

    }

    public void LVChoresClicked(int position) {
        String points = preferences.getString("points", "0");
        String lvl = preferences.getString("level", "1");
        String chorePoints = this.points.get(position);
        int newPoints = (Integer.parseInt(points) + Integer.parseInt(chorePoints));
        if(newPoints >= 500) {
            //ToDo
        }
        mainFragment.setTVPoints("Poäng: " + newPoints + " / 500");
        mainFragment.setTVLevel("Nivå: " + lvl);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("points", String.valueOf(newPoints));
        editor.putString("level", lvl);
        editor.apply();
    }

    public void drawerItemClicked(int position, View view) {
        Fragment fragment = null;
        if(lastView != null) {
            lastView.setBackgroundColor(mainActivity.getResources().getColor(R.color.list_background));
        }

        switch (position) {
            case 0:
                fragment = mainFragment;
                break;
            case 1:
                fragment = addChoresFragment;
                break;
            case 2:
                fragment = new DeleteChoreFragment();
                break;
            case 3:
                fragment = new HistoryFragment();
                break;

            default:
                break;
        }

        view.setBackgroundColor(mainActivity.getResources().getColor(R.color.list_background_pressed));
        lastView = view;

        if (fragment != null) {
            swapFragment(fragment,false);
        }
    }

}
