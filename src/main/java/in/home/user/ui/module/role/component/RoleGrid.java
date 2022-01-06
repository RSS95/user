package in.home.user.ui.module.role.component;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.spring.annotation.SpringComponent;
import in.home.user.api.model.Role;
import in.home.user.api.query.Pagination;
import in.home.user.api.query.role.RoleFilter;
import in.home.user.api.query.role.RoleQuery;
import in.home.user.ui.feign.client.RoleClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Optional;

@SpringComponent
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RoleGrid extends Div {

    private final Grid<Role> grid = new Grid<>();

    private transient RoleClient roleClient;

    @PostConstruct
    private void init() {
        initGrid();
        add(grid);
        setSizeFull();
    }

    public void refreshAllGrid() {
        Optional<Role> roleOp = this.getSelectedRole();
        this.grid.getDataProvider().refreshAll();
        roleOp.ifPresent(role -> selectRoleId(role.getId()));
    }

    public void refreshGridItem(Role role) {
        this.grid.getDataProvider().refreshItem(role);
    }

    public Optional<Role> getSelectedRole() {
        return this.grid.asSingleSelect().getOptionalValue();
    }

    private void initGrid() {
        //    style grid
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setSizeFull();

        // defining columns
        grid.addColumn(Role::getId).setHeader("ID");
        grid.addColumn(Role::getRoleName).setHeader("Role Name");
        grid.addColumn(Role::isActive).setHeader("Active");
        grid.addColumn(Role::getCreatedBy).setHeader("Created By ID");
        grid.addColumn(new LocalDateTimeRenderer<>(Role::getCreatedOn,
                                                   DateTimeFormatter.ofLocalizedDateTime(
                                                           FormatStyle.MEDIUM, FormatStyle.MEDIUM)))
            .setHeader("Created On");
        grid.addColumn(Role::getUpdatedBy).setHeader("Updated By ID");
        grid.addColumn(new LocalDateTimeRenderer<>(Role::getUpdatedOn,
                                                   DateTimeFormatter.ofLocalizedDateTime(
                                                           FormatStyle.MEDIUM, FormatStyle.MEDIUM)))
            .setHeader("Updated On");

        // column config
        grid.setMultiSort(true);
        grid.setColumnReorderingAllowed(true);
        grid.getColumns().forEach(userColumn -> {
            userColumn.setSortable(true);
            userColumn.setResizable(true);
            userColumn.setAutoWidth(true);
        });

        DataProvider<Role, Void> dataProvider = DataProvider.fromCallbacks(
                // First callback fetches items based on a query
                query -> {
                    // The index of the first item to load
                    int offset = query.getOffset();

                    // The number of items to load
                    int limit = query.getLimit();

                    RoleQuery roleQuery = RoleQuery.builder()
                                                   .pagination(Pagination.builder()
                                                                         .offset((long) offset)
                                                                         .limit((long) limit)
                                                                         .build())
                                                   .roleFilter(RoleFilter.builder().build())
                                                   .build();

                    List<Role> roleList = roleClient.fetch(roleQuery);

                    return roleList.stream();
                },
                // Second callback fetches the total number of items currently in the Grid.
                // The grid can then use it to properly adjust the scrollbars.
                query -> roleClient.count(RoleQuery.builder()
                                                   .pagination(Pagination.builder().build())
                                                   .roleFilter(RoleFilter.builder().build())
                                                   .build()).intValue());

        // set data provider for grid
        grid.setDataProvider(dataProvider);

        // footer
        grid.appendFooterRow()
            .getCells()
            .stream()
            .findFirst()
            .ifPresent(footerCell -> footerCell.setComponent(
                    new Label("Total: " + dataProvider.size(new Query<>()))));

        dataProvider.addDataProviderListener(event -> grid.getFooterRows()
                                                          .get(0)
                                                          .getCells()
                                                          .stream()
                                                          .findFirst()
                                                          .ifPresent(
                                                                  footerCell -> footerCell.setComponent(
                                                                          new Label("Total: " +
                                                                                    event.getSource()
                                                                                         .size(new Query<>())))));
    }

    public void selectRoleId(Long id) {
        this.grid.getDataProvider()
                 .fetch(new Query<>())
                 .filter(role -> role.getId().equals(id))
                 .findFirst()
                 .ifPresent(this.grid::select);
    }

    public void deselectAll() {
        this.grid.deselectAll();
    }

    public void addGridSelectionListener(SelectionListener<Grid<Role>, Role> listener) {
        this.grid.addSelectionListener(listener);
    }

    @Autowired
    public void setRoleClient(RoleClient roleClient) {
        this.roleClient = roleClient;
    }
}
