package in.home.user.ui.module.role.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import in.home.user.api.model.Role;
import in.home.user.ui.feign.client.RoleClient;
import in.home.user.ui.module.common.model.Mode;
import in.home.user.ui.module.framework.applayout.MainLayout;
import in.home.user.ui.module.framework.component.util.ButtonUtil;
import in.home.user.ui.module.role.component.RoleDialog;
import in.home.user.ui.module.role.component.RoleGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.Optional;

@UIScope
@SpringComponent
@Route(value = RoleView.ROUTE, layout = MainLayout.class)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RoleView extends Div {

    public static final String ROUTE = "role";
    public static final String NAVIGATION_TAB_NAME = "Role";

    private transient ApplicationContext applicationContext;
    private transient RoleClient roleClient;

    private RoleGrid roleGrid;
    private MenuItem activateDeactivateMenuItem;
    private Button activateDeactivateButton;
    private Button activateButton;
    private Button deactivateButton;

    @PostConstruct
    private void init() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.add(getMenuBar());
        layout.add(this.roleGrid);
        add(layout);
        setSizeFull();
        addGridSelectionListener();
    }

    private MenuBar getMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY);

        menuBar.addItem(
                ButtonUtil.getPrimaryButton("Add", event -> openRoleDialog(new Role(), Mode.ADD)));
        menuBar.addItem(ButtonUtil.getDefaultButton("Edit", event -> {
            Optional<Role> roleOp = this.roleGrid.getSelectedRole();
            if (roleOp.isPresent()) {
                openRoleDialog(roleOp.get(), Mode.EDIT);
            }
            else {
                showNotification("Select record from the grid");
            }
        }));
        menuBar.addItem(ButtonUtil.getErrorButton("Delete", event -> {
            Optional<Role> roleOp = this.roleGrid.getSelectedRole();
            if (roleOp.isPresent()) {
                this.roleClient.delete(roleOp.get().getId());
                this.roleGrid.refreshAllGrid();
                showNotification("Role (" + roleOp.get().getRoleName() + ") deleted successfully");
            }
            else {
                showNotification("Select record from the grid");
            }
        }));
        this.activateDeactivateMenuItem = menuBar.addItem(getActivateDeactivateButton());

        return menuBar;
    }

    private void openRoleDialog(Role role, Mode mode) {
        RoleDialog roleDialog = applicationContext.getBean(RoleDialog.class);
        roleDialog.addSaveListener(event -> this.roleGrid.refreshAllGrid());
        roleDialog.open(role, mode);
    }

    private void showNotification(String message) {
        Notification.show(message, 2500, Position.MIDDLE);
    }

    private void addGridSelectionListener() {
        this.roleGrid.addGridSelectionListener(event -> {
            this.activateDeactivateMenuItem.removeAll();
            if (event.getFirstSelectedItem().isPresent()) {
                if (event.getFirstSelectedItem().get().isActive()) {
                    this.activateDeactivateMenuItem.add(getDeactivateButton());
                }
                else {
                    this.activateDeactivateMenuItem.add(getActivateButton());
                }
            }else {
                this.roleGrid.deselectAll();
                this.activateDeactivateMenuItem.add(getActivateDeactivateButton());
            }
        });
    }

    private Button getActivateDeactivateButton() {
        if (this.activateDeactivateButton == null) {
            this.activateDeactivateButton = ButtonUtil.getErrorSecondaryButton(
                    "Activate/Deactivate",
                    event -> showNotification("Select record from the grid"));
        }
        return this.activateDeactivateButton;
    }

    private Button getActivateButton() {
        if (this.activateButton == null) {
            this.activateButton = ButtonUtil.getDefaultButton("Activate", clickEvent -> {
                this.roleGrid.getSelectedRole()
                             .ifPresent(role -> {
                                 this.roleClient.activate(role.getId());
                                 this.roleGrid.refreshAllGrid();
                             });
            });
        }
        return this.activateButton;
    }

    private Button getDeactivateButton() {
        if (this.deactivateButton == null) {
            this.deactivateButton = ButtonUtil.getErrorSecondaryButton("Deactivate", clickEvent -> {
                this.roleGrid.getSelectedRole()
                             .ifPresent(role -> {
                                 this.roleClient.deactivate(role.getId());
                                 this.roleGrid.refreshAllGrid();
                             });
            });
        }
        return this.deactivateButton;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setRoleGrid(RoleGrid roleGrid) {
        this.roleGrid = roleGrid;
    }

    @Autowired
    public void setRoleClient(RoleClient roleClient) {
        this.roleClient = roleClient;
    }
}
