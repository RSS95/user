package in.home.user.ui.module.framework.applayout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import in.home.user.ui.module.dashboard.view.DashboardView;
import in.home.user.ui.module.role.view.RoleView;
import in.home.user.ui.module.user.view.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

@UIScope
@SpringComponent
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Theme(value = Lumo.class, variant = Lumo.DARK)
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

    private VerticalLayout getNavBarRightLayout() {
        VerticalLayout logoutLayout = new VerticalLayout();
        logoutLayout.setSizeFull();
        // logoutLayout.setAlignItems(Alignment.END);
        // Button logout =
        //     new Button(LOGOUT, e -> UI.getCurrent().getPage().setLocation(LoginView
        //     .LOGOUT_ROUTE));
        // logout.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        // logout.getStyle().set("margin", "unset");
        // logout.getStyle().set("margin-top", "var(--lumo-space-m)");
        // logout.setDisableOnClick(true);
        // logoutLayout.add(logout);
        // logoutLayout.getStyle().set("padding-top", "unset");
        // logoutLayout.getStyle().set("padding-bottom", "unset");
        return logoutLayout;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (navigationTargetToTab.get(event.getNavigationTarget()) != null && tabs.indexOf(navigationTargetToTab.get(
                event.getNavigationTarget())) != -1) {
            tabs.setSelectedTab(navigationTargetToTab.get(event.getNavigationTarget()));
        }
        else {
            tabs.setSelectedTab(tabs.getSelectedTab());
        }
    }
}
