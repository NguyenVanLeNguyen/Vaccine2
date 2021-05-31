import gcs.RequestGcs;
import gmap.RequestApis;
import object.nodes.ResponseNodes;
import object.places.ResponsePlaces;
import object.places.ResultsItem;
import object.result.RequestBodyAddPlace;
import object.ways.ResponseWays;
import object.ways.WaysItem;

import java.util.List;

public class Run {
    public static void main(String[] args) {
        RequestApis requestApis = new RequestApis();
        requestApis.setmListener(new RequestApis.Listener() {
            @Override
            public void onFinishGetDataWays(ResponseWays dataWay) {
                for (WaysItem item : dataWay.getData().getWays()) {
                    requestApis.getNodesInWay(item);
                }
            }

            @Override
            public void onFinshGetListNodes(ResponseNodes responseNodes, WaysItem waysItem) {
                RequestGcs requestGcs = new RequestGcs.Builder()
                        .setDistance(waysItem.getmFieldDetect().getDistance())
                        .setLocation(waysItem.getmFieldDetect().getCenterLocation())
                        .setmListener(new RequestGcs.Listener() {
                            @Override
                            public void onGetPlacesFisnish(List<ResultsItem> responsePlaces) {
                                for (ResultsItem resultItem : responsePlaces) {
                                    if (!waysItem.getListLatLng().equals(resultItem.getGeometry().getLocation().toStringForGcs())
                                            && !waysItem.getListPlaceName().equals(resultItem.getName())) {
                                        RequestBodyAddPlace requestBodyAddPlace = new RequestBodyAddPlace();
                                        requestBodyAddPlace.setLatitude(String.format("%.7f", resultItem.getGeometry().getLocation().getLat()));
                                        requestBodyAddPlace.setLongitude(String.format("%.7f", resultItem.getGeometry().getLocation().getLng()));
                                        requestBodyAddPlace.setName(resultItem.getName());
                                        requestBodyAddPlace.setStreet(resultItem.getVicinity().isEmpty()? "undefined":resultItem.getVicinity());
                                        int typeId = 114;
                                        for(String typeItem : resultItem.getTypes()){
                                            if(!typeItem.equals("real_estate_agency") && !typeItem.equals("establishment") && !typeItem.equals("point_of_interest" )){
                                                if(getTypeId(typeItem) != 114 ){
                                                    typeId = getTypeId(typeItem);
                                                }
                                            }
                                        }
                                        requestBodyAddPlace.setType(String.valueOf(typeId));
                                        requestBodyAddPlace.setStreetNumber("undefined");
                                    }
                                }

                            }
                        })
                        .build();
                requestGcs.requestGoogleService(0, "");
            }
        });
        requestApis.getWays();
    }

    private void getDataPlace(ResponseWays responseWays) {
        if (responseWays != null) {
            for (WaysItem waysItem : responseWays.getData().getWays()) {

            }
        }
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
