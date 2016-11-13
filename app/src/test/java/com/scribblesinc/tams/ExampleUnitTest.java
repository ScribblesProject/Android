package com.scribblesinc.tams;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.google.gson.JsonObject;
import com.scribblesinc.tams.backendapi.AssetLocation;
import com.scribblesinc.tams.backendapi.Assets;
import com.scribblesinc.tams.network.HttpJson;
import com.scribblesinc.tams.network.HttpResponse;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    private static final String TAG = AssetList.class.getSimpleName();

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_asset_json_compilation() throws Exception {

        AssetLocation loc1 = new AssetLocation(0.1,0.2);
        AssetLocation loc2 = new AssetLocation(0.3,0.4);

        ArrayList<AssetLocation> locations = new ArrayList<AssetLocation>();
        locations.add(loc1);
        locations.add(loc2);
        JsonObject json = Assets.createJSON("Charizard", "description1","categ1", "categDesc1", "typeName1", locations);

        System.out.println("\nTESTING JSON:");
        System.out.println(json.toString());
    }

    @Test
    public void test_asset_list() throws Exception
    {
        String url = "https://tams-142602.appspot.com/api/asset/list/";





        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void test_asset_create_connection() throws Exception {
        String url = "https://tams-142602.appspot.com/api/asset/create/";

        ArrayList<AssetLocation> locations = new ArrayList<AssetLocation>();
        locations.add(new AssetLocation(0.1, 0.2));
        locations.add(new AssetLocation(0.3, 0.4));
        JsonObject json = Assets.createJSON("Charizard", "description1", "categ1", "categDesc1", "typeName1", locations);

        TimeUnit.SECONDS.sleep(5);
    }

    public void test_asset_attach_image() throws Exception
    {

    }
}