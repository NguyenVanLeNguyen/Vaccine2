package object.ways;

import com.google.gson.annotations.SerializedName;
import object.FieldDetect;

import java.util.ArrayList;
import java.util.HashSet;

public class WaysItem{

	@SerializedName("way_detail_dto")
	private WayDetailDto wayDetailDto;

	@SerializedName("point_counter")
	private int pointCounter;

	@SerializedName("line")
	private String line;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	private FieldDetect mFieldDetect;
	private HashSet<String> listPlaceName = new HashSet<>();
	private HashSet<String> listLatLng = new HashSet<>();
	public void setWayDetailDto(WayDetailDto wayDetailDto){
		this.wayDetailDto = wayDetailDto;
	}

	public WayDetailDto getWayDetailDto(){
		return wayDetailDto;
	}

	public void setPointCounter(int pointCounter){
		this.pointCounter = pointCounter;
	}

	public int getPointCounter(){
		return pointCounter;
	}

	public void setLine(String line){
		this.line = line;
	}

	public String getLine(){
		return line;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public FieldDetect getmFieldDetect() {
		return mFieldDetect;
	}

	public void setmFieldDetect(FieldDetect mFieldDetect) {
		this.mFieldDetect = mFieldDetect;
	}

	public HashSet<String> getListPlaceName() {
		return listPlaceName;
	}

	public HashSet<String> getListLatLng() {
		return listLatLng;
	}
}