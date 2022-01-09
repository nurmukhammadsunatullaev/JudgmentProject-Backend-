package undp.judgment.list.judgment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undp.judgment.list.judgment.models.attributes.AttributeEntity;
import undp.judgment.list.judgment.models.cases.ClaimEntity;
import undp.judgment.list.judgment.models.attributes.SearchEntity;
import undp.judgment.list.judgment.services.utils.ClaimRowMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@Service
public class ReportService {

    private final NamedParameterJdbcTemplate template;
    private final MessageService messageService;
    private final ClaimRowMapper claimRowMapper;


    @Autowired
    public ReportService(NamedParameterJdbcTemplate template, ClaimRowMapper claimRowMapper,MessageService messageService) {
        this.template = template;
        this.messageService=messageService;
        this.claimRowMapper = claimRowMapper;

    }

    @Transactional
    public List<ClaimEntity> findAll(SearchEntity searchEntity,String lang) {
        String sql=messageService.getMessage("application.claims.search.sql");
        if(lang.equals("ru")){
            sql=sql.replace("name_uz","name_ru");
        }
        return template.query(sql, parametersMap(new String[]{"fromDate", "toDate", "courtId", "requirementId", "states"}, getParametersValues(searchEntity)),  claimRowMapper);
    }

    @Transactional
    public List<AttributeEntity> findAllAttributes(String attribute, Integer parentId, String lang)throws NoSuchMessageException {
        String messageKey=parentId !=0 ? "application."+attribute+".sql":"application."+attribute+"_0.sql";
        String sql=messageService.getMessage(messageKey);
        if(lang.equals("ru")){
            sql=sql.replace("name_uz","name_ru");
        }
        return template.query(sql, parametersMap(new String[]{"parentId"},new Object[]{parentId}), (resultSet, i) -> new AttributeEntity(resultSet.getInt("attributeId"),resultSet.getString("attributeName")));
    }

    @Transactional
    public HashMap<String, Integer> findById(Integer id){
        SqlRowSet rowSet = template.queryForRowSet("select *from db_level where id=:id;", parametersMap(new String[]{"id"},new Object[]{id}));
        HashMap hashMap = new HashMap<String, Integer>();
        if(rowSet.next()){
            hashMap.put("type", rowSet.getInt("id"));
            hashMap.put("value", rowSet.getInt("level_count"));
        }
        return hashMap;
    }

    public static MapSqlParameterSource parametersMap(String [] parametersName, Object [] parametersValue){
        MapSqlParameterSource map=new MapSqlParameterSource();
        for (int i=0;i<parametersName.length;i++){
            map.addValue(parametersName[i],parametersValue[i]);
        }
        return map;
    }

    private Object[] getParametersValues(SearchEntity searchEntity){
        Object[] objects=new Object[5];
        objects[0]=searchEntity.getFromDate();
        objects[1]=searchEntity.getToDate();
        objects[2]=searchEntity.getRegionId() != 0 ? (
                searchEntity.getCourtId()!=0 ? searchEntity.getCourtId() : template.queryForList(
                        "select id from db_court where region_id=:parentId;",
                        parametersMap(
                                new String[]{"parentId"},
                                new Object[]{searchEntity.getRegionId()}),
                        Short.class))
                : template.queryForList("select id from db_court;",
                parametersMap(
                        new String[]{},
                        new Object[]{}),
                Short.class);
        objects[3]=searchEntity.getCategoryId() != 0 ?
                (searchEntity.getRequirementId() !=0 ? searchEntity.getRequirementId() : template.queryForList(
                        "select id from db_type where category_id=:parentId;",
                        parametersMap(
                                new String[]{"parentId"},
                                new Object[]{searchEntity.getCategoryId()}),
                        Short.class))
                : template.queryForList(
                "select id from db_type;",
                parametersMap(
                        new String[]{},
                        new Object[]{}),
                Short.class);
        objects[4]=Arrays.asList(12,13,21);
        return objects;
    }
    @Transactional
    public List<AttributeEntity> findAllStatistics(String attribute,Integer parentId) {
        String messageId= parentId!=0 ? "application.statistics."+attribute : "application.statistics."+attribute+"_0";
        return template.query(messageService.getMessage(messageId),new MapSqlParameterSource("parentId",parentId),
                ((resultSet, i) -> new AttributeEntity<Integer>(
                        resultSet.getInt("id"),
                        resultSet.getString("labels"),
                        resultSet.getInt("counts")))
        );
    }


    public void link() {
        String sql="SELECT case_id, demandant_name, defendant_name, (select name_uz from db_type where id=type_id) case_type_name, (select name_uz from db_court where id=court_id) court_name, (select full_name from db_user where id=judge_id) judge_name, (select name_uz from db_level where id=level_id) level_name, global_number case_global_number, TO_CHAR(signed_date, 'DD-MM-YYYY') date_add, (select name_uz from db_result where id=case_result) case_result, judgement_id, judgement_privacy, file_path FROM public.db_case;";
        template.query(sql,claimRowMapper).forEach(item->{
            if(fileExists(item)){
                updateFilePath(item.getCaseId(),item.getJudgementId(),String.format("/pdf/%s_%s.pdf",item.getCaseId(),item.getJudgementId()));
            }
            else{
                if(item.getJudgementPrivacy()==0){
                   updateFilePath(item.getCaseId(),item.getJudgementId(),generatedLink(item.getJudgementId()));
                }
            }
        });
    }

    public boolean fileExists(ClaimEntity item){
        String fileName = String.format("uploads/%s_%s.pdf",item.getCaseId(),item.getJudgementId());
        return Files.exists(Paths.get(fileName));
    }

    public String generatedLink(Long judgementId){
        String url =judgementId.toString();
        url= Base64.getEncoder().encodeToString(url.replaceAll("1", "a").replaceAll("9", "f").getBytes());
        return "http://v3.esud.uz/crm/download?t="+url;
    }

    public void updateFilePath(Long caseId,Long judgementId,String filePath){
        String updateSQL="UPDATE db_case SET file_path=:filePath WHERE case_id=:caseId AND judgement_id=:judgementId;";
        template.update(updateSQL, ReportService.parametersMap(new String[]{"filePath","caseId","judgementId"},new Object[]{filePath,caseId,judgementId}));
    }


}
