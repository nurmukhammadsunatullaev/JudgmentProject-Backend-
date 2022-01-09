package undp.judgment.list.judgment.services.utils;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import undp.judgment.list.judgment.models.cases.ClaimEntity;
import java.sql.ResultSet;
import java.sql.SQLException;



@Component
public class ClaimRowMapper implements RowMapper<ClaimEntity> {

    @Override
    public ClaimEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        ClaimEntity claimEntity=new ClaimEntity();
        claimEntity.setCaseId(resultSet.getLong("case_id"));
        claimEntity.setCaseResult(resultSet.getString("case_result"));
        claimEntity.setCaseTypeName(resultSet.getString("case_type_name"));
        claimEntity.setCaseGlobalNumber(resultSet.getString("case_global_number"));
        claimEntity.setDemandantName(resultSet.getString("demandant_name"));
        claimEntity.setDefendantName(resultSet.getString("defendant_name"));
        claimEntity.setCourtName(resultSet.getString("court_name"));
        claimEntity.setJudgeName(resultSet.getString("judge_name"));
        claimEntity.setCaseDateAdd(resultSet.getString("date_add"));
        claimEntity.setLevelName(resultSet.getString("level_name"));
        claimEntity.setJudgementId(resultSet.getLong("judgement_id"));
        claimEntity.setJudgementPrivacy(resultSet.getShort("judgement_privacy"));
        String actLink=resultSet.getString("file_path");
        claimEntity.setActLink(actLink == null ? "#" : actLink);
        return claimEntity;
    }

}
