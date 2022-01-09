package undp.judgment.list.judgment.services;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import undp.judgment.list.judgment.models.attributes.AttributeEntity;

import javax.sql.DataSource;
import java.util.List;


public class BlockedService {

    private NamedParameterJdbcTemplate template;
    private String block_sql="select *from(select courtid_ id, " +
            "  (select name_ from courtt where courtt.id_=cbr.courtid_) attributeName, " +
            "  usert.name_ attributeValue, " +
            "  blockeddate_ fromdate, " +
            "  (select min(blockeddate_) from court_block_reason where courtid_ = cbr.courtid_ and userid_ = cbr.userid_ and blockeddate_ >=cbr.blockeddate_ and blockreason_=0) toDate, " +
            "  (select nvl(w.name_, s.name_) from spravT s, wordT w where s.typeid_ = 528 and s.id_ = w.id_ and w.langid_ = 1 and w.typeid_ = 3 and s.keyid_ = cbr.blockreason_) reasonValue " +
            "  from court_block_reason cbr inner join usert on cbr.userid_=usert.id_ where usert.datetermination_ is null and blockreason_ > 0 and courtid_ not in(1,99)) a order by toDate desc;";
    private  BlockedService() {
        DataSource dataSource= DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://192.168.88.131:5432/esud")
                .username("esud")
                .password("xB5GM7Db34")
                .build();
        template=new NamedParameterJdbcTemplate(dataSource);
    }
    private static BlockedService blockedService;
    public static BlockedService newInstance(){
        if(blockedService==null){
            blockedService=new BlockedService();
        }
        return blockedService;
    }


    @Transactional
    public List<AttributeEntity> findAll() {
        return template.query(block_sql,new MapSqlParameterSource(),
                ((resultSet, i) ->
                {

                    AttributeEntity<String> attr=new AttributeEntity<String>(
                            resultSet.getInt("id"),
                            resultSet.getString("attributeName"),
                            resultSet.getString("attributeValue"),
                            resultSet.getTimestamp("fromDate"),
                            resultSet.getTimestamp("toDate"),
                            resultSet.getString("reasonValue"));
                    return attr;})
        );
    }
}
