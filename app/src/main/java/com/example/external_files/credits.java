package com.example.external_files;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class credits extends AppCompatActivity {
    /**
     * @author Roey Schwartz rs7532@bs.amalnet.k12.il
     * @version 1
     * @since 18.11.2023
     * this code will show the credits and can return us to the main screen
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
    }
    /**
     * <p
     *      the function get a variable of Menu type
     * </>
     * @return the function will create a general menu
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
     * @return the function will as the user choice will return to the main screen or close the menu
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        String st = item.getTitle().toString();
        if(st.equals("replace screen")){
            finish();
        }
        else{
            closeOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }
}