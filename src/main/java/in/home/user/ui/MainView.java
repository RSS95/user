package in.home.user.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.Route;
import lombok.Value;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A sample Vaadin view class.
 *
 * <p>To implement a Vaadin view just extend any Vaadin component and use @Route annotation to
 * announce it in a URL as a Spring managed bean.
 *
 * <p>A new instance of this class is created for every new user and every browser tab/window.
 *
 * <p>The main view contains a text field for getting the user name and a button that shows a
 * greeting message in a notification.
 */
@Route
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

  public MainView(@Autowired MessageBean bean) {
    Button button = new Button("Click me", e -> Notification.show(bean.getMessage()));
    add(button);

    Button logout = new Button("Logout", e -> UI.getCurrent().getPage().setLocation("/logout"));
    add(logout);
  }
}
