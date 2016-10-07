package io.pivotal.gemfire.pubs.model.key;

import com.gemstone.gemfire.DataSerializable;
import com.gemstone.gemfire.DataSerializer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by zhansen on 10/7/16.
 */
public class TitleKey implements DataSerializable {

    private String titleId;

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    @Override
    public String toString() {
        return "TitleKey{" +
                "titleId='" + titleId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TitleKey titleKey = (TitleKey) o;

        return titleId != null ? titleId.equals(titleKey.titleId) : titleKey.titleId == null;

    }

    @Override
    public int hashCode() {
        return titleId != null ? titleId.hashCode() : 0;
    }

    @Override
    public void toData(DataOutput dataOutput) throws IOException {
        DataSerializer.writeString(titleId, dataOutput);
    }

    @Override
    public void fromData(DataInput dataInput) throws IOException, ClassNotFoundException {
        titleId = DataSerializer.readString(dataInput);
    }
}
