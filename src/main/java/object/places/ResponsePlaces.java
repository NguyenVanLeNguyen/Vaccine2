package object.places;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponsePlaces{

	@SerializedName("next_page_token")
	private String nextPageToken;

	@SerializedName("html_attributions")
	private List<Object> htmlAttributions;

	@SerializedName("results")
	private List<ResultsItem> results;

	@SerializedName("status")
	private String status = "OK ";

	public String getNextPageToken(){
		return nextPageToken == null? "" : nextPageToken;
	}

	public List<Object> getHtmlAttributions(){
		return htmlAttributions;
	}

	public List<ResultsItem> getResults(){
		return results;
	}

	public String getStatus(){
		return status;
	}
}