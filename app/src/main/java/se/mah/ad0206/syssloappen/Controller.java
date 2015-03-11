package se.mah.ad0206.syssloappen;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sebastian Aspegren on 2015-03-04.
 *
 */
public class Controller {
    private MainActivity mainActivity;
    private MainFragment mainFragment;
    private AddChoresFragment addChoresFragment;
    private HistoryFragment historyFragment;
    private DeleteChoreFragment deleteChoreFragment;
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
        historyFragment= new HistoryFragment();
        deleteChoreFragment = new DeleteChoreFragment();
        deleteChoreFragment.setController(this);
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
       mainFragment.setAdapter(new ChoreListAdapter(mainActivity, chores, points));
        deleteChoreFragment.setAdapter(new ChoreListAdapter(mainActivity, chores, points));

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
    public String getDate(){
        return new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault()).format(new Date());
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

    public void prepHistoryFragment(){
        dbController.open();
        Cursor c= dbController.getHistory();
        ArrayList<String> history = new ArrayList<>();
        if( c.moveToFirst() ) {
            do {
                history.add(c.getString(0) + "\nPoäng: " + c.getString(1) + "\nDatum: " + c.getString(2));
            }while(c.moveToNext());
        }
        c.close();
        dbController.close();
        historyFragment.setAdapter(new ArrayAdapter<>(mainActivity,android.R.layout.simple_list_item_1, history));
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
            newPoints = newPoints - 500;
            lvl = String.valueOf(Integer.parseInt(lvl) + 1);
        }
        mainFragment.setTVPoints(newPoints +"");
        mainFragment.setTVLevel("Nivå: " + lvl);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("points", String.valueOf(newPoints));
        editor.putString("level", lvl);
        editor.apply();
        dbController.open();
        dbController.saveHistory(this.chores.get(position), this.points.get(position), getDate());
        dbController.close();
    }

    public void setPointsAndLevel() {
        String points = preferences.getString("points", "0");
        String lvl = preferences.getString("level", "1");
        mainFragment.setTVPoints("Poäng: " + points + "/500");
        mainFragment.setTVLevel("Nivå: " + lvl);
    }

    public void LVDeleteChoresClicked(int position) {
        dbController.open();
        dbController.deleteChore(this.chores.get(position), this.points.get(position));
        dbController.close();
        this.chores.remove(position);
        this.points.remove(position);
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
                fragment = deleteChoreFragment;
                break;
            case 3:
                fragment = historyFragment;
                prepHistoryFragment();
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
