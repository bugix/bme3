package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.UserAccessRights;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.UserAccessRights")
@Component
public class UserAccessRightsLocator extends Locator<UserAccessRights, Long> {

    public UserAccessRights create(Class<? extends medizin.server.domain.UserAccessRights> clazz) {
        return new UserAccessRights();
    }

    public UserAccessRights find(Class<? extends medizin.server.domain.UserAccessRights> clazz, Long id) {
        return UserAccessRights.findUserAccessRights(id);
    }

    public Class<medizin.server.domain.UserAccessRights> getDomainType() {
        return UserAccessRights.class;
    }

    public Long getId(UserAccessRights userAccessRights) {
        return userAccessRights.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(UserAccessRights userAccessRights) {
        return userAccessRights.getVersion();
    }
}
