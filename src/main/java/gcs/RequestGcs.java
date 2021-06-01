package gcs;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import object.places.Location;
import object.places.ResponsePlaces;
import object.places.ResultsItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RequestGcs {
    String key = "";
    Location location;
    Double distance = 1000.0;
    Double ratio = 2.0;
    int pageNumberRequest =10;
    String requestUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";

    public RequestGcs(Location location, Double distance,Listener mListener) {
        this.distance = distance*ratio;
        this.location = location;
        this.mListener = mListener;
    }


    public static class Builder{
        Location location;
        Double distance = 1000.0;
        Listener mListener;
        public Builder setLocation(Location location) {
            this.location = location;
            return this;
        }

        public Builder setDistance(Double distance) {
            this.distance = distance;
            return this;
        }

        public Builder setmListener(Listener mListener) {
            this.mListener = mListener;
            return this;
        }

        public RequestGcs build(){
            RequestGcs requestGcs = new RequestGcs(location,distance,mListener);
            return requestGcs;
        }
    }

    public void getPlaces(String pageToken,FinishRequestListener finishRequestListener) {
        try {
            String locationStr = location.toStringForGcs();
            String radius = String.valueOf(distance);
            URL url ;
            if(pageToken.isEmpty()){
                url = new URL(requestUrl + String.format("?location=%s&radius=%s&key=%s&language=vi",locationStr,radius,key));
                //System.out.println(requestUrl + String.format("?location=%s&radius=%s&key=%s&language=vi",locationStr,radius,key));
            }else {
                url = new URL(requestUrl + String.format("?location=%s&radius=%s&key=%s&language=vi&pagetoken=%s",locationStr,radius,key,pageToken));
                //System.out.println(requestUrl + String.format("?location=%s&radius=%s&key=%s&language=vi&pagetoken=%s",locationStr,radius,key,pageToken));
            }

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
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
                ResponsePlaces responsePlaces = parsePlace(response.toString());
                if(finishRequestListener != null && responsePlaces != null){
                    finishRequestListener.onFinish(responsePlaces);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    ArrayList<ResultsItem> mListResult = new ArrayList<>();
    public void requestGoogleService(int countPage,String pagetoken){
        if(countPage < pageNumberRequest){
            getPlaces(pagetoken, new FinishRequestListener() {
                @Override
                public void onFinish(ResponsePlaces responsePlaces) {
                    if(responsePlaces.getStatus().equals("OK")){
                        String pagetoken = responsePlaces.getNextPageToken();
                        int countNextPage = countPage+1;
                        mListResult.addAll(responsePlaces.getResults());
                        if(!pagetoken.isEmpty() && countNextPage < pageNumberRequest){
                            requestGoogleService(countNextPage,pagetoken);
                        }else {
                            if(mListener != null && !mListResult.isEmpty()){
                                mListener.onGetPlacesFisnish(mListResult);
                            }
                        }
                    }else {
                        if(mListener != null && !mListResult.isEmpty()){
                            mListener.onGetPlacesFisnish(mListResult);
                        }
                    }
                }
            });
        }else{
            if(mListener != null && !mListResult.isEmpty()){
                mListener.onGetPlacesFisnish(mListResult);
            }
        }
    }

    private ResponsePlaces parsePlace(String json){
        ResponsePlaces responsePlaces;
        try {
            Gson gson = new Gson();
             responsePlaces = gson.fromJson(json,ResponsePlaces.class);
        }catch (JsonSyntaxException e){
            responsePlaces = null;
        }
        return responsePlaces;
    }
    Listener mListener;
    public interface Listener{
        void onGetPlacesFisnish(List<ResultsItem> responsePlaces);
    }

    private interface FinishRequestListener{
        void onFinish(ResponsePlaces responsePlaces);
    }
}
