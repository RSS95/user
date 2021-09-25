package in.home.user.ui.view.user;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@UIScope
@SpringComponent
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Route(value = "user-view", layout = UserRootLayout.class)
public class UserView extends Div {
  public UserView() {
    add(new Span("UserView view content"));
  }
}
