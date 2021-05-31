package gmap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import object.FieldDetect;
import object.nodes.NodesItem;
import object.nodes.ResponseNodes;
import object.places.Location;
import object.result.RequestBodyAddPlace;
import object.result.ResponseAddPlace;
import object.ways.ResponseWays;
import object.ways.WaysItem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class RequestApis {
    String domain = "https://gmap-marker-tool-be.ghtk.vn";
    String requestGetWays = "/api/v1/ways";
    String getRequestNodesInWay = "/api/v1/$s/nodes";

    public void getWays() {
        try {
            URL url = new URL(domain+requestGetWays);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "Bearer c_ij3813jdckzl5il4qph2rdqsdnj1zeeuzwhrl2kanfdbvkz2dymgexbbzksxrrgl");
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                ResponseWays dataWay = parseDataWays(response.toString());
                if(mListener != null && dataWay != null){
                    mListener.onFinishGetDataWays(dataWay);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getNodesInWay(WaysItem waysItem){
        URL url = null;
        try {
            url = new URL(domain+String.format("/api/v1/%s/nodes",String.valueOf(waysItem.getId())));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "Bearer c_ij3813jdckzl5il4qph2rdqsdnj1zeeuzwhrl2kanfdbvkz2dymgexbbzksxrrgl");
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                ResponseNodes responseNodes = parseDataNode(response.toString());
                if(responseNodes != null && waysItem.getPointCounter() > 0){
                    if(responseNodes.getData().getNodes().size() > 0 ){
                         for(NodesItem node : responseNodes.getData().getNodes()){
                             waysItem.getListPlaceName().add(node.getName());
                             waysItem.getListLatLng().add(String.format("%.7f,%.7f",node.getLatitude(),node.getLongitude()));
                         }
                    }
                }
                if(mListener != null && responseNodes != null){
                    mListener.onFinshGetListNodes(responseNodes,waysItem);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createNewPlace(RequestBodyAddPlace requestBody,int wayId) {
        try {
            URL url = new URL(domain + String.format("/api/v1/%s/nodes", String.valueOf(wayId)));
            HttpURLConnection con = null;
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "Bearer c_ij3813jdckzl5il4qph2rdqsdnj1zeeuzwhrl2kanfdbvkz2dymgexbbzksxrrgl");
            con.setDoOutput(true);
            Gson gson = new Gson();
            String jsonRequestBody = gson.toJson(requestBody);
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonRequestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                try {
                    Gson gsonResponse = new Gson();
                    ResponseAddPlace responseAddPlace = gsonResponse.fromJson(response.toString(),ResponseAddPlace.class);
                    if(responseAddPlace.getStatus().equals("OK")){
                        System.out.println(jsonRequestBody);
                    }
                }catch (JsonSyntaxException e){
                    e.printStackTrace();
                }
            }
        } catch (IOException protocolException) {
            protocolException.printStackTrace();
        }
    }


    public ResponseWays parseDataWays(String json){
        ResponseWays dataWay;
        try {
            Gson gson = new Gson();
            dataWay  = gson.fromJson(json,ResponseWays.class);
            dataWay.getData().getWays().sort(new Sortbyroll());
            for(WaysItem waysItem : dataWay.getData().getWays()){
                try{
                    ArrayList<Location> listLocation = new ArrayList<>();
                    JsonParser jsonParser = new JsonParser();
                    JsonArray jsonFile = (JsonArray) jsonParser.parse(waysItem.getLine());
                    if(jsonFile != null && jsonFile.size() > 0){
                        for(int i = 0; i < jsonFile.size() ; i++){
                            JsonArray locationFiles = (JsonArray) jsonParser.parse(jsonFile.get(i).toString());
                            if(locationFiles.size() == 2){
                                double lat;
                                double lng;
                                try {
                                    lat  = Double.parseDouble(locationFiles.get(0).toString());
                                    lng  = Double.parseDouble(locationFiles.get(1).toString());
                                }catch (NumberFormatException e){
                                    lat =  0.0;
                                    lng = 0.0;
                                }
                                Location location = new Location(lat,lng);
                                listLocation.add(location);
                            }
                        }
                        FieldDetect fieldDetect = new FieldDetect(listLocation);
                        waysItem.setmFieldDetect(fieldDetect);
                    }
                }catch (JsonSyntaxException e){
                    e.printStackTrace();
                }

            }
        }catch (JsonSyntaxException e){
            dataWay= null;
        }
        return dataWay;
    }

    public ResponseNodes parseDataNode(String json){
        ResponseNodes responseNodes;
        try {
            Gson gson = new Gson();
            responseNodes  = gson.fromJson(json,ResponseNodes.class);
        }catch (JsonSyntaxException e){
            responseNodes = null;
        }
        return responseNodes;
    }
    Listener mListener;

    public void setmListener(Listener mListener) {
        this.mListener = mListener;
    }
    static class Sortbyroll implements Comparator<WaysItem>
    {
        @Override
        public int compare(WaysItem a, WaysItem b)
        {
            return a.getPointCounter() - b.getPointCounter();
        }
    }
    public interface Listener{
        void onFinishGetDataWays(ResponseWays dataWay);
        void onFinshGetListNodes(ResponseNodes responseNodes,WaysItem waysItem);
    }
}
