package in.home.user.ui.module.user.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
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
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import in.home.user.api.model.Role;
import in.home.user.api.model.User;
import in.home.user.ui.feign.client.RoleClient;
import in.home.user.ui.feign.client.UserClient;
import in.home.user.ui.module.common.model.Mode;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.vaadin.gatanaso.MultiselectComboBox;

@SpringComponent
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserDialog extends Dialog {

  private static final String DEFAULT_PASSWORD = "0000";

  private final UserClient userClient;
  private final RoleClient roleClient;
  private final Binder<User> binder = new Binder<>();
  private final VerticalLayout dialogLayout = new VerticalLayout();
  private final HorizontalLayout headerLayout = new HorizontalLayout();
  private final Scroller scrollerBody = new Scroller();
  private final HorizontalLayout footerLayout = new HorizontalLayout();
  private BCryptPasswordEncoder passwordEncoder;
  private List<ComponentEventListener<DialogUserSaveEvent>> userSaveEventListenerList =
      new ArrayList<>();

  @Autowired
  public UserDialog(
      UserClient userClient, RoleClient roleClient, BCryptPasswordEncoder passwordEncoder) {
    this.userClient = userClient;
    this.roleClient = roleClient;
    this.passwordEncoder = passwordEncoder;
  }

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
    setHeight("500px");
    setWidth("400px");
  }

  public void open(User user, Mode mode) {
    addTitle(mode);
    addBody(user, mode);
    addFooterButtons(mode);
    open();
  }

  private void addBody(User user, Mode mode) {
    TextField userName = new TextField();
    userName.setLabel("UserName");
    userName.setWidthFull();
    userName.setClearButtonVisible(true);
    this.binder
        .forField(userName)
        .asRequired("Please enter username")
        .bind(User::getUserName, User::setUserName);

    EmailField email = new EmailField();
    email.setLabel("Email");
    email.setWidthFull();
    email.setClearButtonVisible(true);
    this.binder
        .forField(email)
        .asRequired("Please enter an email for user")
        .withValidator(new EmailValidator("Incorrect E-Mail"))
        .bind(User::getEmail, User::setEmail);

    MultiselectComboBox<Role> roles = new MultiselectComboBox<>();
    roles.setLabel("Role");
    roles.setItemLabelGenerator(Role::getRoleName);
    roles.setItems(roleClient.findAll());

    this.binder
        .forField(roles)
        .asRequired("Please select a role for user")
        .bind(User::getRoleSet, User::setRoleSet);

    VerticalLayout bodyLayout = new VerticalLayout();
    bodyLayout.setSizeFull();
    bodyLayout.setPadding(false);
    bodyLayout.add(userName, email, roles);

    this.scrollerBody.setContent(bodyLayout);
    this.scrollerBody.setSizeFull();
    this.scrollerBody.setScrollDirection(ScrollDirection.VERTICAL);

    this.binder.setBean(user);
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

  private void addTitle(Mode mode) {
    if (Mode.ADD.equals(mode)) {
      this.headerLayout.add(new Label("Add User"));
    } else if (Mode.EDIT.equals(mode)) {
      this.headerLayout.add(new Label("Edit User"));
    } else {
      this.headerLayout.add(new Label("View User"));
    }
  }

  private void addFooterButtons(Mode mode) {
    if (Mode.ADD.equals(mode) || Mode.EDIT.equals(mode)) {
      Button saveButton = new Button("Save", event -> saveUser());
      saveButton.setDisableOnClick(true);
      saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

      Button saveAndAddButton = new Button("Save and Add", event -> saveAndAddUser());
      saveAndAddButton.setDisableOnClick(true);
      saveAndAddButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

      HorizontalLayout otherButton = new HorizontalLayout();
      otherButton.setWidthFull();
      otherButton.setJustifyContentMode(JustifyContentMode.START);
      otherButton.add(saveButton, saveAndAddButton);

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

  private void saveAndAddUser() {
    save();
    this.binder.setBean(new User());
  }

  private void saveUser() {
    save();
    this.close();
  }

  private void save() {
    if (this.binder.isValid()) {
      User bean = this.binder.getBean();
      bean.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
      if(bean.getCreatedOn() == null){
        bean.setCreatedBy(getLoggedInUserId());
      }
      bean.setUpdatedBy(getLoggedInUserId());
      User savedUser = this.userClient.save(bean);
      showNotification("User(" + savedUser.getUserName() + ") saved successfully");
      userSaveEventListenerList.forEach(
          listener -> listener.onComponentEvent(new DialogUserSaveEvent(this, false, savedUser)));
    } else {
      showNotification("Please enter all valid fields");
    }
  }

  private Long getLoggedInUserId() {
    UserDetails loggedInUserDetails =
        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User loggedInUser = userClient.loadUserByUsernameWithoutPassword(loggedInUserDetails.getUsername());
    return loggedInUser.getId();
  }

  private void showNotification(String message) {
    Notification.show(message, 2500, Position.MIDDLE);
  }

  public Registration addSaveListener(ComponentEventListener<DialogUserSaveEvent> listener) {
    return Registration.addAndRemove(userSaveEventListenerList, listener);
  }

  public class DialogUserSaveEvent extends ComponentEvent<Component> {
    User savedUser;

    public DialogUserSaveEvent(Component source, boolean fromClient, User savedUser) {
      super(source, fromClient);
      this.savedUser = savedUser;
    }
  }
}
