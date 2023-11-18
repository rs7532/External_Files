package com.example.external_files;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    /**
     * @author Roey Schwartz rs7532@bs.amalnet.k12.il
     * @version 1
     * @since 18.11.2023
     * this code will show an EditText and you can save to a file what you wrote, reset the content of the file and
    close the application with that saves the current content of the file
     */

    private static final int REQUEST_CODE_PERMISSION = 1;

    private final String FILENAME = "ExtText.txt";
    private boolean extExist, permExist;
    EditText et;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);
        et = findViewById(R.id.et);

        onResume();
        tv.setText(Read_text());
    }

    /**
     * <p>
     *     The function doesn't get any variable,
     *     The function will check that there is an SD card
     * </>
     */
    public boolean isExternalStorageAvailable(){
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * <p>
     *     The function doesn't get any variable,
     *     The function will check that there is an permission to get the SD card
     * </>
     */
    public boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * <p>
     *     The function doesn't get any variable,
     *     The function will request a permission to get to the SD card
     * </>
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
    }

    /**
     * <p>
     *     The function get a number that represents our request permission code, a list type of String
           of the permissions that the system have, and a list type of int with number that represents the grant permissions by the user and system
     *     The function will check that our permission granted
     * </>
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            Toast Toast = null;
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Permission to access external storage NOT granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * <p>
     *     the function doesn't get any variable,
     *     the function will check that we have all the access the SD card and prepare one if not
     * </>
     */
    @Override
    protected void onResume(){
        super.onResume();

        extExist = isExternalStorageAvailable();
        permExist = checkPermission();

        if (!extExist) {
            Toast.makeText(this, "External memory not installed", Toast.LENGTH_SHORT).show();
        }

        if (!permExist) {
            requestPermission();
        }
    }

    public boolean isFilePresent(Context context, String fileName) {
        File externalDir = Environment.getExternalStorageDirectory();
        File[] filesArray = externalDir.listFiles();
        for (File file : filesArray) {
            if (file.getName().equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     *     the function get a text that the user wants to save in the file,
     *     the function will add that text to the current content in the file
     * </>
     */
    public void Write_text(String text){
        if (extExist && permExist) {
            try{
                File externalDir = Environment.getExternalStorageDirectory();
                File file = new File(externalDir, FILENAME);
                file.getParentFile().mkdirs();
                FileWriter writer = new FileWriter(file);
                writer.write(text);
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * <p>
     *     the function doesn't get an variable,
     *     the function will return a text type of String that including the current content in the file
     * </>
     */
    public String Read_text(){
        StringBuilder sB = null;
        if (isFilePresent(this, FILENAME)) {
            if(permExist && extExist) {
                try {
                    File externalDir = Environment.getExternalStorageDirectory();
                    File file = new File(externalDir, FILENAME);
                    file.getParentFile().mkdirs();
                    FileReader reader = new FileReader(file);
                    BufferedReader bR = new BufferedReader(reader);
                    sB = new StringBuilder();
                    String line = bR.readLine();
                    while (line != null) {
                        sB.append(line + '\n');
                        line = bR.readLine();
                    }
                    sB.append(et.getText());
                    bR.close();
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            Write_text("");
            return "";
        }
        return String.valueOf(sB);
    }

    /**
     * <p>
     *     the function get a variable of View type,
     *     the function will save the text that the user wants to save and show it.
     * </>
     */
    public void savePressed(View view) {
        // READ:
        String fileString = Read_text();
        // WRITE:
        Write_text(String.valueOf(fileString));
        tv.setText(fileString);
    }

    /**
     * <p>
     *     the function get a variable of View type,
     *     the function will delete the content of the file
     * </>
     */
    public void resetPressed(View view) {
        Write_text("");
        tv.setText("");
    }

    /**
     * <p>
     *     the function get a variable of View type,
     *     the function will save the content in the EditText to the file and the current content in the file and close the application
     * </>
     */
    public void exitPressed(View view) {
        // READ:
        String sB = Read_text();
        // WRITE:
        Write_text(String.valueOf(sB));
        finish();
    }


    /**
     * <p>
     *     the function get a variable of View type,
     *     the function will transfer the user to the credits screen
     * </>
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * <p
     *      the function get a variable of MenuItem type
     * </>
     * @return the function will as the user choice will move to the credits screen or close the menu
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        Intent si = new Intent(this, credits.class);
        String st = item.getTitle().toString();
        if(st.equals("replace screen")){
            startActivity(si);
        }
        else{
            closeOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }
}