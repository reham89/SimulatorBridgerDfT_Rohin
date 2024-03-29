/*
 * This file is generated by jOOQ.
 */
package uk.ncl.giacomobergami.utils.database.jooq.tables.records;


import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;

import uk.ncl.giacomobergami.utils.database.jooq.tables.Vehinformation;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class VehinformationRecord extends UpdatableRecordImpl<VehinformationRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.vehinformation.di_entry_id</code>.
     */
    public void setDiEntryId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.vehinformation.di_entry_id</code>.
     */
    public Integer getDiEntryId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.vehinformation.vehicle_id</code>.
     */
    public void setVehicleId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.vehinformation.vehicle_id</code>.
     */
    public String getVehicleId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.vehinformation.x</code>.
     */
    public void setX(Double value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.vehinformation.x</code>.
     */
    public Double getX() {
        return (Double) get(2);
    }

    /**
     * Setter for <code>public.vehinformation.y</code>.
     */
    public void setY(Double value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.vehinformation.y</code>.
     */
    public Double getY() {
        return (Double) get(3);
    }

    /**
     * Setter for <code>public.vehinformation.angle</code>.
     */
    public void setAngle(Double value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.vehinformation.angle</code>.
     */
    public Double getAngle() {
        return (Double) get(4);
    }

    /**
     * Setter for <code>public.vehinformation.vehicle_type</code>.
     */
    public void setVehicleType(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.vehinformation.vehicle_type</code>.
     */
    public String getVehicleType() {
        return (String) get(5);
    }

    /**
     * Setter for <code>public.vehinformation.speed</code>.
     */
    public void setSpeed(Double value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.vehinformation.speed</code>.
     */
    public Double getSpeed() {
        return (Double) get(6);
    }

    /**
     * Setter for <code>public.vehinformation.pos</code>.
     */
    public void setPos(Double value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.vehinformation.pos</code>.
     */
    public Double getPos() {
        return (Double) get(7);
    }

    /**
     * Setter for <code>public.vehinformation.lane</code>.
     */
    public void setLane(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>public.vehinformation.lane</code>.
     */
    public String getLane() {
        return (String) get(8);
    }

    /**
     * Setter for <code>public.vehinformation.slope</code>.
     */
    public void setSlope(Double value) {
        set(9, value);
    }

    /**
     * Getter for <code>public.vehinformation.slope</code>.
     */
    public Double getSlope() {
        return (Double) get(9);
    }

    /**
     * Setter for <code>public.vehinformation.simtime</code>.
     */
    public void setSimtime(Double value) {
        set(10, value);
    }

    /**
     * Getter for <code>public.vehinformation.simtime</code>.
     */
    public Double getSimtime() {
        return (Double) get(10);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached VehinformationRecord
     */
    public VehinformationRecord() {
        super(Vehinformation.VEHINFORMATION);
    }

    /**
     * Create a detached, initialised VehinformationRecord
     */
    public VehinformationRecord(Integer diEntryId, String vehicleId, Double x, Double y, Double angle, String vehicleType, Double speed, Double pos, String lane, Double slope, Double simtime) {
        super(Vehinformation.VEHINFORMATION);

        setDiEntryId(diEntryId);
        setVehicleId(vehicleId);
        setX(x);
        setY(y);
        setAngle(angle);
        setVehicleType(vehicleType);
        setSpeed(speed);
        setPos(pos);
        setLane(lane);
        setSlope(slope);
        setSimtime(simtime);
        resetChangedOnNotNull();
    }
}
