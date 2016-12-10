package com.scribblesinc.tams.util;
/*
*Item.java
* Item class has its use in AssetAdd class, where it is use for creating default ListView.
*/
public class Item {
    private String title;//title of row layout
    private String description;//description
    private int icon;//icon for row layout
    private int viewType;

    //Constructor
    public Item(String title, String description){
        super();
        this.title = title;
        this.description = description;
    }
    //constructor
    public Item(String title, String description, int icon){
        super();
        this.title = title;
        this.description = description;
        this.icon = icon;
    }
    //getters and setters
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getIcon() {
        return icon;
    }
    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Item{" +
                "title=" + title+
                ", description='" + description+
                '}';
    }
}
