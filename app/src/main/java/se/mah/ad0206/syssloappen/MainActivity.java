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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

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
     *                          I have no idea what this does.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new Controller(this);

        //Check if bluetooth is available and enabled
        hasBluetoothSupport();
        ensureBluetoothEnabled();

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

    private boolean hasBluetoothSupport()	{
        mBluetoothAdapter =	BluetoothAdapter.getDefaultAdapter();
        return (mBluetoothAdapter != null);
    }

    private void ensureBluetoothEnabled()	{
        if (!mBluetoothAdapter.isEnabled())	{
            Intent enableBtIntent =	new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            //	Bluetooth	är	redan	aktiverat,	genomför	uppkoppling
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent	data) {
        if (requestCode	== REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                connectToBluetoothDevice(mac);
            } else {
                // Bluetooth blev inte aktiverat
            }
        }
    }

    private void connectToBluetoothDevice(String mac) {
        if (BluetoothAdapter.checkBluetoothAddress(mac)) {
            BluetoothDevice mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(mac);
            connectThread = new ConnectThread(mBluetoothDevice);
            connectThread.start();
        }
    }

    private class ConnectThread	extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp	= null;
            mmDevice = device;
            try {
                tmp	= device.createRfcommSocketToServiceRecord(SPP_UUID);
            }	catch (IOException e) {
            }
            mmSocket = tmp;
        }
        public void run()	{
            try {
                mmSocket.connect();
            }	catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                }
                return;
            }

            connectedThread = new ConnectedThread(mmSocket);
            connectedThread.start();

        }
        public void cancel()	{
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn =	null;
            OutputStream tmpOut	= null;
            try {
                tmpIn =	socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e)	{
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer =	new byte[1024];
            int bytes;
            while (true) {
                try {
                    bytes =	mmInStream.read(buffer);
                    if(	bytes >	0)
                        Log.i("DA119A", "Got value " + buffer[0]);
                } catch (IOException e)	{
                    break;
                }
            }
        }

        public void write(byte[] bytes)	{
            try {
                mmOutStream.write(bytes);
            } catch (IOException e)	{
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e)	{
            }
        }
    }

    private synchronized void stop()	{
        if (connectThread !=	null)	{
            connectThread.cancel();
            connectThread =	null;
        }
        if (connectedThread !=	null)	{
            connectedThread.cancel();
            connectedThread =	null;
        }
    }

    public void sendMessage(byte[] message) {
        connectedThread.write(message);
    }

}