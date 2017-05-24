package utils;

import java.util.ArrayList;
import java.util.List;

public class LocationUtils {

	public static List getNine(double lat1, double lng1, double lat2, double lng2) 
	{
		List list     = new ArrayList();
		double lat_ju = (lat1-lat2)/3;
		double wd1 	  = lat2+lat_ju;
    	double wd2 	  = lat2+lat_ju*2;
    	double lng_ju = 0.d;
    	double jd1    = 0.d;
    	double jd2    = 0.d;
    	if(lng1 > 0 && lng2 < 0)
    	{
    		lng_ju = ((180-lng1)+(180+lng2))/3;
    		jd1 	= lng1+lng_ju;
    		if(jd1>180)
    		{
    			jd1  = jd1-360;
    		}
    		jd2 	= lng1+lng_ju*2;
    		if(jd2>180)
    		{
    			jd2  = jd2-360;
    		}
    	}else{
    		lng_ju = (lng2-lng1)/3;
    		jd1 	= lng1+lng_ju;
    		jd2 	= lng1+lng_ju*2;
    	}
    	list.add(new Double[]{lat1, lng1,wd2, jd1});
    	list.add(new Double[]{lat1, jd1, wd2, jd2});
    	list.add(new Double[]{lat1, jd2, wd2, lng2});
    	list.add(new Double[]{wd2,  lng1,wd1, jd1});
    	list.add(new Double[]{wd2,  jd1, wd1, jd2});    	
    	list.add(new Double[]{wd2,  jd2, wd1, lng2});    	
    	list.add(new Double[]{wd1,  lng1,lat2,jd1});   	
    	list.add(new Double[]{wd1,   jd1,lat2,jd2});   	
    	list.add(new Double[]{wd1,  jd2, lat2,lng2});
    	return list;
	}
}
