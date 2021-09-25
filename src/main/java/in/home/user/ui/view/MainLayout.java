package in.home.user.ui.view;

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
import in.home.user.ui.view.dashboard.DashboardView;
import in.home.user.ui.view.user.UserListView;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@UIScope
@SpringComponent
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MainLayout extends AppLayout implements BeforeEnterObserver {

  private final Tabs tabs = new Tabs();
  private final Map<Class<? extends Component>, Tab> navigationTargetToTab = new HashMap<>();
  private Map<String, Tab> tabMap = new HashMap<>();

  @Autowired
  public MainLayout() {
    addMenuTab("Dashboard", DashboardView.class);
    addMenuTab("User", UserListView.class);
    tabs.setOrientation(Tabs.Orientation.VERTICAL);
    addToDrawer(tabs);
    addToNavbar(new DrawerToggle(), new Label("H:user"), getNavBarRightLayout());
  }

  public void addMenuTab(String label, Class<? extends Component> target) {
    Tab oldTab = tabMap.get(label);
    if (oldTab != null) {
      ((RouterLink) oldTab.getChildren().findFirst().get()).setRoute(target);
    } else {
      Tab tab = new Tab(new RouterLink(label, target));
      tabMap.put(label, tab);
      navigationTargetToTab.put(target, tab);
      tabs.add(tab);
    }
  }

  private FlexLayout getNavBarRightLayout() {
    FlexLayout logoutLayout = new FlexLayout();
    logoutLayout.setSizeFull();
    logoutLayout.setJustifyContentMode(JustifyContentMode.END);
    logoutLayout.setAlignItems(Alignment.END);
    Button logout = new Button("Logout", e -> UI.getCurrent().getPage().setLocation("/logout"));
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
