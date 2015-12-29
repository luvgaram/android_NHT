package org.nhnnext.nearhoneytip.item;

import java.io.Serializable;

/**
 * Created by eunjooim on 2015. 12. 22.
 */
public class TipLocation implements Serializable {

//    + type - type
//    + Coordinates - Coordinates
//
//    + type - type
//    + coordinates -
//    {
//        "type": "Point",
//        "coordinates": [
//            127.108548,
//            37.401319
//        ]
//    },

    public String type;

    public double[] coordinates;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

}
