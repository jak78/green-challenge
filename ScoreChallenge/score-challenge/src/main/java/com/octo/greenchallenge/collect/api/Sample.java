package com.octo.greenchallenge.collect.api;

import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;
import java.util.Map;

/**
 * One CPU time measure.
 */
@PersistenceCapable
public class Sample {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key id;

    @Persistent
    private String challengerID;

    @Persistent
    private long cpuCycles;

    @Persistent
    private Date timestamp;

    @Persistent
    private SampleSource source;

    /**
     * Blank constructor for JDO.
     */
    public Sample() {

    }

    public Sample(String challengerID, long cpuCycles, Date timestamp, SampleSource source) {
        this.challengerID = challengerID;
        this.cpuCycles = cpuCycles;
        this.timestamp = timestamp;
        this.source = source;
    }

    /**
     * Constructor from a map of fields.
     *
     * @param data fields
     * @return sample
     * @throws InvalidDataException when data is invalid
     */
    public static Sample build(Map<String, String> data) throws InvalidDataException {
        Sample sample = new Sample();

        sample.challengerID = data.get("challengerID");
        // challengerID is mandatory
        assertNotBlank(sample.challengerID, "challengerID");

        try {
            String rawCpuCycles = data.get("CPUCycles");
            // CPUCycles is mandatory
            assertNotBlank(rawCpuCycles, "CPUCycles");
            sample.cpuCycles = Long.parseLong(rawCpuCycles);
            if (sample.cpuCycles < 0) {
                // CPUCycles must be a positive 64 bits integer
                throw new NumberFormatException("invalid CPUCycles");
            }
        } catch (Exception ex) {
            // CPUCycles must be a positive 64 bits integer
            throw new InvalidDataException("invalid CPUCycles", ex);
        }

        try {
            String rawSource = data.get("source");
            assertNotBlank(rawSource, "source");
            // source is mandatory
            sample.source = SampleSource.valueOf(rawSource);
        } catch (Exception ex) {
            // source must have a known value
            throw new InvalidDataException("invalid source", ex);
        }
        return sample;
    }

    private static void assertNotBlank(String value, String name) throws InvalidDataException {
        if (value == null || value.matches("^[ \t\r\n]*$")) {
            throw new InvalidDataException(name + " is mandatory");
        }
    }

    public String getChallengerID() {
        return challengerID;
    }

    public void setChallengerID(String challengerID) {
        this.challengerID = challengerID;
    }

    public long getCpuCycles() {
        return cpuCycles;
    }

    public void setCpuCycles(long cpuCycles) {
        this.cpuCycles = cpuCycles;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public SampleSource getSource() {
        return source;
    }

    public void setSource(SampleSource source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sample sample = (Sample) o;

        if (cpuCycles != sample.cpuCycles) return false;
        if (challengerID != null ? !challengerID.equals(sample.challengerID) : sample.challengerID != null)
            return false;
        if (source != sample.source) return false;
//        if (timestamp != null ? !timestamp.equals(sample.timestamp) : sample.timestamp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = challengerID != null ? challengerID.hashCode() : 0;
        result = 31 * result + (int) (cpuCycles ^ (cpuCycles >>> 32));
        result = 31 * result + (source != null ? source.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Sample{" +
                "id=" + id +
                ", challengerID='" + challengerID + '\'' +
                ", cpuCycles=" + cpuCycles +
                ", timestamp=" + timestamp +
                ", source=" + source +
                '}';
    }
}
