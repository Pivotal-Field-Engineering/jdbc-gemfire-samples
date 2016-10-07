package io.pivotal.gemfire.pubs.model.key;

import com.gemstone.gemfire.DataSerializable;
import com.gemstone.gemfire.DataSerializer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by zhansen on 10/7/16.
 */
public class TitleAuthorKey implements DataSerializable {
    private String titleId;
    private String auId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TitleAuthorKey that = (TitleAuthorKey) o;

        if (titleId != null ? !titleId.equals(that.titleId) : that.titleId != null) return false;
        return auId != null ? auId.equals(that.auId) : that.auId == null;

    }

    @Override
    public int hashCode() {
        int result = titleId != null ? titleId.hashCode() : 0;
        result = 31 * result + (auId != null ? auId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TitleAuthorKey{" +
                "titleId='" + titleId + '\'' +
                ", auId='" + auId + '\'' +
                '}';
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getAuId() {
        return auId;
    }

    public void setAuId(String auId) {
        this.auId = auId;
    }

    @Override
    public void toData(DataOutput dataOutput) throws IOException {
        DataSerializer.writeString(auId, dataOutput);
        DataSerializer.writeString(titleId, dataOutput);
    }

    @Override
    public void fromData(DataInput dataInput) throws IOException, ClassNotFoundException {
        auId = DataSerializer.readString(dataInput);
        titleId = DataSerializer.readString(dataInput);
    }
}
