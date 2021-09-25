package in.home.user.ui.view.dashboard;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import in.home.user.ui.view.MainLayout;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@UIScope
@SpringComponent
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Route(value = "", layout = MainLayout.class)
public class DashboardView extends Div {
  public DashboardView() {
    add(new Span("DashboardView view content"));
  }
}
