package se.mah.ad0206.syssloappen;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * The main activity.
 * Starts the controller-class and handles the communication with the bluetooth-adapter.
 * @author Sebastian Aspegren, Jonas Dahlström.
 */
public class MainActivity extends ActionBarActivity {

    private Controller controller;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private BluetoothAdapter mBluetoothAdapter;
    private final static int REQUEST_ENABLE_BT = 1234;
    private String mac = "00:06:66:64:43:AB";
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;

    /**
     * The main activity's onCreate method. It starts the controller and the drawer menu.
     * @param savedInstanceState
     *      The bundle used.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new Controller(this);

        //Check if bluetooth is available and enabled
        if(hasBluetoothSupport()) {
            ensureBluetoothEnabled();
        }

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

        //The text in the menu.
        String[] pages = new String[]{getResources().getString(R.string.pageMain), getResources().getString(R.string.pageAddChore), getResources().getString(R.string.pageRemoveChore), getResources().getString(R.string.pageHistory), getResources().getString(R.string.pageAddUser), getResources().getString(R.string.pageRemoveUser), getResources().getString(R.string.pageSwapUser)};
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
     * onDestroy method that stops the bluetooth-communication when the app is closed.
     */
    @Override
    protected void onDestroy() {
        stop();
        super.onDestroy();
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
     * If the user clicks the back button.
     */
    @Override
    public void onBackPressed() {
        //Warn them that they are about to exit the app.
        new AlertDialog.Builder(this)
                .setTitle(R.string.exitApp)
                .setMessage(R.string.exitAppConfirm)
                        //if yes, close the app.
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                        //if no...
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Method that checks if the unit has bluetooth-support.
     * @return
     *      True or false.
     */
    private boolean hasBluetoothSupport()	{
        mBluetoothAdapter =	BluetoothAdapter.getDefaultAdapter();
        return (mBluetoothAdapter != null);
    }

    /**
     * Method that checks if bluetooth is enabled. If it's not, it will ask the user to activate it.
     */
    private void ensureBluetoothEnabled() {
        if (!mBluetoothAdapter.isEnabled())	{
            //Bluetooth is not activated. Ask the user to activate it and start intent.
            Intent enableBtIntent =	new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            //Bluetooth is already activated, start communication
            connectToBluetoothDevice(mac);
        }
    }

    /**
     * If bluetooth wasn't enabled the intent will be sent here with the result of the users choice.
     * If it's activated now it will start the communication.
     * @param requestCode
     *      The requestCode.
     * @param resultCode
     *      The resultCode.
     * @param data
     *      The intent.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent	data) {
        if (requestCode	== REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                connectToBluetoothDevice(mac);
            }
        }
    }

    /**
     * Method that start the thread that connects to the bluetooth-device.
     * @param mac
     *      The MAC-address to the bluetooth-device.
     */
    private void connectToBluetoothDevice(String mac) {
        if (BluetoothAdapter.checkBluetoothAddress(mac)) {
            BluetoothDevice mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(mac);
            connectThread = new ConnectThread(mBluetoothDevice);
            connectThread.start();
        }
    }

    /**
     * Inner class that starts the connection to the bluetooth-device. Extends Thread.
     */
    private class ConnectThread	extends Thread {
        private final BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp	= null;
            try {
                tmp	= device.createRfcommSocketToServiceRecord(SPP_UUID);
            } catch (IOException ignored) {
            }
            mmSocket = tmp;
        }

        public void run() {
            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException ignored) {
                }
                return;
            }

            //Start the ConnectedThread.
            connectedThread = new ConnectedThread(mmSocket);
            connectedThread.start();

        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Inner class that handles the messages sent to the bluetooth-device. Extends Thread.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            OutputStream tmpOut	= null;
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException ignored)	{
            }
            mmOutStream = tmpOut;
        }

        public void write(byte[] bytes)	{
            try {
                mmOutStream.write(bytes);
            } catch (IOException ignored)	{
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException ignored)	{
            }
        }
    }

    /**
     * Method that stops the threads and sets them to null.
     */
    private synchronized void stop() {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread =	null;
        }
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }
    }

    /**
     * Method that sends a message to the bluetooth-device when the user level up.
     * If the communication isn't working, it will save the reward until next time the user login.
     * @param message
     *      The message to send to the bluetooth-device.
     */
    public void sendMessage(byte[] message) {
        if(connectedThread != null) {
            connectedThread.write(message);
        } else {
            Toast.makeText(this, getResources().getString(R.string.toastBTError), Toast.LENGTH_SHORT).show();
            controller.addMissedReward();
        }
    }

}