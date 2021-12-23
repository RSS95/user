package in.home.user.ui.module.user.component;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.spring.annotation.SpringComponent;
import in.home.user.api.model.Role;
import in.home.user.api.model.User;
import in.home.user.api.query.Pagination;
import in.home.user.api.query.user.UserFilter;
import in.home.user.api.query.user.UserQuery;
import in.home.user.ui.feign.client.UserClient;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserGrid extends Div {

  private final Grid<User> grid = new Grid<>();

  private UserClient userClient;

  @Autowired
  public UserGrid(UserClient userClient) {
    this.userClient = userClient;
  }

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
    grid.setSizeFull();

    // defining columns
    grid.addColumn(User::getId).setHeader("ID");
    grid.addColumn(User::getUserName).setHeader("User Name");
    grid.addColumn(User::getEmail).setHeader("Email ID");
    grid.addColumn(
            user ->
                user.getRoleSet().stream()
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
    grid.getColumns()
        .forEach(
            userColumn -> {
              userColumn.setSortable(true);
              userColumn.setResizable(true);
              userColumn.setAutoWidth(true);
            });

    DataProvider<User, Void> dataProvider =
        DataProvider.fromCallbacks(
            // First callback fetches items based on a query
            query -> {
              // The index of the first item to load
              int offset = query.getOffset();

              // The number of items to load
              int limit = query.getLimit();

              UserQuery userQuery =
                  UserQuery.builder()
                      .pagination(
                          Pagination.builder().offset((long) offset).limit((long) limit).build())
                      .userFilter(UserFilter.builder().build())
                      .build();

              List<User> userList = userClient.fetch(userQuery);

              return userList.stream();
            },
            // Second callback fetches the total number of items currently in the Grid.
            // The grid can then use it to properly adjust the scrollbars.
            query ->
                userClient
                    .count(
                        UserQuery.builder()
                            .pagination(Pagination.builder().build())
                            .userFilter(UserFilter.builder().build())
                            .build())
                    .intValue());

    // set data provider for grid
    grid.setDataProvider(dataProvider);

    // footer
    grid.appendFooterRow().getCells().stream()
        .findFirst()
        .get()
        .setComponent(new Label("Total: " + dataProvider.size(new Query<>())));
    dataProvider.addDataProviderListener(
        event -> {
          grid.getFooterRows().get(0).getCells().stream()
              .findFirst()
              .get()
              .setComponent(new Label("Total: " + event.getSource().size(new Query<>())));
        });
  }

  public void refreshAllGrid() {
    this.grid.getDataProvider().refreshAll();
  }

  public void refreshGridItem(User user) {
    this.grid.getDataProvider().refreshItem(user);
  }
}
