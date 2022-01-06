package in.home.user.ui.module.role.component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.Scroller.ScrollDirection;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import in.home.user.api.model.Role;
import in.home.user.api.model.User;
import in.home.user.ui.feign.client.RoleClient;
import in.home.user.ui.feign.client.UserClient;
import in.home.user.ui.module.common.model.Mode;
import in.home.user.ui.module.role.event.DialogRoleSaveEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringComponent
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RoleDialog extends Dialog {

    private transient UserClient userClient;
    private transient RoleClient roleClient;

    private final Binder<Role> binder = new Binder<>();
    private final VerticalLayout dialogLayout = new VerticalLayout();
    private final HorizontalLayout headerLayout = new HorizontalLayout();
    private final Scroller scrollerBody = new Scroller();
    private final HorizontalLayout footerLayout = new HorizontalLayout();
    private final List<ComponentEventListener<DialogRoleSaveEvent>> roleSaveEventListenerList
            = new ArrayList<>();

    private Mode mode;

    @PostConstruct
    private void init() {
        this.dialogLayout.setPadding(false);
        this.dialogLayout.setSizeFull();

        addDialogHeader();
        addDialogBody();
        addDialogFooter();

        add(dialogLayout);
        setCloseOnOutsideClick(false);
        setResizable(true);
        setDraggable(true);
        setHeight("250px");
        setWidth("400px");
    }

    public void open(Role role, Mode mode) {
        this.mode = mode;
        addTitle(mode);
        addBody(role);
        addFooterButtons(mode);
        open();
    }

    private void addTitle(Mode mode) {
        if (Mode.ADD.equals(mode)) {
            this.headerLayout.add(new Label("Add Role"));
        }
        else if (Mode.EDIT.equals(mode)) {
            this.headerLayout.add(new Label("Edit Role"));
        }
        else {
            this.headerLayout.add(new Label("View Role"));
        }
    }

    private void addBody(Role role) {
        TextField userName = new TextField();
        userName.setLabel("RoleName");
        userName.setWidthFull();
        userName.setClearButtonVisible(true);
        this.binder.forField(userName)
                   .asRequired("Please enter role name")
                   .bind(Role::getRoleName, Role::setRoleName);

        VerticalLayout bodyLayout = new VerticalLayout();
        bodyLayout.setSizeFull();
        bodyLayout.setPadding(false);
        bodyLayout.add(userName);

        this.scrollerBody.setContent(bodyLayout);
        this.scrollerBody.setSizeFull();
        this.scrollerBody.setScrollDirection(ScrollDirection.VERTICAL);

        this.binder.setBean(role);
    }

    private void addFooterButtons(Mode mode) {
        if (Mode.ADD.equals(mode) || Mode.EDIT.equals(mode)) {
            Button saveButton = new Button("Save", event -> saveRole());
            saveButton.setDisableOnClick(true);
            saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            HorizontalLayout otherButton = new HorizontalLayout();
            otherButton.setWidthFull();
            otherButton.setJustifyContentMode(JustifyContentMode.START);
            otherButton.add(saveButton);

            if (Mode.ADD.equals(mode)) {
                Button saveAndAddButton = new Button("Save and Add New", this::saveAndAddRole);
                saveAndAddButton.setDisableOnClick(true);
                saveAndAddButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

                otherButton.add(saveAndAddButton);
            }

            this.footerLayout.add(otherButton);
        }

        HorizontalLayout closeButtonLayout = new HorizontalLayout();
        closeButtonLayout.setAlignSelf(Alignment.END);

        Button closeButton = new Button("Cancel", event -> this.close());
        closeButton.setDisableOnClick(true);
        closeButtonLayout.add(closeButton);

        this.footerLayout.add(closeButtonLayout);
        this.footerLayout.setWidthFull();
        this.footerLayout.setJustifyContentMode(JustifyContentMode.START);
    }

    private void saveAndAddRole(ClickEvent<Button> event) {
        save();
        this.binder.setBean(new Role());
        event.getSource().setEnabled(true);
    }

    private void saveRole() {
        save();
        this.close();
    }

    private void save() {
        if (this.binder.isValid()) {
            Role bean = this.binder.getBean();
            if (bean.getCreatedOn() == null) {
                bean.setCreatedBy(getLoggedInUserId());
            }
            bean.setUpdatedBy(getLoggedInUserId());
            if (Mode.ADD.equals(this.mode)) {
                bean.setActive(true);
            }
            Role savedRole = this.roleClient.save(bean);
            showNotification("Role (" + savedRole.getRoleName() + ") saved successfully");
            roleSaveEventListenerList.forEach(listener -> listener.onComponentEvent(
                    new DialogRoleSaveEvent(this, false, savedRole)));
        }
        else {
            showNotification("Please enter all valid fields");
        }
    }

    private Long getLoggedInUserId() {
        UserDetails loggedInUserDetails = (UserDetails) SecurityContextHolder.getContext()
                                                                             .getAuthentication()
                                                                             .getPrincipal();
        User loggedInUser = userClient.loadUserByUsernameWithoutPassword(
                loggedInUserDetails.getUsername());
        return loggedInUser.getId();
    }

    private void showNotification(String message) {
        Notification.show(message, 2500, Position.MIDDLE);
    }

    public Registration addSaveListener(ComponentEventListener<DialogRoleSaveEvent> listener) {
        return Registration.addAndRemove(roleSaveEventListenerList, listener);
    }

    private void addDialogHeader() {
        this.headerLayout.setWidthFull();
        this.dialogLayout.addComponentAtIndex(0, this.headerLayout);
    }

    private void addDialogBody() {
        this.scrollerBody.setHeightFull();
        this.dialogLayout.addComponentAtIndex(1, this.scrollerBody);
    }

    private void addDialogFooter() {
        this.footerLayout.setWidthFull();
        this.dialogLayout.addComponentAtIndex(2, this.footerLayout);
    }

    @Autowired
    public void setRoleClient(RoleClient roleClient) {
        this.roleClient = roleClient;
    }

    @Autowired
    public void setUserClient(UserClient userClient) {
        this.userClient = userClient;
    }
}
