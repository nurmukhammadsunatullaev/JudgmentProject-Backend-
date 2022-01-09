package undp.judgment.list.judgment.models.attributes;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class SearchEntity implements Serializable {

    @NotNull
    private Date fromDate;
    @NotNull
    private Date toDate;
    @NotNull
    private Short regionId;
    @NotNull
    private Short courtId;
    @NotNull
    private Short categoryId;
    @NotNull
    private Short requirementId;

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Short getRegionId() {
        return regionId;
    }

    public void setRegionId(Short regionId) {
        this.regionId = regionId;
    }

    public Short getCourtId() {
        return courtId;
    }

    public void setCourtId(Short courtId) {
        this.courtId = courtId;
    }

    public Short getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Short categoryId) {
        this.categoryId = categoryId;
    }

    public Short getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(Short requirementId) {
        this.requirementId = requirementId;
    }



    @Override
    public String toString() {
        return String.format("{%s\t%s\t%s\t%s\t%s\t%s}\n", getFromDate(), getToDate(), getRegionId(), getCourtId(), getCategoryId(), getRequirementId());
    }
}
