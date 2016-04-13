package com.scribblesinc.tams.androidcustom;

/*ArrayView items*/
public class Item {
    private String title;//title of row layout
    private String description;//description
    private int icon;//icon for row layout
    public Item(String title, String description){
        super();
        this.title = title;
        this.description = description;
    }
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

    public int getIcon() {return icon;}
    public void setIcon(int icon) { this.icon = icon;}

    public enum ColorValues{
        ONE,TWO,THREE
    }
}
