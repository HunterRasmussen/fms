package fms.facade;


class Location {

    double latitude;
    double longitude;
    String city;
    String country;


    public Location(){
        latitude = 0;
        longitude = 0;
        city = null;
        country = null;
    }
    public Location(String country, String city, Double latitude, Double longitude){
        this.country = country;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
