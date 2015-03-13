package se.mah.ad0206.syssloappen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {

    private Controller controller;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    /**
     * The main activity's onCreate method. It starts the controller and the drawer menu.
     * @param savedInstanceState
     *                          I have no idea what this does.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new Controller(this);
        //find components.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ListView mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        //set menu icon.
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_drawer);

        //Construct the action drawer, set it as a listener for the layout and set some stuff.
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawerOpen,R.string.drawerClose);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //The text in the manu.
        String[] pages = new String[]{"Huvudsidan", "Lägg till syssla", "Ta bort syssla", "Historik", "Lägg till användare", "Ta bort användare","Byt användare" };
        DrawerAdapter drawerAdapter = new DrawerAdapter(this, pages);
        mDrawerList.setAdapter(drawerAdapter);
        //If it is clicked...
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Message the controller and close the menu.
                controller.drawerItemClicked(position, view);
                mDrawerLayout.closeDrawers();
            }
        });
    }

    /**
     * A method that is activated when the options item is clicked.
     * @param item
     *              the menu item
     * @return
     *          if it has handled the click or not.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     * If the user clicks the back button...
     */
    @Override
    public void onBackPressed() {
        //Warn them that they are about to exit the app.
        new AlertDialog.Builder(this)
                .setTitle(R.string.exitApp)
                .setMessage(R.string.exitAppConfirm)
                //if yes, close the app.
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                //if no...
                .setNegativeButton("Nej", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
