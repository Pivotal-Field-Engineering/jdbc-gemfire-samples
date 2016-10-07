package io.pivotal.gemfire.pubs.model.key;

import com.gemstone.gemfire.DataSerializable;
import com.gemstone.gemfire.DataSerializer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by zhansen on 10/7/16.
 */
public class TitleEditorKey implements DataSerializable {
    private String edId;
    private String titleId;

    @Override
    public String toString() {
        return "TitleEditorKey{" +
                "edId='" + edId + '\'' +
                ", titleId='" + titleId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TitleEditorKey that = (TitleEditorKey) o;

        if (edId != null ? !edId.equals(that.edId) : that.edId != null) return false;
        return titleId != null ? titleId.equals(that.titleId) : that.titleId == null;

    }

    @Override
    public int hashCode() {
        int result = edId != null ? edId.hashCode() : 0;
        result = 31 * result + (titleId != null ? titleId.hashCode() : 0);
        return result;
    }

    public String getEdId() {

        return edId;
    }

    public void setEdId(String edId) {
        this.edId = edId;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    @Override
    public void toData(DataOutput dataOutput) throws IOException {
        DataSerializer.writeString(edId, dataOutput);
        DataSerializer.writeString(titleId, dataOutput);
    }

    @Override
    public void fromData(DataInput dataInput) throws IOException, ClassNotFoundException {
        edId = DataSerializer.readString(dataInput);
        titleId = DataSerializer.readString(dataInput);
    }
}
