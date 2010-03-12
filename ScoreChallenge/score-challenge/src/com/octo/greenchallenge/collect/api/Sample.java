package com.octo.greenchallenge.collect.api;

import com.google.appengine.api.datastore.Key;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.Map;

/**
 * One measure.
 */
@Entity
public class Sample {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key id;

    private String challengerID;
    private long cpuCycles;
    private Date timestamp;
    private SampleSource source;

    /**
     * Blank constructor for JPA and XML marshalling.
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
        try {
            Sample sample = new Sample();
            sample.challengerID = (String) data.get("challengerID");
            assertNotBlank(sample.challengerID, "challengerID");

            try {
                String rawCpuCycles = (String) data.get("CPUCycles");
                assertNotBlank(rawCpuCycles, "CPUCycles");
                sample.cpuCycles = Long.parseLong(rawCpuCycles);
                if (sample.cpuCycles < 0) {
                    throw new NumberFormatException("invalid CPUCycles");
                }
            } catch (Exception ex) {
                throw new InvalidDataException("invalid CPUCycles", ex);
            }

            try {
                String rawSource = (String) data.get("source");
                assertNotBlank(rawSource, "source");
                sample.source = SampleSource.valueOf(rawSource);
            } catch (Exception ex) {
                throw new InvalidDataException("invalid source", ex);
            }
            return sample;
        } catch (ClassCastException ex) {
            throw new InvalidDataException("invalid API usage");
        }
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
