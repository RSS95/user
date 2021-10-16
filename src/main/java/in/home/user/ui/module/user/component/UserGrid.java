package in.home.user.ui.module.user.component;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.spring.annotation.SpringComponent;
import in.home.user.api.model.Role;
import in.home.user.api.model.User;
import in.home.user.ui.feign.client.UserClient;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserGrid extends Div {

  private Grid<User> grid = new Grid<>();

  @Autowired private UserClient userClient;

  @PostConstruct
  private void init() {
    initGrid();
    add(grid);
    setSizeFull();
  }

  private void initGrid() {
    //    style grid
    grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    //    grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    grid.setHeightFull();

    // defining columns
    grid.addColumn(User::getId).setHeader("ID");
    grid.addColumn(User::getUserName).setHeader("User Name");
    grid.addColumn(User::getEmail).setHeader("Email ID");
    grid.addColumn(
            user ->
                user.getRoleList().stream()
                    .sorted(Comparator.comparing(Role::getCreatedOn))
                    .map(Role::getRoleName)
                    .collect(Collectors.joining(", ")))
        .setHeader("Roles");
    grid.addColumn(User::isActive).setHeader("Active");
    grid.addColumn(User::getCreatedBy).setHeader("Created By ID");
    grid.addColumn(
            new LocalDateTimeRenderer<>(
                User::getCreatedOn,
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM)))
        .setHeader("Created On");
    grid.addColumn(User::getUpdatedBy).setHeader("Updated By ID");
    grid.addColumn(
            new LocalDateTimeRenderer<>(
                User::getUpdatedOn,
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM)))
        .setHeader("Updated On");

    // column config
    grid.setMultiSort(true);
    grid.setColumnReorderingAllowed(true);
    grid.getColumns().stream()
        .peek(userColumn -> userColumn.setSortable(true))
        .peek(userColumn -> userColumn.setResizable(true))
        .forEach(userColumn -> userColumn.setAutoWidth(true));

    // footer
    grid.appendFooterRow().getCells().stream()
        .findFirst()
        .get()
        .setComponent(new Label("Total: " + 10 + " people"));

    // set data provider for grid
    grid.setItems(userClient.findAll());
  }
}
