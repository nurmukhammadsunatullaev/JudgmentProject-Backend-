package undp.judgment.list.judgment.models.cases;


public class ClaimEntity {
    private Long caseId;
    private String caseResult;
    private String caseGlobalNumber;
    private String caseTypeName;
    private String caseDateAdd;
    private String demandantName;
    private String defendantName;
    private String courtName;
    private String judgeName;
    private String levelName;
    private String actLink;
    private Long judgementId;

    private Short judgementPrivacy;

    public Short getJudgementPrivacy() {
        return judgementPrivacy;
    }

    public void setJudgementPrivacy(Short judgementPrivacy) {
        this.judgementPrivacy = judgementPrivacy;
    }


    public Long getJudgementId() {
        return judgementId;
    }

    public void setJudgementId(Long judgementId) {
        this.judgementId = judgementId;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public String getCaseResult() {
        return caseResult;
    }

    public void setCaseResult(String caseResult) {
        this.caseResult = caseResult;
    }

    public String getCaseGlobalNumber() {
        return caseGlobalNumber;
    }

    public void setCaseGlobalNumber(String caseGlobalNumber) {
        this.caseGlobalNumber = caseGlobalNumber;
    }

    public String getCaseTypeName() {
        return caseTypeName;
    }

    public void setCaseTypeName(String caseTypeName) {
        this.caseTypeName = caseTypeName;
    }

    public String getCaseDateAdd() {
        return caseDateAdd;
    }

    public void setCaseDateAdd(String caseDateAdd) {
        this.caseDateAdd = caseDateAdd;
    }

    public String getDemandantName() {
        return demandantName;
    }

    public void setDemandantName(String demandantName) {
        this.demandantName = demandantName;
    }

    public String getDefendantName() {
        return defendantName;
    }

    public void setDefendantName(String defendantName) {
        this.defendantName = defendantName;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public String getJudgeName() {
        return judgeName;
    }

    public void setJudgeName(String judgeName) {
        this.judgeName = judgeName;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getActLink() {
        return actLink;
    }

    public void setActLink(String actLink) {
        this.actLink = actLink;
    }
}
