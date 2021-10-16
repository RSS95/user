package in.home.user.ui.configuration;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import in.home.user.ui.module.login.view.LoginView;
import org.springframework.stereotype.Component;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

  @Override
  public void serviceInit(ServiceInitEvent event) {
    event
        .getSource()
        .addUIInitListener(
            uiEvent -> {
              final UI ui = uiEvent.getUI();
              ui.addBeforeEnterListener(this::beforeEnter);
            });
  }

  /**
   * Reroutes the user if (s)he is not authorized to access the view.
   *
   * @param event before navigation event with event details
   */
  private void beforeEnter(BeforeEnterEvent event) {
    if (!LoginView.class.equals(event.getNavigationTarget()) && !SecurityUtils.isUserLoggedIn()) {
      event.rerouteTo(LoginView.class);
    }
  }
}

// @formatter:on
// eg 1
// Component findComponentWithId(HasComponents root, String id) {
//    for(Component child : root) {
//        if(id.equals(child.getId())) {
//            // found it!
//            return child;
//        } else if(child instanceof HasComponents) {
//            // recursively go through all children that themselves have children
//            Component result = findComponentWithId((HasComponents) child, id);
//            if(result != null){
//                return result;
//            }
//        }
//    }
//    // none was found
//    return null;
// }
// eg 2
// class FindComponent {
//    public Component findById(HasComponents root, String id) {
//        System.out.println("findById called on " + root);
//
//        Iterator<Component> iterate = root.iterator();
//        while (iterate.hasNext()) {
//            Component c = iterate.next();
//            if (id.equals(c.getId())) {
//                return c;
//            }
//            if (c instanceof HasComponents) {
//                Component cc = findById((HasComponents) c, id);
//                if (cc != null)
//                    return cc;
//            }
//        }
//
//        return null;
//    }
// }
//
// FindComponent fc = new FindComponent();
// Component myComponent = fc.findById(blueMainLayout, "azerty");
// @formatter:off
