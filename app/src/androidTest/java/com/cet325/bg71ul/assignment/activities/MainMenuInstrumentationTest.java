package com.cet325.bg71ul.assignment.activities;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;

import com.cet325.bg71ul.assignment.R;

import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by Anthony Vest on 18/12/2017.
 */
public class MainMenuInstrumentationTest extends ActivityInstrumentationTestCase2<MainMenu>{


    private Activity mainMenuActivity = null;
    private Button testButton;


    public MainMenuInstrumentationTest() {
        super(MainMenu.class);
    }


    @Before
    public void beforeEach(){
        testButton = null;
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        mainMenuActivity = getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGalleryButton() throws Exception {
        // Make sure the activity has returned
        assertNotNull(mainMenuActivity);

        testButton = (Button) mainMenuActivity.findViewById(R.id.btn_gallery);
        assertNotNull(testButton);

    }

    public void testGoogleMapsButton() throws Exception {
        // Make sure the activity has returned
        assertNotNull(mainMenuActivity);

        testButton = (Button) getActivity().findViewById(R.id.btn_google_maps);
        assertNotNull(testButton);
    }

}