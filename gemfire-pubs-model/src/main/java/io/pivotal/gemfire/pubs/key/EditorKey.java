package io.pivotal.gemfire.pubs.key;

import com.gemstone.gemfire.DataSerializable;
import com.gemstone.gemfire.DataSerializer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by zhansen on 10/7/16.
 */
public class EditorKey implements DataSerializable {
    private String edId;

    public String getEdId() {
        return edId;
    }

    public void setEdId(String edId) {
        this.edId = edId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EditorKey editorKey = (EditorKey) o;

        return edId != null ? edId.equals(editorKey.edId) : editorKey.edId == null;

    }

    @Override
    public String toString() {
        return "EditorKey{" +
                "edId='" + edId + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return edId != null ? edId.hashCode() : 0;
    }

    @Override
    public void toData(DataOutput dataOutput) throws IOException {
        DataSerializer.writeString(edId, dataOutput);
    }

    @Override
    public void fromData(DataInput dataInput) throws IOException, ClassNotFoundException {
        edId = DataSerializer.readString(dataInput);
    }
}
