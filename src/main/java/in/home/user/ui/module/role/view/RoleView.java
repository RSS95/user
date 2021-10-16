package in.home.user.ui.module.role.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import in.home.user.ui.module.framework.applayout.MainLayout;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@UIScope
@SpringComponent
@Route(value = RoleView.ROUTE, layout = MainLayout.class)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RoleView extends Div {

  public static final String ROUTE = "role";
  public static final String NAVIGATION_TAB_NAME = "Role";

  public RoleView() {
    add(new Span("RoleView view content"));
  }
}
