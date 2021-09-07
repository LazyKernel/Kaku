package ca.fuwafuwa.kaku.Database.JmDictDatabase.Models;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by LazyKernel on 9/7/2021.
 */

public class PitchOptimized  {
    @Expose(serialize = false)
    @DatabaseField(generatedId = true)
    private Integer id;

    @Expose
    @DatabaseField(dataType = DataType.LONG_STRING)
    private String expression;

    @Expose
    @DatabaseField(dataType = DataType.LONG_STRING)
    private String reading;

    @Expose
    @DatabaseField(dataType = DataType.INTEGER)
    private int pitch;

    @Expose
    @DatabaseField(dataType = DataType.LONG_STRING)
    private String pos;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public Integer getPitch() {
        return pitch;
    }

    public void setPitch(Integer pitch) {
        this.pitch = pitch;
    }

    public String getPos() {
        if (pos == null)
        {
            return "";
        }

        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

}
