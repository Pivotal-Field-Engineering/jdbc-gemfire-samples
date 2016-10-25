package io.pivotal.gemfire.pubs.key;

import com.gemstone.gemfire.DataSerializable;
import com.gemstone.gemfire.DataSerializer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by zhansen on 10/7/16.
 */
public class SaleKey implements DataSerializable {
    private BigDecimal sonum;

    @Override
    public void toData(DataOutput dataOutput) throws IOException {
        DataSerializer.writeObject(sonum, dataOutput);
    }

    @Override
    public void fromData(DataInput dataInput) throws IOException, ClassNotFoundException {
        sonum = DataSerializer.readObject(dataInput);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SaleKey saleKey = (SaleKey) o;

        return sonum != null ? sonum.equals(saleKey.sonum) : saleKey.sonum == null;

    }

    @Override
    public int hashCode() {
        return sonum != null ? sonum.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "SaleKey{" +
                "sonum=" + sonum +
                '}';
    }

    public BigDecimal getSonum() {
        return sonum;
    }

    public void setSonum(BigDecimal sonum) {
        this.sonum = sonum;
    }
}
