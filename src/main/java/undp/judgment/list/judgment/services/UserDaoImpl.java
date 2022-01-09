package undp.judgment.list.judgment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undp.judgment.list.judgment.security.models.Role;
import undp.judgment.list.judgment.security.models.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserDaoImpl {

    private final NamedParameterJdbcTemplate template;
    private final BCryptPasswordEncoder encoder;
    @Autowired
    public UserDaoImpl(NamedParameterJdbcTemplate template,BCryptPasswordEncoder passwordEncoder) {
        this.template = template;
        this.encoder=passwordEncoder;
    }

    @Transactional
    public Optional<User> findByUsername(String username){
        String userSQL="select db_user.* , (select role_name from db_role where id=role_id) role_name from db_user where user_login = :username;";
        return template.queryForObject(userSQL,new MapSqlParameterSource("username",username),new UserRowMapper());
    }

    public boolean canUploadFile(Long caseId, Long judgementId, Integer userId) {
        Map params=new HashMap<String,Object>();
        params.put("caseId",caseId);
        params.put("judgementId",judgementId);
        Integer id=template.queryForObject("select judge_id from db_case where case_id=:caseId and judgement_id=:judgementId;",new MapSqlParameterSource(params), Integer.class);
        return userId.equals(id);
    }
    @Transactional
    public void updateFilePath(Long caseId,Long judgementId,String filePath){
        String updateSQL="UPDATE db_case SET file_path=:filePath WHERE case_id=:caseId AND judgement_id=:judgementId;";
        template.update(updateSQL, ReportService.parametersMap(new String[]{"filePath","caseId","judgementId"},new Object[]{filePath,caseId,judgementId}));
    }

    class UserRowMapper implements RowMapper<Optional<User>>{
        @Override
        public Optional<User> mapRow(ResultSet resultSet, int i) throws SQLException {
            User user=new User();
            user.setUserId(resultSet.getInt("id"));
            user.setUsername(resultSet.getString("user_login"));
            user.setPassword(encoder.encode(resultSet.getString("user_password")));
            user.setFullName(resultSet.getString("full_name"));
            user.setEnabled(!resultSet.getBoolean("active_user"));
            Role role=new Role(resultSet.getInt("role_id"),resultSet.getString("role_name"));
            user.setAuthority(role);
            return Optional.of(user);
        }
    }
}
