package se.mah.ad0206.syssloappen;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new Controller(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuAddChore) {
            AddChoresFragment addChoresFragment = new AddChoresFragment();
            addChoresFragment.setController(controller);
            controller.swapFragment(addChoresFragment, true);
            return true;
        } else if(id == R.id.menuDeleteChore) {
            DeleteChoreFragment deleteChoreFragment = new DeleteChoreFragment();
            deleteChoreFragment.setController(controller);
            controller.swapFragment(deleteChoreFragment, true);
            return true;
        } else if(id == R.id.menuSeeHistory) {
            HistoryFragment historyFragment = new HistoryFragment();
            historyFragment.setController(controller);
            controller.swapFragment(historyFragment, true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}
