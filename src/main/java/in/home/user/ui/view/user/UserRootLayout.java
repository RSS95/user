package in.home.user.ui.view.user;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import in.home.user.ui.view.MainLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Slf4j
@UIScope
@SpringComponent
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RoutePrefix(value = "user")
@ParentLayout(value = MainLayout.class)
public class UserRootLayout extends AppLayout implements BeforeEnterObserver {

  private final Map<Class<? extends Component>, Tab> navigationTargetToTab = new HashMap<>();
  private final Tabs tabs = new Tabs();
  private final VerticalLayout content = new VerticalLayout();

  public UserRootLayout() {
    addMenuTab("All Users", UserListView.class);
    addMenuTab("User View", UserView.class);

    tabs.setOrientation(Orientation.HORIZONTAL);
    addToNavbar(tabs);

    VerticalLayout layout = new VerticalLayout();
    layout.setSizeFull();
    content.setSizeFull();
    layout.add(tabs, content);

    setContent(layout);
  }

  private void addMenuTab(String label, Class<? extends Component> target) {
    Tab tab = new Tab(new RouterLink(label, target));
    navigationTargetToTab.put(target, tab);
    tabs.add(tab);
  }

  @Override
  public void showRouterLayoutContent(HasElement hasElement) {
    log.debug("showRouterLayoutContent - UserRootLayout");
    Objects.requireNonNull(hasElement);
    Objects.requireNonNull(hasElement.getElement());
    content.removeAll();
    content.getElement().appendChild(hasElement.getElement());
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    log.debug("user root tab change");
    tabs.setSelectedTab(navigationTargetToTab.get(event.getNavigationTarget()));
    getParent()
        .ifPresent(
            mainLayout ->
                ((MainLayout) mainLayout)
                    .addMenuTab("User", (Class<? extends Component>) event.getNavigationTarget()));
  }
}
