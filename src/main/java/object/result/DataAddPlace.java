package object.result;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataAddPlace {

	@SerializedName("nodes")
	private List<NodesItem> nodes;

	@SerializedName("way_id")
	private int wayId;

	public List<NodesItem> getNodes(){
		return nodes;
	}

	public int getWayId(){
		return wayId;
	}
}