package com.ecalm.ez_health.calculate;

public class CapitalizeFirstLetter {
    public static String capitaliseName(String name) {
        String collect[] = name.split(" ");
        String returnName = "";
        for (int i = 0; i < collect.length; i++) {
            collect[i] = collect[i].trim().toLowerCase();
            if (collect[i].isEmpty() == false) {
                returnName = returnName + collect[i].substring(0, 1).toUpperCase() + collect[i].substring(1) + " ";
            }
        }
        return returnName.trim();
    }
    public static String capitaliseOnlyFirstLetter(String data)
    {
        return data.substring(0,1).toUpperCase()+data.substring(1);
    }
}
