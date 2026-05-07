package web_prak.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import web_prak.models.Client;
import web_prak.models.Manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class ClientUserDetails implements UserDetails {
    private final Client client;
    private final Manager manager;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var l = new ArrayList<SimpleGrantedAuthority>();
        l.add(new SimpleGrantedAuthority("ROLE_USER"));
        if(manager != null)
            l.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
        return l;
    }

    @Override
    public String getPassword() {
        return client == null? manager.getPasswordHash() : client.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return client == null? manager.getEmail() : client.getEmail();
    }

    public Client getClient() {
        return client;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    public Manager getManager() {
        return manager;
    }
}
