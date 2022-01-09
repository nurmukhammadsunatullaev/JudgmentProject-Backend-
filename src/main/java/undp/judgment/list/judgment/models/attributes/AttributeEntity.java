package undp.judgment.list.judgment.models.attributes;

import java.sql.Timestamp;
import java.util.Date;

public class AttributeEntity<T> {

    private Integer attributeId;
    private String attributeName;
    private T attributeValue;
    private Timestamp fromDate;
    private Timestamp toDate;
    private String reasonValue;



    public AttributeEntity() {
    }

    public AttributeEntity(Integer attributeId, String attributeName) {
        this.attributeId = attributeId;
        this.attributeName = attributeName;
    }

    public AttributeEntity(Integer attributeId, String attributeName, T attributeValue) {
        this.attributeId = attributeId;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    public AttributeEntity(Integer attributeId, String attributeName, T attributeValue, Timestamp fromDate, Timestamp toDate, String reasonValue) {
        this.attributeId = attributeId;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reasonValue = reasonValue;
    }

    public Integer getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }


    public T getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(T attributeValue) {
        this.attributeValue = attributeValue;
    }

    public Timestamp getFromDate() {
        return fromDate;
    }

    public void setFromDate(Timestamp fromDate) {
        this.fromDate = fromDate;
    }

    public Timestamp getToDate() {
        return toDate;
    }

    public void setToDate(Timestamp toDate) {
        this.toDate = toDate;
    }

    public String getReasonValue() {
        return reasonValue;
    }

    public void setReasonValue(String reasonValue) {
        this.reasonValue = reasonValue;
    }
}
