package in.home.user.ui.view.user;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import in.home.user.api.model.Role;
import in.home.user.api.model.User;
import in.home.user.ui.security.client.UserClient;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.stream.Collectors;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@UIScope
@SpringComponent
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Route(value = "user-list", layout = UserRootLayout.class)
public class UserListView extends VerticalLayout {

  private UserClient userClient;

  public UserListView(UserClient userClient) {
    this.userClient = userClient;
    add(new Span("UserListView view content"));
    Button gotoTestView = new Button("GO TO TEST VIEW");
    gotoTestView.addClickListener(event -> getUI().get().navigate(TestView.class));
    add(gotoTestView);
    add(getUserGridComponent());
    setSizeFull();
  }

  private Grid<User> getUserGridComponent() {
    Grid<User> userGrid = new Grid<>();
    //    userGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
    userGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
    userGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    userGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    //    userGrid.addThemeVariants(GridVariant.MATERIAL_COLUMN_DIVIDERS);
    userGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    //    userGrid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS);
    //    userGrid.setHeightByRows(true);
    userGrid.setHeightFull();

    userGrid.addColumn(User::getId).setHeader("ID");
    userGrid.addColumn(User::getUserName).setHeader("User Name");
    userGrid.addColumn(User::getEmail).setHeader("Email ID");
    userGrid
        .addColumn(
            user ->
                user.getRoleList().stream()
                    .sorted(Comparator.comparing(Role::getCreatedOn))
                    .map(Role::getRoleName)
                    .collect(Collectors.joining(", ")))
        .setHeader("Roles");
    userGrid.addColumn(User::isActive).setHeader("Active");
    userGrid.addColumn(User::getCreatedBy).setHeader("Created By ID");
    userGrid
        .addColumn(
            new LocalDateTimeRenderer<>(
                User::getCreatedOn,
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM)))
        .setHeader("Created On");
    userGrid.addColumn(User::getUpdatedBy).setHeader("Updated By ID");
    userGrid
        .addColumn(
            new LocalDateTimeRenderer<>(
                User::getUpdatedOn,
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM)))
        .setHeader("Updated On");

    userGrid.setMultiSort(true);
    userGrid.getColumns().stream()
        .peek(userColumn -> userColumn.setSortable(true))
        .forEach(userColumn -> userColumn.setAutoWidth(true));

    userGrid.setItems(userClient.findAll());

    userGrid.appendFooterRow();
    userGrid
        .appendFooterRow()
        .join(userGrid.getColumns().toArray(new Column[] {}))
        .setComponent(new Label("Total: " + 10 + " people"));

    return userGrid;
  }
}
