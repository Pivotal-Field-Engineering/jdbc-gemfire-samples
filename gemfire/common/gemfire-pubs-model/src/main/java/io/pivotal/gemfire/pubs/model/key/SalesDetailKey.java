package io.pivotal.gemfire.pubs.model.key;

import com.gemstone.gemfire.DataSerializable;
import com.gemstone.gemfire.DataSerializer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by zhansen on 10/7/16.
 */
public class SalesDetailKey implements DataSerializable {
    private String titleId;
    private BigDecimal sonum;

    @Override
    public String toString() {
        return "SalesDetailKey{" +
                "titleId='" + titleId + '\'' +
                ", sonum=" + sonum +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SalesDetailKey that = (SalesDetailKey) o;

        if (titleId != null ? !titleId.equals(that.titleId) : that.titleId != null) return false;
        return sonum != null ? sonum.equals(that.sonum) : that.sonum == null;

    }

    @Override
    public int hashCode() {
        int result = titleId != null ? titleId.hashCode() : 0;
        result = 31 * result + (sonum != null ? sonum.hashCode() : 0);
        return result;
    }

    public String getTitleId() {

        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public BigDecimal getSonum() {
        return sonum;
    }

    public void setSonum(BigDecimal sonum) {
        this.sonum = sonum;
    }

    @Override
    public void toData(DataOutput dataOutput) throws IOException {
        DataSerializer.writeObject(sonum, dataOutput);
        DataSerializer.writeString(titleId, dataOutput);
    }

    @Override
    public void fromData(DataInput dataInput) throws IOException, ClassNotFoundException {
        sonum = DataSerializer.readObject(dataInput);
        titleId = DataSerializer.readString(dataInput);
    }
}
