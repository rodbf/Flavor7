package com.flavorplus.recipes.explore;

import java.util.HashMap;

public class ExploreParameters{
    private HashMap<String, String> params = new HashMap<>();
    public static final int MODE_UNSORTED = 0;
    public static final int MODE_RANDOM = 1;
    public static final int MODE_SCORE = 2;

    public HashMap<String, String> getParams(){
        return params;
    }

    public void setSearchString(String str){
        params.put("search_str", str);
    }

    public void setJWT(String jwt){
        params.put("jwt", jwt);
    }

    public void useDietWhitelist(boolean bool){
        params.put("use_diet_whitelist", bool+"");
    }

    public void useIngredientBlacklist(boolean bool){
        params.put("use_ingredient_blacklist", bool+"");
    }

    public void useTagBlacklist(boolean bool){
        params.put("use_tag_blacklist", bool+"");
    }

    public void setWhitelistedTag(int tag){
        params.put("whitelisted_tag", tag+"");
    }

    public void setMealWhitelistArray(int[] array){
        String str = "";
        if(array.length > 0)
            str = array[0]+"";
        for(int i = 1; i < array.length; i++){
            str += ","+array[i];
        }
        params.put("meal_whitelist_array", str);
    }

    public void setMaxResults(int max){
        params.put("max_results", max+"");
    }

    public void setSortMode(int mode){
        params.put("sort_mode", mode+"");
    }

    public void ignoreDownvoted(boolean bool){
        params.put("ignore_downvoted", bool+"");
    }

}