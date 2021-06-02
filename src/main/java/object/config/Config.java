package object.config;

import com.google.gson.annotations.SerializedName;

public class Config{

	@SerializedName("action")
	private String action;

	@SerializedName("key")
	private String key;

	@SerializedName("file_data")
	private String fileData;

	@SerializedName("token")
	private String token;

	@SerializedName("is_testing")
	private String isTesting;

	@SerializedName("is_test_request")
	private String isTestRequest;

	public String getAction(){
		return action == null ? "":action;
	}

	public String getKey(){
		return key == null ? "":key;
	}

	public String getFileData(){
		return fileData == null ? "":fileData;
	}

	public String getToken(){
		return token == null ? "":token;
	}

	public String getIsTesting() {
		return isTesting;
	}

	public String getIsTestRequest() {
		return isTestRequest;
	}
}