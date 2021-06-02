import com.google.gson.Gson;
import gcs.RequestGcs;
import gmap.RequestApis;
import object.config.Config;
import object.mark.MarkPoint;
import object.nodes.ResponseNodes;
import object.places.ResponsePlaces;
import object.places.ResultsItem;
import object.result.DataForRequestCreatPlaceGmap;
import object.result.RequestBodyAddPlace;
import object.ways.ResponseWays;
import object.ways.WaysItem;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Run {
    final static String ACTION_GETPLACE = "get";
    final static String CREATE_PLACE = "request";
    static Config configData;
    static boolean isTestSmallScale = false;
    public static void main(String[] args) {
        if (args.length == 1) {
            String fileName = args[0];
            try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    try {
                        configData = new Gson().fromJson(line, Config.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("Syntax error!");
            return;
        }
        if(configData == null){
            System.out.println("Format file config error!");
            return ;
        }else {
            if(configData.getAction().isEmpty()){
                System.out.println("missing action!");
                return;
            }else {
                if(configData.getAction().equals("1")){ // step one
                    if(configData.getKey().isEmpty()){
                        System.out.println("missing google clound service key!");
                        return;
                    }else {
                        RequestGcs.key = configData.getKey();
                    }

                    if(configData.getToken().isEmpty()){
                        System.out.println("missing token!");
                        return;
                    }else {
                        RequestApis.token = configData.getToken();
                    }
                    if(configData.getIsTesting().isEmpty() || !configData.getIsTesting().equals("true")){
                       isTestSmallScale = false;
                    }else {
                        isTestSmallScale = true;
                    }
                    if(configData.getIsTestRequest().isEmpty() || !configData.getIsTestRequest().equals("true")){
                        isTestSmallScale = false;
                    }else {
                        isTestSmallScale = true;
                    }
                    runStepOne();
                }else if(configData.getAction().equals("2")){
                    if(configData.getToken().isEmpty()){
                        System.out.println("missing token!");
                        return;
                    }else {
                        RequestApis.token = configData.getToken();
                    }
                    if(configData.getFileData().isEmpty()){
                        System.out.println("missing file data!");
                        return;
                    }else {
                        RunStepTwo(configData.getFileData());
                    }
                }
            }
        }

//        runStepOne();
        //RunStepTwo("noduplicate.txt");
        //updateData("content_request.txt","content_request_2.txt");
    }
    static Path file = Paths.get("allplace.txt");
    static Path fileNotDuplicate = Paths.get("noduplicate.txt");
    static Path fileMark = Paths.get("mark.txt");
    static private HashSet<String> listPlaceNameRequested = new HashSet<>();
    static private HashSet<String> listLatLngRequested = new HashSet<>();
    private static void runStepOne(){
        RequestApis requestApis = new RequestApis();
        requestApis.setmListener(new RequestApis.Listener() {
            @Override
            public void onFinishGetDataWays(ResponseWays dataWay) {
                ArrayList<String> pointsData = new ArrayList<>();
                int index = 0;
                Gson gson = new Gson();
                for (WaysItem item : dataWay.getData().getWays()) {
                    requestApis.getNodesInWay(item,0);
                    index++;
                    if(isTestSmallScale){
                        if (index >= 2) {
                            break;
                        }
                    }
                }

                /*Lấy danh sách điểm mốc lưu vào file*/
                /*for (WaysItem item : dataWay.getData().getWays()) {
                    for(MarkPoint point: item.getmFieldDetect().getmListPoint()){
                        pointsData.add(gson.toJson(point));
                    }
                }
                try {
                    Files.write(fileMark,pointsData, StandardCharsets.UTF_8,StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }

            @Override
            public void onFinshGetListNodes(WaysItem waysItem) {
                Gson gson = new Gson();
                for (MarkPoint point : waysItem.getmFieldDetect().getmListPoint()) {
                    RequestGcs requestGcs = new RequestGcs.Builder()
                            .setDistance(waysItem.getmFieldDetect().getDistance())
                            .setLocation(point.getLocation())
                            .setmListener(new RequestGcs.Listener() {
                                @Override
                                public void onGetPlacesFisnish(List<ResultsItem> responsePlaces) {
                                    ArrayList<String> content = new ArrayList<>();
                                    ArrayList<String> contentNotDuplicate = new ArrayList<>();
                                    for (ResultsItem resultItem : responsePlaces) {
                                        RequestBodyAddPlace requestBodyAddPlace = new RequestBodyAddPlace();
                                        requestBodyAddPlace.setLatitude(String.format("%.7f", resultItem.getGeometry().getLocation().getLat()));
                                        requestBodyAddPlace.setLongitude(String.format("%.7f", resultItem.getGeometry().getLocation().getLng()));
                                        requestBodyAddPlace.setName(resultItem.getName());
                                        requestBodyAddPlace.setStreet(resultItem.getVicinity().isEmpty() ? "undefined" : resultItem.getVicinity());
                                        int typeId = 114;
                                        for (String typeItem : resultItem.getTypes()) {
                                            if (!typeItem.equals("real_estate_agency") && !typeItem.equals("establishment") && !typeItem.equals("point_of_interest")) {
                                                if (getTypeId(typeItem) != 114) {
                                                    typeId = getTypeId(typeItem);
                                                }
                                            }
                                        }
                                        requestBodyAddPlace.setType(String.valueOf(typeId));
                                        requestBodyAddPlace.setStreetNumber(getNumberStreet(requestBodyAddPlace.getStreet()));
                                        DataForRequestCreatPlaceGmap data = new DataForRequestCreatPlaceGmap(requestBodyAddPlace, waysItem.getId());
                                        content.add(gson.toJson(data));
                                        if (!listPlaceNameRequested.contains(resultItem.getName())
                                                && !listLatLngRequested.contains(resultItem.getGeometry().getLocation().toStringForGcs())) {
                                            listPlaceNameRequested.add(resultItem.getName());
                                            listLatLngRequested.add(resultItem.getGeometry().getLocation().toStringForGcs());
                                            if (!waysItem.getListLatLng().contains(resultItem.getGeometry().getLocation().toStringForGcs())
                                                    && !waysItem.getListPlaceName().contains(resultItem.getName())) {
                                                contentNotDuplicate.add(gson.toJson(data));
                                            }

                                        }
                                    }
                                    try {
                                        Files.write(file, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                                        Files.write(fileNotDuplicate, contentNotDuplicate, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).build();
                    requestGcs.requestGoogleService(0, "", new ArrayList<>());
                }
            }
        });
        requestApis.getWays();
    }

    private static void RunStepTwo(String fileName){
        StringBuilder sb = new StringBuilder();
        Gson gson = new Gson();
        ArrayList<DataForRequestCreatPlaceGmap> data = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {

            String line;
            while ((line = br.readLine()) != null) {
                try {
                    data.add(gson.fromJson(line,DataForRequestCreatPlaceGmap.class));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            Thread countDownThread = new Thread() {
                @Override
                public void run() {
                    RequestApis requestApis = new RequestApis();
                    int count = 0;
                    for(DataForRequestCreatPlaceGmap request : data){
                        if(request.getmRequest() != null ){//&& request.getmWayId()== 687216053
                            requestApis.createNewPlace(request.getmRequest(), request.getmWayId());
                        }
                        if(count % 100 == 0){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        count++;
                    }
                }
            };
            countDownThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void updateData(String inputFile,String outputFile){
        Gson gson = new Gson();
        ArrayList<DataForRequestCreatPlaceGmap> data = new ArrayList<>();
        Path file = Paths.get(outputFile);
        try (BufferedReader br = Files.newBufferedReader(Paths.get(inputFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                try {
                    DataForRequestCreatPlaceGmap item = gson.fromJson(line, DataForRequestCreatPlaceGmap.class);
                    item.getmRequest().setStreetNumber(getNumberStreet(item.getmRequest().getStreet()));
                    data.add(item);

                } catch (Exception e) {
                    e.printStackTrace();
                }
//                sb.append(line).append("\n");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> content = new ArrayList<>();
        for(DataForRequestCreatPlaceGmap item :data){
            content.add(gson.toJson(item));
        }
        try {
            Files.write(file,content, StandardCharsets.UTF_8,StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean strIsNumer(String item){
        try{
            Integer.parseInt(item);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }


    private static String getNumberStreet(String street){
        String numberStreet = "undefined";
        String[] fisrtSlip = street.split(",");
        if(fisrtSlip.length >0){
            String[] secondSlip = fisrtSlip[0].split(" ");
            int index = 0;
            for(String item2: secondSlip){
                if(strIsNumer(item2)){
                    if(index == 0){
                        return item2;
                    }else if(index > 0){
                        String preStr = secondSlip[index-1] ;
                        if(preStr.equals("Số") || preStr.equals("số")
                                || preStr.equals("sỐ") || preStr.equals("Nhà") || preStr.equals("nhà")){
                            return item2;
                        }
                    }
                }
                index++;
            }
        }
        return numberStreet;
    }

    private static int getTypeId(String type) {
        switch (type) {
            case "bakery":
            case "meal_delivery":
            case "meal_takeaway":
                return 120;
            case "bank":
                return 59;
            case "bar":
            case "night_club":
                return 115;
            case "beauty_salon":
            case "spa":
                return 84;
            case "bicycle_store":
            case "liquor_store":
            case "pet_store":
            case "shoe_store":
            case "electronics_store":
            case "florist":
            case "jewelry_store":
            case "home_goods_store":
            case "department_store":
            case "hardware_store":
            case "store":
                return 93;
            case "book_store":
                return 89;
            case "bowling_alley":
                return 96;
            case "bus_station":
                return 107;
            case "cafe":
                return 52;
            case "campground":
                return 116;
            case "cemetery":
                return 72;
            case "lawyer":
            case "real_estate_agency":
            case "veterinary_care":
            case "insurance_agency":
            case "travel_agency":
            case "roofing_contractor":
                return 103;
            case "local_government_office":
                return 128;
            case "lodging":
                return 24;
            case "mosque":
            case "hindu_temple":
            case "synagogue":
                return 67;
            case "movie_rental":
            case "movie_theater":
                return 53;
            case "park":
            case "rv_park":
            case "zoo":
                return 54;
            case "parking":
                return 75;
            case "pharmacy":
            case "drugstore":
                return 97;
            case "car_dealer":
            case "car_rental":
            case "car_repair":
            case "car_wash":
                return 108;
            case "physiotherapist":
            case "doctor":
                return 86;
            case "city_hall":
                return 31;
            case "primary_school":
            case "school":
            case "secondary_school":
                return 5;
            case "shopping_mall":
                return 13;
            case "subway_station":
            case "fire_station":
            case "train_station":
            case "transit_station":
                return 113;
            case "gym":
                return 94;
            case "supermarket":
                return 66;
            case "restaurant":
                return 50;
            case "stadium":
                return 65;
            case "funeral_home":
                return 101;
            case "university":
                return 45;
            case "hair_care":
                return 100;
            case "dentist":
                return 85;
            case "courthouse":
                return 63;
            case "hospital":
            case "health":
                return 4;
            case "tourist_attraction":
                return 118;
            case "storage":
                return 126;
            case "intersection":
                return 78;
            case "post_office":
                return 73;
            case "sublocality":
                return 2;
            default:
                return 114;
        }
    }
}
