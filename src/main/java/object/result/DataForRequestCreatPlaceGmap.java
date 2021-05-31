package object.result;

import com.google.gson.annotations.SerializedName;

public class DataForRequestCreatPlaceGmap {

    @SerializedName("request")
    RequestBodyAddPlace mRequest;
    @SerializedName("way_id")
    int mWayId;

    public DataForRequestCreatPlaceGmap(RequestBodyAddPlace mRequest, int mWayId) {
        this.mRequest = mRequest;
        this.mWayId = mWayId;
    }

    public RequestBodyAddPlace getmRequest() {
        return mRequest;
    }

    public void setmRequest(RequestBodyAddPlace mRequest) {
        this.mRequest = mRequest;
    }

    public int getmWayId() {
        return mWayId;
    }

    public void setmWayId(int mWayId) {
        this.mWayId = mWayId;
    }
}
