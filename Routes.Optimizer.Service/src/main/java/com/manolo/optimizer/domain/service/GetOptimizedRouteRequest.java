package com.manolo.optimizer.domain.service;

/**
 * GetOptimizedRouteRequest store information used while requesting for optimized routes, such originCity
 *
 */
public class GetOptimizedRouteRequest {

    /** The origin city. */
    protected String originCity;
        
    /**
     * Instantiates a new gets the optimized route request.
     */
    public GetOptimizedRouteRequest() {        
    }

    /**
     * Instantiates a new gets the optimized route request.
     *
     * @param city the city
     */
    public GetOptimizedRouteRequest(String city) {
        this.originCity = city;;
    }
    
    /**
     * Gets the origin city.
     *
     * @return the origin city
     */
    public String getOriginCity() {
        return originCity;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GetOptimizedRouteRequest [originCity=").append(originCity).append("]");
        return builder.toString();
    }
}
