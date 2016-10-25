package io.pivotal.gemfire.pubs.key;

import com.gemstone.gemfire.DataSerializable;
import com.gemstone.gemfire.DataSerializer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by zhansen on 10/7/16.
 */
public class PublisherKey implements DataSerializable{
    private String pubId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PublisherKey that = (PublisherKey) o;

        return pubId != null ? pubId.equals(that.pubId) : that.pubId == null;

    }

    @Override
    public int hashCode() {
        return pubId != null ? pubId.hashCode() : 0;
    }

    public String getPubId() {

        return pubId;
    }

    public void setPubId(String pubId) {
        this.pubId = pubId;
    }

    @Override
    public void toData(DataOutput dataOutput) throws IOException {
        DataSerializer.writeString(pubId, dataOutput);
    }

    @Override
    public String toString() {
        return "PublisherKey{" +
                "pubId='" + pubId + '\'' +
                '}';
    }

    @Override
    public void fromData(DataInput dataInput) throws IOException, ClassNotFoundException {
        pubId = DataSerializer.readString(dataInput);
    }
}
