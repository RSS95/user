package in.home.user.ui.module.user.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import in.home.user.ui.module.framework.applayout.MainLayout;
import in.home.user.ui.module.user.component.UserGrid;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@UIScope
@SpringComponent
@Route(value = UserView.ROUTE, layout = MainLayout.class)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserView extends Div {

  public static final String ROUTE = "user";
  public static final String NAVIGATION_TAB_NAME = "User";

  @Autowired private UserGrid userGrid;

  @PostConstruct
  private void init(){
    add(this.userGrid);
    setSizeFull();
  }
}
