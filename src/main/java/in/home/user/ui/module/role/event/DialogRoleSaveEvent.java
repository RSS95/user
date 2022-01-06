package in.home.user.ui.module.role.event;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import in.home.user.api.model.Role;

public class DialogRoleSaveEvent extends ComponentEvent<Component> {

    Role savedRole;

    public DialogRoleSaveEvent(Component source, boolean fromClient, Role savedRole) {
        super(source, fromClient);
        this.savedRole = savedRole;
    }
}
