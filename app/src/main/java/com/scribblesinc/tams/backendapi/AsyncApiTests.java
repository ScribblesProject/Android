package com.scribblesinc.tams.backendapi;

/**
 * Created by danielmj on 11/14/16.
 */


import com.android.volley.Response;

import java.util.ArrayList;

/**
 *
 * Because unit tests don't easily allow for asynchronous tests to be ran, this class is designed to
 * ran from anywhere to ensure that these classes function properly.
 *
 */
public class AsyncApiTests {

    /**
     * Tests to ensure that the category and type lists are returning. Will print results to System.out
     */
    public static void testCategoryAndTypeEndpoints()
    {
        AssetCategory.list(new Response.Listener<ArrayList<AssetCategory>>() {
            @Override
            public void onResponse(ArrayList<AssetCategory> response) {
                System.out.println("CATEGORY LIST RESPONSE: " + response.toString());

                if (response.size() > 0) {
                    AssetCategory category = response.get(0);

                    AssetType.list(category.getId(), new Response.Listener<ArrayList<AssetType>>() {
                        @Override
                        public void onResponse(ArrayList<AssetType> response) {
                            System.out.println("TYPE LIST RESPONSE: " + response.toString());
                        }
                    }, null);
                }
            }
        }, null);
    }

    /**
     * Tests asset endpoints in the following order: create, fetch, update, fetch, delete.
     * */
    public static void testAssetsEndpoints() {
        System.out.println("CREATING....");
        //Create TEST
        ArrayList<AssetLocation> locations = new ArrayList<AssetLocation>();
        locations.add(new AssetLocation(0.1, 0.2));
        locations.add(new AssetLocation(0.3, 0.4));
        Assets.create("TEST NAME", "Some Description", "Pokemon", "Fire, grass, water, etc", "Fire", locations, new Response.Listener<Long>() {
            @Override
            public void onResponse(Long response) {
                System.out.println("THE NEW ID IS: " + response);

                if (response > 0) {

                    //Fetch TEST
                    System.out.println("FETCHING....");
                    Assets.fetch(response, new Response.Listener<Assets>() {
                        @Override
                        public void onResponse(final Assets asset) {

                            //Update TEST
                            System.out.println("UPDATING....");
                            asset.setName("TEST 2");
                            asset.update(new Response.Listener<Boolean>() {
                                @Override
                                public void onResponse(Boolean response) {
                                    if (response) {
                                        System.out.println("UPDATE SUCCESS!!");

                                        //Fetch TEST
                                        System.out.println("FETCHING....");
                                        Assets.fetch(asset.getId(), new Response.Listener<Assets>() {
                                            @Override
                                            public void onResponse(Assets response) {
                                                System.out.println("THE UPDATED ASSET: " + response);

                                                //Delete TEST
                                                System.out.println("DELETING....");
                                                response.delete(new Response.Listener<Boolean>() {
                                                    @Override
                                                    public void onResponse(Boolean response) {
                                                        if (response) {
                                                            System.out.println("DELETION SUCCESS!!");
                                                        }
                                                        else {
                                                            System.out.println("DELETION FAILED!!");
                                                        }
                                                    }
                                                }, null);

                                            }
                                        },null);
                                    }
                                }
                            }, null);

                        }
                    }, null);
                }
            }
        }, null);
    }
}
