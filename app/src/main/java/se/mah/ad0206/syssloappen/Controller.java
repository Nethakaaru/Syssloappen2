package se.mah.ad0206.syssloappen;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
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
    private AddUserFragment addUserFragment;
    private DeleteUserFragment deleteUserFragment;
    private SwapUserFragment swapUserFragment;
    private DBController dbController;
    private String user;
    private ArrayList<String> chores = new ArrayList<>();
    private ArrayList<String> points = new ArrayList<>();
    private ArrayList<String> history = new ArrayList<>();
    private ArrayList<String> users = new ArrayList<>();
    private View lastView = null;
    private SharedPreferences preferences;

    /**
     * This is the constructor. It prepares all the fragments and the database for usage.
     * It also checks if we need to present the user with instructions on how the app works.
     *
     * @param mainActivity
     *                      A normal reference to the mainActivity.
     */
    public Controller(MainActivity mainActivity){
        //prepare our classes.
        this.mainActivity = mainActivity;
        preferences = mainActivity.getSharedPreferences("myCache", Context.MODE_PRIVATE);
        mainFragment = new MainFragment();
        mainFragment.setController(this);
        addChoresFragment = new AddChoresFragment();
        addChoresFragment.setController(this);
        historyFragment = new HistoryFragment();
        historyFragment.setController(this);
        deleteChoreFragment = new DeleteChoreFragment();
        deleteChoreFragment.setController(this);
        dbController = new DBController(mainActivity);
        addUserFragment = new AddUserFragment();
        addUserFragment.setController(this);
        deleteUserFragment = new DeleteUserFragment();
        deleteUserFragment.setController(this);
        swapUserFragment = new SwapUserFragment();


        //If it is the users first time we show them the instructions.
        if(isFirstTime()){
            WelcomeFragment welcomeFragment = new WelcomeFragment();
            welcomeFragment.setController(this);
            swapFragment(welcomeFragment, false);
        }else {
            //swap to the main fragment.
            swapFragment(swapUserFragment, false);
            swapUserFragment.setController(this);
        }
        //load data and use it to set adapters for the lists.
        getChoresAndPoints();

        mainFragment.setAdapter(new ChoreListAdapter(mainActivity, chores, points));
        deleteChoreFragment.setAdapter(new ChoreListAdapter(mainActivity, chores, points));
        historyFragment.setAdapter(new ArrayAdapter<>(mainActivity,android.R.layout.simple_list_item_1, history));
        deleteUserFragment.setAdapter(new ArrayAdapter<>(mainActivity,android.R.layout.simple_list_item_1,users));
        swapUserFragment.setAdapter(new ArrayAdapter<>(mainActivity,android.R.layout.simple_list_item_1,users));
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
     * A method to get today's date in a european format.
     * @return
     *          today's date as a string.
     */
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
    public void loadUsers(){
        users.clear();
        dbController.open();
        Cursor c = dbController.getUsers();
        if( c.moveToFirst() ){
            do{
                users.add(c.getString(0));

            }while(c.moveToNext());
        }
        c.close();
        dbController.close();
    }
    /**
     * Method to prepare the users history. Load att the data saved in the history table.
     */
    public void prepHistoryFragment(){
        dbController.open();
        Cursor c = dbController.getHistory();
        history.clear();
        if( c.moveToFirst() ) {
            do {
                //Add user, the chore, points and the date neatly to an arrayList.
                history.add(c.getString(3) + "\n" + c.getString(0) + "\nPoäng: " + c.getString(1) + "\nDatum: " + c.getString(2));
            }while(c.moveToNext());
        }
        c.close();
        dbController.close();
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
     *A method that loads all the chores with their points awarded from the database and puts them in arrayLists.
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

    /**
     * A method to add data to the chore table in the database.
     * @param chore
     *              the chore we are storing to the database.
     * @param points
     *              the points it awards.
     */
    public void insertIntoDB(String chore, String points){
        dbController.open();
        dbController.save(chore, points);
        dbController.close();
    }

    /**
     * If a chore is added we add it to the database and message the user it was added.
     * @param chore
     *              the chore the user added.
     * @param points
     *              the points the chore awards.
     */
    public void btnAddChoreClicked(String chore, String points) {
        insertIntoDB(chore, points);
        Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.choreAdded), Toast.LENGTH_SHORT).show();

    }

    /**
     * If the user clicks on the list...
     *
     * @param position
     *                  where in the list the user clicked.
     */
    public void LVChoresClicked(int position) {
        if(user!=null) {
            //get the users current points and lvl.
            String points = preferences.getString(user + "points", "0");
            String lvl = preferences.getString(user + "level", "1");
            //get points awarded for the chore clicked and add it to the users points.
            String chorePoints = this.points.get(position);
            int newPoints = (Integer.parseInt(points) + Integer.parseInt(chorePoints));
            //if it exceeds 500 the user levels up.
            if (newPoints >= 500) {
                newPoints = newPoints - 500;
                lvl = String.valueOf(Integer.parseInt(lvl) + 1);
                //And Arduino is messaged to reward the user.
                messageArduino();
            }
            //Show the user their points have increased.
            mainFragment.setTVPoints(newPoints + "");
            mainFragment.setTVLevel(lvl);
            //Save the new points and lvl the user has.
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(user + "points", String.valueOf(newPoints));
            editor.putString(user + "level", lvl);
            editor.apply();
            //Save the chore that was done to the users history.
            dbController.open();
            dbController.saveHistory(this.chores.get(position), this.points.get(position), getDate(), user);
            dbController.close();
        }else
            Toast.makeText(mainActivity,"Välj en användare",Toast.LENGTH_SHORT).show();
    }

    /**
     * A method that messages Arduino to move when the user levels up.
     */
    private void messageArduino() {
        //ToDo
    }

    /**
     * Get the stored points and level and present them visually to the user.
     */
    public void setPointsAndLevel() {
        String points = preferences.getString(user+"points", "0");
        String lvl = preferences.getString(user+"level", "1");
        mainFragment.setTVPoints(points);
        mainFragment.setTVLevel(lvl);
    }

    /**
     * A method called when the user wants to delete a chore.
     * It removes the chore then updates the list.
     * @param position
     *                  The position in the list the user clicked.
     */
    public void LVDeleteChoresClicked(int position) {
        dbController.open();
        dbController.deleteChore(this.chores.get(position), this.points.get(position));
        dbController.close();
        this.chores.remove(position);
        this.points.remove(position);
    }

    /**
     * A method used to clear the users completed chores in the history list.
     */
    public void btnClearHistoryClicked() {
        //Is the user sure or did they click by mistake?
        new AlertDialog.Builder(mainActivity)
                .setTitle(mainActivity.getResources().getString(R.string.clearHistory))
                .setMessage(mainActivity.getResources().getString(R.string.deleteHistoryConfirm))
                //If they say yes...
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Delete all the completed chores.
                        dbController.open();
                        dbController.clearHistory();
                        dbController.close();
                        Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.historyCleared), Toast.LENGTH_SHORT).show();
                        //And swap to the mainfragment.
                        swapFragment(mainFragment, false);
                        lastView.setBackgroundColor(mainActivity.getResources().getColor(R.color.list_background));
                    }
                })
                //If they clicked by mistake...
                .setNegativeButton("Nej", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     *  If the user clicked in the drawer...
     * @param position
     *                  where in the list the user clicked.
     * @param view
     *                  which view the user clicked on.
     */
    public void drawerItemClicked(int position, View view) {
        Fragment fragment = null;
        if(lastView != null) {
            //reset the color of the last pressed view.
            lastView.setBackgroundColor(mainActivity.getResources().getColor(R.color.list_background));
        }
        //Switch which fragment is being shown depending on what was clicked.
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
            case 4:
                fragment = addUserFragment;
                loadUsers();
                break;
            case 5:
                fragment = deleteUserFragment;
                break;
            case 6:
                fragment = swapUserFragment;
                break;

            default:
                break;
        }
        //set the color of the clicked one to red.
        view.setBackgroundColor(mainActivity.getResources().getColor(R.color.list_background_pressed));
        lastView = view;

        if (fragment != null) {
            //Swap the fragment.
            swapFragment(fragment,false);
        }
    }

    public void btnAddUserClicked(String user) {
        dbController.open();
        if(!users.contains(user))
        dbController.saveUser(user);
        dbController.close();
    }

    public void deleteUserClicked(int position) {
        users.remove(position);
        dbController.open();
        dbController.deleteUser(users.get(position));
        dbController.close();
    }

    public void swapUserClicked(int position) {
        user = users.get(position);
        swapFragment(mainFragment, false);
        Toast.makeText(mainActivity, user + " är nu aktiv användare", Toast.LENGTH_SHORT).show();
        if(lastView != null)
            lastView.setBackgroundColor(mainActivity.getResources().getColor(R.color.list_background));
    }
}
