package in.home.user.ui.module.framework.applayout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import in.home.user.ui.module.dashboard.view.DashboardView;
import in.home.user.ui.module.login.view.LoginView;
import in.home.user.ui.module.role.view.RoleView;
import in.home.user.ui.module.user.view.UserView;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@UIScope
@SpringComponent
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MainLayout extends AppLayout implements BeforeEnterObserver {

  public static final String APPLICATION_NAME = "H:user";
  private static final String LOGOUT = "Logout";

  private final Tabs tabs = new Tabs();
  private final Map<Class<? extends Component>, Tab> navigationTargetToTab = new HashMap<>();

  @Autowired
  public MainLayout() {
    addMenuTab(DashboardView.NAVIGATION_TAB_NAME, DashboardView.class);
    addMenuTab(UserView.NAVIGATION_TAB_NAME, UserView.class);
    addMenuTab(RoleView.NAVIGATION_TAB_NAME, RoleView.class);
    tabs.setOrientation(Tabs.Orientation.VERTICAL);
    addToDrawer(tabs);
    addToNavbar(new DrawerToggle(), new Label(APPLICATION_NAME), getNavBarRightLayout());
  }

  public void addMenuTab(String label, Class<? extends Component> target) {
    Tab tab = new Tab(new RouterLink(label, target));
    navigationTargetToTab.put(target, tab);
    tabs.add(tab);
  }

  private FlexLayout getNavBarRightLayout() {
    FlexLayout logoutLayout = new FlexLayout();
    logoutLayout.setSizeFull();
    logoutLayout.setJustifyContentMode(JustifyContentMode.END);
    logoutLayout.setAlignItems(Alignment.END);
    Button logout =
        new Button(LOGOUT, e -> UI.getCurrent().getPage().setLocation(LoginView.LOGOUT_ROUTE));
    logout.setDisableOnClick(true);
    logoutLayout.add(logout);
    return logoutLayout;
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (navigationTargetToTab.get(event.getNavigationTarget()) != null
        && tabs.indexOf(navigationTargetToTab.get(event.getNavigationTarget())) != -1) {
      tabs.setSelectedTab(navigationTargetToTab.get(event.getNavigationTarget()));
    } else {
      tabs.setSelectedTab(tabs.getSelectedTab());
    }
  }
}
