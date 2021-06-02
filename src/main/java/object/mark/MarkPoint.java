package object.mark;

import com.google.gson.annotations.SerializedName;
import object.places.Location;

public class MarkPoint {
    @SerializedName("location")
    private Location Location;

    @SerializedName("wayid")
    private int  mWayId;

    public MarkPoint(Location location, int mWayId) {
        Location = location;
        this.mWayId = mWayId;
    }

    public Location getLocation() {
        return Location;
    }

    public void setLocation(Location location) {
        Location = location;
    }

    public int getmWayId() {
        return mWayId;
    }

    public void setmWayId(int mWayId) {
        this.mWayId = mWayId;
    }
}
