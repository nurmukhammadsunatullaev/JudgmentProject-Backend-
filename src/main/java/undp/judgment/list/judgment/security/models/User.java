package undp.judgment.list.judgment.security.models;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


import com.google.common.collect.ImmutableSet;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails {

    private Integer userId;

    @NotBlank
    @Size(min=3, max = 50)
    private String fullName;

    @NotBlank
    @Size(min=3, max = 50)
    private String username;

    @NotBlank
    @Size(min=6, max = 100)
    private String password;

    private boolean enabled;

    private Role authority;

    public User(){

    }

    public User(@NotBlank @Size(min = 3, max = 50) String fullName, @NotBlank @Size(min = 3, max = 50) String username, @NotBlank @Size(min = 6, max = 100) String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Set<Role> getAuthorities() {
        return ImmutableSet.of(authority);
    }

    public Role getAuthority() {
        return authority;
    }

    public void setAuthority(Role authority) {
        this.authority = authority;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return username+"\t" +password+ "\t" +fullName + "\t" +enabled +"\n";
    }
}
