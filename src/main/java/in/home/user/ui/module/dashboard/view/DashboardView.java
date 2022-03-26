package in.home.user.ui.module.dashboard.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import in.home.user.ui.module.framework.applayout.MainLayout;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@UIScope
@SpringComponent
@Route(value = DashboardView.ROUTE, layout = MainLayout.class)
@RouteAlias(value = "dashboard")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DashboardView extends Div {

    public static final String ROUTE = "";
    public static final String NAVIGATION_TAB_NAME = "Dashboard";

    public DashboardView() {
        add(new Span("DashboardView view content"));
    }
}
