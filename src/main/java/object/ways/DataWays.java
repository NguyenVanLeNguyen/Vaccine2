package object.ways;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataWays {

	@SerializedName("ways")
	private List<WaysItem> ways;

	@SerializedName("user_id")
	private int userId;

	public void setWays(List<WaysItem> ways){
		this.ways = ways;
	}

	public List<WaysItem> getWays(){
		return ways;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}
}