package in.home.user.ui.module.user.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import in.home.user.api.model.User;
import in.home.user.ui.module.common.model.Mode;
import in.home.user.ui.module.framework.applayout.MainLayout;
import in.home.user.ui.module.user.component.UserDialog;
import in.home.user.ui.module.user.component.UserGrid;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

@UIScope
@SpringComponent
@Route(value = UserView.ROUTE, layout = MainLayout.class)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserView extends Div {

  public static final String ROUTE = "user";
  public static final String NAVIGATION_TAB_NAME = "User";

  private ApplicationContext applicationContext;
  private UserGrid userGrid;

  @PostConstruct
  private void init() {
    VerticalLayout layout = new VerticalLayout();
    layout.setSizeFull();
    layout.add(getMenuBar());
    layout.add(this.userGrid);
    add(layout);
    setSizeFull();
  }

  private MenuBar getMenuBar() {
    MenuBar menuBar = new MenuBar();
    menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY);

    menuBar.addItem("Add User", event -> openUserDialog(new User(), Mode.ADD));

    return menuBar;
  }

  private void openUserDialog(User user, Mode mode) {
    UserDialog userDialog = applicationContext.getBean(UserDialog.class);
    userDialog.addSaveListener(event -> this.userGrid.refreshAllGrid());
    userDialog.open(user, mode);
  }

  @Autowired
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Autowired
  public void setUserGrid(UserGrid userGrid) {
    this.userGrid = userGrid;
  }
}
