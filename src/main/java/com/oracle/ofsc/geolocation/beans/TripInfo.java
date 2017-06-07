package com.oracle.ofsc.geolocation.beans;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Feeds information for Google Drive Matrix and stores the results.
 */
public class TripInfo {

    private String id;
    private String gMessage;
    private Date routeDay;
    private String resourceId;
    private String originEventId;
    private String destEventId;
    private BigDecimal originLat;
    private BigDecimal originLong;
    private BigDecimal destLat;
    private BigDecimal destLong;
    private String originAddress;
    private String destAddress;
    private BigDecimal mileage;
    private int driveTime;
    private String status = "Unknown";

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public TripInfo() {}

    public Date getRouteDay() {
        return routeDay;
    }

    public void setRouteDay(Date routeDay) {
        this.routeDay = routeDay;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public BigDecimal getOriginLat() {
        return originLat;
    }

    public void setOriginLat(BigDecimal originLat) {
        this.originLat = originLat;
    }

    public BigDecimal getOriginLong() {
        return originLong;
    }

    public void setOriginLong(BigDecimal originLong) {
        this.originLong = originLong;
    }

    public BigDecimal getDestLat() {
        return destLat;
    }

    public void setDestLat(BigDecimal destLat) {
        this.destLat = destLat;
    }

    public BigDecimal getDestLong() {
        return destLong;
    }

    public void setDestLong(BigDecimal destLong) {
        this.destLong = destLong;
    }

    public String getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(String originAddress) {
        this.originAddress = originAddress;
    }

    public String getDestAddress() {
        return destAddress;
    }

    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }

    public BigDecimal getMileage() {
        return mileage;
    }

    public void setMileage(BigDecimal mileage) {
        this.mileage = mileage;
    }

    public int getDriveTime() {
        return driveTime;
    }

    public void setDriveTime(int driveTime) {
        this.driveTime = driveTime;
    }

    public String getOriginEventId() {
        return originEventId;
    }

    public void setOriginEventId(String originEventId) {
        this.originEventId = originEventId;
    }

    public String getDestEventId() {
        return destEventId;
    }

    public void setDestEventId(String destEventId) {
        this.destEventId = destEventId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getgMessage() {
        return gMessage;
    }

    public void setgMessage(String gMessage) {
        this.gMessage = gMessage;
    }

    /**
     * Provides the output string in the format of a Google Distance Matrix URL query string.
     * Will use used when this object is provided as part of a In Body and used by the GET
     * call for HTTP4 to query Google Distance Matrix.
     *
     * @return
     */
    @Override
    public String toString() {
        return String.format("language=en&origins=%f,%f&destinations=%f,%f", originLat, originLong, destLat, destLong);
    }

    public String toSQLInsertString() {
        return String.format("INSERT INTO route_metrics "
                        + "(g_request, g_result, g_msg, from_activity, "
                        + "to_activity, route_day, resource_id, origin_lat, origin_long, dest_lat, dest_long, "
                        + "g_drive_time, g_drive_distance, origin, dest) "
                        + "VALUES "
                        + "('%s', '%s', '%s', '%s', '%s', '%s', '%s', %f, %f, %f, %f, %d, %.2f, '%s', '%s')",
                id,
                status,
                gMessage,
                originEventId,
                destEventId,
                df.format(routeDay),
                resourceId,
                originLat, originLong,
                destLat, destLong,
                driveTime, mileage,
                StringUtils.remove(originAddress, "'"),
                StringUtils.remove(destAddress, "'")
        );
    }
}
