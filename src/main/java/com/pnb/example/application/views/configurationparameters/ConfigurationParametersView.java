package com.pnb.example.application.views.configurationparameters;

import com.pnb.example.application.data.entity.ConfigurationParameters;
import com.pnb.example.application.data.service.ConfigurationParametersService;
import com.pnb.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Configuration Parameters")
@Route(value = "configurationparameters/:configurationParametersID?/:action?(edit)", layout = MainLayout.class)
public class ConfigurationParametersView extends Div implements BeforeEnterObserver {

    private final String CONFIGURATIONPARAMETERS_ID = "configurationParametersID";
    private final String CONFIGURATIONPARAMETERS_EDIT_ROUTE_TEMPLATE = "configurationparameters/%s/edit";

    private final Grid<ConfigurationParameters> grid = new Grid<>(ConfigurationParameters.class, false);

    private TextField parameterId;
    private TextField parameterName;
    private TextField value;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<ConfigurationParameters> binder;

    private ConfigurationParameters configurationParameters;

    private final ConfigurationParametersService configurationParametersService;

    public ConfigurationParametersView(ConfigurationParametersService configurationParametersService) {
        this.configurationParametersService = configurationParametersService;
        addClassNames("configuration-parameters-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("parameterId").setAutoWidth(true);
        grid.addColumn("parameterName").setAutoWidth(true);
        grid.addColumn("value").setAutoWidth(true);
        grid.setItems(query -> configurationParametersService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent()
                        .navigate(String.format(CONFIGURATIONPARAMETERS_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ConfigurationParametersView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(ConfigurationParameters.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(parameterId).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("parameterId");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.configurationParameters == null) {
                    this.configurationParameters = new ConfigurationParameters();
                }
                binder.writeBean(this.configurationParameters);
                configurationParametersService.update(this.configurationParameters);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(ConfigurationParametersView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> configurationParametersId = event.getRouteParameters().get(CONFIGURATIONPARAMETERS_ID)
                .map(Long::parseLong);
        if (configurationParametersId.isPresent()) {
            Optional<ConfigurationParameters> configurationParametersFromBackend = configurationParametersService
                    .get(configurationParametersId.get());
            if (configurationParametersFromBackend.isPresent()) {
                populateForm(configurationParametersFromBackend.get());
            } else {
                Notification.show(String.format("The requested configurationParameters was not found, ID = %s",
                        configurationParametersId.get()), 3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ConfigurationParametersView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        parameterId = new TextField("Parameter Id");
        parameterName = new TextField("Parameter Name");
        value = new TextField("Value");
        formLayout.add(parameterId, parameterName, value);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(ConfigurationParameters value) {
        this.configurationParameters = value;
        binder.readBean(this.configurationParameters);

    }
}
