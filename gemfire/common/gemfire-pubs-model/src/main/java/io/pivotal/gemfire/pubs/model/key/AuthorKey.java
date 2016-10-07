package io.pivotal.gemfire.pubs.model.key;

import com.gemstone.gemfire.DataSerializable;
import com.gemstone.gemfire.DataSerializer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by zhansen on 10/7/16.
 */
public class AuthorKey implements DataSerializable {
    private String auId;

    @Override
    public void toData(DataOutput dataOutput) throws IOException {
        DataSerializer.writeObject(auId, dataOutput);
    }

    @Override
    public void fromData(DataInput dataInput) throws IOException, ClassNotFoundException {
        auId = DataSerializer.readObject(dataInput);
    }

    public String getAuId() {
        return auId;
    }

    public void setAuId(String auId) {
        this.auId = auId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthorKey authorKey = (AuthorKey) o;

        return auId != null ? auId.equals(authorKey.auId) : authorKey.auId == null;

    }

    @Override
    public int hashCode() {
        return auId != null ? auId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AuthorKey{" +
                "auId='" + auId + '\'' +
                '}';
    }
}
