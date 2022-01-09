package undp.judgment.list.judgment.security.models;

import org.springframework.security.core.GrantedAuthority;


public class Role implements GrantedAuthority {

    private Integer roleId;

    private String authority;

    public Role() {
    }

    public Role(Integer roleId, String authority) {
        this.roleId = roleId;
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
